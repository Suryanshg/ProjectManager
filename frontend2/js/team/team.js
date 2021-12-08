class Team {
  constructor(header, topdiv) {
    this.header = header;
    this.topdiv = topdiv;

    this.apiurl = getApiUrl();

    this.render();
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
