package org.jbomberman.utils;

public record Triad(Coordinate coordinate, Direction direction, boolean isLast) {

    @Override
    public String toString() {
        return coordinate + " " + direction + " " + isLast;
    }
}
