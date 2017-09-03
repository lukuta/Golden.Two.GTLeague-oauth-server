package com.goldentwo.integration;

import com.goldentwo.model.BettingTimeRestriction;
import com.jayway.restassured.path.json.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OAuth2TokenTest {

    private static final String CLIENT = "client";
    private static final String SECRET = "secret";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldGetUnauthorizedRequest() {
        ResponseEntity<String> response = restTemplate.getForEntity("/banner", String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldGetValidTokenForClient() {

        HttpHeaders headers = createBasicAuthHeaders(CLIENT, SECRET);

        ResponseEntity<String> response = restTemplate
                .postForEntity("/oauth/token?grant_type=client_credentials", new HttpEntity<>(headers), String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldGetValidTokenForUser() {
        HttpHeaders headers = createBasicAuthHeaders(CLIENT, SECRET);
        String credentialParams = String.format("&username=%s&password=%s", USER, PASSWORD);

        ResponseEntity<String> response = restTemplate
                .postForEntity("/oauth/token?grant_type=password" + credentialParams,
                        new HttpEntity<>(headers),
                        String.class);

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldGetAccessToProtectedResourceHavingValidToken() {
        HttpHeaders headers = createBasicAuthHeaders(CLIENT, SECRET);
        String credentialParams = String.format("&username=%s&password=%s", USER, PASSWORD);

        ResponseEntity<String> response = restTemplate
                .postForEntity("/oauth/token?grant_type=password" + credentialParams,
                        new HttpEntity<>(headers),
                        String.class);
        JsonPath jsonPath = new JsonPath(response.getBody());
        String accessToken = jsonPath.get("access_token");

        ResponseEntity<String> responseWithToken = restTemplate
                .getForEntity("/banner?access_token=" + accessToken,
                        String.class);

        assertThat(responseWithToken.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldGetAccessDenied() {
        HttpHeaders headers = createBasicAuthHeaders(CLIENT, SECRET);
        String credentialParams = String.format("&username=%s&password=%s", USER, PASSWORD);

        ResponseEntity<String> response = restTemplate
                .postForEntity("/oauth/token?grant_type=password" + credentialParams,
                        new HttpEntity<>(headers),
                        String.class);
        JsonPath jsonPath = new JsonPath(response.getBody());
        String accessToken = jsonPath.get("access_token");

        ResponseEntity<String> responseWithToken = restTemplate
                .postForEntity("/betting-times?access_token=" + accessToken,
                        BettingTimeRestriction.class,
                        String.class);

        assertThat(responseWithToken.getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    private HttpHeaders createBasicAuthHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }
}
