package br.com.gabrielspassos.poc.route;

import br.com.gabrielspassos.poc.model.Item;
import br.com.gabrielspassos.poc.model.Relatory;
import br.com.gabrielspassos.poc.model.Sale;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class AnalisisRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:analisisRelatory")
                .routeId("analisisRelatory")
                .to("file://data/out/?fileName=result.dat&charset=utf-8")
                .process(this::calculateCustumersNumber)
                .process(this::calculateSalesmanNumber)
                //.process(this::getMostExpensiveSale)
                .end();
    }

    private void calculateCustumersNumber(Exchange exchange) {
        Relatory relatory = getRelatory(exchange);
        Integer custumersNumber = relatory.getCustomers().size();
        exchange.setProperty("custumersNumber", custumersNumber);
    }

    private void calculateSalesmanNumber(Exchange exchange) {
        Relatory relatory = getRelatory(exchange);
        Integer salesmanNumber = relatory.getSalesmens().size();
        exchange.setProperty("salesmanNumber", salesmanNumber);
    }

    private void getMostExpensiveSale(Exchange exchange) {
        Relatory relatory = getRelatory(exchange);
        List<Item> items = relatory.getSales().stream()
                .map(Sale::getItems)
                .map(items1 -> new ArrayList<>(items1));
    }

    private Relatory getRelatory(Exchange exchange) {
        return exchange.getProperty("relatory", Relatory.class);
    }
}