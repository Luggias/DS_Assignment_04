package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.assignment4.tasks.UdpServer.isInteger;

public class ServerThread implements Runnable {

  // Logger for logging information and errors
  private static final Logger LOGGER = Logger.getLogger(ServerThread.class.getCanonicalName());
  
  // Thread pool for handling tasks concurrently, with a fixed pool size of 4 threads corresponding to the maximum of 4 clients
  private static final ExecutorService executor = Executors.newFixedThreadPool(4);

  // Datagram socket to send and receive packets
  private final DatagramSocket socket;
  private final DatagramPacket receivePacket; // Packet received from the client
  private final InetAddress clientIP; // IP address of the client
  private final int clientPort; // Port number of the client

  private byte[] sendData = new byte[1024]; // Buffer for sending data

  // Constructor to initialize the ServerThread with necessary parameters
  public ServerThread(
      DatagramSocket serverSocket,
      DatagramPacket receivePacket,
      InetAddress clientIP,
      int clientPort) {
    this.socket = serverSocket;
    this.receivePacket = receivePacket;
    this.clientIP = clientIP;
    this.clientPort = clientPort;
  }

  @Override
  public void run() {
    String responseMessage = null;
    
    // Convert received packet data to a string and trim whitespace
    String messageFromClient =
        (new String(receivePacket.getData(), 0, receivePacket.getLength())).trim();

    if (!messageFromClient.isEmpty()) {
      // Split the received message into parts using ":" as a delimiter
      String[] receivedMessage = messageFromClient.split(":");
      String receivedMessageBody = receivedMessage[0]; // First part of the message

      if (!receivedMessageBody.isEmpty()) {
        if (receivedMessageBody.equalsIgnoreCase("history")) {
          // If the message is "history", send scrambled chat history to the client
          LOGGER.log(Level.INFO, "Sending the chat history...");
          sendScrambledHistory();
        } else {
          // Log the received message and notify about broadcasting it
          LOGGER.log(Level.INFO, "Received message: " + messageFromClient);
          LOGGER.log(Level.INFO, "Broadcasting to connected clients: " + messageFromClient);

          // Prepare the data to be sent
          sendData = messageFromClient.getBytes();

          // Broadcast the message to all subscribed clients except the sender
          for (InetSocketAddress client : UdpServer.subscribedClients) {
            if (!client.getAddress().equals(clientIP) || client.getPort() != clientPort) {
              executor.submit(() -> {
                try {
                  // Introduce a random delay if Lamport Timestamp is used
                  if (isInteger(receivedMessage[1])) {
                    long randomDelay = (long) (Math.random() * 4000) + 1000;
                    TimeUnit.MILLISECONDS.sleep(randomDelay);
                  }

                  // Create and send the packet to the target client
                  DatagramPacket sendPacket =
                      new DatagramPacket(sendData, sendData.length, client.getAddress(), client.getPort());
                  socket.send(sendPacket);
                } catch (IOException | InterruptedException e) {
                  e.printStackTrace(); // Print stack trace for exceptions
                }
              });
            }
          }
        }
      }
    }
  }

  // Method to shut down the executor service gracefully
  public static void shutdownExecutor() {
    executor.shutdown();
    try {
      if (!executor.awaitTermination(60, TimeUnit.SECONDS)) { // Wait for tasks to finish
        executor.shutdownNow(); // Force shutdown if timeout is reached
      }
    } catch (InterruptedException e) {
      executor.shutdownNow(); // Force shutdown if interrupted
    }
  }

  // Method to send the history for Task 2.2 
  private void sendScrambledHistory() {
    List<String> scrambledHistory =
        new ArrayList<>(
            Arrays.asList(
                "A:[1, 0, 0, 0]:1",
                "B:[1, 1, 0, 0]:2",
                "D:[2, 1, 1, 0]:1",
                "C:[1, 1, 1, 0]:3",
                "E:[2, 2, 1, 0]:2",
                "F:[3, 2, 1, 0]:1"));

    for (String message : scrambledHistory) {
      try {
        // Convert the message to bytes and send it to the client
        sendData = message.getBytes();
        DatagramPacket sendPacket =
            new DatagramPacket(sendData, sendData.length, clientIP, clientPort);
        socket.send(sendPacket);
        Thread.sleep(200); // Short delay between sending each message
      } catch (IOException | InterruptedException e) {
        e.printStackTrace(); // Print stack trace for exceptions
      }
    }
  }
}
