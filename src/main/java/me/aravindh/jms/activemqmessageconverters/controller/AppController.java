package me.aravindh.jms.activemqmessageconverters.controller;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import me.aravindh.jms.activemqmessageconverters.model.Book;
import me.aravindh.jms.activemqmessageconverters.model.BookOrder;
import me.aravindh.jms.activemqmessageconverters.model.Customer;
import me.aravindh.jms.activemqmessageconverters.service.BookOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
public class AppController {

    private final BookOrderService bookOrderService;

    List<Book> books = Arrays.asList(
            new Book("jpw-1234", "Lord of the Flies"),
            new Book("uyh-2345", "Being and Nothingness"),
            new Book("iuhj-87654", "At Sea and Lost"));


    List<Customer> customers = Arrays.asList(
            new Customer("mr-1234", "Michael Rodgers"),
            new Customer("jp-2345", "Jeff Peek"),
            new Customer("sm-8765", "Steve McClarney")
    );

    public AppController(BookOrderService bookOrderService) {
        this.bookOrderService = bookOrderService;
    }

    @RequestMapping("/")
    public String appHome(ModelMap map) {


        map.addAttribute("customers", customers);
        map.addAttribute("books", books);
        return "index";
    }

    @RequestMapping(path = "/process/order/{orderId}/{customerId}/{bookId}/", method = RequestMethod.GET)
    public @ResponseBody
    String processOrder(@PathVariable("orderId") String orderId,
                        @PathVariable("customerId") String customerId,
                        @PathVariable("bookId") String bookId) throws JsonMappingException, JsonParseException,
            IOException {

        try {
            bookOrderService.send(build(customerId, bookId, orderId));
        } catch (Exception exception) {
            return "Error occurred!" + exception.getLocalizedMessage();
        }
        return "Order sent to warehouse for bookId = " + bookId + " from customerId = " + customerId + " successfully" +
                " processed!";
    }

    private BookOrder build(String customerId, String bookId, String orderId) {
        Book myBook = null;
        Customer myCustomer = null;

        for (Book bk : books) {
            if (bk.getBookId().equalsIgnoreCase(bookId)) {
                myBook = bk;
            }
        }
        if (null == myBook) {
            throw new IllegalArgumentException("Book selected does not exist in inventory!");
        }

        for (Customer ct : customers) {
            if (ct.getCustomerId().equalsIgnoreCase(customerId)) {
                myCustomer = ct;
            }
        }

        if (null == myCustomer) {
            throw new IllegalArgumentException("Customer selected does not appear to be valid!");
        }

        return new BookOrder(orderId, myBook, myCustomer);
    }


}

