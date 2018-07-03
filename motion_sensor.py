import RPi.GPIO as GPIO
import time

pir_sensor = 11
GPIO.setmode(GPIO.BOARD)
GPIO.setup(pir_sensor, GPIO.IN)

current_state = 0
count_events = 0
try:
  while True:
    time.sleep(0.1)
    current_state = GPIO.input(pir_sensor)
    if current_state == 1:
      count_events += 1
      print("Motion detected: %s" % (count_events))
      time.sleep(5)
except KeyboardInterrupt:
  pass
finally:
  GPIO.cleanup()
