#include <avr/io.h>
#include <util/delay.h>
#include <avr/sleep.h>
#include <avr/interrupt.h>
#include <stdio.h>

#include "../lib_avr/rbserialmessages/rbserialmessages.h"
#include "../lib_avr/servoreceiver/servoreceiver.h"
#include "../lib_avr/uart/uart_extra.h"
#include "servo.h"
// #include "uart.h"
#include "system_clock.h"
#include "fingerprint.h"

#define BAUD 115200

#define DEBUG_DDR  DDRB
#define DEBUG_PORT PORTB
#define DEBUG_PINN PB7 // arduino 13
#define SERVO_DDR  DDRB
#define SERVO_PORT PORTB
#define SERVO_PINN PB5 // arduino 11 TODO: this is not used here
#define CONNECTION_TIMEOUT_US 1000000L // 1000ms

/**
 * These values map the physical input/output (voltage/ms of pwm pulse) to a
 * fixed physical angle. When software commands a steering angle of 10 deg
 * (1000 hundredths), the PWM_SCALE_STEERING_OUT and POT_SCALE_STEERING_IN
 * should be adjusted until a 10 deg input/output is seen/observed.
 */
#if BUGGY == transistor 
  #define PWM_OFFSET_STEERING_OUT 1850
  #define PWM_SCALE_STEERING_OUT -220
  #define PWM_OFFSET_STORED_ANGLE 0
  #define PWM_SCALE_STORED_ANGLE 1000 // in hundredths of a degree for precision
  #define POT_OFFSET_STEERING_IN 121
  #define POT_SCALE_STEERING_IN -10
#elif BUGGY == nixie
  #define PWM_OFFSET_STEERING_OUT 1789
  #define PWM_SCALE_STEERING_OUT -150
  #define PWM_OFFSET_STORED_ANGLE 0
  #define PWM_SCALE_STORED_ANGLE 1000 // in hundredths of a degree for precision
#else
  #error "must complie with BUGGY_TRANSISTOR or BUGGY_NIXI flag"
#endif

#define PWM_STATE_THRESHOLD 120

#define RX_STEERING_DDR  DDRE
#define RX_STEERING_PORT PORTE
#define RX_STEERING_PIN  PINE
#define RX_STEERING_PINN PE4 // arduino 2
#define RX_STEERING_INT  INT4_vect
#define RX_STEERING_INTN 4
#define RX_BRAKE_DDR  DDRD
#define RX_BRAKE_PORT PORTD
#define RX_BRAKE_PIN  PIND
#define RX_BRAKE_PINN PD0 // arduino 21
#define RX_BRAKE_INT  INT0_vect
#define RX_BRAKE_INTN 0
#define RX_AUTON_DDR  DDRD
#define RX_AUTON_PORT PORTD
#define RX_AUTON_PIN  PIND
#define RX_AUTON_PINN PD1 // arduino 20
#define RX_AUTON_INT  INT1_vect
#define RX_AUTON_INTN 1

#define ENCODER_DDR  DDRD
#define ENCODER_PORT PORTD
#define ENCODER_PIN  PIND
#define ENCODER_PINN PD2 // arduino 19
#define ENCODER_INT  INT2_vect
#define ENCODER_INTN 2
#define ENCODER_TIMEOUT_US 500 // 50mph w/ 6" wheel = 280 ticks/sec; 4000us/tick

#define BATTERY_ADC 0
#define STEERING_POT_ADC 9

#define LED_DANGER_DDR  DDRB
#define LED_DANGER_PORT PORTB
#define LED_DANGER_PINN PB6 // arduino 12
#define BRAKE_OUT_DDR  DDRH
#define BRAKE_OUT_PORT PORTH
#define BRAKE_OUT_PINN PH5 // arduino 8
#define BRAKE_INDICATOR_DDR  DDRE
#define BRAKE_INDICATOR_PORT PORTE
#define BRAKE_INDICATOR_PINN PE3 // arduino 5


// Global state
static bool g_brake_state_engaged; // 0 = disengaged, !0 = engaged.
static bool g_brake_needs_reset; // 0 = nominal, !0 = needs reset
static bool g_is_autonomous;
static unsigned long g_current_voltage;
static unsigned long g_steering_feedback;
static volatile unsigned long g_encoder_ticks;
static volatile unsigned long g_encoder_time_last;
static int smoothed_thr;
static int smoothed_auton;
static int steer_angle;
static int auto_steering_angle;

RBSerialMessages g_rbsm;
rb_message_t g_new_rbsm;
ServoReceiver g_steering_rx;
ServoReceiver g_brake_rx;
ServoReceiver g_auton_rx;

FILE g_uart_rbsm;
FILE g_uart_debug;


inline long map_signal(long x,
                       long in_offset,
                       long in_scale,
                       long out_offset,
                       long out_scale) 
{
  return ((x - in_offset) * out_scale / in_scale) + out_offset;
}


void adc_init(void) 
{
  // set up adc hardware in non-freerunning mode
  // prescaler of 128 gives 125kHz sampling
  // AREF = AVCC
  // 8 bit return values
  ADCSRA |= _BV(ADEN) | _BV(ADPS2) | _BV(ADPS1) | _BV(ADPS0);
  ADMUX |= _BV(REFS0) | _BV(ADLAR);
}

uint8_t adc_read_blocking(uint8_t channel) {
  // in single ended mode, highest bit of ADC channel is in ADCSRB
  ADMUX = (ADMUX & 0xE0) | (channel & 0x07);
  ADCSRB = (ADCSRB & 0xF7) | (channel & 0x08);
  ADCSRA |= _BV(ADSC);
  while((ADCSRA & _BV(ADSC)) == 1) {}
  return ADCH;
}


void steering_set(int angle) 
{
  int servo_value_us = map_signal(angle,
                                  PWM_OFFSET_STORED_ANGLE,
                                  PWM_SCALE_STORED_ANGLE,
                                  PWM_OFFSET_STEERING_OUT,
                                  PWM_SCALE_STEERING_OUT);
  servo_set_us(servo_value_us);
}


void brake_init() 
{
  BRAKE_OUT_DDR |= _BV(BRAKE_OUT_PINN);
  BRAKE_INDICATOR_DDR |= _BV(BRAKE_INDICATOR_PINN);
}


// Note: High-voltage raises the brake.
// Do not call before brake_init
void brake_raise() 
{
  BRAKE_OUT_PORT |= _BV(BRAKE_OUT_PINN);
  BRAKE_INDICATOR_PORT &= ~_BV(BRAKE_INDICATOR_PINN);
}


// Drops the brake
// Do not call before brake_init
void brake_drop() 
{
  BRAKE_OUT_PORT &= ~_BV(BRAKE_OUT_PINN);
  BRAKE_INDICATOR_PORT |= _BV(BRAKE_INDICATOR_PINN);
}


int main(void) 
{
  // turn the ledPin on
  // DEBUG_PORT |= _BV(DEBUG_PINN);
  DEBUG_PORT &= ~_BV(DEBUG_PINN);
  DEBUG_DDR |= _BV(DEBUG_PINN);

  // setup encoder pin and interrupt
  ENCODER_DDR &= ~_BV(ENCODER_PINN);
  EIMSK |= _BV(INT2);
  EICRA |= _BV(ISC20);
  EICRA &= ~_BV(ISC21);

  // prepare uart0 (onboard usb) for rbsm
  uart0_init(UART_BAUD_SELECT(BAUD, F_CPU));
  uart0_fdevopen(&g_uart_rbsm);

  // prepare uart2 (because servo conflicts with uart1) for debug output
  uart2_init(UART_BAUD_SELECT(BAUD, F_CPU));
  uart2_fdevopen(&g_uart_debug);
  // map stdio for printf
  stdin = stdout = stderr = &g_uart_debug;

  // setup hardware
  sei(); // enable interrupts
  // uart_init();
  system_clock_init();
  servo_init();
  adc_init();
  brake_init();
  LED_DANGER_DDR |= _BV(LED_DANGER_PINN);
  
  // setup rbsm
  g_rbsm.Init(&g_uart_rbsm, &g_uart_rbsm);

  // set up rc receivers
  g_steering_rx.Init(&RX_STEERING_PIN, RX_STEERING_PINN, RX_STEERING_INTN);
  g_brake_rx.Init(&RX_BRAKE_PIN, RX_BRAKE_PINN, RX_BRAKE_INTN);
  g_auton_rx.Init(&RX_AUTON_PIN, RX_AUTON_PINN, RX_AUTON_INTN);

  printf("Hello world! This is debug information\r\n");
  printf("Compilation date: %s\r\n", FP_COMPDATE);
  printf("Compilation time: %s\r\n", FP_COMPTIME);
  printf("Branch name: %s\r\n", FP_BRANCHNAME);
  printf("Most recent commit: %s\r\n", FP_STRCOMMITHASH);
  printf("Branch clean? %d\r\n", FP_CLEANSTATUS);
  printf("\nEnd of compilation information\r\n");

  // loop forever
  while(1) 
  {
    // get new command messages
    // TODO: make reading messages work
    //
    // rb_message_t new_command;
    // int read_status;
    
    // while((read_status = g_rbserialmessages.Read(&new_command))
    //       != RBSM_ERROR_INSUFFICIENT_DATA) {
    //   if(read_status == false) {
    //     // dipatch complete message
    //     switch(new_command.message_id) {
    //       case RBSM_MID_MEGA_STEER_ANGLE:
    //         auto_steering_angle = (int)(long)new_command.data;
    //         break;

    //       default:
    //         // report unknown message
    //         g_rbserialmessages.Send(RBSM_MID_ERROR, RBSM_EID_RBSM_INVALID_MID);
    //         dbg_println("Got message with invalid mid:");
    //         dbg_println(new_command.message_id);
    //         dbg_println(new_command.data);
    //         break;
    //     }
    //   } else if(read_status == RBSM_ERROR_INVALID_MESSAGE) {
    //     // report stream losses for tracking
    //     g_rbserialmessages.Send(RBSM_MID_ERROR, RBSM_EID_RBSM_LOST_STREAM);
    //   }
    //   // drop responses with other faults
    // }

    // find the new steering angle, if available
    steer_angle = g_steering_rx.GetAngleThousandths();

    // find the new brake state, if available
    smoothed_thr = g_brake_rx.GetAngle();
    // TODO make this code...less...something
    if(smoothed_thr > PWM_STATE_THRESHOLD) 
    {
      // read as engaged
      g_brake_state_engaged = true;
      // brake has been reset
      g_brake_needs_reset = false;
    } 
    else 
    {
      // read as disengaged
      g_brake_state_engaged = false;
    }

    // find the new autonomous state, if available
    smoothed_auton = g_auton_rx.GetAngle();
    // TODO make this code...less...something
    if(smoothed_auton > PWM_STATE_THRESHOLD) 
    { 
      // read as engaged
      g_is_autonomous = true;
    } 
    else 
    {
      // read as disengaged
      g_is_autonomous = false;
    }

    // detect dropped conections
    // note: interrupts must be disabled while checking system clock so that
    //       timestamps are not updated under our feet
    cli();
    unsigned long time_now = micros();
    unsigned long time1 = g_steering_rx.GetLastTimestamp();
    unsigned long time2 = g_brake_rx.GetLastTimestamp();
    unsigned long time3 = g_auton_rx.GetLastTimestamp();
    unsigned long delta1 = time_now - time1;
    unsigned long delta2 = time_now - time2;
    unsigned long delta3 = time_now - time3;
    sei();
    if(delta1 > CONNECTION_TIMEOUT_US ||
       delta2 > CONNECTION_TIMEOUT_US ||
       delta3 > CONNECTION_TIMEOUT_US) 
    {
      // we haven't heard from the RC receiver in too long

      if(g_brake_needs_reset == false) 
      {
        g_rbsm.Send(RBSM_MID_ERROR, RBSM_EID_RC_LOST_SIGNAL);
      }
      g_brake_needs_reset = true;
    }

    // For the old buggy, the voltage divider is 10k ohm on the adc side and
    // 16k ohm on top.
    // Calculated map normally set to 13000, but the avcc is 4.86 volts
    // rather than 5.
    g_current_voltage = adc_read_blocking(BATTERY_ADC);
    g_current_voltage = map_signal(g_current_voltage, 0, 255, 0, 12636); // in millivolts
    
    // Read/convert steering pot
    g_steering_feedback = adc_read_blocking(STEERING_POT_ADC);
    g_steering_feedback = map_signal(g_steering_feedback,
                                     POT_OFFSET_STEERING_IN,
                                     POT_SCALE_STEERING_IN,
                                     PWM_OFFSET_STORED_ANGLE,
                                     PWM_SCALE_STORED_ANGLE);

    // Set outputs
    if(g_brake_state_engaged == false && g_brake_needs_reset == false) 
    {
      brake_raise();
    } 
    else 
    {
      brake_drop();
    }

    if(g_is_autonomous)
    {
      steering_set(auto_steering_angle);
      g_rbsm.Send(RBSM_MID_MEGA_STEER_ANGLE, (long int)(auto_steering_angle));
    }
    else if(!g_is_autonomous)
    {
      steering_set(steer_angle);
      g_rbsm.Send(RBSM_MID_MEGA_STEER_ANGLE, (long int)steer_angle);
    }
    else
    {
      steering_set(PWM_OFFSET_STORED_ANGLE); // default to centered
    }

    if(g_brake_needs_reset == true) 
    {
      LED_DANGER_PORT |= _BV(LED_DANGER_PINN);
    } 
    else 
    {
      LED_DANGER_PORT &= ~_BV(LED_DANGER_PINN);
    }

    // Send the rest of the telemetry messages
    g_rbsm.Send(RBSM_MID_DEVICE_ID, RBSM_DID_MEGA);
    g_rbsm.Send(RBSM_MID_MEGA_BRAKE_STATE,(long unsigned)g_brake_state_engaged);
    g_rbsm.Send(RBSM_MID_MEGA_AUTON_STATE, (long unsigned)g_is_autonomous);
    g_rbsm.Send(RBSM_MID_MEGA_BATTERY_LEVEL, g_current_voltage);
    g_rbsm.Send(RBSM_MID_MEGA_STEER_FEEDBACK, (long int)g_steering_feedback);
    g_rbsm.Send(RBSM_MID_ENC_TICKS_RESET, g_encoder_ticks);
    g_rbsm.Send(RBSM_MID_ENC_TIMESTAMP, millis());
    g_rbsm.Send(RBSM_MID_COMP_HASH, (long unsigned)(FP_HEXCOMMITHASH));
  }


  return 0;
}


ISR(RX_STEERING_INT) 
{
  g_steering_rx.OnInterruptReceiver();
}


ISR(RX_BRAKE_INT) 
{
  g_brake_rx.OnInterruptReceiver();
}


ISR(RX_AUTON_INT) 
{
  g_auton_rx.OnInterruptReceiver();
}

ISR(ENCODER_INT) {
  unsigned long time_now = micros();
  // debounce encoder tick count
  if(time_now - g_encoder_time_last > ENCODER_TIMEOUT_US) {
    g_encoder_ticks++;
  }
  g_encoder_time_last = time_now;
}
