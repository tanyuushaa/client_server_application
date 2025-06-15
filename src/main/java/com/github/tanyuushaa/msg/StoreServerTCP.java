package com.github.tanyuushaa.msg;

import com.github.tanyuushaa.core.Message;
import com.github.tanyuushaa.crypto.Decryptor;
import com.github.tanyuushaa.crypto.MessageDecryptor;
import com.github.tanyuushaa.process.Processor;
import com.github.tanyuushaa.process.WarehouseProcessor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

public class StoreServerTCP {
    private final int port = 8080;
    //private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    //private final BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("TCP-Server started on port " + port);
//            for(int i = 0; i < 5; i++) {
//                executorService.submit(new MessageProcessor(
//                        queue,
//                        new MessageDecryptor(),
//                        new WarehouseProcessor(),
//                        new MessageEncryptor(),
//                        new FakeSender()
//                ));
//            }
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("TCP-Server client conected " + socket.getInetAddress());
                //executorService.submit(() -> handleCient(socket));
                handleCient(socket);
            }
        } catch (IOException e) {
            System.err.println("TCP-Server failed to listen on port " + e.getMessage());
        }
    }

    private void handleCient(Socket socket) {
        try (CommunicationTCP com = new CommunicationTCP(socket)) {
            String receivedBase64;
            while ((receivedBase64 = com.read()) != null) {

                byte[] receivedBytes = Base64.getDecoder().decode(receivedBase64);

                Decryptor decryptor = new MessageDecryptor();
                Message request = decryptor.decrypt(receivedBytes);
                request.setAddress(socket.getInetAddress());
                System.out.println("TCP-Server received " + request);

                Processor processor = new WarehouseProcessor();
                Message responce = processor.process(request);

                responce.encrypt();
                byte[] responseByte = responce.getEncryptedMessage();

                String responseBase64 = new String(Base64.getEncoder().encodeToString(responseByte));
                com.write(responseBase64);
                System.out.println("TCP-Server received " + responseBase64);
            }


        } catch (IOException e) {
            System.err.println("TCP-Server failed to receive data from client " + e.getMessage());

        }
    }
    public static void main(String[] args) {
        new StoreServerTCP().start();
    }
}
