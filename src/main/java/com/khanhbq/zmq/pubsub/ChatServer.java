package com.khanhbq.zmq.pubsub;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ChatServer {

    public static void main(String[] args) {

        try (ZContext context = new ZContext()) {
            // Create a Publisher socket
            ZMQ.Socket socket = context.createSocket(SocketType.PUB);
            socket.bind("tcp://*:5555");

            System.out.println("Chat server started on port 5555");

            try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
                while (!Thread.currentThread().isInterrupted()) {
                    String message = scanner.nextLine(); // Get input from the console
                    if ("exit".equalsIgnoreCase(message)) {
                        break;
                    }
                    // Publish the message to all subscribers
                    socket.send(message);
                }
            }
        }
    }
}
