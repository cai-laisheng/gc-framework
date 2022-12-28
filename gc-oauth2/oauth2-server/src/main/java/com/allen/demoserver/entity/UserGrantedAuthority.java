package com.allen.demoserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

public class UserGrantedAuthority implements GrantedAuthority {
    private static final long serialVersionUID = 600L;
    private String role;

    public UserGrantedAuthority(String role){
        this.role = role;
    }


    public UserGrantedAuthority(){}

    @JsonIgnore
    @Override
    public String getAuthority() {
        return role;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof UserGrantedAuthority ? this.role.equals(((UserGrantedAuthority)obj).role) : false;
        }
    }

    public int hashCode() {
        return this.role.hashCode();
    }

    public String toString() {
        return this.role;
    }
}
