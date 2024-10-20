package com.attus.processmanager.models.enums;

public enum ActionType {
    PETICAO,
    AUDIENCIA,
    SENTENCA;

    public static ActionType tryConvert(String text) throws IllegalArgumentException {
        try {
            return ActionType.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid action type");
        }
    }
}
