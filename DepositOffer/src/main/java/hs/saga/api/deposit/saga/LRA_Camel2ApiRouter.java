package hs.saga.api.deposit.saga;


import hs.saga.integration.c1.dto.AssessmentRequestSchema;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class LRA_Camel2ApiRouter extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        from("direct:camel2api")
                .to("log:DEBUG?showBody=true&showHeaders=true")
                .routeId("camel2api")

                .saga().propagation(SagaPropagation.REQUIRED)
                .compensation("direct:cancelOffer")
                .completion("direct:completeOffer")

                .removeHeader(Exchange.HTTP_URI)
                .setHeader(Exchange.HTTP_METHOD, simple("POST"))
                .setHeader(Exchange.HTTP_PATH, simple("api/customer-eligibility-assessment"))
                .setHeader(Exchange.CONTENT_TYPE, simple("application/json"))
                //.convertBodyTo(String.class)
                //.marshal().json(JsonLibrary.Jackson)
                // marshal > object to Json (or xml ... )
                // unmarshal > Json (or xml) to object
                .marshal().json(JsonLibrary.Jackson, AssessmentRequestSchema.class)
                .to("log:DEBUG?showBody=true&showHeaders=true")
                .log("-- Header HTTP_PATH --" + simple("deposit-offer/${header.customerId}"))
                .to("http4://{{demo.assessment-service-hostname}}:{{demo.assessment-service-port}}/")
                .convertBodyTo(String.class)
                .to("log:DEBUG?showBody=true&showHeaders=true")
                ;
    }
}
