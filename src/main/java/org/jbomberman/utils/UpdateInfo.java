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
    private SubMap block;

    // Costruttore privato per il Builder
    private UpdateInfo(UpdateType updateType) {
        this.updateType = updateType;
    }

    // Builder statico
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
        private SubMap block;

        public Builder(UpdateType updateType) {
            this.updateType = updateType;
        }

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

        public Builder setPoints(int points) {
            this.points = points;
            return this;
        }

        public Builder setEarnedPoints(int earnedPoints) {
            this.earnedPoints = earnedPoints;
            return this;
        }

        public Builder setSubBlock(SubMap block) {
            this.block = block;
            return this;
        }

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
            updateInfo.block = this.block;
            return updateInfo;
        }
    }

    // Metodi getter
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

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public boolean isEnemyLastLife() {
        return enemyLastLife;
    }

    public SubMap getSubBlock() {
        return block;
    }

    public List<Triad> getTriadList() {
        return triadList;
    }

    public int getPoints() {
        return points;
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }

    public boolean isInvincible() {
        return isPlayerInvincible;
    }
}
