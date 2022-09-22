

FD1                                                     Q1                                          EV1 √
FD2                                                     Q2                                          EV2 x
FD3         >>>   EXCHANGE0 -- FIRE --> EXCHANGE1 >>>>> Q3.   <<<< PROTECTION SYSTEM >>>>>          EV3 √
...                         |           EXCHANGE2                                                   ...
FD(n)                       |                           Q(m).                                       EV(m) x
                            |                                            Synchonous
            Asynchronous    |
                            |
                            |                           QA                                          ALARM !!!!
                            |       -----------------------------------------------------------
                            |---->   ControlQueue.   < Depending on the mes >    A light ON in a control panel !!!
                                    -----------------------------------------------------------


FD ->   Producer. CLI Tool
            Sends messages in loop ... each 5 seconds... 
            Whenever you press ENTER... or you type fire !

PROTECTION SYSTEM -> Consumer
        Read messages each 5 seconds (1 seconds)
        Once a message is received
        In your program... When reading from queue2---> Force a requeue -> PRODUCER -> EXCHANGE2

CONTROL PROGRAM
        Read messages each 5 seconds (1 seconds)
        Display in the console
    