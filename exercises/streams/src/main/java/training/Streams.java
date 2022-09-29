package training;

import com.rabbitmq.stream.*;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class Streams{
    
    public static void main(String args[]) throws Exception{
        System.out.println("Connecting...");
        Environment environment = Environment.builder()
            .uri("rabbitmq-stream://guest:guest@localhost:5552/%2f")  
            .build();        
        String stream = UUID.randomUUID().toString();
        environment.streamCreator().stream(stream).create();  
        
        Thread.sleep(5000);
        
        System.out.println("Starting publishing...");
        int messageCount = 10000;
        CountDownLatch publishConfirmLatch = new CountDownLatch(messageCount);
        Producer producer = environment.producerBuilder()  
                .stream(stream)
                .build();
        IntStream.range(0, messageCount)
                .forEach(i -> producer.send(  
                        producer.messageBuilder()                    
                            .addData(String.valueOf(i).getBytes())   
                            .build(),                                
                        confirmationStatus -> publishConfirmLatch.countDown()  
                ));
        publishConfirmLatch.await(10, TimeUnit.SECONDS);  
        producer.close();  
        System.out.printf("Published %,d messages%n", messageCount);

        System.out.println("Starting consuming...");
        AtomicLong sum = new AtomicLong(0);
        CountDownLatch consumeLatch = new CountDownLatch(messageCount);
        Consumer consumer = environment.consumerBuilder()  
                .stream(stream)
                .offset(OffsetSpecification.first()) 
                .messageHandler((offset, message) -> {  
                    sum.addAndGet(Long.parseLong(new String(message.getBodyAsBinary())));  
                    consumeLatch.countDown();  
                })
                .build();
        
        consumeLatch.await(10, TimeUnit.SECONDS);  
        
        System.out.println("Sum: " + sum.get());  
        
        consumer.close();  
        
        environment.deleteStream(stream);  
        environment.close();  

    }
}