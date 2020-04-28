package com.company.website.service;

import com.company.website.model.Role;
import com.company.website.model.User;
import com.company.website.repository.RoleRepository;
import com.company.website.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Dmitry Matrizaev
 * @since 28.04.2020
 */
public class WebsiteUserInfoTokenServices implements ResourceServerTokenServices {

    protected static final Logger LOGGER = LoggerFactory.getLogger(WebsiteUserInfoTokenServices.class);
    private String userInfoEndpointUrl;
    private String clientId;
    private OAuth2RestOperations restTemplate;
    private String tokenType = "Bearer";
    private PrincipalExtractor principalExtractor = new FixedPrincipalExtractor();

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public WebsiteUserInfoTokenServices() {
    }

    public WebsiteUserInfoTokenServices(String userInfoEndpointUrl, String clientId) {
        this.userInfoEndpointUrl = userInfoEndpointUrl;
        this.clientId = clientId;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setRestTemplate(OAuth2RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setPrincipalExtractor(PrincipalExtractor principalExtractor) {
        Assert.notNull(principalExtractor, "PrincipalExtractor must not be null");
        this.principalExtractor = principalExtractor;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);
        User user = null;
        if (map.containsKey("sub")) {
            String googleEmail = (String) map.get("email");
            user = userRepository.findByLogin(googleEmail);
            if (Objects.isNull(user)) {
                user = new User();
                Role role = roleRepository.findByName("USER");
                user.setRoles(Collections.singleton(role));
            }
            user.setLogin(googleEmail);
            user.setPassword(passwordEncoder.encode("oauth2user"));
            userRepository.save(user);
        }
        if (map.containsKey("error")) {
            LOGGER.debug("userinfo returned error: " + map.get("error"));
            throw new InvalidTokenException(accessToken);
        }
        return extractAuthentication(map, user);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String s) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    private OAuth2Authentication extractAuthentication(Map<String, Object> map, User user) {
        Object principal = getPrincipal(map);
        List<GrantedAuthority> authorities = getAuthorities(user);
        OAuth2Request request = new OAuth2Request(null, this.clientId, null, true, null,
                null, null, null, null);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                principal, "N/A", authorities);
        token.setDetails(map);
        return new OAuth2Authentication(request, token);
    }

    protected Object getPrincipal(Map<String, Object> map) {
        Object principal = this.principalExtractor.extractPrincipal(map);
        return (principal == null ? "unknown" : principal);
    }

    private Map<String, Object> getMap(String path, String accessToken) {
        this.LOGGER.debug("Getting user info from: " + path);
        try {
            OAuth2RestOperations restTemplate = this.restTemplate;
            if (restTemplate == null) {
                BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
                resource.setClientId(this.clientId);
                restTemplate = new OAuth2RestTemplate(resource);
            }
            OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext()
                    .getAccessToken();
            if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
                DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(
                        accessToken);
                token.setTokenType(this.tokenType);
                restTemplate.getOAuth2ClientContext().setAccessToken(token);
            }
            return restTemplate.getForEntity(path, Map.class).getBody();
        } catch (Exception ex) {
            this.LOGGER.warn("Could not fetch user details: " + ex.getClass() + ", "
                    + ex.getMessage());
            return Collections.<String, Object>singletonMap("error",
                    "Could not fetch user details");
        }
    }

    private static List<GrantedAuthority> getAuthorities(User user) {
        List<String> list = new ArrayList<>();
        for (Role role : user.getRoles()) {
            String name = role.getName();
            list.add(name);
        }
        String[] userRoles = list.toArray(new String[0]);
        return AuthorityUtils.createAuthorityList(userRoles);
    }
}
