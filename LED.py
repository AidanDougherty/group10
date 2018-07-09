import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
pin_to_circuit = 18

GPIO.setup(pin_to_circuit, GPIO.OUT)
GPIO.output(pin_to_circuit, GPIO.HIGH)
print "high"
time.sleep(2)
GPIO.output(pin_to_circuit, GPIO.LOW)
print "low"
#GPIO.setup(pin_to_circuit, GPIO.IN)
#while (GPIO.input(pin_to_circuit) == GPIO.HIGH):
    
    
