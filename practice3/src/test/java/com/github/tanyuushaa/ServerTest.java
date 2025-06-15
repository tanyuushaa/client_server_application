//package com.github.tanyuushaa;
//
//import com.github.tanyuushaa.msg.CommunicationTCP;
//import com.github.tanyuushaa.prc.Server;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.net.Socket;
//
//class ServerTest {
//
//    @Test
//    void test() throws IOException {
//        Server server = new Server(8383);
//        CommunicationTCP clientComTCP = new CommunicationTCP(new Socket("localhost", 8383));
//        CommunicationTCP serverComTCP = server.listen();
//
//        new Thread(){
//            @Override
//            public void run() {
//                clientComTCP.write("hello");
//            }
//        }.start();
//
//        Assertions.assertThat(serverComTCP.read()).isEqualTo("hello");
//    }
//}