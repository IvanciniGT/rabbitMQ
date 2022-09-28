# RABBITMQ CORE CONCEPTS
                             |                                       |
Producer/Publisher -> msg ---|---> Exchange -> routing --> queue < --|--- Consumer/Subscriber
                             |                                       |
                             |---------------------------------------|
                             |              RabbitMQ                 |

## Exchange

They route messages to queues.

Do we assign  queues to exchanges in advance? YES, that is called a bind.

### Exchange Types

- Direct:
    
    The publisher must add a routing key to each message, so that it gets used by the exchange in order to decide where 
    the message is gonna be placed (queue).

- Fanout
    
    It doesn't matter what the publisher says, the msg is going to be place in every single queue.   

- Topic

    Binds are defined using wildcars that allos msgs to be routed in a more flexible way... even to multiple queues.

- Header 

    The publisher must add 1 or more headers to each message, so that they are used by the exchange in order to decide where 
    the message is gonna be placed (queue). A message may go to 1 or multiple queues.
    
    Msg:    Size: Big
            Revlevance: Important
    
    Exchange1 ---bind: {Size: Big} -------------------------------    QUEUE1 
              ---bind: {Size: Big, Relevance: Important} ---------    QUEUE2
              ---bind: {Size: Small} -----------------------------    QUEUE2

                        Match: all headers have to match
                               any header need to match

Extra configuration:
- Auto-delete: The exchange is deleted when every single attached queue is deleted
- Durability: 
    - Durable:      The exchange survives a rabbitmq restart
    - Transient:    The exchange dies if the rabbitmq cluster is stopped or gets broken
- Internal:
    - Yes: Can only receive messages from other exchanges
    - No:  They can receive message from other exxchanges but also from producers

## Queue

A FIFO data structure where rabbit is going to store message until they are consumed

## AMQP 0.9 version

Protocol being used in these commications

# Connection

Between Producer -> Exchange
        and Consumer -> Queue

A connection consumes/requires a huge amount of resources

Inside a connection we open....

# Channels

We can open a bunch of channel within a connection.

# Message

Information that the producer may to supply:
- Exchange ?        
- Routing key
- Headers
- Message itself
- Additional Properties (metadata)
    - size
    - type of content

----

# Scenerio 1: Multiplayer Internet Game 

Clash royale: 2 vs 2... 4 people: 3 minutes + 2 extra match

                                RABBITMQ
    Player1         Direct.Exchange  ------  Queue1                              Player1
    Player2                          |-----  Queue2                              Player2                                         
    Player3                          |-----  Queue3                              Player3
    Player4                          |-----  Queue4                              Player4

    player1 -> I did throw the dragon!!!!
        How many messages needs to be sent? 3. <<<<< NOOOOOP We eant only 1 single message
        

                                RABBITMQ
    Player1         Fanout.Exchange.1.                                           Player1
    Player2                          |-----  Queue2                              Player2                                         
    Player3                          |-----  Queue3                              Player3
    Player4                          |-----  Queue4                              Player4

    play1 -> I did throw the dragon!!!!
    How many messages needs to be sent? Only 1... Do we like that behaviour? YES !!
    Who is going to receive the message? All 4 players? Do we like that behaviour? NOPP !!!!!!!!
    SOLUTION? How many exchanges do we need to solve this problem ? 4... Do we like this? PROBABLY NO !!!!
    
    
                                RABBITMQ
    Player1         Topic.Exchange.  |-----  Queue1                              Player1
    Player2                          |-----  Queue2                              Player2                                         
    Player3                          |-----  Queue3                              Player3
    Player4                          |-----  Queue4                              Player4
    
    play1 -> I did throw the dragon!!!!
    How many messages needs to be sent? Only 1... Do we like that behaviour? YES !!
    Who is going to receive the message? All 4 players? Only required players. GREAT !!!!!!!
    SOLUTION? How are we going to configure the system?
    
                        routing_key
            player1 ->  send.0.1.1.1            Who needs to receive the message.. Who did send the message
            player2 ->  send.1.0.1.1
            player3 ->  send.1.1.0.1 
            player4 ->  send.1.1.1.0
            
        binds
            send.1.*.*.*    - Queue1
            send.*.1.*.*    - Queue2
            send.*.*.1.*    - Queue3
            send.*.*.*.1    - Queue4
    
    This is a good solution to our problem !!!!
    We need only 1 single exchange with 4 binds. GOOD !!!!!
        
    Who is going to create the exchange and the queues...Also create the binds?
        A service in the main server controlling the game
        When? when the game starts
        Deleted when the game ends?
            Exchange? Autodelete?   YES
            Queues?   Autodelete?   NO 
                (We don't want to delete the queue if the player loses its signal)
                      Classic vs Quorum:  Classic
                      Durable or Transient: Transient
                      Time to live:     Could be between 3 and 5 minutes
                                        5 minutes  x 60 x 1000
                      Auto Expire:      If the player is not playing in more than 30s 
                      
        Who is going to delete the queues?  A service in the main server controlling the game
        
        Durability of the Queues and the Exchange? In case rabbitMQ gets OFFLINE do we still want these objects? NO


    
                                RABBITMQ
    Player1         Header.Exchange. |-----  Queue1                              Player1
    Player2                          |-----  Queue2                              Player2                                         
    Player3                          |-----  Queue3                              Player3
    Player4                          |-----  Queue4                              Player4
    
    play1 -> I did throw the dragon!!!!
    How many messages needs to be sent? Only 1... Do we like that behaviour? YES !!
    Who is going to receive the message? All 4 players? Only required players. GREAT !!!!!!!
    SOLUTION? How are we going to configure the system?
    
                        headers
            player1 ->  key: value            Who needs to receive the message.. Who did send the message
                        sendToPlayer2: True
                        sendToPlayer3: True
                        sendToPlayer4: True
            player2 ->  
                        sendToPlayer1: True
                        sendToPlayer3: True
                        sendToPlayer4: True
            player4 ->  
                        sendToPlayer1: True
                        sendToPlayer2: True
                        sendToPlayer3: True
    
        binds
            {sendToPlayer1: True}       Queue1
            {sendToPlayer2: True}       Queue2
            {sendToPlayer3: True}       Queue3
            {sendToPlayer4: True}       Queue4
    
    This is a good solution to our problem !!!!
    We need only 1 single exchange with 4 binds. GOOD !!!!!
        
    Who is going to create the exchange and the queues...Also create the binds?
        A service in the main server controlling the game
        When? when the game starts
        Deleted when the game ends?
            Exchange? Autodelete?   YES
            Queues?   Autodelete?   NO (We don't want to delete the queue if the player lose its signal)
        Who is going to delete the queues?  A service in the main server controlling the game
        
        Durability of the Queues and the Exchange? In case rabbitMQ gets OFFLINE do we still want these objects? NO

---- DONE !!!!!! We can make a game like clash royale.... or maybe not....  

## Scenery2

Credit card readers in a toll.. I want to pay
                
                < -----------Toll Bank------------------------- >
PRODUCER        < -------- RABBITMQ ---------- >                                                    sync communication
Card reader --> EXCHANGE--> PendingPaymentQueue < --   Program is going to look for Pending payments ----> Customer Bank
                    ^                                                            |
                    |                 REQUEUE                                    | No answer
                    +------------------------------------------------------------+               
 
 How many queues would we interesting? 1, as I draw it?
    Depending on the messages Count:
    - If I have not that much number of messages, 1 queue is Ok
    - If I have too many messages, Maybe I want a separate queue for those messages which have already been rejected (not processed)

PendingPaymentQueue: 
    Auto-Delete? NO, for sure !
    Classic or a Quorum: Quorum -> Durable
    Max length bytes: Space enough (depending on my HDD)
    Overflow Behaviour: I prefer to lose new Messages ** Not that important
    Dead letter exchange: Another exchange -> Another node

Exchange? Direct It is enough
    Auto-Delete? NO, for sure !

## Scenery 3

Commercial place

Fire detectors... WIFI - A bunch of them -> Producer EVENT ... FIRE !!!!!!!! -> ACTION !!!! For sure! ASAP !!!
 
In case of fire.... A protection system needs to be enabled!

-----|------
-----|------
    electro-valve -> WIFI - Network < ON OFF - Passive component


FD1         >>>                                         Q1                                          EV1 √
FD2                                                     Q2                                          EV2 √
FD3         >>>   EXCHANGE0 -- FIRE --> EXCHANGE1 >>>>> Q3.   <<<< PROTECTION SYSTEM >>>>>          EV3 x
...                         |           EXCHANGE2                                                   ...
FD(n)                       |                           Q(m).                                       EV(m) x
                            |                                            Synchonous
            Asynchronous    |
                            |
                            |                           QA                                          ALARM !!!!
                            |       -----------------------------------------------------------
                            |---->   ControlQueue.   < Depending on the mes >    A light ON in a control panel !!!
                                        1
                                    -----------------------------------------------------------
                                                Is going to open m threads...
                                                    each one is gonna try to send a sync msg to its own EV
                                                    once and again (in loop) till it answers
                                                
                                                What if the protection system is restarted?
                                                
                                                How many conenctions do my PS needs againt RABBIT? 1
                                                How many channels? m
    EXCHANGE0:
        TYPE: 
            HEADER:                     HEADERS
                bind:
                    EXCHANGE1           fire=true
                    
                    ControlQueue        firedetector=true
                                        alive=true
                                        x-match=any             # All the headers have to be matched
                                        x-match=all
                                    
                                        ROUTING_KEY
            TOPIC:
                    EXCHANGE1           firedetector.fire
                    ControlQueue        firedetector.*
                    
                    
            Routing keys consist of several word with a '.' between them
            An asterisk '*' may be used for 1 single word:
                firedetector.*          firedetector.fire       √
                                        firedetector.           √
                                        firedetector.fire.big   x
                firedetector.*.big
            An '#' can be used as a wildcard to include any number of words (even 0)    
                firedetector.#          firedetector.fire       √
                                        firedetector.           √
                                        firedetector.fire.big   √
        INTERNAL: FALSE
    EXCHANGE1. TYPE: FANOUT
               INTERNAL: TRUE 
    EXCHANGE2. TYPE: DIRECT? For sure !
               INTERNAL: FALSE
               
    When are Fire detectors going to send a message? When they detect fire ? Really?
        Would I like to receive a message even if there is no FIRE at all? FOR SURE !!!!
            Why? Is the detector working properly?
            
            
Q1
    Quorum: We cannot lose fire messages
    Max length: 1
    Dead letter exchange: YES !!!! Why? Audit
    
Control System Queue
    Quorum: We cannot lose messages
    Max length: 1 


HomeWork:
- Priorities
- Lazy Mode 

    Is the data important for me? YES (DURABLE QUEUE)
    What is more important ... the data or the performance? PERFORMANCE