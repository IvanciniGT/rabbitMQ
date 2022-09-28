YAML: Is a markup language, same as JSON, XML, HTML

docker-compose < Another client for the Docker Daemon
docker

IMAGE:
rabbitmq:3.10-management
<------> <--------------->
  repo          tag


# Do we need a RabbitMQ Cluster in a production environment?

Yes... what kind of cluster? Depending on the situation.
- Active/active... that is what we created yesterday
- Active/Pasive... that is just a RabbitMQ and another RabbitMQ 


We need to ensure HA: ALWAYS < Cluster

We need to ensure scalability? ALWAYS? Not really... maybe
- No scalability
- Vertical scalability: MORE MACHINE !!!!!!
- Horizontal Scalability: MORE MACHINES !!!!! < Cluster

What kind of clusters do we have?
- Active/Active clusters: HA + Scalability
    We have N copies of the software working in parallel
- Active/Pasive clusters: HA + NO Scalability
    We have 2 copies of the software.... but only 1 running
    If that process goes down, the other one goes up !




Rabbit1
    Exchange1 ------> firstQueue (classic)
                 / /
Rabbit2         / /
    Exchange1  / /
                /
Rabbit3        /
    Exchange1 /


What do we achieve with Quorum Queues?

We are not going to lose information.
Quorum queues are always Persistent.

Messages are always going to be stored in the disk... Is that OK? YES
We want to make sure that we are not going to lose any message.

Is this going to impact us in any way?
- Performance. YES !!!!
- SIZE !!!!!

# QUEUE Extra Arguments:

- Classic & Quourum

- Time to live: TTL
- Auto Expire

- Max length    
- Max length bytes
- Overflow Behaviour

- Dead letter exchange
- Dead letter Routing Key

- Maximum Priority

- Lazy Mode

## Auto-Delete

    The queue will be deleted afte the last connection gets closed

## Time to live: TTL

    x-message-ttl       : NUMBER(milliseconds)
    The amount of time the msg will be available after ITS CREATION.
    After that the msg is going to be deleted.
    
## Auto Expire

    x-expires           : NUMBER(milliseconds)
    The amount of time the queue will be available after the LAST CONNECTION
    After that the queue is going to be deleted.

## Max length    

    x-max-length        : Number
    The maximum amount of messages the queue can hold
    By default, messages will be deleted by the HEAD of the QUEUE
    
    x-max-length: 3
        3>2>1
        +4
        4>3>2           1 is going to be deleted

## Max length bytes

    x-max-length-bytes  : Number(bytes)
    The maximum size the queue can grow.
    By default, messages will be deleted by the HEAD of the QUEUE once the queue gets to this size!
    
## Overflow Behaviour

    x-overflow:     STRING ( drop-head | reject-publish )

    x-overflow: reject-publish
    x-max-length: 3
        3>2>1
        +4
                4 is rejected

## Dead letter exchange

    x-dead-letter-exchange  : String(Valid EXCHANGE name)
    If a messge is rejected, or if expires.... 
    The message will be republished against the supplied EXCHANGE

## Dead letter Routing Key

    x-dead-letter-routing-key  : String( arbitraryValue)
    If a message is sent to the DEAD LETTER EXCHANGE, the routing key will be updated to this value

    Producer (MSG1, routing_key_1) -> EXCHANGE -routing_key_1-> QUEUE1
                                                                 | Reject MSG1 or MSG1 expired
                                                                 |
                                                                 x-dead-letter-routing-key
                                                                 |
                                                                 V
                                                                DEAD_LETTER_EXCHANGE
    
## Maximum Priority

    A queue can have priorities. If this argument is not set... It is not going to control those priorities
    But I a set a:

    x-max-priority:             NUMBER 1-255
    
    Messages may come with a "priority" set:
        "priority": 1
        "priority": 10
    
    x-max-priority: 5
    
    M1(priority:1)
        M2(priority:2)
            M3(priority:3)
                M4(priority:1)
                    M5(priority:6) ---> REJECTED !
                           < --------- A consumer connects
    
    Which msg is delivered first? M3 < M2 < M1 < M4
    
## Lazy Mode

    We have Transient and Durable queues
    - Classic queues can be Transient or Durable    
    - Quorum  queues are Durable
    
    When a MSG comes to a DURABLE QUEUE... it is stored in the disk.
        MSG are stored in a RAM memory buffer, which is flushed each 0.5s (by default)
    
    When a message comes to a TRANSIENT QUEUE... it is tried to be kept only in RAM.
    If the system goes into RAM pressure, RabbitMQ is going to store msgs in the disk.

    x-queue-mode        : lazy
    
    For DURABLE QUEUES msgs are not going to be stored in the disk as soon as posible.