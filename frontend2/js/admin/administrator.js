class Administrator {
  constructor(topdiv, topdivarchived, adminheader, archivedheader) {
    this.topdiv = topdiv;
    this.topdivarchived = topdivarchived;
    this.adminheader = adminheader;
    this.archivedheader = archivedheader;
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
        $("#topdiv-archived").empty()
        $("#topdiv-archived").removeClass("placeholder-glow")
        $(this.adminheader).removeClass("placeholder-glow")
        $(this.adminheader).html("Active Projects");
        $(this.archivedheader).removeClass("placeholder-glow")
        $(this.archivedheader).html("Archived Projects");
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
          if (project.archived) {
            $(this.topdivarchived).append(project.render())
          } else {
            $(this.topdiv).append(project.render());
          }
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
        $("#archiveProjectModal").modal("hide")
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
        $("#deleteProjectModal").modal("hide")
      } else {
        $("#deleteProjectError").html("Failed to delete project with status code " + response['statusCode'] + ": " + response['error'])
        $("#deleteProjectErrorDiv").show()
      }
    })
  }
}
