# Why do we want something like RabbitMQ

It is a messaging broker: Kafka, ActiveMQ

Whatsapp is a messaging broker!!!!

Why do we need/want a messaging broker? Whenever we need an asynchronous communication

Communication:
- Syncronous: This is a phone call
              It requires 2 participants to be present 

                Participant 1 -----> Participant 2
                Me -------> Mom                     (phone)
- Asyncronous ********
                Me ----> Whatsapp <---- Mon
                         Queue Mom(conversation)
                         Queue Brother
                         Queue friend
                Does Whatsapp allow me to send a message to a bunch of queue at a time? YES

---
Why do we want an asynchronous communication?
- Functional requirements
- The information is not lost < Hardware (servers, networks) and software(program, service) are unreliable


# Scenerio 1: Supermaket... and you want to pay the cart.... 89€ ... and you decide to pay with your debit card.

Program in the supermaket computer -> Message (Please this person want to pay 89€ to me) -> A program in the bank
                It needs to wait for an answer. We can wait a little bit (timeout)
                If the bank doesn't answer... can I leave the supermarket with my cart? NO
    Kind of communication? It doens't need to be asynchronous (No functional requirement)
    In this case... Would it be a good Idea to have an asynchronous communication?
        If the bank program can be stressed at some point, It could not ansker new incomming messages....
        but Is it important for this scenerio? 

    Synchronous communication
        Me(Supermarket) -> payment request -> Bank
                              yes/no       <- (answer)          YES: Accepted       NO: Rejected
                                              (no answer)
        wait (timeout): Bank is not available... Try it later
        I decide to try it again... or not 
            YES...... and I send a new message.... and I wait ... and... TIMEOUT... no answer
            NO... I leave the supermarcket with no goods
            
    Asynchronous communication
        Me(Supermarket) -> payment request -> Messaging broker  < program > bank
            wait                           <- OK. got it!               
                                                                            ONLINE
                                                                    Yes
                                                                    No
                                                    Ok, got It!
        I'm going to be checking for an anwer....
        How long? Me ... a human 1 minute, 1 hour
            We may wait 5 minutes.
                            YES ... I can leave the superMarket with my cart
                            NO....  My cart remains there
                            If I have no answer.... 50 minutes... no answer?
                            Do I leave the supermarket? 
                                No... wait.... 
                                Yes... Are they going to allow me to get my buy with me? No...
                                            The don't know if the payment has been processed
                                    Problem???? At some point the payment is gonna be processed... 
                                            Buy I will not be there to get my buy
        
                                                                    
# Scenerio 2: I am travelng by car... And i get one of those toll railway. 

Program in the toll computer -> Message (Please this person want to pay 2€ to me) -> A program in the bank
    Does the program wait for an answer?
    If the bank doesn't answer... can I leave the toll with my car? YES !!!! It would be in troubles (toll company)
        Imagine I have a medical issue
    Tolls doent accept prepayment cards (debit card or credit card)
    Kind of communication? Asynchronous (Functional requirement)
    
    Toll -> messaging Broker < program > bank
     OK , you can go !!!!
---

- Keep the integrity of the information. Is rabbitMQ going to help with this? YES
---
Queue: Data structure
FIFO: First In, First Out
---
How do we create/design apps nowadays?
Favourite Arquitecture? We love Microservices!!!!!
Why?
- Microservices are more independent within each other... TRUE 
    - Best technology for each one
    - I can update/upgrade only 1 microservice
        - Release a new version of that microservice
          - Some programs will use that new version...
          - But I still can use the older version from other programs
    - Scalability is gonna be improved... I can make another copy of a service... Just that part of the system
            - Not the whole system

If I decide to create a monolithic app.... I can still have multiple teams... with different responsabilities....






-----

# RabbitMQ Main Objects/Concepts

## AMQP

Advanced Message Queuing Protocol

Current version of AMQP is 1
But rabbitMQ support version 0.9... and they have no interest at all in move to the next version. 
    It is not included in their roadmap

## Connection

A connection is created between a producer or consumer and rabbitMQ.

To open a connection is a heavy process... It consumes a lot of resources.

Sometimes we need a producer to send a bunch of messages in parallel to rabbitMQ.

We are not going to open multiple connections for this situation.... We said that they consume too many resources.

### Channel

A channel is opened sithin a connection, so a consumer or producer can send/receiver messages in parallel.

Inside a conenction we may have multiple channels.

This make sense if our app use multiple Threads to send/receive messages, each one may use its own channel.

All the channel will be tied to the same connection.

## Exchanges

They route messages to queues.

A rabbitMQ cluste may contain a bunch of exchanges.

Producers/Publishers need to inform the target Exchange.

## Queues

They hold messages till they are consumed.

## Routes

An algorith to decide the destination queues of a message which was sent to an Exchange

## Virtual Hosts

Is an isolated environment inside out rabbitmq cluster.
Inside each virtualhost, we define Users, Exchanges, Queues, ....


Web server: Apache httpd, nginx?
Host websites.

client -> request http(s)                       -> web server
                                                        Virtual Server1: Company 1
          http://mycompany1:80/index.html                     Context part of the URL /index.html
                              /contact.html                                           /contact.html
                              /services/booking.html                                  /services/booking.html
                                                             DocumentRoot: /var/www/mycompany1
                                                        Virtual Server1: Company 2
          http://mycompany2:80/index.html
                                                             DocumentRoot: /var/www/mycompany2

------

# Main RabbitMQ components
                                |                                   |
Publisher/Producer --publish-------> Exchange --Route-> Queue < ------ consume ------ Consumer 
       Connection               |                                   |                   Connection
        Multiple channel.       |                                   |                       Multiple channels
                                |                                   |
                                |-----------------------------------|
                                              RABBITMQ
                                              
---
With RabbitMQ we always use this Cluster Word. We always have a cluster. It may have only 1 single node, thats fine


# Exchanges

They route messages to queues... How many queues? Just 1 or multiple queues? 
MULTIPLE... or JUST ONE... depending on the configuratión

Exchange types              Pre-created exchanges in RabbitMQ
- Direct exchange           (AMQP default)      amq.direct
- Fanout exchange           amq.fanout
- Topic exchange            amq.rabbitmq.trace  amq.topic
- Headers exchange          amq.headers         amq.match

It doesn't matter the type of exchange, all of them can have additional configurations:
- Name
- Durability
    - Durable:      The exchange will survive a broker restart
    - Transient:    The exchange is going to die when the broker stops.
- Auto-Delete:
    The exchange is going to be deleted when all its bounded queues are deleted, automatically
- Internal: Whether or not, publisher can send messages to that exchange.
    That make sense? If the exchange doesn't allow messages from a publisher... only from other exchanges
**** - Arguments... We dont use arguments that much. (they are used by plugins)

- Bindings -> Against another exchange or against a queue
    Each binding is going to have a predefined routing algorithm

## Direct exchange

Routes messages to 1 single queue, depending on the "routing key" property of the message.

    Exchange        binds               Queues
    
    Exchange 1.     ---route1---              Queue1
                    ---route2---              Queue2
    
    Direct exhange                
                    
When do an incomming message be roued to only 1 queue or to the other or to no-one or to both

## Fanout exchange

It doesn't use the routing key or any other argument to decide where to route ( the destination)

    Exchange        binds               Queues
    
    Exchange 1.     ------              Queue1
                    ------              Queue2
    
    Fanout exchange                

A message is routed to every single bind in the exchange.... 


## Topic exchange

In this case, we will use routingkey. A messahe may have headers... (extra properties)
Binds are going to be defined by using wildcards.

    Exchange        binds                                       Queues
    
    Exchange 1.     ---destination.Europe.Madrid---                Madrid
                    ---destination.Europe.Rome  ---                Rome
                    ---destination.Europe.*     ---                Europe
    
    Topic exchange            
    
    message. routingkey: destination.Europe.Berlin      Europe
    message. routingkey: destination.Europe.Madrid      Madrid & Europe

----

## Headers exchange

In this case, we will use HEADERS. A messahe may have headers... (extra properties)
Binds are going to be defined by using wildcards.

    Exchange        binds                                       Queues
    
    Exchange 1.     ---destination.Europe.Madrid---                Madrid
                    ---destination.Europe.Rome  ---                Rome
                    ---destination.Europe.*     ---                Europe
    
    Topic exchange            
    
    message. HEADER: destination.Europe.Berlin      Europe
    message. HEADER: destination.Europe.Madrid      Madrid & Europe


                             binds (predefined)
                                -----------
                                 routed         Queue1
                                -----------
        Message ---> Exchange                   Queue2
                                -----------
                                                Queue3
                                -----------
                                
                                
                                First thing     Second thing
                                
                                
    
    
    Do you play any smartphone game? Clash Royale !!!!!! 
    
    Asynchronous communication 
    
     A(phone)                                     QueueC        
        Fireball!!!                           /
                        ExchangeA             -   QueueD        
                        (Fanout)              \
                                                  QueueB        
                                                    
                                                  QueueA
                                                 
    
                    When the game starts, a server software (At the company riding the game) is going to create:
                        QueueA(AD)      ExchangeA(AD)
                        QueueB(AD)      ExchangeB(AD)
                        QueueC(AD)      ExchangeC(AD)
                        QueueD(AD)      ExchangeD(AD)
    
    Synchronous
    Image you lost yor signal... just during 5 seconds.... It make sense if you lose the messages???? NOT AT ALL..
                    Everybody needs to receive the messages... ASAP
    
    
    