# Regex

GO TO regex101.com

We will define a PATTERN.

Once we have a pattern we can check:
- wether a text matches a pattern
- Where a pattern appears inside a text

There are several syntax that we can use to deal with these patterns. 
The most widely implemented syntax for regex is PERL.

PATTERN is a set (usually a sequence) of SUBPATTERNS

SUBPATTERN: is a set of characters followed by a occurrency factor

SET OF CHARS:
    hello                       A text must contain those chars in that order... all of them
            Hi, hello, how are you doing?
                -----
    [abc]                       A text must contain any of those chars
            Hi, hello, how are you doing?
                           -
    [a-z]                       Any char between the "a" char and the "z" char (ASCII order)
                                abc.....mno....wyz. NO: á ç ê ò ü
    [a-c]
    [a-em-p]
    [a-zA-Z0-9]
    [a-zA-Z0-9ñê-]              Any char (upper or lower case), any digit and the dash
    .                           Any char... It doesn't matter at all
    [.] \.                      A dot '.'
    
HOW MANY TIMES THE SEQUENCE OF CHARS CAN / HAVE TO APPEAR IN THE TEXT
    Nothing                     Only once                                       1
    ?                           At most once.                                   0-1
    +                           At least once                                   1+
    *                           It doesn't matter how many times.               0+
    {3}                         3 times                                         3
    {3,7}                       From 3 to 7 times                               3-7
    {3,}                        At least 3 times                                3+
    
SPECIAL CHARS:
    ^                           Starts with
    $                           Ends with
    |                           or
    ()                          To group subpatters


Pattern for:
- Name of a person              [A-Z][a-z]+
        Hi Ivan. How are you doing?   
        -- ----  ---
        1   2     3
                                ^[A-Z][a-z]+$
        Hi Ivan. How are you doing?                                             x

- Name of a person with lastName(s)
                                ^[A-Z][a-z]+( [A-Z][a-z]+)+$
        Ivan Osuna Ayuste                                                       √

- A number between 0-20         ^([0-9]|1[0-9]|20)$
            I have 18 years old   
            0ab
            Hello 20
                                ^(1?[0-9]|20)$

- A number between 00-99                                [0-9]{2}
                   01   
                   34

- Email                         [a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+[.][a-z]+
                                                               \.   


## RabbitMQ Policies

2 kinds of policies:
- User policies             -> Regular users
- Operator policies         -> Administrators (RBMQ enforce these policies over User policies)

They work the same way.
It is just a set of arguments (properties) that we can apply to Queues or/and Exchanges.


Policy1:
- max-length
- dead-letter-exchange
- ....

Queue -> policy1            - We won't have a rigid bind

Each policy is created with a PATTERN (regex).
Any queue which name matches that pattern is binded with the POLICY automatically.

Each policy is going to have a priority. What is this for?
A queue or an exchange may match a bunch of policy's patterns.
Only the higher priotity policy is applied.

On top of that, Operator policies are enforced.

Policy1: ^ev[0-9]+$
- max-length: 100
- priority: 1

Policy1: ^ev1$
- max-length: 10
- priority: 2

Operator policy: .*   < Any queue or exchange
- max-length: 1000

Queue: ev2          
    max-length: 1000
    
Queue: ev1
    max-length: 1000
    

----
Classic Queues and Quourum Queues work almost the same way

What does it happen when a consumer reads a msg from a classic or quorum queue?
The message is deleted from the queue -> If I want a msg to be read by a bunch of consumers...
what should I do? I have to create a queue for each consumer (FANOUT EXCHANGE)

RabbitMQ has Stream Queues because of Kafka

A Stream queue can be used by a bunch of consumers. When a consumer reads a message, 
the message is not deleted from the queue. It remains in the queue.
- They have a much better performance
- They are a bit (just a bit) more complicated to use.

When I (as a consumer) connects against a Stream Queue, 
    I need to tell RabbitMQ where should Rabbit start sending messages
    - from the start
    - from the msg #92948
    
This is kind a log!!!!

Rabbit is going to store messages FOREVER in Stream Queues.... Is that OK?
Upss... we may get in troubles? The HDD can collapse.

Max length bytes: The max length of the queue....
                  If the queue reaches that size? Oldest messages will be deleted
                  
Max time retention: Once the message reaches that age, msg is removed from the queue.
UNITS (Y=Years, M=Months, D=Days, h=hours, m=minutes, s=seconds)

Messages are stored in files. The queue is a set of files. Each file is called a segment.

Max segment size in bytes:

Initial cluster size:   Number of copies of the queue for HA

Leader locator:         Where the master (primary) copy of the queue is going to live




RabbitMQ 
Producer -> msg -> Queue < - Connection(Channel) - Consumer  (subscribe)
                    Rabbit sends thru that connection every single msg that is received . PUSH !!!


----

Extract Transform Load

    Insurance Company: REALE 

    Production Environment
        Apps
         |
         |
         v
        DBs     <  Do I want information in this database from 3 years ago? Probably no. In any case.
         |        
         |           That information is dead... In not gonna change
         |
         |  ETL (each night, each week, each month)
         v
        DataWareHouse. Denormalized... to improve queries HUGE !!!! BIG DATA Infraestrutures
          ^      ^
    Maybe we have to keep that information (legal stuff)

    Analize data - Business Intelligence Department
                    BI
                    Data Mining
                    Machine learng 

# BIG DATA

Is when we need to deal with :
- A huge amount of information
- Extremly complex information
- Information which is created at a huge rate.

HUGE means that NO TRADITIONAL TECHNIQUES allows me to solve that problem.

You want to store a table with:
- 100 items                         EXCEL                               |
- 100.000 item                      MS ACCESS                           |
- 1.000.000 items                   MYSQL, MARIADB, POSTGRESQL          |   Vertical Scalability
- 10.000.000 items                  MS SQL SERVER                       |
- 200.000.000 item                  ORACLE !                            v
- 10.000.000.000 items              There is no computer that allows to store this amount


Downloaded a movie from the Internet  - 5 Gbs 
Pen drive: 16Gbs of memory... All free, just new !!!
Can I store his movie in that pendrive? Not so sure !!!!
    Why? It depends on the Format, File System
        FAT16 -> NTFS -> ?????
        
Clash royale
4 players... how many messages per second? 2x3 = 6 msgs x 4 = 24 msg/s
50.000 games in parallel= 1.250.000 msg/sec NO COMPUTER ALLOWs TO DO THAT


BIG DATA :
Huge amount of commodity hardware.  HADOOP.
    KAFKA
    MONGO
    CASSANDRA
    HBASE


                    E                                       T                          L

DB (Oracle) --> Extract ----> RABBIT STREAM QUEUE ---> TRANSFORMATION -> RABBIT STREAM -> LOAD -> DATAWAREHOUSE
                                                  ---> TRANSFORMATION -> ...-> DATASTORE 2
                            temporary storage for my data
                                etl.May.1
                                etl.May.2
ETL
TEL
TELT
ETLT

----

System where a queue with priorities works goods?

Hospital -> Pacients priorities 

APP -> Send emails
    Is my app going to send emails in a synchronous way?
    
    My app send different kind of email. 
        - Confirmation email                         2
        - Weekly summary                             1
        - Alarms TAKE ACTION !!!!!!                 10
                 NEW JOB PENDING
    
    LOGIN FORM 
    The user add his information -> I need to add that to my DB and send an email for confirmation
    I want asynchronous communication.
    My app (JAVA CODE)
        sendEmail.... This is going to be done in the background
    
    At certain point I may have a huge amount of email pending to be send
    And maybe the email server is down, is not answering
    
    Each message needs to be processes by different programs (consumers)
        SMS
        email
        stored DB
----
    header
                                20 x HDD
    EXCHANGE FANOUT ---> QUOURUM QUEUE (priority)   SMS
                         QUOURUM QUEUE (priority)   EMAIL
                         QUOURUM QUEUE (priority)   STORED DB
    
    EXCHANGE HEADER ---> STREAM QUEUE 1             SMS *           +24 hours x 50€ = 1250 €
             TOPIC       STREAM QUEUE 2             EMAIL
                         STREAM QUEUE 3             STORED DB                         
----

HTTP REST protocol RABBITMQ... JSON
