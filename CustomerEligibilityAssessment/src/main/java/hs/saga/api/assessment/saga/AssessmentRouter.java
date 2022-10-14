package hs.saga.api.assessment.saga;

import hs.saga.config.camel.CamelThreadLocal;
import hs.saga.domain.assessment.service.AssessmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AssessmentRouter extends RouteBuilder {

    @Autowired
    AssessmentService assessmentService;

    @Override
    public void configure() throws Exception {
        from("direct:controllerTest")
                .routeId("AssessmentRoute")
                .to("log:DEBUG?showBody=true&showHeaders=true")
                //.setHeader("customer")
                .saga().propagation(SagaPropagation.REQUIRED)
                .compensation("direct:cancelAssessment")
                .completion("direct:completeAssessment")
                .process(new LraSaveProcessor())
                .to("log:DEBUG?showBody=true&showHeaders=true")
                .bean(assessmentService)
                .to("log:DEBUG?showBody=true&showHeaders=true")
.option("customerId", simple("${header.customerId}"))	// Notice that '.option' is always evaluated before '.bean' executions
        ;


        from("direct:cancelAssessment")
                .routeId("cancelAssessment")
                //.transform().header("customerId")
                .process((exchange)->{
                    Map<String, Object> headers = exchange.getIn().getHeaders();
                    String body = exchange.getIn().getBody(String.class);

                    String param =
                            exchange.getIn().getHeader("customerId", String.class)
                            + "||"
                            + exchange.getIn().getHeader("Long-Running-Action", String.class)
                    ;
                    exchange.getOut().setBody(param);
                })
                .log("Assessment ${body} cancelled")
                .bean(assessmentService,"compensate")
                .to("log:DEBUG?showBody=true&showHeaders=true")
                ;
        from("direct:completeAssessment")
                .routeId("completeAssessment")
                .transform().header("customerId")	// Retrieve the orderId
                .process((exchange)->{
                    Map<String, Object> headers = exchange.getIn().getHeaders();
                    String body = exchange.getIn().getBody(String.class);

                    String param =
                            exchange.getIn().getHeader("customerId", String.class)
                                    + "||"
                                    + exchange.getIn().getHeader("Long-Running-Action", String.class)
                            ;
                    exchange.getOut().setBody(param);
                })
                .log("Assessment ${body} complete")
                .bean(assessmentService,"complete")
                .to("log:DEBUG?showBody=true&showHeaders=true")

        ;


    }
}

@Slf4j
@Component
class LraSaveProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("Thread Information -> {}", Thread.currentThread());
        log.info("LraSaveProcessor - {}", exchange.getMessage().getBody());
        log.info("LraSaveProcessor - {}", exchange.getMessage().getHeader("Long-Running-Action"));
        CamelThreadLocal.setLra(exchange.getMessage().getHeader("Long-Running-Action").toString());
    }
}