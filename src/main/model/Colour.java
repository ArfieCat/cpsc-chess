package model;

/**
 * Represents a player or piece colour.
 */
public enum Colour {
    WHITE(1),
    BLACK(-1);

    private final int direction;

    Colour(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }
}
