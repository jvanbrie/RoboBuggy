//Jeremy Huang 2015-10-16
//

#include <avr/io.h>
#include <util/delay.h>
#include <avr/sleep.h>
#include <avr/interrupt.h>
#include <stdio.h>

#include "../lib_avr/Light_WS2812/light_ws2812.h"
#include "../lib_avr/rbserialmessages/rbserialmessages.h"
#include "../lib_avr/uart/uart_extra.h"
// #include "uart.h"
#include "system_clock.h"


#define BAUD 9600

#define DEBUG_DDR  DDRB
#define DEBUG_PORT PORTB
#define DEBUG_PINN PB7 // arduino 13



#define port13 PB7
#define port12 PB6
#define port12 PB6

cRGB MAXBRIGHT = {255, 255, 255};

int main(void) {
 /* PORTB = _BV(port12);
  PORTB |= _BV(port13);
  TCCR1B = ( 62500) | 0x01;*/
  
  cRGB lightarr[24];
  
  for(int i=0; i < 24; i++)
    lightarr[i] = MAXBRIGHT;
  
  ws2812_setleds(lightarr, 24);
  
  while(true) {
    
  }
}
