package br.com.gabrielspassos.poc.processor;

import br.com.gabrielspassos.poc.model.Customer;
import org.apache.camel.Exchange;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomerFlatFileProcessor extends FlatFileProcessor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String message = getBody(exchange);
        Pattern customerPattern = Pattern.compile(getCustomerRegex());
        List<Customer> customerList = Stream.of(message)
                .flatMap(msg -> Arrays.stream(msg.split(getLineBreaker())))
                .map(line -> buildMatcher(line, customerPattern))
                .filter(Matcher::find)
                .map(this::buildCustomer)
                .collect(Collectors.toList());
        exchange.setProperty("customerList", customerList);
    }

    private Customer buildCustomer(Matcher customerMatcher) {
        Customer customer = new Customer();
        customer.setCnpj(customerMatcher.group(1));
        customer.setName(customerMatcher.group(2));
        customer.setBusinessArea(customerMatcher.group(3));
        return customer;
    }

    private String getCustomerRegex() {
        return propertiesReader.getProperty("flat-file.decoder.customer-regex");
    }

}
