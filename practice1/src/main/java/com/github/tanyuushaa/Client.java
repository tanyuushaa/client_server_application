package com.github.tanyuushaa;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

//encode
public class Client {
    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static byte[] encode(CreateProduct message) {
        byte[] messageBytes = mapper.writeValueAsBytes(message);
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
}
