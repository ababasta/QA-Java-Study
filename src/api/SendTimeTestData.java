package api;

public class SendTimeTestData {
    private String name;
    private String job;

    public SendTimeTestData() {
    }

    public SendTimeTestData(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }
}
