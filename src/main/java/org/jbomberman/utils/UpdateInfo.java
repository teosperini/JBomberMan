package org.jbomberman.utils;

import javafx.scene.input.KeyCode;

import java.util.List;

public class UpdateInfo {
    private final UpdateType updateType;
    private KeyCode keyCode;
    private boolean enemyLastLife;
    private boolean isPlayerInvincible;
    private Coordinate oldPosition;
    private Coordinate newPosition;
    private Coordinate coordinate;
    private List<Coordinate> blocks;
    private List<Coordinate> entities;
    private List<Triad> triadList;
    private int index;
    private int points;
    private int earnedPoints;
    private BlockType blockType;
    private int healthPoint;
    private int level;

    /**
     * The private constructor with the only mandatory attribute
     * @param updateType
     */
    private UpdateInfo(UpdateType updateType) {
        this.updateType = updateType;
    }

    //############ UpdateInfo GETTERS #############//
    public UpdateType getUpdateType() {
        return updateType;
    }

    public Coordinate getOldCoord() {
        return oldPosition;
    }

    public Coordinate getNewCoord() {
        return newPosition;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public List<Coordinate> getEntities() {
        return entities;
    }

    public List<Coordinate> getBlocks() {
        return blocks;
    }

    public int getIndex() {
        return index;
    }

    public int getLevel(){
        return level;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public boolean isEnemyLastLife() {
        return enemyLastLife;
    }

    public BlockType getSubBlock() {
        return blockType;
    }

    public List<Triad> getTriadList() {
        return triadList;
    }

    public int getPoints() {
        return points;
    }

    public int getHealthPoint(){
        return healthPoint;
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }

    public boolean isInvincible() {
        return isPlayerInvincible;
    }

    /**
     * The Builder class to implement the builder pattern
     */
    public static class Builder {
        private final UpdateType updateType;
        private KeyCode keyCode;
        private boolean enemyLastLife;
        private boolean isPlayerInvincible;
        private Coordinate oldPosition;
        private Coordinate newPosition;
        private Coordinate coordinate;
        private List<Coordinate> blocks;
        private List<Coordinate> entities;
        private List<Triad> triadList;
        private int index;
        private int points;
        private int earnedPoints;
        private BlockType blockType;
        private int healthPoint;
        private int level;

        public Builder(UpdateType updateType) {
            this.updateType = updateType;
        }

        //############### BUILDER SETTER METHODS ##############//
        public Builder setKeyCode(KeyCode keyCode) {
            this.keyCode = keyCode;
            return this;
        }

        public Builder setEnemyLastLife(boolean enemyLastLife) {
            this.enemyLastLife = enemyLastLife;
            return this;
        }

        public Builder setPlayerInvincible(boolean isPlayerInvincible) {
            this.isPlayerInvincible = isPlayerInvincible;
            return this;
        }

        public Builder setOldPosition(Coordinate oldPosition) {
            this.oldPosition = oldPosition;
            return this;
        }

        public Builder setNewPosition(Coordinate newPosition) {
            this.newPosition = newPosition;
            return this;
        }

        public Builder setCoordinate(Coordinate coordinate) {
            this.coordinate = coordinate;
            return this;
        }

        public Builder setBlocks(List<Coordinate> blocks) {
            this.blocks = blocks;
            return this;
        }

        public Builder setEntities(List<Coordinate> entities) {
            this.entities = entities;
            return this;
        }

        public Builder setTriadList(List<Triad> triadList) {
            this.triadList = triadList;
            return this;
        }

        public Builder setIndex(int index) {
            this.index = index;
            return this;
        }

        public Builder setLevel(int level) {
            this.level = level;
            return this;
        }

        public Builder setHealthPoint(int healthPoint) {
            this.healthPoint = healthPoint;
            return this;
        }

        public Builder setPoints(int points) {
            this.points = points;
            return this;
        }

        public Builder setEarnedPoints(int earnedPoints) {
            this.earnedPoints = earnedPoints;
            return this;
        }

        public Builder setSubBlock(BlockType block) {
            this.blockType = block;
            return this;
        }

        /**
         * This method creates the UpdateInfo instance
         *
         * @return
         */
        public UpdateInfo build() {
            UpdateInfo updateInfo = new UpdateInfo(updateType);
            updateInfo.keyCode = this.keyCode;
            updateInfo.enemyLastLife = this.enemyLastLife;
            updateInfo.isPlayerInvincible = this.isPlayerInvincible;
            updateInfo.oldPosition = this.oldPosition;
            updateInfo.newPosition = this.newPosition;
            updateInfo.coordinate = this.coordinate;
            updateInfo.blocks = this.blocks;
            updateInfo.entities = this.entities;
            updateInfo.triadList = this.triadList;
            updateInfo.index = this.index;
            updateInfo.points = this.points;
            updateInfo.earnedPoints = this.earnedPoints;
            updateInfo.blockType = this.blockType;
            updateInfo.healthPoint = this.healthPoint;
            updateInfo.level = this.level;
            return updateInfo;
        }
    }
}