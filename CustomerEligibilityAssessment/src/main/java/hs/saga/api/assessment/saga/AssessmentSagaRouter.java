package hs.saga.api.assessment.saga;

import hs.saga.api.assessment.dto.AssessmentResponseSchema;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Component
public class AssessmentSagaRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        rest("/customer-eligibility-assessment").description("Customer Assessment")
                .produces("application/json")
                //.post("/{itemId}/{quantity}").outType(Order.class).description("Request a new purchase")
                .post().outType(String.class)
        .route().routeId("assessment")
        .to("log:DEBUG?showBody=true&showHeaders=true")
        .to("direct:buy");

        from("direct:buy")
            .routeId("buyRoute")
                .saga()
                //.propagation(SagaPropagation.MANDATORY)		  // A saga must be already present. The existing saga is joined.
                .propagation(SagaPropagation.SUPPORTS)		  //If a saga already exists, then join it.
                .compensation("direct:cancelSagaAssess")
                .completion("direct:completeSagaAssess")
            .convertBodyTo(String.class)
            .transform().simple("${body}")
        .to("log:DEBUG?showBody=true&showHeaders=true")
        ;

        from("direct:cancelSagaAssess")
                .routeId("cancelSagaAssess")
                .transform().header("customerId")	// Retrieve the orderId
                //.bean(orderManagerService, "cancelOrder")
                .log("Saga Assessment ${body} cancelled");

        from("direct:completeSagaAssess")
                .routeId("completeSagaAssess")
                .transform().header("customerId")	// Retrieve the orderId
                //.bean(orderManagerService, "notifyOrder")
                //.to("jms:notifyOrder")
                .log("Saga Assessment ${body} complete");
    }
}
