package com.matchub.api.matchub_api.domain.enums;

public enum Continent {
    AMERICAS,
    EUROPE,
    ASIA,
    OCEANIA,
    ESPORT;

    @Override
    public String toString() {
        return this.name();
    }
}
