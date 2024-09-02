package it.ale.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PaymentService {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    private final RestTemplate restTemplate = new RestTemplate();

    private String getAccessToken() throws Exception {
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + encodedAuth);

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api." + mode + ".paypal.com/v1/oauth2/token",
                HttpMethod.POST,
                request,
                String.class
        );

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    public String createPayPalPayment(Double total, String currency, String description, String cancelUrl, String successUrl) throws Exception {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> amount = new HashMap<>();
        amount.put("currency", currency);
        amount.put("total", String.format("%.2f", total));

        Map<String, Object> transaction = new HashMap<>();
        transaction.put("amount", amount);
        transaction.put("description", description);

        Map<String, Object> payer = new HashMap<>();
        payer.put("payment_method", "paypal");

        Map<String, Object> redirectUrls = new HashMap<>();
        redirectUrls.put("cancel_url", cancelUrl);
        redirectUrls.put("return_url", successUrl);

        Map<String, Object> payment = new HashMap<>();
        payment.put("intent", "sale");
        payment.put("payer", payer);
        payment.put("transactions", new Object[]{transaction});
        payment.put("redirect_urls", redirectUrls);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payment, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api." + mode + ".paypal.com/v1/payments/payment",
                request,
                String.class
        );

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.getBody());
        return jsonNode.get("links").get(1).get("href").asText(); // URL di approvazione
    }

    public String executePayPalPayment(String paymentId, String payerId) throws Exception {
        String accessToken = getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> paymentExecution = new HashMap<>();
        paymentExecution.put("payer_id", payerId);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(paymentExecution, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api." + mode + ".paypal.com/v1/payments/payment/" + paymentId + "/execute",
                request,
                String.class
        );

        return response.getBody();
    }
}
