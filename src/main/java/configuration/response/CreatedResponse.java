package configuration.response;

public class CreatedResponse {

    private String createdIdentifier;

    public CreatedResponse(String createdIdentifier) {
        this.createdIdentifier = createdIdentifier;
    }

    public String getCreatedIdentifier() {
        return createdIdentifier;
    }

    public void setCreatedIdentifier(String createdIdentifier) {
        this.createdIdentifier = createdIdentifier;
    }
}
