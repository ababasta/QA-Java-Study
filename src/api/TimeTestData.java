package api;

public class TimeTestData extends SendTimeTestData{
    private String updatedAt;

    public TimeTestData() {
    }

    public TimeTestData(String name, String job, String updatedAt) {
        super(name, job);
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
