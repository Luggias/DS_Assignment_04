# Vector Clock

This repository contains the project template for completing Task 1, Task 2 of Assignment 4. Execute the commands from the root folder BCS-DS-Assignment-Package/tasks.

> **_NOTE:_**
> After loading the project depending on which IDE (if using), you might see some file not found errors. However, they will disappear after you have successfully built the project using following command from the tasks folder.

## Table of Contents
- [Project Structure](#ProjectStructure)
- [Run Server](#run-server)
- [Task 1](#Task-1)
- [Task 2](#Task-2)

## Project Structure

```bash
src
    ├── main
    │   └── java
    │       └── com
    │           └── assignment4
    │               └── tasks
    │                   ├── LTClientThread.java
    │                   ├── LamportTimestamp.java (Lamports Timestamp implementaion)
    │                   ├── Message.java (Message format to be exchanged between server and the clients. This class can be instantiated to hold and access the content of the message and the timestamp)
    │                   ├── ServerThread.java (Server thread)
    │                   ├── UdpLTClient.java (UDP client to use lamports Timestamp implementation)
    │                   ├── UdpServer.java (Server main implementation)
    │                   ├── UdpVectorClient.java (UDP client to use vector clock)
    │                   ├── VectorClientThread.java
    │                   └── VectorClock.java (VectorClock implementation)
    └── test
        └── java
            └── com
                └── assignment4
                    └── test
                        ├── LamportTimestampTest.java (Test file for LogicalClock.java)
                        └── VectorClockTest.java (Test file for VectorClock.java)

```

## Run Server  

###### Note: Make sure you run the commands from the location of the "tasks" folder: C:\path\to\your\project\BCS-DS-Assignment-Package\tasks>


###### On MacOS:
```bash
./gradlew build  -x test
./gradlew run -PchooseMain="com.assignment4.tasks.UdpServer"  
```
###### On Windows:
```bash
.\gradlew build  -x test
.\gradlew run -PchooseMain="com.assignment4.tasks.UdpServer"  
```

## Task 1

In this task you will have to implement the Lamport Timestamp algorithm for message ordering in a simple chat application.  
To do this, you should complete these classes:  

- [LamportTimestamp](tasks/app/src/main/java/com/assignment4/tasks/LamportTimestamp.java)
- [UdpLTClient](tasks/app/src/main/java/com/assignment4/tasks/UdpLTClient.java)
- [LTClientThread](tasks/app/src/main/java/com/assignment4/tasks/LTClientThread.java)

1. Test your Lamport Timestamp implementation (tests the LamportTimestamp.java logic)
###### On MacOS:
```bash
./gradlew test --tests "LamportTimestampTest"
```
###### On Windows:
```bash
.\gradlew test --tests "LamportTimestampTest"
```

2. Run your Lamport Timestamp client
###### On MacOS:

```bash
./gradlew run -PchooseMain="com.assignment4.tasks.UdpLTClient" --console=plain 
```

###### On Windows:
```bash
.\gradlew run -PchooseMain="com.assignment4.tasks.UdpLTClient" --console=plain 
```

## Task 2.1

In task you implement the same functionality as in task 1, but now using Vector Clocks instead.  
To do this, you should complete these classes:

- [VectorClock](tasks/app/src/main/java/com/assignment4/tasks/VectorClock.java)
- [UdpVectorClient](tasks/app/src/main/java/com/assignment4/tasks/UdpVectorClient.java)
- [VectorClientThread](tasks/app/src/main/java/com/assignment4/tasks/VectorClientThread.java)

## Task 2.2

Based on your code from Task 2.1, implement out-of-order message detection and buffering to ensure that messages are delivered in the correct order.
To do this, complete the checkAcceptMessage method in the [VectorClock](tasks/app/src/main/java/com/assignment4/tasks/VectorClock.java) class and additionally modify the code in [VectorClientThread](tasks/app/src/main/java/com/assignment4/tasks/VectorClientThread.java) to process the messages only when they are next in order.

1. Test your vector clock implementation (tests the VectorClock.java logic)
###### On MacOS:
```bash
./gradlew test --tests "VectorClockTest"
```
###### On Windows:
```bash
.\gradlew test --tests "VectorClockTest"
```

2. Run your vector clock client
###### MacOS
```bash
 ./gradlew run -PchooseMain="com.assignment4.tasks.UdpVectorClient" --console=plain 
 ```
###### Windows
```bash
 .\gradlew run -PchooseMain="com.assignment4.tasks.UdpVectorClient" --console=plain 
 ```