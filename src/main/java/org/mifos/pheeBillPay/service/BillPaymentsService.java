package org.mifos.pheeBillPay.service;

import org.mifos.pheeBillPay.data.BillPaymentsReqDTO;
import org.mifos.pheeBillPay.zeebe.ZeebeProcessStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.mifos.pheeBillPay.zeebe.ZeebeVariables.*;

@Service
public class BillPaymentsService {
    @Autowired
    private ZeebeProcessStarter zeebeProcessStarter;

    @Value("${bpmn.flows.payment-notification}")
    String paymentNotificationFlow;

    String transactionId;

    public String billPayments(String tenantId, String correlationId, String callbackUrl, String payerFspId,
                               BillPaymentsReqDTO body) {
        Map<String, Object> extraVariables = new HashMap<>();
        extraVariables.put(TENANT_ID, tenantId);
        extraVariables.put(CLIENTCORRELATIONID, correlationId);
        extraVariables.put(PAYER_FSP_ID, payerFspId);
        extraVariables.put(BILL_ID, body.getBillId());
        extraVariables.put(PAYMENTS_REF_ID, body.getPaymentReferenceID());
        extraVariables.put(BILL_REQ_ID, body.getBillInquiryRequestId());
        extraVariables.put(CALLBACK_URL, callbackUrl);
        extraVariables.put(BILL_PAYMENTS_REQ, body);
        String tenantSpecificBpmn = paymentNotificationFlow.replace("{dfspid}", tenantId);
        transactionId = zeebeProcessStarter.startZeebeWorkflow(tenantSpecificBpmn,
                body.toString(), extraVariables);
        return transactionId;
    }

}
