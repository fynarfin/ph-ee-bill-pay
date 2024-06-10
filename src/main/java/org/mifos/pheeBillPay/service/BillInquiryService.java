package org.mifos.pheeBillPay.service;

import org.mifos.pheeBillPay.zeebe.ZeebeProcessStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.mifos.pheeBillPay.zeebe.ZeebeVariables.*;

@Service
public class BillInquiryService {
    private Logger logger = LoggerFactory.getLogger(BillInquiryService.class);

    @Autowired
    private ZeebeProcessStarter zeebeProcessStarter;

    @Value("${bpmn.flows.bill-pay}")
    String billPayFlow;

    String transactionId;

        public String billInquiry(String tenantId, String correlationId, String callbackUrl, String payerFspId, String billId, String field) {
            Map<String, Object> extraVariables = new HashMap<>();
            extraVariables.put(TENANT_ID, tenantId);
            extraVariables.put(CLIENTCORRELATIONID, correlationId);
            extraVariables.put(CALLBACK_URL, callbackUrl);
            extraVariables.put(PAYER_FSP_ID, payerFspId);
            extraVariables.put(BILL_ID, billId);
            extraVariables.put(FIELD, field);
            String tenantSpecificBpmn = billPayFlow.replace("{dfspid}", tenantId);
            try {
                transactionId = zeebeProcessStarter.startZeebeWorkflow(tenantSpecificBpmn,
                        null, extraVariables);
            }
            catch (Exception e) {
                logger.info("Exception in starting workflow: {}", e.getMessage());
                transactionId = "NA";
            }

            return transactionId;
        }

}
