

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
    print "Waiting for connection on RFCOMM"
    client_sock, client_info = server_sock.accept()
    print "Accepted connection from: ", client_info
    try:
        data = client_sock.recv(1024)
    
        if len(data) == 0: break
        print "received [%s]" % data
        if data == 'Hello?':
            counter += 1
            data = 'Hello World [' + str(counter) + ']!'
        else:
            data = 'What?!'
        client_sock.send(data)
        print "sending [%s]" % data

    except IOError:
        pass

    except KeyboardInterrupt:
        print "disconnected"
        client_sock.close()
        server_sock.close()
        print "all done!"
        break

