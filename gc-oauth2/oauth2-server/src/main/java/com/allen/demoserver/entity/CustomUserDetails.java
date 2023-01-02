package com.allen.demoserver.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @Author xuguocai
 * @Date 22:22 2022/12/30
 **/
public class CustomUserDetails extends SysUser implements UserDetails {
//    private static final List<GrantedAuthority> ROLE_USER = Collections
//            .unmodifiableList(AuthorityUtils.createAuthorityList("ROLE_USER"));

    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(SysUser sysUser,Collection<? extends GrantedAuthority> authorities) {
        super(sysUser.getId(), sysUser.getUsername(), sysUser.getNickName(),sysUser.getEmail(),
                sysUser.getPhone(),sysUser.getDepartmentId(),sysUser.getDepartmentName(),
                sysUser.getPassword(),sysUser.getUserGender(),sysUser.getRoles());
        this.authorities = handleAuthorities(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

    private static List<GrantedAuthority> handleAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        // Ensure array iteration order is predictable (as per
        // UserDetails.getAuthorities() contract and SEC-717)
        List<GrantedAuthority> sortedAuthorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }
        return sortedAuthorities;
    }
}
