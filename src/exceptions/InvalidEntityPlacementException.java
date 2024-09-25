package exceptions;

/**
 * This custom exception should be triggered when an Entity is placed on the game
 * world where it shouldn't be e.g. overlapping another Entity, outside the bounds of
 * the map, on top of an obstacle etc.
 */
public class InvalidEntityPlacementException extends Exception{
    /**
     * Thrown to indicate the Entity has been placed incorrectly, such as on top
     * of another entity or on an obstacle
     */
    public InvalidEntityPlacementException(String message) {
        super(message);
    }
}
