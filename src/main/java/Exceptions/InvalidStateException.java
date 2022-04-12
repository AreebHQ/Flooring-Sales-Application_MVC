package Exceptions;

public class InvalidStateException extends Exception{
    public InvalidStateException (String msg)
    {
        super(msg);
    }

    public InvalidStateException(String msg, Throwable cause)
    {
        super(msg,cause);
    }
}
