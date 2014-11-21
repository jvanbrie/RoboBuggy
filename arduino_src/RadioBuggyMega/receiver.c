/**
 * @file receiver.c
 * @brief Contains Code for dealing with RC Receiver
 * NOTE: AIL and THR are flipped.
 * AIL is on pin 2
 * THR is on pin 3
 *
 * @author Matt Sebek (msebek)
 * @author Zach Dawson (zsd)
 *
 * These functions are used to connect/communicate with
 * the RC radio receiver.
 */
#include <Arduino.h>
#include "receiver.h"

// legacy: needed by receiver_get_angle
#define AIL_RIGHTMOST 980

#define PWM_OFFSET_RC_IN 1510
#define PWM_SCALE_RC_IN 350
#define PWM_OFFSET_STEERING_OUT 1789
#define PWM_SCALE_STEERING_OUT -150
#define PWM_OFFSET_STORED_ANGLE 0
#define PWM_SCALE_STORED_ANGLE 1000 // in hundredths of a degree for precision

#define AIL_RECEIVER_PIN 2
#define AIL_RECEIVER_INT 0

#define THR_RECEIVER_PIN 21
#define THR_RECEIVER_INT 2

inline long map_signal(long x,
                       long in_offset,
                       long in_scale,
                       long out_offset,
                       long out_scale) {
  return ((x - in_offset) * out_scale / in_scale) + out_offset;
}

////////////////////////////////////////

//For 72 MHz RC reciever
//#define PWM_TIME 21800
//#define PWM_THRESH 130
//#define BIG_PULSE 10
//High pulse lasts over 1/10 of period
//#define SHORT_PULSE 21
//High pulse lasts under 1/21 of period

//For 2.4 GHz racecar reciever
#define PWM_TIME 18370   //the pwn period (us)
#define PWM_THRESH 1300
#define BIG_PULSE 8
//High pulse lasts over 1/8 of period
#define SHORT_PULSE 19
//High pulse lasts under 1/19 of period

// Defined in receiver.h
// #define THR_INDEX 0
// #define AIL_INDEX 1

// Note: arr[0] is thr, arr[1] is ail

static volatile unsigned long up_switch_time[2];
static volatile unsigned long down_switch_time[2];
static volatile unsigned long rc_value[2];

// NOTE THAT WE ARE ASSUMING:
//  Main Loop-length is shorter than PWM pulse length. 
//    Otherwise, you could recieve stale values in your main loop.
// Loop reading rc_values must set rc_available false after reading
// When our code gets really busy this will become inaccurate,
// (i believe since micros gets shifted a bit) but for
// the current application its easy to understand and works very well
// TODO if things start twitching, move to using registers directly.
// TODO if this starts twitching, also micros has a resolution of 4us.
static void receiver_on_ail_interrupt() {
  if(digitalRead(AIL_RECEIVER_PIN) == HIGH) {
    // High Received
    if((micros() - up_switch_time[AIL_INDEX] > PWM_TIME - PWM_THRESH) &&
       (micros() - up_switch_time[AIL_INDEX] < PWM_TIME + PWM_THRESH)) {
      // TODO this prevent an instantaneous start-up error, where the
      // above condition is true, and up_switch_time 
      if((down_switch_time[AIL_INDEX] > up_switch_time[AIL_INDEX]) &&	
	 (rc_available[AIL_INDEX] == 0)) {
	  rc_value[AIL_INDEX] = (down_switch_time[AIL_INDEX] - 
				 up_switch_time[AIL_INDEX]);
	  rc_available[AIL_INDEX] = 1;
          if(rc_value[AIL_INDEX]/(PWM_TIME/BIG_PULSE) >= 1 || rc_value[AIL_INDEX]/(PWM_TIME/SHORT_PULSE) == 0)
            rc_available[AIL_INDEX] == 0;
      }
    }
    up_switch_time[AIL_INDEX] = micros();
  } else {
    // Low received
    if(up_switch_time[AIL_INDEX]) {
      down_switch_time[AIL_INDEX] = micros();
    }
  }
}

static void receiver_on_thr_interrupt() {
  if(digitalRead(THR_RECEIVER_PIN) == HIGH) {
    // High Received
    if((micros() - up_switch_time[THR_INDEX] > PWM_TIME - PWM_THRESH) &&
       (micros() - up_switch_time[THR_INDEX] < PWM_TIME + PWM_THRESH)) {
      // TODO this prevent an instantaneous start-up error, where the
      // above condition is true, and up_switch_time 
      if((down_switch_time[THR_INDEX] > up_switch_time[THR_INDEX]) &&	
	 (rc_available[THR_INDEX] == 0)) {
	  rc_value[THR_INDEX] = (down_switch_time[THR_INDEX] - 
				 up_switch_time[THR_INDEX]);
	  rc_available[THR_INDEX] = 1;
          if(rc_value[THR_INDEX]/(PWM_TIME/10) >= 1 || rc_value[THR_INDEX]/(PWM_TIME/21) == 0)
            rc_available[THR_INDEX] == 0;
      }
    }
    up_switch_time[THR_INDEX] = micros();
  } else {
    // Low received
    if(up_switch_time[THR_INDEX]) {
      down_switch_time[THR_INDEX] = micros();
    }
  }
}

// Returns error code
int receiver_init() {
  up_switch_time[0] = 0;
  up_switch_time[1] = 0;
  down_switch_time[0] = 0;
  down_switch_time[1] = 0;
  attachInterrupt(THR_RECEIVER_INT, receiver_on_thr_interrupt, CHANGE);
  attachInterrupt(AIL_RECEIVER_INT, receiver_on_ail_interrupt, CHANGE);
}


// Index = 0 to check thr, index = 1 to check
// Returns 0 to 180, with 90 being center.
// TODO measure throttle positions.
int receiver_get_angle(int index) {
  // Math to convert nThrottleIn to 0-180.
  int ret_val = (int)(rc_value[index]-AIL_RIGHTMOST)*3/17;
  rc_available[index] = 0;
  return ret_val;
}

int receiver_get_steering_angle(int index) {
  // Scale the received signal into hundredths of a degree
  int value = (int)map_signal(rc_value[index],
                              PWM_OFFSET_RC_IN,
                              PWM_SCALE_RC_IN,
                              PWM_OFFSET_STORED_ANGLE,
                              PWM_SCALE_STORED_ANGLE);
  rc_available[index] = 0;
  return value;
}




