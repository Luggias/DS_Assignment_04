# Assignment 4

## Team Members
 - Amelia Gasser
 - Lukas Kapferer
 - Paul Kottmann

## GitHub link to your (forked) repository (if submitting through GitHub)
 - https://github.com/Luggias/DS_Assignment_04.git

...

### Task 2

1. Why did message D have to be buffered and can we now always guarantee that all clients
   display the same message order?
Message D had to be buffered because its vector timestamp showed that it depended on earlier messages that the client had not received yet. 
D had to wait until the missing messages arrived.
With this mechanism we can ensure that all clients show the same message order, but not necessarily a total global order. 
Independent messages can still appear in different orders on different clients, which is normal.

2. Note that the chat application uses UDP. What could be an issue with this design choice—and
   how would you fix it?
Using UDP can cause problems because UDP does not guarantee delivery or ordering. Messages can get lost or arrive out of order, which would break delivery if an important message never arrives.
A better solution would be to switch to TCP. 
   
### Task 3

1. What is potential causality in Distributed Systems, and how can you model it? Why
   “potential causality” and not just “causality”?
>

2. If you look at your implementation of Task 2.1, can you think of one limitation of Vector Clocks? How would you overcome the limitation?
>

3. Figure 4 shows an example of enforcing causal communication using Vector Clocks. You can find a detailed explanation of this example and the broadcast algorithm being used in
   the Distributed Systems book by van Steen and Tannenbaum (see Chapter 5.2.2, page 270). Would you achieve the same result if you used the same broadcast algorithm but replaced
   Vector Clocks with Lamport Clocks? If not, why not? Explain briefly. 
>
