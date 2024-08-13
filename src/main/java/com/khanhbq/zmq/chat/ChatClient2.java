package com.khanhbq.zmq.chat;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatClient2 {
    public static void main(String[] args) throws Exception {
        try (ZContext context = new ZContext()) {
            ZMQ.Socket dealer = context.createSocket(SocketType.DEALER);
            dealer.connect("tcp://localhost:5555");

            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter your name: ");
            String name = console.readLine().trim();

            System.out.println("Choose a chat room (0, 1, 2): ");
            int chatRoom = Integer.parseInt(console.readLine().trim());

            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            subscriber.connect("tcp://localhost:" + (5556 + chatRoom));
            subscriber.subscribe("");

            Thread listener = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    String received = subscriber.recvStr();
                    System.out.println(received);
                }
            });

            listener.start();

            System.out.println("You are now in chat room " + chatRoom);

            while (!Thread.currentThread().isInterrupted()) {
                String line = console.readLine();
                if ("exit".equalsIgnoreCase(line)) {
                    break;
                }
                dealer.send(chatRoom + " " + name + ": " + line);
            }

            listener.interrupt();
        }
    }
}
