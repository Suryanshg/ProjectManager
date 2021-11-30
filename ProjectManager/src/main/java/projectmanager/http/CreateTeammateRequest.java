package projectmanager.http;

public class CreateTeammateRequest {
    String name;
    String projectid;

    public CreateTeammateRequest() {}

    public CreateTeammateRequest(String name, String projectid) {
        this.name = name;
        this.projectid = projectid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectId() {
        return this.projectid;
    }

    public void setProjectId(String projectid) {
        this.projectid = projectid;
    }
}
