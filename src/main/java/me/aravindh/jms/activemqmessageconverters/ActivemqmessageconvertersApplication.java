package me.aravindh.jms.activemqmessageconverters;

import me.aravindh.jms.activemqmessageconverters.service.jms.Sender;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@EnableJms
@SpringBootApplication
public class ActivemqmessageconvertersApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ActivemqmessageconvertersApplication.class,
                args);
        /*Sender sender = context.getBean(Sender.class);

        System.out.println("Preparing to send a message");
        sender.sendMessage("order-queue", "item: 1234, customer: 1234");*/
    }

}
