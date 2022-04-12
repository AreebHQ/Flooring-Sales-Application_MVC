package Exceptions;

public class InvalidOrderException extends Exception{
    public InvalidOrderException (String msg)
    {
        super(msg);
    }

    public InvalidOrderException(String msg, Throwable cause)
    {
        super(msg,cause);
    }
}
