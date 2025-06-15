package com.github.tanyuushaa;

import com.github.tanyuushaa.msg.CommunicationTCP;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TesterClientTCP {
    public static void main(String[] args) {
        try{
            InetAddress address = InetAddress.getByName("localhost");
            Socket socket = new Socket(address, 8080);
            System.out.println("Connected to server");
            try (CommunicationTCP com = new CommunicationTCP(socket)) {
                com.write("Hello Server");
                System.out.println("Message sent");
                String response = com.read();
                System.out.println("Received response: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
