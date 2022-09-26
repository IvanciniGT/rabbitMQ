RabbitMQ: Messaging Broker

Cluster <<<
    Nodes
        Node1
        Node2
    Exchange ?      Route messages to Queues   [Types: Direct, Fanout, Topic, Header]
      x bind
    Queue
    Connection / Channel
    Publisher
    Subscriber/Consumer
    
RabbitMQ is going to be installed in a Production Environment:
- High Availability
- Scalability


RabbitMQ Cluster1
    Node 1 - IP1                    \   LoaD BaLancer - IP3
        Exchange1 -> Quorum Queue1 (MSG1, MSG2)
        Exchange2 -> Classic Queue2 (stored in Node1)
    Node 2 - IP2       v^           /
        Exchange1 -> Quorum Queue1 (MSG1, MSG2)
        Exchange2 -> Node 1

MSG1 and MSG2 -> QUEUE1

OPTION #1 Producer (C#, JAVA) --> IP3
OPCION #2 Producer (C#, JAVA) --> IP1, IP2, ...

Where does my exchange need to be defined? In every single Node
What about the queue? Where should It be?  In every single Node

    Exchange? Process for routing messages. OUTPUT -> LIST OF QUEUES.
    
I want to send MSG1. Does it make a difference if 
    I send MSG1 to EXCHANGE1 at Node 1 or Node2? It doesn't matter
    
What about the queues? Is it enough I my MSG1 is store in QUEUE1 at Node1? NO
When Queues receive new MSGs they need to AGREE in the order of the QUEUE

Quorum between the Queues

Every Single EXCHANGE is created and EXECUTED in all of the NODEs


---
If a I delete a container, Its filesystem gets deleted.... Is that OK?

RabbitMQ v3.0.7 >> Remove this container
RabbitMQ v3.0.8 >> Create a new container with the new Image... That's all.


Persistent Volume? Folder from the external FileSystem (HOST FS) 
                    that we can MOUNT into the CONTAINER's FS
                    
                    
POSIX?
/
/home           user folders
/root           root user folder
/bin            commands / binaries
/opt            software
/var            software persistent information
/etc            configuration files
/tmp            Temporary files
/mnt            Mount points

What is UNIX®? POSIX & SUS < Austin Group

What was UNIX®? UNIX was an Operating System.... Lab. Bells @ AT&T 
    +400 versions of UNIX 

2 Standart
Oracle - Solaris (UNIX®)
HP     - HP-UX   (UNIX®)
IBM    - AIX     (UNIX®)
Apple  - MacOS   (UNIX®)
         Oracle Unbreakable Linux

Berkley University (California) 386-BSD > 4 years of legal troubles...  
BSD family:
    netBSD
    freeBSD
    MacOS

GNU (Richard Stallman): GNU is Not Unix
    gnome
    gedit
    gcc
    bash
    chess
    NO KERNEL !!!!!!

Linus Torvalds: Linus' UNIX : Linux

GNU/Linux -> Linux
    RedHat
    Suse
    Debian -> Ubuntu

Android -> Linux + Google libs



4040
8080
8086
286
386 <
486
Pentium
P DualCore
i3
i5
i7
i9

