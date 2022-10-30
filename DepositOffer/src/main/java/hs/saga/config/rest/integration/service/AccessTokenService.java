// THIS FILE IS AUTOGENERATED. MODIFICATIONS WILL BE LOST!

package hs.saga.config.rest.integration.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kbstar.saga.sagadpoffr.sdk.integration.config.AccessTokenConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import static de.knowis.cp.util.lang.ByteUtil.limit;
import static de.knowis.cp.util.lang.ByteUtil.transform;

@Service
public class AccessTokenService {

  private static final Logger log = LoggerFactory.getLogger(AccessTokenService.class);

  private static final int BODY_LENGHT_LIMIT = 1024;

  private final AccessTokenConfiguration config;
  private final RestTemplate restTemplate;
  private final ClientRegistrationRepository clientRegistrationRepository;

  public AccessTokenService(RestTemplate restTemplate, ClientRegistrationRepository clientRegistrationRepository, AccessTokenConfiguration config) {
    this.restTemplate = restTemplate;
    this.clientRegistrationRepository = clientRegistrationRepository;
    this.config = config;
  }


  /**
   * This method performs technichal user sign-in flow using configured OIDC client
   */
  public Authentication loginAsTechnicalUser() {

    // Check for clientRegistrationId property
    if (config.getClientRegistrationId() == null) {
      throw new IllegalStateException("Missing configuration property de.knowis.cp.oidc.clientRegistrationId");
    }

    // Obtain an access token
    String tokenValue = obtainAccessToken();

    // Get Security Context Authentication
    Authentication originalAuthentication = SecurityContextHolder.getContext().getAuthentication();

    // Set JwtAuthenticationToken in Authentication object
    JwtAuthenticationToken jwtAuth = convert(tokenValue);
    SecurityContextHolder.getContext().setAuthentication(jwtAuth);

    return originalAuthentication;
  }

  /**
   * This method obtains access token using configured OIDC client
   */
  private String obtainAccessToken() {

    ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(config.getClientRegistrationId());
    if (clientRegistration == null) {
      throw new IllegalStateException(String.format("Missing client registration with client registration id %s ", config.getClientRegistrationId()));
    }

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "client_credentials");
    params.add("client_id", clientRegistration.getClientId());
    params.add("client_secret", clientRegistration.getClientSecret());
    params.add("scope", "openid");

    String token;
    try {
      ObjectNode tokenHolder = restTemplate.postForObject(clientRegistration.getProviderDetails().getTokenUri(), params, ObjectNode.class);
      token = tokenHolder.get("access_token").asText();
    } catch (RestClientResponseException e) {
      String body = transform(limit(e.getResponseBodyAsByteArray(), BODY_LENGHT_LIMIT));
      String errorMessage = String.format("RestClientResponseException [statusCode=%s, statusText=%s, headers=%s, body=%s]",
              e.getRawStatusCode(), e.getStatusText(), e.getResponseHeaders(), body);
      throw new RuntimeException(errorMessage, e);
    }
    return token;
  }

  /**
   * This method converts jwt token into JwtAuthenticationToken
   */
  private JwtAuthenticationToken convert(String tokenValue) {
    JwtDecoder jwtDecoder = setupJwtDecoder(config);
    Jwt jwt = jwtDecoder.decode(tokenValue);
    return new JwtAuthenticationToken(jwt);
  }

  /**
   * This method sets up JwtDecoder using registered client
   */
  private JwtDecoder setupJwtDecoder(AccessTokenConfiguration config) {

    String clientRegistrationId = config.getClientRegistrationId();

    ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(clientRegistrationId);

    NimbusJwtDecoder jwtDecoder = null;
    if (clientRegistration != null) {
      try {
        jwtDecoder = NimbusJwtDecoder.withJwkSetUri(clientRegistration.getProviderDetails().getJwkSetUri()).build();
        jwtDecoder.setJwtValidator(t -> OAuth2TokenValidatorResult.success());
      } catch (RuntimeException e) {
        throw new RuntimeException("Failed to instantiate a JwtDecoder", e);
      }
    } else {
      throw new IllegalStateException(String
              .format("a client registration with client registration id %s is not available", clientRegistrationId));
    }
    return jwtDecoder;
  }
}
