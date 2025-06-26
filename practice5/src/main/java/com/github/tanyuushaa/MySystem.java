package com.github.tanyuushaa;

import com.github.tanyuushaa.crypto.Decryptor;
import com.github.tanyuushaa.crypto.Encryptor;
import com.github.tanyuushaa.crypto.MessageDecryptor;
import com.github.tanyuushaa.crypto.MessageEncryptor;
import com.github.tanyuushaa.msg.FakeReceiver;
import com.github.tanyuushaa.msg.FakeSender;
import com.github.tanyuushaa.msg.Sender;
import com.github.tanyuushaa.process.MessageProcessor;
import com.github.tanyuushaa.process.Processor;
import com.github.tanyuushaa.process.WarehouseProcessor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class MySystem {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();

        Decryptor decryptor = new MessageDecryptor();
        Processor processor = new WarehouseProcessor();
        Encryptor encryptor = new MessageEncryptor();
        Sender sender = new FakeSender();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Thread receiverThread = new Thread(() -> {
            FakeReceiver fakeReceiver = new FakeReceiver(queue);
            while (!Thread.currentThread().isInterrupted()) {
                fakeReceiver.receiveMessage();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        receiverThread.start();

        for (int i = 0; i < 10; i++) {
            executorService.submit(new MessageProcessor(queue, decryptor, processor, encryptor, sender));
        }
        Thread.sleep(5000);

        receiverThread.interrupt();
        executorService.shutdown();
        if(!executorService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS))
            executorService.shutdownNow();
        queue.clear();
    }
}
