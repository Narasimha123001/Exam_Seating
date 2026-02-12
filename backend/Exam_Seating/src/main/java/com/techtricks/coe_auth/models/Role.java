package com.techtricks.coe_auth.models;


import lombok.Getter;


import java.util.Set;

@Getter
public enum Role {

    ADMIN(Set.of(Permission.READ, Permission.WRITE, Permission.DELETE)),
    STUDENT(Set.of(Permission.READ)),
    STAFF(Set.of(Permission.READ));
    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

}
