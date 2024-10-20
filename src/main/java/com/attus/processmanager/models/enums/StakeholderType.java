package com.attus.processmanager.models.enums;

public enum StakeholderType {
    AUTOR,
    REU,
    ADVOGADO;

    public static StakeholderType tryConvert(String text) throws IllegalArgumentException {
        try {
            return StakeholderType.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid stakeholder type");
        }
    }
}
