package Exceptions;

import GameDescriptor.GameType;

public class GameTypeException extends Exception{

    public static final String INVALID_VALUE = "Invalid value for GameType";

    public GameTypeException(String message)
    {
        super(message);
    }
}
