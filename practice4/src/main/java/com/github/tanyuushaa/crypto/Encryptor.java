package com.github.tanyuushaa.crypto;

import com.github.tanyuushaa.core.Message;

public interface Encryptor {
    byte[] encrypt(Message message);
}
