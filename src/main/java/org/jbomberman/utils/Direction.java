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


    public static Direction fromKeyCode(KeyCode keyCode) {
        for (Direction direction : values()) {
            if (direction.getKeyCode() == keyCode) {
                return direction;
            }
        }
        return null;
    }
}

