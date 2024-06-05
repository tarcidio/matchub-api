package com.matchhub.matchhub.security.token.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {

    ADMIN(Set.of(
                    Permission.ADMIN_READ,
                    Permission.ADMIN_UPDATE,
                    Permission.ADMIN_CREATE,
                    Permission.ADMIN_DELETE,
                    Permission.ADMIN_PATCH,
                    Permission.MODERATOR_READ,
                    Permission.MODERATOR_UPDATE,
                    Permission.MODERATOR_CREATE,
                    Permission.MODERATOR_DELETE,
                    Permission.MODERATOR_PATCH,
                    Permission.HUBUSER_READ,
                    Permission.HUBUSER_UPDATE,
                    Permission.HUBUSER_CREATE,
                    Permission.HUBUSER_DELETE,
                    Permission.HUBUSER_PATCH)
    ),
    MODERATOR(Set.of(
                    Permission.MODERATOR_READ,
                    Permission.MODERATOR_UPDATE,
                    Permission.MODERATOR_CREATE,
                    Permission.MODERATOR_DELETE,
                    Permission.MODERATOR_PATCH,
                    Permission.HUBUSER_READ,
                    Permission.HUBUSER_UPDATE,
                    Permission.HUBUSER_CREATE,
                    Permission.HUBUSER_DELETE,
                    Permission.HUBUSER_PATCH)
    ),
    HUBUSER(Set.of(
                    Permission.HUBUSER_READ,
                    Permission.HUBUSER_UPDATE,
                    Permission.HUBUSER_CREATE,
                    Permission.HUBUSER_DELETE,
                    Permission.HUBUSER_PATCH)
    ),
    GUEST(Set.of(Permission.GUEST_CONFIRM));
    @Getter
    private final Set<Permission> permissions;

    // Get a list of authorities (roles and permissions) for a user or role
    // Example: [new SimpleGrantedAuthority("management:read"),
    //           new SimpleGrantedAuthority("management:update"),
    //           new SimpleGrantedAuthority("management:create"),
    //           new SimpleGrantedAuthority("management:delete"),
    //           new SimpleGrantedAuthority("ROLE_MANAGER")]
    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}