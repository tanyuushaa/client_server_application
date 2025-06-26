package com.github.tanyuushaa.msg;

import java.net.InetAddress;

public interface Sender {
    void sendMessage(byte[] bytes, InetAddress address);
}
