/**
 * @file steering.c
 * @brief These functions are used to control the steering servo motor
 * @author Haley Dalzell
 * @author Zach Dawson
 * @author Matt Sebek
 *
 */
#include <Servo.h>


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


Servo steer;  // create servo object to control a servo
static int s_angle;
static int s_left, s_center, s_right;

void steering_init(int SERVO_PIN, int left, int center, int right) {
  steer.attach(SERVO_PIN);  // attaches the servo on pin 9 to the servo object
  s_left = left;
  s_center = center;
  s_right = right;
}

// expect the angle in hundredths of a degree
void steering_set(int angle) {
  int servo_value_ms = map_signal(angle,
                                  PWM_OFFSET_STORED_ANGLE,
                                  PWM_SCALE_STORED_ANGLE,
                                  PWM_OFFSET_STEERING_OUT,
                                  PWM_SCALE_STEERING_OUT);
  steer.writeMicroseconds(servo_value_ms);
}
