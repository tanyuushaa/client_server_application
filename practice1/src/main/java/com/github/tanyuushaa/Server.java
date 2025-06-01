package com.github.tanyuushaa;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

//decode
public class Server {
    private static final ObjectMapper mapper = new ObjectMapper();

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
        byte[] plain = Message.decrypt(messageBytes);
        return mapper.readValue(plain, CreateProduct.class);
    }
}
