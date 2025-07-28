package com.gifree.security;

import com.gifree.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class CustomUserDetails implements UserDetails {

    private final String email;
    private final String password;
    private final boolean social;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Member member) {
        this.email = member.getEmail();
        this.password = member.getPw();
        this.social = member.isSocial();
        this.authorities = member.getMemberRoleList()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 필요 시 조절
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 필요 시 조절
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 필요 시 조절
    }

    @Override
    public boolean isEnabled() {
        return true; // 필요 시 조절
    }
}
