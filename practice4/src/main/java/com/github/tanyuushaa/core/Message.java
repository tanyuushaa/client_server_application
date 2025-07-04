package com.github.tanyuushaa.core;

import java.net.InetAddress;

public class Message {
    private final int type; // команда
    private final int userId; // ідентифікатор користувача
    private String message; //запит
    private byte[] encryptedMessage; // шифрування
    private InetAddress address;

    public Message(MessageBuilder builder) {
        this.type = builder.type;
        this.userId = builder.userId;
        this.message = builder.message;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public byte[] getEncryptedMessage() {
        return encryptedMessage;
    }

    public void setEncryptedMessage(byte[] encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

    public void encrypt() {
        this.encryptedMessage = CryptoUtils.encrypt(this.message);
        this.message = null;
    }

    public void decrypt() {
        if (this.encryptedMessage != null)
            this.message = CryptoUtils.decrypt(this.encryptedMessage);
    }

    @Override
    public String toString() {
        return "Message [type=" + type + ", userId=" + userId + ", message=" + (message != null ? message : "[encrypted]") + "]";
    }
}

