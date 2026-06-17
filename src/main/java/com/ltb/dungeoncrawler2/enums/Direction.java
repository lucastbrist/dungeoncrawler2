package com.ltb.dungeoncrawler2.enums;

public enum Direction {
    NORTH, SOUTH, EAST, WEST, UP, DOWN;

    public Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST  -> WEST;
            case WEST  -> EAST;
            case UP    -> DOWN;
            case DOWN  -> UP;
        };
    }
}