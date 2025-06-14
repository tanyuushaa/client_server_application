package com.github.tanyuushaa.msg;

import com.github.tanyuushaa.core.Message;
import com.github.tanyuushaa.core.MessageBuilder;

import java.util.Queue;
import java.util.Random;

// генерація повідомлення
public class FakeReceiver implements Receiver {
    private final Queue<byte[]> queue;
    private final Random random = new Random();
    private final String[] products = {"apple", "sugar", "flour", "milk"};

    public FakeReceiver(Queue<byte[]> queue) {
        this.queue = queue;
    }

    // число у масив, формат BIG_ENDIAN
    private byte[] intToBytes(int i) {
        return new byte[] {
                (byte) (i >>> 24),
                (byte) (i >>> 16),
                (byte) (i >>> 8),
                (byte) i
        };
    }

    @Override
    public void receiveMessage() {
        int type = random.nextInt(products.length);
        int userId = 1000 + random.nextInt(100);
        String product = products[random.nextInt(products.length)];
        int quantity = 1 + random.nextInt(100);
        String text = product + " " + quantity + " " + userId;

        Message message = new MessageBuilder()
                .type(type)
                .userId(userId)
                .message(text)
                .build();

        message.encrypt();

        // байтове представлення + формування пакету
        byte[] typeBytes = intToBytes(message.getType());
        byte[] userIdBytes = intToBytes(message.getUserId());
        byte[] playload = message.getEncryptedMessage();
        byte[] packet = new byte[8 + playload.length];
        System.arraycopy(typeBytes, 0, packet, 0, typeBytes.length);
        System.arraycopy(userIdBytes, 0, packet, typeBytes.length, userIdBytes.length);
        System.arraycopy(playload, 0, packet, typeBytes.length + userIdBytes.length, playload.length);

        queue.add(packet);
        System.out.println(message);
    }
}
