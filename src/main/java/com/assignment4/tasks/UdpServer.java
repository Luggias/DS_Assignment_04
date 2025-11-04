package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UdpServer {

  // Set of subscribed clients (ensures no duplicate entries)
  static Set<InetSocketAddress> subscribedClients = new HashSet<>();

  // Utility method to check if a string can be parsed into an integer
  public static boolean isInteger(String s) {
    try {
      Integer.parseInt(s); // Attempt to parse the string as an integer
    } catch (NumberFormatException e) {
      return false; // Return false if parsing fails
    }
    return true; // Return true if parsing succeeds
  }

  public static void main(String[] args) {
    int port = 4040; // Port on which the server will listen for incoming packets

    // Create a DatagramSocket to listen on the specified port
    try (DatagramSocket serverSocket = new DatagramSocket(port)) {

      // Inform that the server is now active and ready to receive data
      System.out.println("Server is now Listening...");

      while (true) { // Infinite loop to keep the server running
        try {
          // Create a buffer to store received data
          byte[] receivedData = new byte[1024];

          // Create a DatagramPacket to receive data from clients
          DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);

          // Wait to receive data from a client (blocking call)
          serverSocket.receive(receivePacket);

          // Extract client's IP address and port from the received packet
          InetAddress clientIP = receivePacket.getAddress();
          int clientPort = receivePacket.getPort();

          // Subscribe the new client by adding its address to the set
          InetSocketAddress clientAddress = new InetSocketAddress(clientIP, clientPort);
          subscribedClients.add(clientAddress);

          // Start a new ServerThread to handle the received message from the client
          ServerThread server = new ServerThread(serverSocket, receivePacket, clientIP, clientPort);
          new Thread(server).start(); // Run the server thread on a new thread

        } catch (IOException e) {
          // Break out of the loop if there's an error with the server socket
          break;
        }
      }
    } catch (IOException e) {
      // Print the stack trace to help with debugging in case of an exception
      System.out.println(Arrays.toString(e.getStackTrace()));
    }
  }
}
