package com.github.tanyuushaa;

// дешифрування байтів
public interface Decryptor {
    Message decrypt(byte[] key);
}
