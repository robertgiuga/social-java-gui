package com.example.socialtpygui.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient{
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public TCPClient() throws IOException {
        clientSocket= new Socket("127.0.0.1",5555);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public int createConnection(String email) throws IOException {
        out.println(email);
        String port= in.readLine();
        System.out.println();
        in.close();
        out.close();
        clientSocket.close();
        return Integer.valueOf(port);
    }

}
