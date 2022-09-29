# RabbitMQ Stream Queues

- They store messages FOREVER (well, we have a little control on that)
    max-length:
    max-time-retention:
- Once the queue reaches its max size (or length) which messages are removed? Always the oldest ones
- Can we have multiple consumers consumming the same msg? YES 
- Can I re-consume a msg? YES

Use cases:
- When we need extremly good performance
- When we need to consume msgs from multiple consumers 
   (instead of having a bunch of queues and a fanout exchange)
- When msgs need to be processed once and again

But... in order to use them... its a bit tricky? What do I need to do?
As a Consumer? I need to track what the last msg POSITION I did consume. OFFSET
               So, next tme I can start at the following one.
            (In kafka I don't have to... Kafka is going to keep track for me of that position)
            