package com.htt.elearning.payment.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import java.util.Base64;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class MomoService {

    @Value("${momo.partnerCode}")
    private String partnerCode;

    @Value("${momo.accessKey}")
    private String accessKey;

    @Value("${momo.secretKey}")
    private String secretKey;

    @Value("${momo.endpoint}")
    private String endpoint;

    public String createSignature(String rawData) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return Hex.encodeHexString(sha256_HMAC.doFinal(rawData.getBytes()));
    }

    public boolean processMomoPayment(Map<String, String> params){
        String status = params.get("status");
        String orderId = params.get("orderId");
        String errorCode = params.get("errorCode");

        if ("0".equals(errorCode)) { // Giao dịch thành công
            return true;
        } else { // Giao dịch thất bại
            return false;
        }
    }

    public String createPaymentRequest(String orderId, float amount, String returnUrl) throws Exception {
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(endpoint);

        String orderInfo = "Thanh toán đơn hàng";
        String notifyUrl = "http://localhost:8080/api/momo/create-payment";
        String requestType = "captureMoMoWallet";

        // **Thêm returnUrl để MoMo chuyển hướng về frontend**
        String successUrl = returnUrl + "?status=success&orderId=" + orderId;
        String cancelUrl = returnUrl + "?status=fail&orderId=" + orderId;

        String rawSignature = "partnerCode=" + partnerCode
                + "&accessKey=" + accessKey
                + "&requestId=" + orderId
                + "&amount=" + String.valueOf((int) amount)
                + "&orderId=" + orderId
                + "&orderInfo=" + orderInfo
                + "&returnUrl=" + successUrl
                + "&notifyUrl=" + notifyUrl
                + "&extraData=";  // Phải để extraData là rỗng như trong JSON

        String signature = createSignature(rawSignature);

        String json = "{"
                + "\"partnerCode\": \"" + partnerCode + "\","
                + "\"accessKey\": \"" + accessKey + "\","
                + "\"requestId\": \"" + orderId + "\","
                + "\"amount\": \"" + String.valueOf((int) amount) + "\","
                + "\"orderId\": \"" + orderId + "\","
                + "\"orderInfo\": \"" + orderInfo + "\","
                + "\"returnUrl\": \"" + successUrl + "\","
                + "\"notifyUrl\": \"" + notifyUrl + "\","
                + "\"extraData\": \"\","
                + "\"requestType\": \"" + requestType + "\","
                + "\"signature\": \"" + signature + "\""
                + "}";

        post.setEntity(new StringEntity(json, "UTF-8"));
        post.setHeader("Content-Type", "application/json");

        HttpResponse response = client.execute(post);

        String jsonResponse = EntityUtils.toString(response.getEntity());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseJson = mapper.readTree(jsonResponse);

        int errorCode = responseJson.get("errorCode").asInt();
        if (errorCode == 0) {
            String payUrl = responseJson.get("payUrl").asText();
            return payUrl;
        } else {
            return "Thanh toán thất bại: " + responseJson.get("message").asText();
        }

//        return jsonResponse;
    }

}
