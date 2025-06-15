package com.github.tanyuushaa;

import com.github.tanyuushaa.core.CryptoUtils;
import com.github.tanyuushaa.core.Message;
import com.github.tanyuushaa.core.MessageBuilder;
import com.github.tanyuushaa.msg.CommunicationTCP;
import com.github.tanyuushaa.msg.StoreServerTCP;
import com.github.tanyuushaa.msg.StoreServerUDP;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.*;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.github.tanyuushaa.core.PacketBuilder.buildPacket;
import static org.junit.jupiter.api.Assertions.*;

public class StoreClientTest {
    @BeforeAll
    static void startServer() {
        Executors.newSingleThreadExecutor().submit(() -> {
            new StoreServerTCP().start();
        });
        Executors.newSingleThreadExecutor().submit(() -> {
            new StoreServerUDP().start();
        });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    void TCPClientTest(){
        try (Socket socket = new Socket("localhost", 8080); CommunicationTCP com = new CommunicationTCP(socket)) {
            Message message = new MessageBuilder()
                    .type(2)
                    .userId(11)
                    .message("milk 10")
                    .build();

            message.encrypt();

            byte[] packed = buildPacket(message.getType(), message.getUserId(), message.getEncryptedMessage());
            String base64 = Base64.getEncoder().encodeToString(packed);
            com.write(base64);

            String responseBase64 = com.read();
            assertNotNull(responseBase64, "TSP response is null");

            byte[] responseBytes = Base64.getDecoder().decode(responseBase64);
            String res = CryptoUtils.decrypt(responseBytes);
            assertTrue(res.contains("OK"));
        } catch (IOException e) {
            fail("TCP client failed " + e.getMessage());
        }
    }

    @Test
    void UDPClientTest(){
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000);
            Message message = new MessageBuilder()
                    .type(2)
                    .userId(11)
                    .message("milk 10")
                    .build();

            message.encrypt();

            byte[] packed = buildPacket(message.getType(), message.getUserId(), message.getEncryptedMessage());
            String base64 = Base64.getEncoder().encodeToString(packed);
            byte[] toSend = base64.getBytes();

            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket request = new DatagramPacket(toSend, toSend.length, address, 8081);
            socket.send(request);

            DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
            socket.receive(response);

            String responseBase64 = new String(response.getData(), 0, response.getLength());
            byte[] decrypted = Base64.getDecoder().decode(responseBase64);
            String res = CryptoUtils.decrypt(decrypted);

            assertNotNull(res, "UDP response is null");

            assertTrue(res.contains("OK"));
        } catch (SocketTimeoutException e) {
            fail("Time out.");
        } catch (IOException e) {
            fail("UDP client failed " + e.getMessage());
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

}
