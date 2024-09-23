package exceptions;

public class TooManyEntitiesException extends Exception{
    public TooManyEntitiesException(String message) {
        super(message);
    }
}
