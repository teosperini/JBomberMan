package org.jbomberman.utils;

public class Triad {
    private final Coordinate coordinate;
    private final Direction direction;
    private final boolean isLast;

    public Triad(Coordinate coordinate, Direction direction, boolean isLast) {
        this.coordinate = coordinate;
        this.direction = direction;
        this.isLast = isLast;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isLast() {
        return isLast;
    }

    @Override
    public String toString() {
        return coordinate + " " + direction + " " + isLast;
    }
}
