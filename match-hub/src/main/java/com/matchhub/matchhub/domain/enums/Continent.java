package com.matchhub.matchhub.domain.enums;

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
