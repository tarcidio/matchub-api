package com.matchub.api.matchub_api.security.token.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    ADMIN_PATCH("admin:patch"),
    MODERATOR_READ("moderator:read"),
    MODERATOR_UPDATE("moderator:update"),
    MODERATOR_CREATE("moderator:create"),
    MODERATOR_DELETE("moderator:delete"),
    MODERATOR_PATCH("moderator:patch"),
    HUBUSER_READ("hubuser:read"),
    HUBUSER_UPDATE("hubuser:update"),
    HUBUSER_CREATE("hubuser:create"),
    HUBUSER_DELETE("hubuser:delete"),
    HUBUSER_PATCH("hubuser:patch"),
    GUEST_CONFIRM("guest:confirm");

    @Getter
    private final String permission;
}
