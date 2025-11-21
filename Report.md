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
   
   ***Answer:***
Message D had to be buffered because its vector timestamp showed that it depended on earlier messages that the client had not received yet. 
D had to wait until the missing messages arrived.
With this mechanism we can ensure that all clients show the same message order, but not necessarily a total global order. 
Independent messages can still appear in different orders on different clients, which is normal.

2. Note that the chat application uses UDP. What could be an issue with this design choice—and
   how would you fix it?

   ***Answer:***
Using UDP can cause problems because UDP does not guarantee delivery or ordering. Messages can get lost or arrive out of order, which would break delivery if an important message never arrives.
A better solution would be to switch to TCP. 
   
### Task 3

1. What is potential causality in Distributed Systems, and how can you model it? Why “potential causality” and not just “causality”?

> Two events are called _potentially causally_ related if one **may** have influenced the other process. This is monitored by the *happened-before* relations from Lamport:
  >- Inside of the **same process**: If event A happens before event B inside of the same process then **A -> B**. 
  >- Causally related via **message passing**: If a process sends a message in event A and another process receives that message in event B then we can say that A -> B
  >  - **Transitive relation** (Slide 24): When A happened before B and B happened before C, then A happened before C. A -> B and B -> C then A -> C. 
>  
> As we can see we call this **potential causality** because "->" only tells us that A could potentially have an effect on B. It does not actually prove it.
  This already showcases a limitation of Lamport Clocks, they cannot detect concurrency (||). So they can preserve causality but cannot fully model it.
  By using **Vector Clocks** we can model potential causality more precisely. When using vector clocks each process keeps a vector (with size n = #(clocks)) of counters for each process. 
  Therefore, vector clocks can detect causality and concurrency.

2. If you look at your implementation of Task 2.1, can you think of one limitation of Vector Clocks? How would you overcome the limitation?
> There are multiple limitations with vector clocks that become very obvious when working with them, just as we did in task 2. 
> When initiating multiple "run configurations" in IntelliJ (one for UdpServer and one for UdpVectorClient with multiple instances allowed) I was 
> already wondering, how we would add a 5th Client since in implementation vector clocks are realized as `int[4]`. This reflects a fundamental 
> limitation of vector clocks -> the number of processes must be constant and known in advnce. If we want to introduce an additional client at 
> runtime, all timestamps and local clocks would become invalid. This already leads me to the next limitation: with $n$ processes, every message 
> has to carry a vector of length $n$ and every clock must store and update $n$ entries. The state and message size grow linearly with the number 
> of processes which can become very expensive and very large systems.
> 
> These limitations can be overcome by dynamic vector clocks. We could add this flexibility to vector clocks by making the 
> vector a matrix with 2 columns. Each row stores:
   >- a process ID
   >- the latest known scalar clock value for that process
>
> Initially every process keeps only its own ID and counter. Whenever it receives a message it merges the senders clock by:
   >- updating existing rows with the maximum
   >- adding new rows for unknown process IDs
> This basically means, that the dynamic vector clock grows only with the number of processes it has actually heard from, instead of having 
> to store 0-values for unheard processes. Dynamic vector clocks preserve the same happened-before relationship, they just change the 
> representation. (I used the proposed document from TUM for this answer)

3. Figure 4 shows an example of enforcing causal communication using Vector Clocks. You can find a detailed explanation of this example and the broadcast algorithm being used in
   the Distributed Systems book by van Steen and Tannenbaum (see Chapter 5.2.2, page 270). Would you achieve the same result if you used the same broadcast algorithm but replaced
   Vector Clocks with Lamport Clocks? If not, why not? Explain briefly. 
> ***NO!***
> 
> Lamport clocks cannot enforce causal ordering because they do not capture the entire causal history of events.
> They can tell the order of causally related events, but not distinguish causality from concurrency. This basically means that under Lamport, 
> two different histories can produce the same timestamp. This is why a message may seem deliverable under Lamport, even though
> some messages, that where sent previously have not arrived yet. Therefore, we can also not postpone a delivery as in Figure 4.