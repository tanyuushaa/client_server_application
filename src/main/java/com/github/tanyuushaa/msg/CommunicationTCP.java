package com.github.tanyuushaa.msg;

import java.io.*;
import java.net.Socket;

public class CommunicationTCP implements Closeable{

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public CommunicationTCP(Socket s) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
    }

    public String read() throws IOException {
        return in.readLine();
    }

    public void write(String str){
        out.println(str);
        out.flush();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
