package projectmanager.http;

public class CreateTeammateRequest {
    public String name;
    public String projectid;

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

    public String getprojectid() {
        return this.projectid;
    }

    public void setprojectid(String projectid) {
        this.projectid = projectid;
    }
}
