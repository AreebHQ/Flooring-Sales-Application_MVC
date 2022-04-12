package Exceptions;

public class InvalidProductException extends Exception{

    public InvalidProductException (String msg)
    {
        super(msg);
    }

    public InvalidProductException(String msg, Throwable cause)
    {
        super(msg,cause);
    }
}
