package com.github.tanyuushaa;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class Main {
    //private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    //private static ObjectMapper objectMapper = new ObjectMapper();
    public static void main(String[] args) throws Exception {
        CreateProduct message = new CreateProduct("test", 100.0);
        byte[] out = Client.encode(message);
        System.out.println(Client.bytesToHex(out));
        CreateProduct decoded = Server.decode(out);
        System.out.println(decoded);

        String testMessage = "test";
        byte[] encrypted = Message.encrypt(testMessage.getBytes(StandardCharsets.UTF_8));
        System.out.println(Client.bytesToHex(encrypted));
        byte[] decrypted = Message.decrypt(encrypted);
        String result = new String(decrypted, StandardCharsets.UTF_8);
        System.out.println(result);
    }


    /*@SneakyThrows
    public static byte[] encode(CreateProduct message) {
        byte[] messageBytes = objectMapper.writeValueAsBytes(message);
        int messageSize = messageBytes.length + 4 + 4;
        int size = 1 + 1 + 8 + 4 + 2 + 2 + messageSize;
        ByteBuffer buffer = ByteBuffer.allocate(size).order(ByteOrder.BIG_ENDIAN);
        buffer.put((byte)0x13)
                .put((byte)1)
                .putLong(2)
                .putInt(messageSize)
                .putShort(Crc16.calculateCrc(buffer.array(), 0, 14))
                .putInt(3)
                .putInt(4)
                .put(messageBytes)
                .putShort(Crc16.calculateCrc(buffer.array(), 16, messageSize));
        return buffer.array();
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    @SneakyThrows
    public static CreateProduct decode(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
        byte bMagic = buffer.get();
        if (bMagic != 0x13) {
            throw new IllegalArgumentException();
        }
        byte bSrc = buffer.get();
        long bPktId = buffer.getLong();
        int wlen = buffer.getInt();
        short wCrc16 = buffer.getShort();
        short expectedCrc = Crc16.calculateCrc(buffer.array(), 0, 14);
        if (wCrc16 != expectedCrc)
            throw new IllegalArgumentException();
        int cType = buffer.getInt();
        int bUserId = buffer.getInt();
        int messageSize = wlen - 8;
        byte[] messageBytes = new byte[messageSize];
        buffer.get(messageBytes, 0, messageSize);
        short w2Crc16 = buffer.getShort(bytes.length - 2);
        short expectedCrc2 = Crc16.calculateCrc(buffer.array(), 16, wlen);
        if (w2Crc16 != expectedCrc2)
            throw new IllegalArgumentException();
        return objectMapper.readValue(messageBytes, CreateProduct.class);
    }*/
}