package com.github.tanyuushaa.core;

public class PacketBuilder {
    public static byte[] buildPacket(Message message) {
        byte[] typeBytes = intToBytes(message.getType());
        byte[] userIdBytes = intToBytes(message.getUserId());
        byte[] playload = message.getEncryptedMessage();
        byte[] packet = new byte[8 + playload.length];
        System.arraycopy(typeBytes, 0, packet, 0, typeBytes.length);
        System.arraycopy(userIdBytes, 0, packet, typeBytes.length, userIdBytes.length);
        System.arraycopy(playload, 0, packet, typeBytes.length + userIdBytes.length, playload.length);

        return packet;
    }

    private static byte[] intToBytes(int i) {
        return new byte[] {
                (byte) (i >>> 24),
                (byte) (i >>> 16),
                (byte) (i >>> 8),
                (byte) i
        };
    }
}
