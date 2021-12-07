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
          $("#topdiv").empty()
          $("#topdiv").removeClass("placeholder-glow")
          return;
        }

        $("#topdiv").empty()
        $("#topdiv").removeClass("placeholder-glow")
        $(this.adminheader).removeClass("placeholder-glow")
        $(this.adminheader).html("Here's all the projects in the system.");
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
    $("#archiveSubmitButton").attr("onclick", "adminobject.archiveProjectActual('" + projectid + "')")
  }

  archiveProjectActual(projectid) {
    var requestbody = {"projectid": projectid}
    $("#archiveSubmitButton").attr("disabled", true)
    $("#archiveSubmitButton").html("Archiving...")
    $("#archiveProjectErrorDiv").hide()
    fetch(this.apiurl + "admin/archiveproject", {
      method: "POST",
      body: JSON.stringify(requestbody)
    })
    .then((response) => response.json())
    .then((response) => {
      $("#archiveSubmitButton").attr("disabled", false)
      $("#archiveSubmitButton").html("Archive")
      if (response["statusCode"] == 200) {
        this.render()
      } else {
        $("#archiveProjectError").html("Failed to archive project with status code " + response['statusCode'] + ": " + response['error'])
        $("#archiveProjectErrorDiv").show()
      }
    })
  }

  deleteProject(projectid) {
    $("#deleteSubmitButton").attr("onclick", "adminobject.deleteProjectActual('" + projectid + "')")
  }

  deleteProjectActual(projectid) {
    $("#deleteSubmitButton").attr("disabled", true)
    $("#deleteSubmitButton").html("Deleting...")
    $("#deleteProjectErrorDiv").hide()
    var requestbody = {"projectid": projectid}
    fetch(this.apiurl + "admin/deleteproject", {
      method: "POST",
      body: JSON.stringify(requestbody)
    })
    .then((response) => response.json())
    .then((response) => {
      $("#deleteSubmitButton").attr("disabled", false)
      $("#deleteSubmitButton").html("Delete")
      if (response["statusCode"] == 200) {
        this.render()
      } else {
        $("#deleteProjectError").html("Failed to delete project with status code " + response['statusCode'] + ": " + response['error'])
        $("#deleteProjectErrorDiv").show()
      }
    })
  }
}
