package hs.saga.api.deposit.saga;

import hs.saga.api.deposit.saga.service.DepositOfferSagaDto;
import hs.saga.api.deposit.saga.service.DepositOfferSagaService;
import hs.saga.config.camel.CamelThreadLocal;
import hs.saga.domain.service.DepositOfferService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Component
public class DepositOfferRouter extends RouteBuilder {

    @Autowired
    DepositOfferService depositOfferService;

//    @Autowired
//    DepositOfferSagaService depositOfferSagaService;

    @Autowired
    LraSaveProcessor lraSaveProcessor;
    @Autowired
    LraRemoveProcessor lraRemoveProcessor;

    @Override
    public void configure() throws Exception {
        from("direct:controllerTest")
        .routeId("DepositOfferRoute")
        .log("## Before Register Saga")
        .to("log:DEBUG?showBody=true&showHeaders=true")
                // 여기서부터 Thread 분리됨.
                .saga().propagation(SagaPropagation.REQUIRED)
                    .compensation("direct:cancelOffer")
                    .completion("direct:completeOffer")
        .process(lraSaveProcessor)
        .log("## After Register Saga and Call Service Bean")
        .to("log:DEBUG?showBody=true&showHeaders=true")
        .bean(depositOfferService)
        .log("## Router Service Done")
        .to("log:DEBUG?showBody=true&showHeaders=true")
        .process(lraRemoveProcessor)
// Notice that '.option' is always evaluated before '.bean' executions
.option("customerId", simple("${header.customerId}"))
.option("currentDate", simple(String.valueOf(new Date())))
        ;

        from("direct:cancelOffer")
        .routeId("cancelOfferRoute")
        .to("log:DEBUG?showBody=true&showHeaders=true")
        //.transform().header("customerId")	// Retrieve the OfferId
                .process((exchange)->{
                    Map<String, Object> headers = exchange.getIn().getHeaders();
                    String body = exchange.getIn().getBody(String.class);

                    DepositOfferSagaDto param = DepositOfferSagaDto.builder()
                            .customerId(exchange.getIn().getHeader("customerId", String.class))
                            .currentDate(exchange.getIn().getHeader("currentDate", String.class))
                            .lraId(exchange.getIn().getHeader("Long-Running-Action", String.class))
                            .build();
                    exchange.getOut().setBody(param);
                })

        .to("log:DEBUG?showBody=true&showHeaders=true")
        .log("Offer ${body} cancelled")
        .bean(depositOfferService,"compensate")
        .to("log:DEBUG?showBody=true&showHeaders=true")
        ;

        from("direct:completeOffer")
        .routeId("completeOfferRoute")
        .to("log:DEBUG?showBody=true&showHeaders=true")
                //.transform().header("customerId")	// Retrieve the OfferId
        .process((exchange)->{
            Map<String, Object> headers = exchange.getIn().getHeaders();
            String body = exchange.getIn().getBody(String.class);

            DepositOfferSagaDto param = DepositOfferSagaDto.builder()
                    .customerId(exchange.getIn().getHeader("customerId", String.class))
                    .currentDate(exchange.getIn().getHeader("currentDate", String.class))
                    .lraId(exchange.getIn().getHeader("Long-Running-Action", String.class))
                    .build();
            exchange.getOut().setBody(param);
        })
        .to("log:DEBUG?showBody=true&showHeaders=true")
        .log("Offer ${body} notified to customer")
        .bean(depositOfferService,"complete")
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
//        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        log.info("SimpleLoggingProcessor - {}", servletRequest);
//        servletRequest.setAttribute("lra",exchange.getMessage().getHeader("Long-Running-Action"));

        CamelThreadLocal.setLra(exchange.getMessage().getHeader("Long-Running-Action").toString());
    }
}

@Slf4j
@Component
class LraRemoveProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("Thread Information -> {}", Thread.currentThread());
        log.info("LraRemoveProcessor -> {}", CamelThreadLocal.getLra());
        CamelThreadLocal.remove();
    }
}


//@Autowired
//LraService lraService;
//@Slf4j
//@Component
//class LraService{
//    public String save(
//            @Header("Long-Running-Action") String lra ) {
//        log.info("Long-Running-Action - {}", lra);
//        HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        log.info("Long-Running-Action - {}", servletRequest);
//        servletRequest.setAttribute("lra",lra);
//        return "S";
//
//    }
//}