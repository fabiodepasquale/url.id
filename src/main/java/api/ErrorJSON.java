package api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ErrorJSON extends JSON{
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private ErrorEntity error;
    public int code;
    public String message;


    public ErrorJSON(ErrorEntity error, int code, String message) {
        this.error = error;
        this.code = code;
        this.message = message;
    }

    public ErrorJSON(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorEntity getError() {
        return error;
    }
    public void setError(ErrorEntity error) {
        this.error = error;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
