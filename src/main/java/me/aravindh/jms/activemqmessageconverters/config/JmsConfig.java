package me.aravindh.jms.activemqmessageconverters.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableJms
@EnableTransactionManagement
@Configuration
public class JmsConfig { //implements JmsListenerConfigurer

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String user;

    @Value("${spring.activemq.password}")
    private String password;


    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }


    @Bean
    public CachingConnectionFactory connectionFactory() {

        CachingConnectionFactory factory = new CachingConnectionFactory(
                new ActiveMQConnectionFactory(user, password, brokerUrl)
        );
        // factory.setReconnectOnException(true);
        factory.setClientId("StoreFront");
        factory.setSessionCacheSize(100);
        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(jacksonJmsMessageConverter());
        //factory.setMessageConverter(xmlMarshallingMessageConverter());
        factory.setTransactionManager(jmsTransactionManager());
        return factory;
    }

    /**
     * On Rollback msg is not sent by producer
     * on consumer side, msg is re-queued on the MOM for retrieval.
     * Re-queueing of messages is broker dependent
     * For ActiveMQ,on rollback, after retries you can configure the broker to move it to DLQ(Dead letter queue)
     */
    @Bean
    public PlatformTransactionManager jmsTransactionManager() {
        return new JmsTransactionManager(connectionFactory());
    }


}
