package api;

public class RegisterData {
    private Integer id;
    private String token;
    private String error;

    public RegisterData() {
    }

    public RegisterData(Integer id, String token, String error) {
        this.id = id;
        this.token = token;
        this.error = error;
    }

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getError() {
        return error;
    }
}
