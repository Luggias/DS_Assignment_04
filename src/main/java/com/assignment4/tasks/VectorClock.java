package com.assignment4.tasks;

import java.util.Arrays;

public class VectorClock {

  private final int[] timestamps;

  public VectorClock(int numOfClients) {
    timestamps = new int[numOfClients];
    Arrays.fill(timestamps, 0);
  }

  public synchronized void setVectorClock(int processId, int time) {
    // TODO: Set the vector clock value for the processId
      timestamps[processId] = time;
  }

  public synchronized void tick(int processId) {
    // TODO: Increment the vector clock value for the processId
      timestamps[processId-1] += 1;
  }

  public synchronized int getCurrentTimestamp(int processId) {
    return timestamps[processId];
  }

  public synchronized void updateClock(VectorClock other) {
    // TODO: Update the vector clock based on the values of another vector clock
      for (int i = 0; i < timestamps.length; i++) {
          timestamps[i] = Math.max(timestamps[i], other.timestamps[i]);
      }
  }

  public synchronized String showClock() {
    return Arrays.toString(timestamps);
  }

  // TODO:
  // For Task 2.2
  // Check if a message can be delivered or has to be buffered
  public synchronized boolean checkAcceptMessage(int senderId, VectorClock senderClock) {
    if( timestamps[senderId]+1== senderClock.timestamps[senderId]) {
        return true;
    }
    return false;

  }
}
