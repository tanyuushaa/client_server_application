package com.github.tanyuushaa;

import java.net.InetAddress;
import java.util.Base64;

public class FakeSender implements Sender {
    @Override
    public void sendMessage(byte[] bytes, InetAddress address) {
        String encoded = Base64.getEncoder().encodeToString(bytes);
        System.out.println(address.getHostAddress() + ": " + encoded);
    }
}
