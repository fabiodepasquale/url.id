package api;

public class ErrorEntity {
    private String reason;
    private String message;
    private String locationType;
    private String location;

    public ErrorEntity(String reason, String message, String locationType, String location) {
        this.reason = reason;
        this.message = message;
        this.locationType = locationType;
        this.location = location;
    }

    public ErrorEntity(String reason, String message) {
        this.reason = reason;
        this.message = message;
    }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getLocationType() { return locationType; }
    public void setLocationType(String locationType) { this.locationType = locationType; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}