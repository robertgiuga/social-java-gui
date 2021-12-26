package com.example.socialtpygui.utils.socket;

import com.example.socialtpygui.service.SuperService;
import com.example.socialtpygui.utils.events.ChangeEventType;
import com.example.socialtpygui.utils.events.NewMessageEvent;
import javafx.application.Platform;

import java.io.IOException;
import java.net.*;

public class UDPClient extends Thread{
    private DatagramSocket socket;
    private InetAddress address;
    private SuperService service;
    private boolean run=true;

    private byte[] buf;

    public UDPClient(SuperService service, int port) throws SocketException, UnknownHostException {
        if(port==0)
            run=false;
        this.service = service;
        socket = new DatagramSocket(port);
        address = InetAddress.getByName("localhost");
    }

    @Override
    public void run(){
        try {
            while (run) {
                buf = new byte[1];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                //first integer of the receive int
                int code= Integer.parseInt(String.valueOf(received.charAt(0)));
                switch (code){
                    case 2:
                        //for msj
                        Platform.runLater(() ->service.notifyObservers(new NewMessageEvent(ChangeEventType.NEW_MSJ)));
                        break;
                    case 3:
                        System.out.println("*");
                        Platform.runLater(() ->service.notifyObservers(new NewMessageEvent(ChangeEventType.NEW_GROUP_MSJ)));
                        break;
                    case 4:
                        //for events
                        break;
                    case 0:
                        run=false;
                        break;
                }
            }
        }catch (IOException e){}
        socket.close();
    }

}
