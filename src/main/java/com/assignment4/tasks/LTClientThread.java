package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.TreeMap;

// This Class handles the continuous listening for incoming messages from the server
public class LTClientThread implements Runnable {

  private final DatagramSocket clientSocket;
  private final LamportTimestamp lc;
  byte[] receiveData = new byte[1024];

  public LTClientThread(DatagramSocket clientSocket, LamportTimestamp lc) {
    this.clientSocket = clientSocket;
    this.lc = lc;
  }

  @Override
  public void run() {
    // TODO:
    // Write your code here to continuously listen for incoming messages from the server and display them.
    // Make use of the Datagram sockets and functions in Java https://docs.oracle.com/javase/8/docs/api/java/net/DatagramSocket.html
    while (clientSocket.isConnected()) {
      try {
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
        String[] partitions = receivedMessage.split(":");

        String message = partitions[0];
        int receivedTimestamp = Integer.parseInt(partitions[1]);
        int senderID = Integer.parseInt(partitions[2]);

        lc.updateClock(receivedTimestamp);
        System.out.println("Client"+ senderID +  ":" + message + ":" + receivedTimestamp);

        // TODO:
        // Update the clock based on the timestamp received from the server.
        System.out.println("Current clock:" + lc.getCurrentTimestamp());

      } catch (IOException e) {
        break;
      }
    }


  }
}