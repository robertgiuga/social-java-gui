package com.example.socialtpygui.Socket;

import com.example.socialtpygui.service.SuperService;
import com.example.socialtpygui.utils.events.ChangeEventType;
import com.example.socialtpygui.utils.events.NewMessageEvent;

import java.io.IOException;
import java.net.*;

public class UDPClient extends Thread{
    private DatagramSocket socket;
    private InetAddress address;
    private SuperService service;
    private boolean run=true;
    private int serverPort;
    private boolean portSended=false;

    private byte[] buf;

    public UDPClient(SuperService service, int port) throws SocketException, UnknownHostException {
        this.service = service;
        socket = new DatagramSocket(port);
        address = InetAddress.getByName("localhost");

    }

    @Override
    public void run(){
        try {
            while (run) {
                buf = new byte[4];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                if(!portSended)
                    serverPort=Integer.parseInt(received);
                if(Integer.parseInt(received)==0)
                    run= false;
                int code= Integer.parseInt(String.valueOf(received.charAt(0)));
                switch (code){
                    case 2:
                        //for msj
                        int nr=Integer.parseInt(String.valueOf(received.charAt(1)));
                        service.notifyObservers(new NewMessageEvent(ChangeEventType.NEW_MSJ, nr));
                        break;
                    case 3:
                        //for requests
                        break;
                    case 4:
                        //for events
                        break;
                }
                System.out.println(received);
            }
        }catch (IOException e){}
        socket.close();
    }

}
