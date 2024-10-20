package com.attus.processmanager.models.enums;

public enum ActionType {
    PETICAO,
    AUDIENCIA,
    SENTENCA;

    public static ActionType tryConvert(String text) {
        try {
            return ActionType.valueOf(text);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid action type");
        }
    }
}
