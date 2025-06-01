package com.github.tanyuushaa;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void testEncodeClient_shouldEncodeToHexString() {
        CreateProduct message = new CreateProduct("test", 100.0);
        byte[] encoded = Client.encode(message);
        String hex = Client.bytesToHex(encoded);
        assertNotNull(hex);
        assertTrue(hex.matches("[0-9A-F]+"));
    }

    @Test
    void testDecodeService_shouldDecodeToOriginal() {
        CreateProduct original = new CreateProduct("test", 100.0);
        byte[] packet = Client.encode(original);
        CreateProduct decoded = Server.decode(packet);
        assertEquals(original, decoded);
    }

    @Test
    void whenMessageCorrupted_decodeThrowsException() {
        CreateProduct message = new CreateProduct("test", 100.0);
        byte[] packet = Client.encode(message);
        packet[0] ^= 0xFF;
        assertThrows(IllegalArgumentException.class, () -> Server.decode(packet));
    }

    @Test
    void testMessageEncryptAndDecrypt() throws Exception {
        String original = "test";
        byte[] encrypted = Message.encrypt(original.getBytes());
        byte[] decrypted = Message.decrypt(encrypted);
        assertEquals(original, new String(decrypted));
    }
}