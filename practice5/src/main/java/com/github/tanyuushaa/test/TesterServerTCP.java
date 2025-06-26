package com.github.tanyuushaa.test;

import com.github.tanyuushaa.msg.CommunicationTCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TesterServerTCP {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            try(CommunicationTCP com = new CommunicationTCP(clientSocket)) {
                String receiver = com.read();
                System.out.println("Client received: " + receiver);
                com.write("OK");
                System.out.println("Reply sent");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
