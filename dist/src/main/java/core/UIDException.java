package core;

public class UIDException extends Exception{

    private String message = null;

    public UIDException() {}

    public UIDException(String message) {
        super(message);
        this.message = message;
    }

    public UIDException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
