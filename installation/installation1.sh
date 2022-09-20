### RabbitMQ installation 1
docker run -it -d --name rabbitmq -p 5672:5672 -p 8080:15672 rabbitmq:3.10.7-management
                                       # ^ RabbitMQ port
                                                    # ^ a managment tool port
                                                    # 8080
                                                    # 8081
                                                    # 8082