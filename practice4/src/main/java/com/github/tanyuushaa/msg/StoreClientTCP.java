package com.github.tanyuushaa.msg;

import com.github.tanyuushaa.core.CryptoUtils;
import com.github.tanyuushaa.core.Message;
import com.github.tanyuushaa.core.MessageBuilder;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;

public class StoreClientTCP {
    private final String host = "localhost";
    private final int port = 8080;

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try (Socket socket = new Socket(InetAddress.getByName(host), port)) {
                System.out.println("TCP-Client connected " + host + ":" + port);
                try (CommunicationTCP com = new CommunicationTCP(socket)) {

                    while (true) {

                        System.out.println("TCP-Client enter the command: ");
                        int type = Integer.parseInt(scanner.nextLine());
                        System.out.println("TCP-Client enter the query: ");
                        String msg = scanner.nextLine();

                        Message message = new MessageBuilder()
                                .type(type)
                                .userId(11)
                                .message(msg)
                                .build();

                        message.encrypt();
                        byte[] encrypted = message.getEncryptedMessage();
                        String base64Encoded = Base64.getEncoder().encodeToString(buildPacket(message.getType(), message.getUserId(), encrypted));
                        com.write(base64Encoded);
                        System.out.println("TCP-Client received the message: " + base64Encoded);

                        String responseBase64 = com.read();
                        if (responseBase64 == null) {
                            System.out.println("TCP-Client received a null response");
                            return;
                        }

                        byte[] responseEncrypted = Base64.getDecoder().decode(responseBase64);
                        String decrypted = CryptoUtils.decrypt(responseEncrypted);
                        System.out.println("TCP-Client received the decrypted message: " + decrypted);
                    }
                }

            } catch (ConnectException e) {
                System.out.println("TCP-Client connect failed. Retry after 5 seconds.");
                sleep(5000);
            } catch (IOException e) {
                System.err.println("TCP-Client error: " + e.getMessage());
                sleep(5000);
            } catch (NumberFormatException e) {
                System.out.println("TCP-Client try again.");
            }
        }
    }
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
        new StoreClientTCP().start();
    }
}
