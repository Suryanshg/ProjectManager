class Team {
  constructor(header, topdiv) {
    this.header = header;
    this.topdiv = topdiv;

    this.apiurl = getApiUrl();

    this.render();
  }
  createTeammate() {
    const data = {
      name: $("#teammateName").val(),
      projectid: getParameterByName("project"),
    };

    $("#teammateSubmitButton").attr("disabled", true)
    $("#teammateSubmitButton").html("Submitting...")
    $("#addTeammateErrorDiv").hide()

    fetch(this.apiurl + "project/teammates/createTeammate", {
      method: "POST",
      body: JSON.stringify(data),
    })
      .then((response) => response.json())
      .then((response) => {
        $("#teammateSubmitButton").attr("disabled", false)
        $("#teammateSubmitButton").html("Submit")

        if (response["statusCode"] != 200) {
          $("#addTeammateErrorDiv").show()
          $("#addTeammateError").html("Failed to add teammate with error code " + response['statusCode'] + ": " + response['error'])
          return;
        }
        $("#teammateName").val("")
        this.render()
      });
  }
  deleteTeammate(teammateid, elementid, buttonid) {
    const button = $(`#${buttonid}`);
    button.attr("disabled", true);
    fetch(this.apiurl + "project/teammates/deleteTeammate", {
      method: "POST",
      body: JSON.stringify({ teammateid }),
    })
      .then((response) => response.json())
      .then((response) => {
        if (response["statusCode"] != 200) {
          button.removeAttr("disabled");
          $(`#${elementid}`).show();
          return;
        }
        window.location.reload();
      });
  }
  render() {
    fetch(this.apiurl + "project?project=" + getParameterByName("project"), {
      method: "GET",
    })
      .then((response) => response.json())
      .then((response) => {
        $(this.teamHeader).empty();
        $(this.topdiv).empty();
        $(this.topdiv).removeClass("placeholder-glow")
        if (response["statusCode"] != 200) {
          if (response["statusCode"] == 422) {
            $(this.header).html("A project with this ID doesn't exist.");
          } else {
            $(this.header).html("Couldn't load project due to an error.");
          }

          return;
        }
        $(this.header).html("Teammate List");
        const project = response["project"];
        const projectid = project.id;
        const teammates = project.teammates;
        $(this.topdiv).append("");
        for (const teammate of teammates) {
          const { id, name, tasks } = teammate;
          const teammateClass = new Teammate("team", id, name, tasks);
          $(this.topdiv).append(teammateClass.render());
        }

        //$(this.header).html(response["project"]["name"]);
      });
  }
}
