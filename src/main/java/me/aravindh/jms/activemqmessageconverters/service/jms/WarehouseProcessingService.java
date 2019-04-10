package me.aravindh.jms.activemqmessageconverters.service.jms;

import me.aravindh.jms.activemqmessageconverters.model.BookOrder;
import me.aravindh.jms.activemqmessageconverters.model.ProcessedBookOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class WarehouseProcessingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseProcessingService.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Transactional
    public Message<ProcessedBookOrder> processOrder(BookOrder bookOrder, String orderState, String storeId) {
        ProcessedBookOrder order = new ProcessedBookOrder(
                bookOrder,
                new Date(),
                new Date()
        );
        if ("NEW".equalsIgnoreCase(orderState)) {
            return add(bookOrder, storeId);
        } else if ("UPDATE".equalsIgnoreCase(orderState)) {
            return update(bookOrder, storeId);
        } else if ("DELETE".equalsIgnoreCase(orderState)) {
            return delete(bookOrder, storeId);
        } else
            throw new IllegalArgumentException("WarehouseProcessingservice.processoder(...)-orderState did not match " +
                    "expected values");
        //jmsTemplate.convertAndSend("book.order.processed.queue", order);
    }

    private Message<ProcessedBookOrder> add(BookOrder bookOrder, String storeId) {
        LOGGER.info("ADDING A NEW ORDER TO THE DB");
        return build(new ProcessedBookOrder(
                bookOrder,
                new Date(),
                new Date()
        ), "Added", storeId);
    }

    private Message<ProcessedBookOrder> update(BookOrder bookOrder, String storeId) {
        LOGGER.info("UPDATING AN ORDER TO THE DB");
        return build(new ProcessedBookOrder(
                bookOrder,
                new Date(),
                new Date()
        ), "UPDATED", storeId);
    }

    private Message<ProcessedBookOrder> delete(BookOrder bookOrder, String storeId) {
        LOGGER.info("DELETING THE ORDER FROM THE DB");
        return build(new ProcessedBookOrder(
                bookOrder,
                new Date(),
                null
        ), "deleted", storeId);
    }

    private Message<ProcessedBookOrder> build(ProcessedBookOrder bookOrder, String orderState, String storeId) {

        return MessageBuilder
                .withPayload(bookOrder)
                .setHeader("orderState", orderState)
                .setHeader("storeId", storeId)
                .build();
    }

}
