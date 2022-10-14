package hs.saga.api.deposit.saga;

import hs.saga.api.deposit.saga.service.Order;
import hs.saga.api.deposit.saga.service.PurchaseService;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestRouter extends RouteBuilder {

    @Autowired
    PurchaseService purchaseService;

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "PURCHASES REST API")
                .apiProperty("api.version", "1.0")
                .apiProperty("cors", "true")
                .apiProperty("base.path", "camel/")
                .apiProperty("api.path", "/")
                .apiProperty("host", "")
                .apiContextRouteId("doc-api")
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true");

        onException(Exception.class)
                .handled(false)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(202))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody(constant(""))
                .log("Error reported: ${exception.message}")
                .transform().simple("Error reported: ${exception.message} - Purchase not processed.");

        rest("/purchases").description("Purchases endpoint")
                .produces("application/json")
                .post("/{itemId}/{quantity}").outType(Order.class).description("Request a new purchase")
                .param().name("itemId").description("Item identification").type(RestParamType.path).endParam()
                .param().name("quantity").description("Item quantity").type(RestParamType.path).endParam()
                .route().routeId("purchaseRoute")
                .to("direct:buy");

        from("direct:buy")
                .routeId("buyRoute")
                .log(">>> ${body}")
                .to("log:DEBUG?showBody=true&showHeaders=true")
                .saga()
                .log(">>> ${body}")
                .propagation(SagaPropagation.REQUIRED)
                .removeHeader(Exchange.HTTP_URI)
                .setBody(simple("${null}"))
                .setHeader(Exchange.HTTP_METHOD, simple("POST"))
                .setHeader(Exchange.HTTP_PATH, simple("${header.itemId}/${header.quantity}"))
                .to("http4://{{demo.order-service-hostname}}:{{demo.order-service-port}}/camel/orders")
                .removeHeader(Exchange.HTTP_URI)
                .setHeader(Exchange.HTTP_METHOD, simple("POST"))
                .setHeader(Exchange.HTTP_PATH, simple("${header.itemId}/${header.quantity}"))
                .to("http4://{{demo.stock-service-hostname}}:{{demo.stock-service-port}}/camel/stock/reservations")

                // If quantity was 27 an exception is thrown
                .bean(purchaseService, "generateError")
                //.convertBodyTo(String.class)
                .unmarshal().json(JsonLibrary.Jackson, Order.class)

                .process((exchange) -> {
                    Order order = (Order) exchange.getIn().getBody();
                    order.setOrderId( (String) exchange.getIn().getHeader("orderId") );
                })

                .log("-- END of ROUTE --");


    }
}
