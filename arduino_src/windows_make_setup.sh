#!/bin/bash
# This script is an example of what you might have to run in order
#  to compile and upload arduino programs.

export ARDUINO_DIR="../../../../../../../arduino"
export ARDMK_DIR=".." 
export MONITOR_PORT="com3"
export BOARD_TAG="mega2560"


# So, I don't know why the following things work, but they do:
#  Make upload is still broken. 
#  $ MONITOR_PORT="COM3" make reset
#	=> port not found
#  $ MONITOR_PORT="/dev/ttyS2" make reset
#	=> works!!!
#	If it doesn't...open up the arduino IDE, upload once, then try 
#	to make reset again.
