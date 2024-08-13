package com.khanhbq.zmq.pubsub;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ChatClient {

    public static void main(String[] args) {

        try (ZContext context = new ZContext()) {
            // Create a Subscriber socket
            ZMQ.Socket socket = context.createSocket(SocketType.SUB);
            socket.connect("tcp://localhost:5555");
            socket.subscribe(""); // Subscribe to all messages

            Thread listener = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    String message = socket.recvStr(); // Receive message
                    System.out.println("Received: " + message);
                }
            });

            listener.start();

            System.out.println("Connected to the chat server");

            try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
                while (!Thread.currentThread().isInterrupted()) {
                    String input = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(input)) {
                        break;
                    }
                    // Here, clients do not send messages to the server, but they could if needed
                }
            }

            listener.interrupt(); // Stop the listener thread
        }
    }
}
