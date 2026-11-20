package com.checkpointgames.checkpoint_games.model;

public enum AgeRating {
    E("Everyone"),
    E10("Everyone 10+"),
    T("Teen"),
    M("Mature 17+"),
    NOT_APPLICABLE("Not Applicable");

    private final String displayName;

    AgeRating(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}