package com.attus.processmanager.models.enums;

public enum ActionType {
    PETICAO,
    AUDIENCIA,
    SENTENCA;

    public static ActionType tryConvert(String text) throws IllegalArgumentException {
        String message = "Invalid action type";
        try {
            if (text == null || text.isEmpty()) {
                throw  new IllegalArgumentException(message);
            }
            return ActionType.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message);
        }
    }
}
