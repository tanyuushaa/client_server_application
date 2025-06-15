package com.github.tanyuushaa.msg;

import com.github.tanyuushaa.core.CryptoUtils;
import com.github.tanyuushaa.core.Message;
import com.github.tanyuushaa.core.MessageBuilder;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Base64;
import java.util.Scanner;

public class StoreClientUDP {
    private final String host = "localhost";
    private final int port = 8081;
    private final int timeout = 5000;
    private final int maxRetry = 5;

    public void start(){
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(timeout);
            Scanner scanner = new Scanner(System.in);
            System.out.println("UDP-Client enter the command: ");
            int type = Integer.parseInt(scanner.nextLine());
            System.out.println("UDP-Client enter the query: ");
            String msg = scanner.nextLine();
            Message message = new MessageBuilder()
                    .type(type)
                    .userId(11)
                    .message(msg)
                    .build();
            message.encrypt();

            byte[] packetData = buildPacket(message.getType(), message.getUserId(), message.getEncryptedMessage());
            String encoded = Base64.getEncoder().encodeToString(packetData);
            byte[] toSend = encoded.getBytes();
            InetAddress address = InetAddress.getByName(host);
            DatagramPacket requestPacket = new DatagramPacket(toSend, toSend.length, address, port);
            byte[] buffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
            boolean success = false;
            int retry = 0;

            while (!success && retry < maxRetry) {
                socket.send(requestPacket);
                System.out.println("UDP-Client message sent. Attempt: " + (retry + 1));
                try {
                    socket.receive(responsePacket);
                    success = true;
                } catch (SocketTimeoutException e) {
                    System.out.println("UDP-Client timed out. Repeated attempt.");
                    retry++;
                }
            }
            if(success) {
                String base64Response = new String(responsePacket.getData(), 0, responsePacket.getLength());
                byte[] encryptedResponse = Base64.getDecoder().decode(base64Response);
                String decrypted = CryptoUtils.decrypt(encryptedResponse);
                System.out.println("UDP-Client decrypted: " + decrypted);
            } else {
                System.out.println("UDP-Client timed out. No response after the " + maxRetry + " attempts.");
            }
        } catch (Exception e) {
            System.err.println("UDP-Client error: " + e.getMessage());
        }
    }
    private byte[] buildPacket(int type, int userId, byte[] encryptedMsg) {
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
    public static void main(String[] args) {
        new StoreClientUDP().start();
    }
}
