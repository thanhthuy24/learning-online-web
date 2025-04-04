package com.htt.elearning.spellcheck;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SpellCheckServiceImpl {
    private final String API_URL = "https://api.languagetool.org/v2/check"; // API của LanguageTool
    private final RestTemplate restTemplate = new RestTemplate();

    public List<LanguageToolResponseDTO> checkSpelling(String text, String language) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("text", text);
        params.add("language", language);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // Gửi request POST đến API
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, requestEntity, String.class);

        return parseResponse(response.getBody()); // Trả về kết quả JSON
    }

    private List<LanguageToolResponseDTO> parseResponse(String responseBody) {
        List<LanguageToolResponseDTO> result = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode matches = root.path("matches");

            for (JsonNode match : matches) {
                String message = match.path("message").asText();
                String shortMessage = match.path("shortMessage").asText();

                List<String> replacements = new ArrayList<>();
                for (JsonNode replacement : match.path("replacements")) {
                    replacements.add(replacement.path("value").asText());
                }

                result.add(new LanguageToolResponseDTO(message, shortMessage, replacements));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
