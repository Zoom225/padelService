package com.padel.model.enums;

import lombok.Getter;

@Getter
public enum UserType {
    GLOBAL("G", true, 21),  // Matricule Gxxxx - tous les sites - 3 semaines avant
    SITE("S", false, 14),    // Matricule Sxxxxx - son site uniquement - 2 semaines avant
    LIBRE("L", true, 5);     // Matricule Lxxxxx - tous les sites - 5 jours avant

    private final String prefix;
    private final boolean allSites;
    private final int reservationDaysLimit;

    UserType(String prefix, boolean allSites, int reservationDaysLimit) {
        this.prefix = prefix;
        this.allSites = allSites;
        this.reservationDaysLimit = reservationDaysLimit;
    }
}
