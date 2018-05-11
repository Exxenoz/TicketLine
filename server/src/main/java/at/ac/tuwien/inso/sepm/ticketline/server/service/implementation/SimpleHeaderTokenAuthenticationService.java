package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationToken;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import at.ac.tuwien.inso.sepm.ticketline.server.configuration.properties.AuthenticationConfigurationProperties;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.UserDisabledException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.HeaderTokenAuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static at.ac.tuwien.inso.sepm.ticketline.server.security.AuthenticationConstants.*;

@Service
public class SimpleHeaderTokenAuthenticationService implements HeaderTokenAuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final SecretKeySpec signingKey;
    private final SignatureAlgorithm signatureAlgorithm;
    private final Duration validityDuration;
    private final Duration overlapDuration;

    @Autowired
    private UserService userService;

    public SimpleHeaderTokenAuthenticationService(
        @Lazy AuthenticationManager authenticationManager,
        AuthenticationConfigurationProperties authenticationConfigurationProperties,
        ObjectMapper objectMapper
    ) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        final var apiKeySecretBytes = Base64.getEncoder().encode(
            authenticationConfigurationProperties.getSecret().getBytes());
        signatureAlgorithm = authenticationConfigurationProperties.getSignatureAlgorithm();
        signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        validityDuration = authenticationConfigurationProperties.getValidityDuration();
        overlapDuration = authenticationConfigurationProperties.getOverlapDuration();
    }

    @Override
    public AuthenticationToken authenticate(String username, CharSequence password) {

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        } catch (AuthenticationException a) {
            LOGGER.error(String.format("Failed to authenticate user with name: %s", username), a);

            if(userService.findUserByName(username) != null) {
                boolean isDisabled = userService.increaseStrikes(userService.findUserByName(username));
                if (!isDisabled) {
                    throw new BadCredentialsException("Wrong password.");
                } else {
                    LOGGER.info("User will been informed that he was disabled.");
                    throw new UserDisabledException("User is disabled.");
                }
            } else {
               throw new BadCredentialsException("User name was not found.");
            }
        }

        final var now = Instant.now();
        var authorities = "";
        try {
            authorities = objectMapper.writeValueAsString(authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to wrap authorities", e);
        }
        final var currentToken = Jwts.builder()
            .claim(JWT_CLAIM_PRINCIPAL_ID, null)
            .claim(JWT_CLAIM_PRINCIPAL, authentication.getName())
            .claim(JWT_CLAIM_AUTHORITY, authorities)
            .setIssuedAt(Date.from(now))
            .setNotBefore(Date.from(now))
            .setExpiration(Date.from(now.plus(validityDuration)))
            .signWith(signatureAlgorithm, signingKey)
            .compact();
        final var futureToken = Jwts.builder()
            .claim(JWT_CLAIM_PRINCIPAL_ID, null)
            .claim(JWT_CLAIM_PRINCIPAL, authentication.getName())
            .claim(JWT_CLAIM_AUTHORITY, authorities)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now
                .plus(validityDuration
                    .minus(overlapDuration)
                    .plus(validityDuration))))
            .setNotBefore(Date.from(now
                .plus(validityDuration
                    .minus(overlapDuration))))
            .signWith(signatureAlgorithm, signingKey)
            .compact();
        return AuthenticationToken.builder()
            .currentToken(currentToken)
            .futureToken(futureToken)
            .build();
    }

    @Override
    public AuthenticationTokenInfo authenticationTokenInfo(String headerToken) {
        final var claims = Jwts.parser()
            .setSigningKey(signingKey)
            .parseClaimsJws(headerToken)
            .getBody();
        final var roles = readJwtAuthorityClaims(claims);
        return AuthenticationTokenInfo.builder()
            .username((String) claims.get(JWT_CLAIM_PRINCIPAL))
            .roles(roles)
            .issuedAt(LocalDateTime.ofInstant(claims.getIssuedAt().toInstant(), ZoneId.systemDefault()))
            .notBefore(LocalDateTime.ofInstant(claims.getNotBefore().toInstant(), ZoneId.systemDefault()))
            .expireAt(LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault()))
            .validityDuration(validityDuration)
            .overlapDuration(overlapDuration)
            .build();
    }

    @Override
    public AuthenticationToken renewAuthentication(String headerToken) {
        final var claims = Jwts.parser()
            .setSigningKey(signingKey)
            .parseClaimsJws(headerToken)
            .getBody();
        final var futureToken = Jwts.builder()
            .claim(JWT_CLAIM_PRINCIPAL_ID, claims.get(JWT_CLAIM_PRINCIPAL_ID))
            .claim(JWT_CLAIM_PRINCIPAL, claims.get(JWT_CLAIM_PRINCIPAL))
            .claim(JWT_CLAIM_AUTHORITY, claims.get(JWT_CLAIM_AUTHORITY))
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(claims.getExpiration().toInstant()
                .plus(validityDuration
                    .minus(overlapDuration))))
            .setNotBefore(Date.from(claims.getExpiration().toInstant().minus(overlapDuration)))
            .signWith(signatureAlgorithm, signingKey)
            .compact();
        return AuthenticationToken.builder()
            .currentToken(headerToken)
            .futureToken(futureToken)
            .build();
    }

    @Override
    public User authenticate(String headerToken) {
        try {
            final Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(headerToken)
                .getBody();
            List<String> authoritiesWrapper = readJwtAuthorityClaims(claims);
            List<SimpleGrantedAuthority> authorities = authoritiesWrapper.stream()
                .map(roleName -> roleName.startsWith(ROLE_PREFIX) ?
                    roleName : (ROLE_PREFIX + roleName))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
            return new User(
                (String) claims.get(JWT_CLAIM_PRINCIPAL),
                headerToken,
                authorities);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("Credentials expired");
        } catch (JwtException e) {
            throw new BadCredentialsException("Bad Credentials");
        }
    }

    private List<String> readJwtAuthorityClaims(Claims claims) {
        var authoritiesWrapper = new ArrayList<String>();
        try {
            authoritiesWrapper = objectMapper.readValue(claims.get(
                JWT_CLAIM_AUTHORITY, String.class),
                new TypeReference<List<String>>() {
                });
        } catch (IOException e) {
            LOGGER.error("Failed to unwrap roles", e);
        }
        return authoritiesWrapper;
    }
}