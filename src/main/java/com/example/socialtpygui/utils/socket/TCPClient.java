package com.example.socialtpygui.utils.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class TCPClient{
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public TCPClient() throws IOException {
        try {
            clientSocket = new Socket("127.0.0.1", 5555);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (ConnectException e){}
    }

    public int createConnection(String email) throws IOException {
        String port ="0";
        if(out!=null) {
            out.println(email);
            port = in.readLine();
            in.close();
            out.close();
            clientSocket.close();
        }
        return Integer.valueOf(port);
    }

    public void closeConnection(String email){
        if(out!=null) {
            out.println(email + ";close");
        }
    }

}
