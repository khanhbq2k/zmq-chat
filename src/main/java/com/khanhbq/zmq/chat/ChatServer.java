package com.khanhbq.zmq.chat;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ChatServer {
    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            ZMQ.Socket router = context.createSocket(SocketType.ROUTER);
            router.bind("tcp://*:5555");

            ZMQ.Socket[] chatRooms = new ZMQ.Socket[3];
            int basePort = 5556;
            for (int i = 0; i < chatRooms.length; i++) {
                chatRooms[i] = context.createSocket(SocketType.PUB);
                chatRooms[i].bind("tcp://*:" + (basePort + i));
            }

            System.out.println("Server is running...");

            while (!Thread.currentThread().isInterrupted()) {
                // Receive the message
                byte[] identity = router.recv(0);
                String message = router.recvStr();

                String[] parts = message.split(" ", 2);
                int roomIndex = Integer.parseInt(parts[0]);
                String text = parts[1];

                if (roomIndex >= 0 && roomIndex < chatRooms.length) {
                    // Forward message to the appropriate chat room
                    chatRooms[roomIndex].send(text);
                }
            }
        }
    }
}
