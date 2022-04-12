package Exceptions;

public class InvalidDateException extends Exception{

    public InvalidDateException (String msg)
    {
        super(msg);
    }

    public InvalidDateException(String msg, Throwable cause)
    {
        super(msg,cause);
    }
}
