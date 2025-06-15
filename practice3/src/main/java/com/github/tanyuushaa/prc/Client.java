package com.github.tanyuushaa.prc;

import com.github.tanyuushaa.msg.CommunicationTCP;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            InetAddress address = InetAddress.getByName(null);
            Socket socket = new Socket(address, 8080);

            try(CommunicationTCP comTCP = new CommunicationTCP(socket)){
               comTCP.write("Hello");
               System.out.println(comTCP.read());
            }

            //BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

            //out.println("Hello!");
            //System.out.println(in.readLine());
            //socket.close();


        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
