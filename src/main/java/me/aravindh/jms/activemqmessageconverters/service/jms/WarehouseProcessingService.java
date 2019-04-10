package me.aravindh.jms.activemqmessageconverters.service.jms;

import me.aravindh.jms.activemqmessageconverters.model.BookOrder;
import me.aravindh.jms.activemqmessageconverters.model.ProcessedBookOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class WarehouseProcessingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseProcessingService.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Transactional
    public ProcessedBookOrder processOrder(BookOrder bookOrder, String orderState, String storeId) {
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

    private ProcessedBookOrder add(BookOrder bookOrder, String storeId) {
        LOGGER.info("ADDING A NEW ORDER TO THE DB");
        return new ProcessedBookOrder(
                bookOrder,
                new Date(),
                new Date()
        );
    }

    private ProcessedBookOrder update(BookOrder bookOrder, String storeId) {
        LOGGER.info("UPDATING AN ORDER TO THE DB");
        return new ProcessedBookOrder(
                bookOrder,
                new Date(),
                new Date()
        );
    }

    private ProcessedBookOrder delete(BookOrder bookOrder, String storeId) {
        LOGGER.info("DELETING THE ORDER FROM THE DB");
        return new ProcessedBookOrder(
                bookOrder,
                new Date(),
                null
        );
    }

}
