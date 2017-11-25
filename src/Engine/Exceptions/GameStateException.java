package Engine.Exceptions;

public class GameStateException extends Exception {


    public static final String INVALID_VALUE = "Invalid value for Menu";

    public GameStateException(String message)
    {
        super(message);
    }
}
