package com.aleyn.best_travel.infrastructure.services;

import com.aleyn.best_travel.domain.entities.documents.AppUserDocument;
import com.aleyn.best_travel.domain.repositories.mongo.AppUserRepository;
import com.aleyn.best_travel.infrastructure.abstract_services.ModifyUserService;
import com.aleyn.best_travel.util.enums.Tables;
import com.aleyn.best_travel.util.exceptions.UserNameNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class AppUserServiceImp implements ModifyUserService, UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public Map<String, Boolean> enabled(String userName) {
        var user = this.appUserRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNameNotFoundException(COLLECTION_NAME));

        user.setEnabled(!user.isEnabled());

        var userSaved = this.appUserRepository.save(user);

        return Collections.singletonMap(userSaved.getUserName(), userSaved.isEnabled());
    }

    @Override
    public Map<String, Set<String>> addRole(String userName, String role) {
        var user = this.appUserRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNameNotFoundException(COLLECTION_NAME));

        user.getRole().getGrantedAuthorities().add(role);

        var userSaved = this.appUserRepository.save(user);

        var authorities = userSaved.getRole().getGrantedAuthorities();

        log.info("User {} add role {}", userSaved.getUserName(), userSaved.getRole().getGrantedAuthorities().toString());

        return Collections.singletonMap(userSaved.getUserName(), authorities);
    }

    @Override
    public Map<String, Set<String>> removeRole(String userName, String role) {
        var user = this.appUserRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNameNotFoundException(COLLECTION_NAME));

        user.getRole().getGrantedAuthorities().remove(role);

        var userSaved = this.appUserRepository.save(user);

        var authorities = userSaved.getRole().getGrantedAuthorities();

        log.info("User {} remove role {}", userSaved.getUserName(), userSaved.getRole().getGrantedAuthorities().toString());

        return Collections.singletonMap(userSaved.getUserName(), authorities);
    }

    @Transactional(readOnly = true)
    public void loadByUsername(String username) {
        var user = this.appUserRepository.findByUserName(username)
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = this.appUserRepository.findByUserName(username)
                .orElseThrow(() -> new UserNameNotFoundException(Tables.CUSTOMER.name()));

        return mapUserToUserDetails(user);
    }

    private UserDetails mapUserToUserDetails(AppUserDocument user) {
        Set<GrantedAuthority> authorities = user.getRole()
                .getGrantedAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        System.err.println("Authority from db: " + authorities);

        return new User(
                user.getUserName(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities
        );

    }


    private static final String COLLECTION_NAME = "app_user";

}
