package com.assignment4.tasks;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class VectorClientThread implements Runnable {

  private final DatagramSocket clientSocket;
  private final VectorClock vcl;
  private final int id;
  private final byte[] receiveData = new byte[1024]; // Buffer for incoming data
  private final List<Message> buffer = new ArrayList<>(); // This buffer can be used for Task 2.2

  public VectorClientThread(DatagramSocket clientSocket, VectorClock vcl, int id) {
    this.clientSocket = clientSocket;
    this.vcl = vcl;
    this.id = id;
  }

  @Override
  public void run() {

  // TODO:
  /*
      Write your code here to continuously listen for incoming messages from the server
      You should first process the received message and then update the vector clock based on the received message (you can use .replaceAll("[\\[\\]]", "").split(",\\s*"); to split a received vector clock into its components)
      Then display the received message and its vector clock
  */
  while (true) {
      try {
          DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
          clientSocket.receive(receivePacket);

          String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
          String[] partitions = receivedMessage.split(":");

          String messageBody = partitions[0];
          String clockString = partitions[1]; // Z.B.: "[1, 0, 0, 0]"
          int senderID = Integer.parseInt(partitions[2]);

          String[] timestampsStr = clockString.replaceAll("[\\[\\]\\s]", "").split(","); // Entferne Klammern und Leerzeichen und splitte bei Komma
          VectorClock receivedClock = new VectorClock(timestampsStr.length);

          for (int i = 0; i < timestampsStr.length; i++) {
              receivedClock.setVectorClock(i, Integer.parseInt(timestampsStr[i]));
          }

          Message message = new Message(messageBody, receivedClock, senderID);
          displayMessage(message);

      } catch (IOException e) {
          break;
      }
  }
  }

// TODO:
/*
    This method should print out the message (e.g. Client 1: Hello World!: [1, 0, 0]) and update
    the vector clock without ticking on receive. Then it should display the the updated vector clock.
    Example: Initial clock [0,0,0], updated clock after message from Client 1: [1, 0, 0]
*/
  private void displayMessage(Message message) {
      if (message == null) {
          return;
      }

      String initialClock = vcl.showClock();
      vcl.updateClock(message.getClock());
      System.out.println("Current clock: " + vcl.showClock());

  }
}
