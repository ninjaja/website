package com.company.website.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @author Dmitry Matrizaev
 * @since 04.05.2020
 */
@Getter
@Setter
public class UserPrincipal implements UserDetails {

    private final String password;
    private final String username;
    private final List<GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public UserPrincipal(String username, String password, List<GrantedAuthority> authorities) {
        this(username, password, authorities, true, true, true, true);
    }

    public UserPrincipal(String username, String password, final List<GrantedAuthority> authorities,
                         boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired,
                         boolean enabled) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

}
