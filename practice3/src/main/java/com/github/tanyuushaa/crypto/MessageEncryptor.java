package com.github.tanyuushaa.crypto;

import com.github.tanyuushaa.core.CryptoUtils;
import com.github.tanyuushaa.core.Message;

public class MessageEncryptor implements Encryptor {
    @Override
    public byte[] encrypt(Message message) {
        String plain = message.getMessage();
        return CryptoUtils.encrypt(plain);
    }
}
