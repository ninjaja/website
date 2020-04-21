package com.company.website.security;

import com.company.website.model.Role;
import com.company.website.model.User;
import com.company.website.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


/**
 * @author Dmitry Matrizaev
 * @since 21.04.2020
 */

@Service
@Transactional
public class WebsiteUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("Login " + login + " not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
                getAuthorities(user));
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        List<String> list = new ArrayList<>();
        for (Role role : user.getRoles()) {
            String name = role.getName();
            list.add(name);
        }
        String[] userRoles = list.toArray(new String[0]);
        return AuthorityUtils.createAuthorityList(userRoles);
    }
}
