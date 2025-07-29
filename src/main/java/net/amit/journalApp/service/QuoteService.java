package net.amit.journalApp.service;

import net.amit.journalApp.api.response.QuoteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class QuoteService {

    @Value("${api-keys.ninja-quotes}")
    private String apiKey;

    private static final String URL = "https://api.api-ninjas.com/v1/quotes";
//    in this api we are passing the api key in header

    @Autowired
    private RestTemplate restTemplate;

    public QuoteResponse getQuote(){
        // Create HttpHeaders and set the API key
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);

        // Create an HttpEntity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<QuoteResponse[]> response = restTemplate.exchange(URL, HttpMethod.GET, entity, QuoteResponse[].class);

        QuoteResponse[] quotes = response.getBody();

        if (quotes != null && quotes.length > 0) {
            return quotes[0]; // Return the first quote in the array
        }

        return null;
    }
}
