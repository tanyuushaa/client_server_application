package com.github.tanyuushaa;

import com.github.tanyuushaa.core.Command;
import com.github.tanyuushaa.core.CryptoUtils;
import com.github.tanyuushaa.core.Message;
import com.github.tanyuushaa.core.MessageBuilder;
import com.github.tanyuushaa.crypto.Decryptor;
import com.github.tanyuushaa.crypto.MessageDecryptor;
import com.github.tanyuushaa.msg.FakeReceiver;
import com.github.tanyuushaa.msg.FakeSender;
import com.github.tanyuushaa.msg.Receiver;
import com.github.tanyuushaa.msg.Sender;
import com.github.tanyuushaa.process.Processor;
import com.github.tanyuushaa.process.WarehouseProcessor;

import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // побудова message
        Command command = Command.ADD_STOCK;
        int type = command.ordinal();
        Message message = new MessageBuilder()
                .type(type)
                .userId(1001)
                .message("apple 20")
                .build();
        System.out.println(message.getType());
        System.out.println(message.getUserId());
        System.out.println(message.getMessage());
        System.out.println(message.toString());

        // шифрування і дешифрування
        String msg = "ADD_STOCK apple 30";
        byte[] encrypted = CryptoUtils.encrypt(msg);
        System.out.println(java.util.Base64.getEncoder().encodeToString(encrypted));
        String decrypted = CryptoUtils.decrypt(encrypted);
        System.out.println(decrypted);

        // генерація повідомлення
        BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();
        Receiver receiver = new FakeReceiver(queue);
        for (int i = 0; i < 5; i++) {
            receiver.receiveMessage();
            Thread.sleep(500);
        }
        System.out.println(queue.size());
        //System.out.println(message.getMessage());
        //message.encrypt();

        // дешифрування
        Decryptor decryptor = new MessageDecryptor();
        receiver.receiveMessage();
        byte[] packet = queue.take();
        Message decryptedMessage = decryptor.decrypt(packet);
        System.out.println(decryptedMessage.getMessage());

        //обробка
        Processor processor = new WarehouseProcessor();
        Message messageBuilder = new MessageBuilder()
                .type(Command.ADD_STOCK.ordinal())
                .userId(42)
                .message("milk 1000")
                .build();
        Message response = processor.process(messageBuilder);
        System.out.println(response.getMessage());

        // шифрування тексту повідомлення перед відправкою
        Message responce = processor.process(decryptedMessage);
        byte[] responseByte = CryptoUtils.encrypt(response.getMessage());
        Sender sender = new FakeSender();
        sender.sendMessage(responseByte, InetAddress.getLoopbackAddress());
        System.out.println(queue.size());
    }
}