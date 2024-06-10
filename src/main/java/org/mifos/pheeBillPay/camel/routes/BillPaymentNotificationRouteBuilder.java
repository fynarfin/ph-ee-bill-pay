package org.mifos.pheeBillPay.camel.routes;

import com.amazonaws.services.dynamodbv2.xspec.B;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.camel.Exchange;
import org.json.JSONObject;
import org.mifos.connector.common.camel.ErrorHandlerRouteBuilder;
import org.mifos.pheeBillPay.data.BillPaymentsReqDTO;
import org.mifos.pheeBillPay.data.BillPaymentsResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.Exchanger;

import static org.mifos.pheeBillPay.zeebe.ZeebeVariables.*;

@Component
public class BillPaymentNotificationRouteBuilder extends ErrorHandlerRouteBuilder {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BillPaymentsResponseDTO billPaymentsResponseDTO;



    @Override
    public void configure() {

        from("direct:paymentNotification-response")
                .routeId("paymentNotification-response")
                .log("Bill Inquiry over, moving to bill payment")
                .setHeader("Content-Type", constant("application/json"))
                .process(exchange -> {
                  BillPaymentsResponseDTO responseDTO = setResponseBody(exchange);
                    exchange.getIn().setHeader(PLATFORM_TENANT,exchange.getIn().getHeader(PLATFORM_TENANT));
                    exchange.getIn().setHeader(CLIENTCORRELATIONID,exchange.getIn().getHeader(CLIENTCORRELATIONID));
                    exchange.getIn().setHeader(PAYER_FSP,exchange.getIn().getHeader(PAYER_FSP));
                    exchange.getIn().setHeader(CALLBACK_URL,"https://webhook.site/2e4aa0de-4b8b-416f-a375-7085c0ec645e");
                    exchange.getIn().setBody(responseDTO.toString());
                })
                .log("Payment Notification Body: ${body}")
                .log("Payment Notification Headers: ${headers}")
                .toD("${exchangeProperty.X-CallbackURL}"+ "?bridgeEndpoint=true&throwExceptionOnFailure=false");

    }

    private BillPaymentsResponseDTO setResponseBody(Exchange exchange){

        billPaymentsResponseDTO.setCode(exchange.getProperty("code").toString());
        billPaymentsResponseDTO.setStatus(exchange.getProperty("status").toString());
        billPaymentsResponseDTO.setReason(exchange.getProperty("reason").toString());
        billPaymentsResponseDTO.setBillId(exchange.getIn().getHeader(BILL_ID).toString());
        billPaymentsResponseDTO.setRequestID(exchange.getIn()
                .getHeader(CLIENTCORRELATIONID).toString());
        return billPaymentsResponseDTO;

    }

}