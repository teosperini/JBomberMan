package org.jbomberman.model;

import org.jbomberman.utils.Coordinate;
import org.jbomberman.utils.Direction;

public abstract class Entity {
    Coordinate position;
    protected Entity(Coordinate position){
        this.position = position;
    }

    protected void move(Direction direction){

    }

}
