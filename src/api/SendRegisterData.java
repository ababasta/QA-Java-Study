package api;

public class SendRegisterData {
    private String email;
    private String password;

    public SendRegisterData() {
    }

    public SendRegisterData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
