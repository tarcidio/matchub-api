package com.matchhub.matchhub.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Region {
    BR1(Continent.AMERICAS), //Brazil
    EUN1(Continent.EUROPE), //Europe Nordic & East
    EUW1(Continent.EUROPE), //Europe West
    JP1(Continent.ASIA), //Japan
    KR(Continent.ASIA), //Korea
    LA1(Continent.AMERICAS), //Latin America North
    LA2(Continent.AMERICAS), //Latin America South
    NA1(Continent.AMERICAS), //North America
    OC1(Continent.OCEANIA), //Oceania
    PH2(Continent.ASIA), //Philippines
    RU(Continent.ASIA), //Russia
    SG2(Continent.ASIA), //Singapore, Malaysia & Indonesia
    TH2(Continent.ASIA), //Thailand
    TR1(Continent.ASIA), //Turkey
    TW2(Continent.ASIA), //Taiwan, Hong Kong & Macao
    VN2(Continent.ASIA); //Vietnam

    @Getter
    private final Continent continent;

    @Override
    public String toString() {
        return this.name();
    }
}

