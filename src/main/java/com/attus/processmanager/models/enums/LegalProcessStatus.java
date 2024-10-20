package com.attus.processmanager.models.enums;

public enum LegalProcessStatus {
    ATIVO,
    SUSPENSO,
    ARQUIVADO;

    public static LegalProcessStatus tryConvert(String text) throws IllegalArgumentException {
        try {
            return LegalProcessStatus.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid process status");
        }
    }
}

