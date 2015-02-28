// This file has been prepared for Doxygen automatic documentation generation.
/*! \file ********************************************************************
*
* Atmel Corporation
*
* - File              : main.c
* - Compiler          : IAR EWAAVR 3.20
*
* - Support mail      : avr@atmel.com
*
* - Supported devices : All devices with an Input Capture Unit can be used.
*                       The example is written for ATmega64
*                       The demonstration is tuned for a 3.6864MHz clock,
*                       but clock speed is not critical to correct operation.
*
* - AppNote           : AVR135 - Pulse-Width Demodulation
*
* - Description       : Example of how to use Pulse-Width Demodulation
*
* Originally authored by Bruce McKenny
* $Revision: 1.5 $
* $Date: Wednesday, November 02, 2005 13:20:02 UTC $
*****************************************************************************/

/*! \mainpage
* \section Intro Introduction
* This documents functions, variables, defines, enums and typedefs in the software
* for application note AVR135: Using Timer Capture to Measure PWM Duty Cycle\n
*
* \section Descr Description
* icp.c contains functions for use of the demodulator.\n
* main.c contains a simple program to demonstrate the demodulator operation. It
* generates a PWM signal on OC2, then samples the demodulator output using calls
* to icp_rx() and writes the values obtained to PORTC.\n
* Approximately twice per second, OCR2 is incremented such that the PWM duty cycle
* steps through the entire [0:256) range.\n
* The intended use is on the STK500/STK501, with a 10-wire jumper connecting the
* PORTC header with the LEDs header and a single-wire jumper connecting PB7 (OC2)
* with PD4 (ICP1).\n
* The expected result is that the LED display cycles repeatedly from 0x00 to 0xFF.\n
*
* \section CI Compilation Info
* This software was written for the IAR Embedded Workbench, 3.20C, but can also be
* built using newer versions of IAR, and also GCC (see note below).\n
* To make project for IAR EWAVR:
* Add the .c files to project (main.c and icp.c). Use device --cpu=m64, small memory
* model, optimization low for debug target and high for release, output format:
* ubrof8 for Debug and intel_extended for Release. \n
*
* A makefile is included for use with GCC. Defines are included in code to make
* porting to GCC easy.\n
*
* \section DI Device Info
* This example is written for ATMega64, but all devices with an Input Capture Unit
* can be used.\n
* The demonstration is tuned for a 3.6864MHz clock, but clock speed is not critical
* to correct operation.\n
*/

#if __GCC__
#include <avr/io.h>
#include <avr/signal.h>
#include <avr/interrupt.h>	/* sei */
#define	SEI()	sei()
#define	CLI()	cli()
#define	SLEEP()	__asm__ __volatile__ ("sleep \r\n" :: )
#else /* IAR */
#include <stdio.h>
#include <inavr.h>
#define ENABLE_BIT_DEFINITIONS
#include <iom64.h>
#define	SEI()	asm("SEI")
#define	CLI()	asm("CLI")
#define	SLEEP()	asm("SLEEP")
#endif

#include "icp.h"

/*
 * LED debug definitions (ATmega64).
 * The current detected PWD duty cycle is displayed on PC[7:0]
 */
#define	LED_DEBUG_DDR	DDRF
#define	LED_DEBUG_PORT	PORTF


/**
 * main()
 *
 * Demonstration main program.
 */
int
main(void)
{
	icp_sample_t sample;

	/*
	 * Init subsystems
	 */
	//hb_init();						/* main.c	*/
	icp_init();						/* icp.c	*/
	//pwm_init();						/* main.c	*/

	/*
	 * PORTC for LED debug
	 */
	LED_DEBUG_PORT = 0xFF;			/* initially off */
	LED_DEBUG_DDR = 0xFF;			/* all output */

	/*
	 * The ISRs do most of the work.
	 */
	SEI();							/* enable interrupts since init is done */

	MCUCR |= (1 << SE);				/* enable (idle mode) sleep */

	/*
	 * Loop forever
	 */
	for (;;)
	{
		/*
		 * Fetch the latest reading and display it on the LEDs.
		 */
		sample = icp_rx();

		LED_DEBUG_PORT = ~sample;

		/*
		 * Sleep until the next interrupt. This will wake up twice
		 * per PWM period, plus (apx.) 4 times per second for the
		 * heartbeat/update timer. This is more often than needed,
		 * but is certainly sufficient for this demonstration.
		 */
		SLEEP();
	}
	/*NOTREACHED*/
	return(0);
}
