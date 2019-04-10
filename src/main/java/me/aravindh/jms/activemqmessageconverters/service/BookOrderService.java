package me.aravindh.jms.activemqmessageconverters.service;

import me.aravindh.jms.activemqmessageconverters.model.BookOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookOrderService {

    @Autowired
    private final JmsTemplate jmsTemplate;

    private static final String BOOK_QUEUE = "book.order.queue";

    public BookOrderService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(BookOrder bookOrder) {
        jmsTemplate.convertAndSend(BOOK_QUEUE, bookOrder);
    }


}
