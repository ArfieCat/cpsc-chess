package model;

/**
 * Represents the 8 cardinal directions.
 */
public enum Direction {
    EAST(1, 0),
    NORTHEAST(1, 1),
    NORTH(0, 1),
    NORTHWEST(-1, 1),
    WEST(-1, 0),
    SOUTHWEST(-1, -1),
    SOUTH(0, -1),
    SOUTHEAST(1, -1);

    private final int directionX;
    private final int directionY;

    Direction(int x, int y) {
        this.directionX = x;
        this.directionY = y;
    }

    public int getX() {
        return directionX;
    }

    public int getY() {
        return directionY;
    }
}
