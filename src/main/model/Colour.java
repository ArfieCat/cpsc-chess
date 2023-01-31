package model;

/**
 * Represents a player or piece colour.
 */
public enum Colour {
    WHITE(Direction.NORTH),
    BLACK(Direction.SOUTH);

    private Direction direction;

    Colour(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
