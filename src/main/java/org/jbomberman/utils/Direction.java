package org.jbomberman.utils;

import javafx.scene.input.KeyCode;

public enum Direction {
    UP(KeyCode.UP),
    DOWN(KeyCode.DOWN),
    LEFT(KeyCode.LEFT),
    RIGHT(KeyCode.RIGHT),
    CENTER(null);

    private final KeyCode keyCode;

    Direction(KeyCode keyCode) {
        this.keyCode = keyCode;
    }


    public KeyCode getKeyCode() {
        return keyCode;
    }
}

