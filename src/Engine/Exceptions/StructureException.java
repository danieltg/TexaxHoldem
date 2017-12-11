package Engine.Exceptions;

public class StructureException extends Exception{

    public static final String CANNOT_CREATE = " -> cannot create Structure";
    public static final String HANDSCOUNT_NOT_A_NUMBER = "HandsCount must be a number"+CANNOT_CREATE;
    public static final String BUY_NOT_A_NUMBER = "Buy must be a number"+CANNOT_CREATE;

    public static final String NEGATIVE_HANDSCOUNT = "HandsCount cannot be negative"+CANNOT_CREATE;
    public static final String NEGATIVE_BUY = "Buy cannot be negative"+CANNOT_CREATE;

    public static final String INVALID_HANDSCOUNT = "Invalid HandsCount (must be >0 and divisible by 4"+CANNOT_CREATE;

    public static final String ILLEGAL_STRUCTURE="Illegal Structure";


    public StructureException(String message)
    {
        super(message);
    }

}
