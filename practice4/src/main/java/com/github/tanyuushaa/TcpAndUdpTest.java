package com.github.tanyuushaa;

import com.github.tanyuushaa.core.CryptoUtils;
import com.github.tanyuushaa.core.Message;
import com.github.tanyuushaa.core.MessageBuilder;
import com.github.tanyuushaa.msg.CommunicationTCP;

import java.io.IOException;
import java.net.*;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpAndUdpTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 5; i++) {
            int clientId = i + 1;
            executorService.submit(() -> runTCPClient(clientId));
        }

        for (int i = 0; i < 5; i++) {
            int clientId = i + 6;
            executorService.submit(() -> runUDPClient(clientId));
        }

        executorService.shutdown();
    }

    private static void runTCPClient(int id) {
        try (Socket socket = new Socket("localhost", 8080);
             CommunicationTCP com = new CommunicationTCP(socket)) {

            String msg = "product" + id + " " + (id * 10);
            Message message = new MessageBuilder()
                    .type(2)
                    .userId(100 + id)
                    .message(msg)
                    .build();
            message.encrypt();

            byte[] encrypted = message.getEncryptedMessage();
            byte[] packet = buildPacket(message.getType(), message.getUserId(), encrypted);
            String base64 = Base64.getEncoder().encodeToString(packet);

            com.write(base64);
            System.out.println("TCP-Client " + id + " Send: " + msg);

            String response = com.read();
            if (response != null) {
                byte[] respBytes = Base64.getDecoder().decode(response);
                String decrypted = CryptoUtils.decrypt(respBytes);
                System.out.println("TCP-Client " + id + " Answer: " + decrypted);
            }

        } catch (IOException e) {
            System.err.println("TCP-Client " + id + " Error: " + e.getMessage());
        }

    }

    private static void runUDPClient(int id) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(2000);

            String msg = "item" + id + " " + (id * 5);
            Message message = new MessageBuilder()
                    .type(2)
                    .userId(200 + id)
                    .message(msg)
                    .build();
            message.encrypt();

            byte[] packet = buildPacket(message.getType(), message.getUserId(), message.getEncryptedMessage());
            String encoded = Base64.getEncoder().encodeToString(packet);
            byte[] toSend = encoded.getBytes();

            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket request = new DatagramPacket(toSend, toSend.length, address, 8081);
            DatagramPacket response = new DatagramPacket(new byte[1024], 1024);

            socket.send(request);
            System.out.println("UDP-Client " + id + " Send: " + msg);

            try {
                socket.receive(response);
                String base64Response = new String(response.getData(), 0, response.getLength());
                byte[] respBytes = Base64.getDecoder().decode(base64Response);
                String decrypted = CryptoUtils.decrypt(respBytes);
                System.out.println("UDP-Client " + id + " Answer: " + decrypted);
            } catch (SocketTimeoutException e) {
                System.err.println("UDP-Client " + id + " Time out.");
            }

        } catch (IOException e) {
            System.err.println("UDP-Client " + id + " Error: " + e.getMessage());
        }
    }

    private static byte[] buildPacket(int type, int userId, byte[] encryptedMsg) {
        byte[] packet = new byte[8 + encryptedMsg.length];
        packet[0] = (byte) (type >> 24);
        packet[1] = (byte) (type >> 16);
        packet[2] = (byte) (type >> 8);
        packet[3] = (byte) type;

        packet[4] = (byte) (userId >> 24);
        packet[5] = (byte) (userId >> 16);
        packet[6] = (byte) (userId >> 8);
        packet[7] = (byte) userId;

        System.arraycopy(encryptedMsg, 0, packet, 8, encryptedMsg.length);
        return packet;
    }
}
