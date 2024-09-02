package it.ale.controller;

import it.ale.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    public String pay(@RequestBody Map<String, Object> paymentDetails) {
        try {
            Double total = Double.parseDouble(paymentDetails.get("total").toString());
            String approvalLink = paymentService.createPayPalPayment(total, "USD", "Payment description",
                    "http://localhost:3000/cancel", "http://localhost:3000/success");
            return approvalLink;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/success")
    public String success(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            return paymentService.executePayPalPayment(paymentId, payerId);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
