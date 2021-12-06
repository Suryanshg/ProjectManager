class Administrator {
  constructor(topdiv, adminheader) {
    this.topdiv = topdiv;
    this.adminheader = adminheader;
    this.apiurl = getApiUrl();

    this.render();
  }

  render() {
    fetch(this.apiurl + "admin", {
      method: "GET",
    })
      .then((response) => response.json())
      .then((response) => {
        $(this.topdiv).empty();
        if (response["statusCode"] != 200) {
          $(this.adminheader).html("Failed to load projects.");
          return;
        } else {
          $(this.adminheader).html("Here's all the projects in the system.");
        }

        for (var i = 0; i < response["projects"].length; i++) {
          var projectobject = response["projects"][i];
          var project = new Project(
            projectobject["id"],
            projectobject["name"],
            projectobject["isArchived"],
            "adminobject",
            projectobject["tasks"],
            projectobject["teammates"]
          );
          $(this.topdiv).append(project.render());
        }
      });
  }

  archiveProject(projectid) {
    console.log(`Attempting to archive project with id ${projectid}`);
  }

  deleteProject(projectid) {
    var requestbody = {"projectid": projectid}
    fetch(this.apiurl + "admin/deleteproject", {
      method: "POST",
      body: JSON.stringify(requestbody)
    })
    .then((response) => response.json())
    .then((response) => {
      if (response["statusCode"] == 200) {
        this.render()
      }
    })
    console.log(`Attempting to delete project with id ${projectid}`);
  }
}
