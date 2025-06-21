package com.github.tanyuushaa.crypto;

import com.github.tanyuushaa.core.Message;

// дешифрування байтів
public interface Decryptor {
    Message decrypt(byte[] key);
}
