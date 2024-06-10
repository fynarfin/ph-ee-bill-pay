package org.mifos.pheeBillPay.api.definition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.mifos.pheeBillPay.data.BillInquiryResponseDTO;
import org.mifos.pheeBillPay.data.BillPaymentsReqDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@Tag(name = "GOV")
public interface BillPaymentsApi   {

    @Operation(
            summary = "Bill Payments API from Payer FSP to PBB")
    @PostMapping("/paymentNotifications")
    ResponseEntity<BillInquiryResponseDTO> billPayments(@RequestHeader(value="Platform-TenantId") String tenantId,
                                                        @RequestHeader(value="X-CorrelationID") String correlationId,
                                                        @RequestHeader(value="X-CallbackURL") String callbackURL,
                                                        @RequestHeader(value = "X-PayerFSP-Id")
                                                                String payerFspId,
                        @RequestBody BillPaymentsReqDTO body)
            throws ExecutionException, InterruptedException;
}