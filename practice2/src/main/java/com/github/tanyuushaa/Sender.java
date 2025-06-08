package com.github.tanyuushaa;

import java.net.InetAddress;

public interface Sender {
    void sendMessage(byte[] bytes, InetAddress address);
}
