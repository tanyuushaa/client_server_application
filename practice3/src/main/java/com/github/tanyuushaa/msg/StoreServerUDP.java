package com.github.tanyuushaa.msg;

import com.github.tanyuushaa.core.Message;
import com.github.tanyuushaa.crypto.MessageDecryptor;
import com.github.tanyuushaa.process.WarehouseProcessor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Base64;

public class StoreServerUDP {
    private static final int PORT = 8081;
    private static final int BUFFER_SIZE = 1024;

    public void start() {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("UDP-Server started on port " + PORT);
            byte[] buffer = new byte[BUFFER_SIZE];
            while(true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                String base64 = new String(packet.getData(), 0, packet.getLength());
                byte[] receivedBytes = Base64.getDecoder().decode(base64);

                MessageDecryptor decryptor = new MessageDecryptor();
                Message request = decryptor.decrypt(receivedBytes);
                request.setAddress(address);
                System.out.println("UDP-Server received request: " + request);

                Message response = new WarehouseProcessor().process(request);
                response.encrypt();

                byte[] encryptedResponse = response.getEncryptedMessage();
                String encodedResponse = Base64.getEncoder().encodeToString(encryptedResponse);
                byte[] responseBytes = encodedResponse.getBytes();

                DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length, address, port);
                socket.send(responsePacket);
                System.out.println("UDP-Server reply sent: " + address.getHostAddress() + ":" + port);
            }
        } catch (Exception e) {
            System.err.println("UDP-Server error: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        new StoreServerUDP().start();
    }
}
