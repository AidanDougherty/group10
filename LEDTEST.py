import RPi.GPIO as GPIO
GPIO.setmode(GPIO.BCM)
pin_to_circuit = 4

GPIO.setup(pin_to_circuit,GPIO.OUT)



from bluetooth import*

uuid = "00001101-0000-1000-8000-00805f9b34ab"
server_sock = BluetoothSocket(RFCOMM)
server_sock.bind(("",PORT_ANY))
server_sock.listen(1)
advertise_service(server_sock, "Group10pi",
                  service_id = uuid,
                  service_classes = [uuid, SERIAL_PORT_CLASS],
                  profiles = [SERIAL_PORT_PROFILE],
                 )
counter = 0
while True:
    GPIO.output(pin_to_circuit, GPIO.HIGH)

    print "Waiting for connection on RFCOMM"
    client_sock, client_info = server_sock.accept()
    print "Accepted connection from: ", client_info
    try:
      
        data = client_sock.recv(1024)
        #if len(data) == 0: break
        
        print "received [%s]" % data
        if data == "ON":
            print('turning on Light')
            GPIO.output(pin_to_circuit, GPIO.HIGH)
        #elif data == "OFF":
         #   GPIO.output(pin_to_circuit, GPIO.LOW)
            

    except IOError:
        pass

    except KeyboardInterrupt:
        print "disconnected"
        client_sock.close()
        GPIO.output(pin_to_circuit, GPIO.HIGH)
        server_sock.close()
        print "all done!"
        break

