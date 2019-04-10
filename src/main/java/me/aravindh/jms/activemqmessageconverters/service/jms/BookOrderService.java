package me.aravindh.jms.activemqmessageconverters.service.jms;

import me.aravindh.jms.activemqmessageconverters.model.BookOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookOrderService {

    @Autowired
    private final JmsTemplate jmsTemplate;

    private static final String BOOK_QUEUE = "book.order.queue";

    public BookOrderService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Transactional
    public void send(BookOrder bookOrder, String storeId, String orderState) {
        jmsTemplate.convertAndSend(BOOK_QUEUE, bookOrder, (message -> {
            message.setStringProperty("bookOrderId", bookOrder.getBookOrderId());
            message.setStringProperty("storeId", storeId);
            message.setStringProperty("orderState", orderState);
            return message;
        }));
    }


}
