package com.github.tanyuushaa.prc;

import com.github.tanyuushaa.msg.CommunicationTCP;

import java.io.*;
import java.net.ServerSocket;

public class Server {
    private ServerSocket ss;
    Server(int port) {
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CommunicationTCP listen() throws IOException {
        return new CommunicationTCP(ss.accept());
    }

//    public static void main(String[] args) {
//        try {
//            ServerSocket ss = new ServerSocket(8080);
//            //Socket s = ss.accept();
//
//            try (CommunicationTCP comTCP = new CommunicationTCP(ss.accept())) {
//                System.out.println(comTCP.read());
//                comTCP.write("ok");
//            }
//
//            //BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
//            //PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
//
//            //System.out.println(in.readLine());
//            //out.println("ok");
//            //s.close();
//            //ss.close();
//
//        } catch (IOException e) {
//            System.err.println(e);
//        }
//    }
}
