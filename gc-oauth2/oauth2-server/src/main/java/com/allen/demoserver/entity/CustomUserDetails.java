package com.allen.demoserver.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Author xuguocai
 * @Date 22:22 2022/12/30
 **/
public class CustomUserDetails extends SysUser implements UserDetails {
    private static final List<GrantedAuthority> ROLE_USER = Collections
            .unmodifiableList(AuthorityUtils.createAuthorityList("ROLE_USER"));

    public CustomUserDetails(SysUser sysUser) {
        super(sysUser.getId(), sysUser.getUsername(), sysUser.getNickName(),sysUser.getEmail(),
                sysUser.getPhone(),sysUser.getDepartmentId(),sysUser.getDepartmentName(),
                sysUser.getPassword(),sysUser.getUserGender(),sysUser.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ROLE_USER;
    }

    @Override
    public String getUsername() {
        return getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
