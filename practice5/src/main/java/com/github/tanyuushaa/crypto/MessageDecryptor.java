package com.github.tanyuushaa.crypto;

import com.github.tanyuushaa.core.CryptoUtils;
import com.github.tanyuushaa.core.Message;
import com.github.tanyuushaa.core.MessageBuilder;

import java.util.Arrays;

// дешифрування повідомлення
public class MessageDecryptor implements Decryptor {

    @Override
    public Message decrypt(byte[] key) {
        int type = bytesToInt(Arrays.copyOfRange(key, 0, 4));
        int userId = bytesToInt(Arrays.copyOfRange(key, 4, 8));
        byte[] encrypted = Arrays.copyOfRange(key, 8, key.length);
        // дешифрування
        String decrypted = CryptoUtils.decrypt(encrypted);

        // побудова message
        Message message = new MessageBuilder()
                .type(type)
                .userId(userId)
                .message(decrypted)
                .build();

        message.setEncryptedMessage(encrypted);
        //System.out.println(message);
        return message;
    }

    // байти в число BIG_ENDIAN
    private int bytesToInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8) |
                (bytes[3] & 0xFF);
    }


}
