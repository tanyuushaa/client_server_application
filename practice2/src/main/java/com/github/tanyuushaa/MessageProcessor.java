package com.github.tanyuushaa;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class MessageProcessor implements Runnable {
    private final BlockingQueue<byte[]> queue;
    private final Decryptor decryptor;
    private final Processor processor;
    private final Encryptor encryptor;
    private final Sender sender;

    public MessageProcessor(BlockingQueue<byte[]> queue, Decryptor decryptor, Processor processor, Encryptor encryptor, Sender sender) {
        this.queue = queue;
        this.decryptor = decryptor;
        this.processor = processor;
        this.encryptor = encryptor;
        this.sender = sender;
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] msg = queue.take();
                Message message = decryptor.decrypt(msg);
                Message processedMessage = processor.process(message);
                processedMessage.encrypt();
                byte[] packet = PacketBuilder.buildPacket(processedMessage);
                sender.sendMessage(packet, processedMessage.getAddress());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
