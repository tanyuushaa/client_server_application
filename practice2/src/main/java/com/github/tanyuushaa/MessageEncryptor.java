package com.github.tanyuushaa;

public class MessageEncryptor implements Encryptor {
    @Override
    public byte[] encrypt(Message message) {
        String plain = message.getMessage();
        return CryptoUtils.encrypt(plain);
    }
}
