# Day 1

## What is a container?

Isolated environment inside a Linux OS where we can run process.
Isolated:
- They have their own network configuration
- They have restricted access to the hardware components
- They have their own environment vars
- They have their custom file system

They solve almost the same problems that we used to solve with VMs.

## Traditional method 

    RabbitMQ | NodeJS       Problems: - Security
    ------------------                - Installation process
        OS                            - Compatibility: Dependencies, configuration OS level
    ------------------                - STABILITY: NodeJS gets crazy. 100% CPU -> NodeJS   offline
        Machine                                                                   RabbitMQ offline

## Virtual machines

    RabbitMQ | NodeJS 
    ------------------
    OS1      | OS2
    ------------------
    VM1      | VM2
    ------------------
        hypervisor: 
        kvm, citrix, 
    vmware, virtualbox, hyperv
    ------------------
        OS            
    ------------------
        Machine       

## Containers

    RabbitMQ | NodeJS 
    ------------------
    C1       | C2
    ------------------
    containers manager:
    docker, podman, 
    crio, containerd
    ------------------
        OS Linux          
    ------------------
        Machine       

Docker desktop for windows
- hyperV - Linux
- wls
Docker desktop for macos
- VM -> linux


does Windows have a Kernel? YES. How is it called? DOS | Windows NT
    DOS-> MS-DOS , Windows 3, 95, 98, millenium
    NT -> Windows NT, XP, Servers, 7,8, 10, 11
    
# Whenever we create a Container we start with a Container Image

Container Image: 

Is just a compressed file (tar) containing:
- dependencies that we need to have installed in order to use a program
- OS configuration
- The software already installed and configured

We don install a container image.... we are not going to install rabbitMQ, just to DEPLOY a rabbitMQ installation
that was already be installed.

They are stored and distributed from a Container Image Repository Registry.
The most relevant one is called: docker hub

latest                      --> points to the latest production version of the software
    Is convinient but we CANNOT use it in production
3.9.22-management
3.9-managment
    this helps me to upgrade my production enviroment with minor versions.

---
RabbitMQ in my computer. Tradicional Way

Prepare dependencies and configuration
Download what? The installer
Execute that installer -> C:\Program files\rabbitmq -> ZIP -> email 

--- 
Production environments:

- High Availability
    Trying to keep the system up(ready) a certain amount of time.
        90% ? This is horrible!!!! 1 day of 10 that my system can be down: 36,5 days/year       | €
        99% ?... wellll.     3,5 days a year with the system down? depending on the business... | €€
            I am a hairdresser                                                                  | €€€€
        99,9% ? 8 hours a year... We can start here                                             | €€€€€€€€
        99,99%  Minutes a year                                                                  V €€€€€€€€€€€€€€€€€
    Trying to make sure that NO INFORMATION is lost

    Replication:
        - Hardware          -   Cluster
        - Software          /
        - Data

- Scalability
    Is the ability to resize the environment depending on the needs that we could have at a certain point of time 

    App3. INTERNET
        Day n       100 users
        Day n+1     10000 users         Horizontal scalability: MORE MACHINES
        Day n+2     10 users
        Day n+3     1000000 users
        
        Maybe the changes are not between days but minutes
    
    In this case, I need to get more machines! Where can I get those? so fast!
    Clouds. They allow me to rent hardware... Pay per use.
    AUTOMATE THE PROCESS OF GETTING NEW HARDWARE: terraform
    Thats ok... I can get 20 new servers in 2 minutes... but is that enough? NO
    What do I need? To insgtall my software there.... What can I use for those instalations.
    I want really easy and fast intallations. 
    What technology can I use? Containers
---
docker is a tool for mananing container in 1 single server/computer
-----
Kubernetes < I want to have installed and running rabbitmq: I want at least 2 instances...
                                                           but It can be more... unitl 10 maybe

Cluster os servers
    Machine 1
        crio or containerd
    Machine 2
        crio or containerd
        rabbitMQ2
    Machine 3 -OFFLINE
        crio or containerd
    ...
    Machine N
        crio or containerd
        rabbitMQ3

Kubernetes distributions:
K8S
K3S
Tamzu - VMWare
Openshift - Redhat

----
We are going to be working with 1 single machine - docker - Development
But there, we are going to have multiple instances of rabbitMQ

----
Webserver
- nginx . By default port: 80 < http protocol

Download the image
$ docker image pull nginx:latest                                                docker pull

Create a new Container from taht image
$ docker container create --name mynginx nginx:latest                           

Start the container..... Start the processes inside taht isolated environment
$ docker container start mynginx                                                docker start

Keeps showing the logs of the container
$ docker container attach mynginx                                               

I can do all those oprations with 1 single command
$ docker run --name mynginx -p 8080:80 nginx:latest
                                NAT: Port redirection

Check all the running containers
$ docker container list                                                         docker ps
                        --all

docker [kind of object] [verb] <extra arguments>
