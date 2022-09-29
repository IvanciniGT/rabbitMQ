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

HTTP REST API

// Lets create a virtual HOST
PUT             http://guest:guest@54.155.42.135:8081/api/vhosts/myvirtualhost
// Create a classic queue
PUT             http://guest:guest@54.155.42.135:8081/api/queues/myvirtualhost/IvanClassicQueue
{
    "auto_delete":false,
    "durable":true,
    "arguments":{},
    "node":"rabbit@rabbit2"
}
// Create and exchange
PUT             http://guest:guest@54.155.42.135:8081/api/exchanges/myvirtualhost/IvanDirectExchange
{
    "type":"direct",
    "auto_delete":false,
    "durable":true,
    "internal": false,
    "arguments":{}
}
// Create the bind
POST            http://guest:guest@54.155.42.135:8081/api/bindings/myvirtualhost/e/IvanDirectExchange/q/IvanClassicQueue/	
{
    "routing_key":"important", 
    "arguments":{}
}
// Send a message
PUT             http://guest:guest@54.155.42.135:8081/api/exchanges/myvirtualhost/IvanDirectExchange/publish
{
    "properties":{},
    "routing_key":"important",
    "payload":"My second message",
    "payload_encoding":"string"
}
// Consume messages from the queue
http://guest:guest@54.155.42.135:8081/api/queues/myvirtualhost/IvanClassicQueue/get
{
    "count":5,
    "ackmode":"ack_requeue_false",  // Delete msgs
    "ackmode":"ack_requeue_true",  // Keeps msgs
    "encoding":"auto",
    "truncate":50000
}

//Let's try to create a policy: 
max-length: 1
Add a pattern that includes your actual queue
^word
http://guest:guest@54.155.42.135:8081/api/policies/myvirtualhost/IvanQueues
{
    "pattern":"^Ivan[A-Z][a-z]+Queue$", 
    "definition": {"max-length":10}, 
    "apply-to": "queues"
}

http://guest:guest@54.155.42.135:8081/api/operator-policies/myvirtualhost/IvanQueues
{
    "pattern":"^Ivan.*", 
    "definition": {"max-length":2}, 
    "apply-to": "queues"
}



// API docs
http://54.155.42.135:8081/api/index.html

---
How have we been creating things in RABBITMQ?

- Thru RABBITMQ MANAGEMENT WEB CONSOLE
- JAVA API , C# API
- Thru HTTP API
- rabbitmqctl                               Against the internal API (more powerful)
    -   MAN: https://www.rabbitmq.com/rabbitmqctl.8.html
- rabbitmqadmin                             HTTP request
    -   CheatSheat: https://www.rabbitmq.com/management-cli.html

When to use the CLI tools?

AUTOMATION !
- Script .sh -> (on any linux)
        - docker exec -it CONTAINER_NAME rabbitmqctl

To setup (configure) a rabbitmq cluster
- Persistent Exchanges
- Persistent Queues
- Users
- VirtualHosts
- Policies
