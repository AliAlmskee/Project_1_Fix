package com.project1.sms;
import com.project1.user.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Service
public class SMSService {

    private static final Logger logger = LoggerFactory.getLogger(SMSService.class);

    @Value("${sms.api_key}")
    private String apiKey;

    @Value("${sms.url}")
    private String apiUrl;

    public String sendSMS(User user, int otp, String message) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(apiUrl);

        // Set request headers
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, apiKey);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        httpPost.setHeader(HttpHeaders.ACCEPT, "application/json");

        // Set request body
        String requestBody = String.format("{\"message\": \"%s %d\", \"to\": \"%s\"}", message, otp, user.getPhone());
        httpPost.setEntity(new StringEntity(requestBody));

        logger.info("Sending SMS to {}: {}", user.getPhone(), requestBody);

        HttpResponse response;
        try {
            // Send the request and get the response
            response = httpClient.execute(httpPost);
            logger.info("Received response: {}", response);
        } catch (ClientProtocolException e) {
            logger.error("HTTP protocol error occurred", e);
            throw new Exception("HTTP protocol error occurred", e);
        } catch (IOException e) {
            logger.error("Network error occurred", e);
            throw new Exception("Network error occurred", e);
        }

        // Process the responseS
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());

        // Handle the response as needed
        if (statusCode == 200) {
            // Successful request
            return responseBody;
        } else {
            // Request failed
            throw new Exception("Failed to send SMS. Status code: " + statusCode + ", Response: " + responseBody);
        }
    }
}
