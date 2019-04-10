package me.aravindh.jms.activemqmessageconverters.config;

import me.aravindh.jms.activemqmessageconverters.listeners.BookOrderProcessingMessageListener;
import me.aravindh.jms.activemqmessageconverters.model.Book;
import me.aravindh.jms.activemqmessageconverters.model.BookOrder;
import me.aravindh.jms.activemqmessageconverters.model.Customer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.jms.ConnectionFactory;


@EnableJms
@Configuration
public class JmsConfig{ //implements JmsListenerConfigurer

    @Autowired
    public ConnectionFactory connectionFactory;

    // @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public MessageConverter xmlMarshallingMessageConverter() {
        MarshallingMessageConverter converter = new MarshallingMessageConverter(xmlMarshaller());
        converter.setTargetType(MessageType.TEXT);
        return converter;
    }

    @Bean
    public XStreamMarshaller xmlMarshaller() {
        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setSupportedClasses(Book.class, Customer.class, BookOrder.class);
        return marshaller;
    }

  /*  @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");
        return factory;
    }*/

    /*@Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setMessageConverter(jacksonJmsMessageConverter());
        return template;
    }*/

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setMessageConverter(xmlMarshallingMessageConverter());
        return factory;
    }

    @Bean
    public BookOrderProcessingMessageListener jmsMessageListener() {
        BookOrderProcessingMessageListener listener = new BookOrderProcessingMessageListener();
        return listener;
    }


   /* @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setMessageListener(jmsMessageListener());
        endpoint.setDestination("book.order.processed.queue");
        endpoint.setId("book-order-processed-queue");
        endpoint.setSubscription("my-subscription");
        endpoint.setConcurrency("1");
        registrar.setContainerFactory(jmsListenerContainerFactory());
        registrar.registerEndpoint(endpoint,jmsListenerContainerFactory());
    }*/
}
