package org.jbomberman.utils;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;

public class UpdateInfo {
    private final UpdateType updateType;
    private KeyCode keyCode;
    private boolean b;
    private String nickname;
    private Coordinate oldc;
    private Coordinate newc;
    private Coordinate c;
    private ArrayList<Coordinate> array;
    private ArrayList<Triad> triadArrayList;
    private int index;
    private int index2;

    private SubMap block;

    public UpdateInfo(UpdateType updateType, Coordinate oldc, Coordinate newc, int index) {
        this.updateType = updateType;
        this.oldc = oldc;
        this.newc = newc;
        this.index = index;
    }
    public UpdateInfo(UpdateType updateType, Coordinate coordinate, int index) {
        this.updateType = updateType;
        this.c = coordinate;
        this.index = index;
    }

    public UpdateInfo(UpdateType updateType, Coordinate c) {
        this.updateType = updateType;
        this.c = c;
    }

    public UpdateInfo(UpdateType updateType) {
        this.updateType = updateType;
    }

    public UpdateInfo(UpdateType updateType, ArrayList<Coordinate> array){
        this.updateType = updateType;
        this.array = array;
    }

    public UpdateInfo(UpdateType updateType, int index) {
        this.updateType = updateType;
        this.index = index;
    }

    public UpdateInfo(UpdateType updateType, ArrayList<Coordinate> array, int index) {
        this.updateType = updateType;
        this.array = array;
        this.index = index;
    }

    public UpdateInfo(UpdateType updateType, Coordinate oldc, Coordinate newc, String name, int index) {
        this.updateType = updateType;
        this.oldc = oldc;
        this.newc = newc;
        this.nickname = name;
        this.index = index;
    }

    public UpdateInfo(UpdateType updateType, boolean b) {
        this.updateType = updateType;
        this.b = b;
    }

    public UpdateInfo(UpdateType updateType, Coordinate coordinate, int index, int index2) {
        this.updateType = updateType;
        this.c = coordinate;
        this.index = index;
        this.index2 = index2;
    }

    public UpdateInfo(UpdateType updateType, SubMap block, ArrayList<Coordinate> array) {
        this.updateType = updateType;
        this.array = array;
        this.block = block;
    }

    public UpdateInfo(ArrayList<Triad> triadArrayList, UpdateType updateType) {
        this.updateType = updateType;
        this.triadArrayList = triadArrayList;
    }

    public UpdateInfo(UpdateType updateType, String nickname) {
        this.updateType = updateType;
        this.nickname = nickname;
    }

    public UpdateInfo(UpdateType updateType, Coordinate oldc, Coordinate newc, int index, KeyCode keyCode) {
        this.updateType = updateType;
        this.oldc = oldc;
        this.newc = newc;
        this.index = index;
        this.keyCode = keyCode;
    }

    public UpdateInfo(UpdateType updateType, Coordinate oldc, Coordinate newc, int index, KeyCode keyCode, boolean b) {
        this.updateType = updateType;
        this.oldc = oldc;
        this.newc = newc;
        this.index = index;
        this.keyCode = keyCode;
        this.b = b;
    }

    public UpdateType getUpdateType() {
        return updateType;
    }

    public Coordinate getOldCoord() {
        return oldc;
    }

    public Coordinate getNewCoord() {
        return newc;
    }

    public Coordinate getCoordinate() {
        return c;
    }

    public ArrayList<Coordinate> getArray() {
        return array;
    }

    public int getIndex() {
        return index;
    }

    public int getIndex2(){return index2;}

    public boolean getBoo(){
        return b;
    }

    public SubMap getSubBlock() {
        return block;
    }

    public ArrayList<Triad> getTriadArrayList() {
        return triadArrayList;
    }

    public String getNickname(){
        return nickname;
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }
}