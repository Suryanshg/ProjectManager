class Team {
  constructor(header, topdiv) {
    this.header = header;
    this.topdiv = topdiv;
    this.apiurl = getApiUrl();
    this.tasks = []
    this.teammates = []
    this.projectid = getParameterByName("project")

    this.render();
  }
  createTeammate() {
    const data = {
      name: $("#teammateName").val(),
      projectid: this.projectid,
    };

    $("#teammateSubmitButton").attr("disabled", true)
    $("#teammateSubmitButton").html("Submitting...")
    $("#addTeammateErrorDiv").hide()

    fetch(this.apiurl + "project/teammates/assignTeammate", {
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
  updateAssignTeammate(teammateid) {
    this.setAddedTasks(teammateid)
    $("#assignSubmitButton").attr("onclick", "team.assignTeammate('assign','" + teammateid + "')")
    $("#unassignSubmitButton").attr("onclick", "team.assignTeammate('unassign','" + teammateid + "')")

  }
  updateDeleteTeammate(teammateid) {
    $("#deleteSubmitButton").attr("onclick", "team.deleteTeammate('" + teammateid + "')")
  }
  assignTeammate(mode, teammateid) {
    $("#assignSubmitButton").attr("disabled", true)
    $("#unassignSubmitButton").attr("disabled", true)
    $(`#${mode}SubmitButton`).html("Assigning...")
    $(`#${mode}TeammateErrorDiv`).hide()

    const requestBody = { teammateid, projectid: this.projectid }

    if (mode == 'assign') requestBody['taskid'] = $("#notGivenSelect").val()
    else requestBody['taskid'] = $("#givenSelect").val()

    fetch(this.apiurl + `project/tasks/${mode}Teammate`, {
      method: "POST",
      body: JSON.stringify(requestBody),
    })
      .then((response) => response.json())
      .then((response) => {
        $("#assignSubmitButton").attr("disabled", false)
        $("#unassignSubmitButton").attr("disabled", false)
        $(`#${mode}SubmitButton`).html(mode.charAt(0).toUpperCase() + mode.slice(1))
        if (response["statusCode"] == 200) {
          this.render()
          $('#assignTeammateModal').modal('hide');

        } else {
          $(`#${mode}TeammateError`).html("Failed to " + mode + " teammate with status code " + response['statusCode'] + ": " + response['error'])
          $(`#${mode}TeammateErrorDiv`).show()
        }
      });
  }
  deleteTeammate(teammateid) {
    $("#deleteSubmitButton").attr("disabled", true)
    $("#deleteSubmitButton").html("Deleting...")
    $("#deleteTeammateErrorDiv").hide()
    fetch(this.apiurl + "project/teammates/deleteTeammate", {
      method: "POST",
      body: JSON.stringify({ teammateid }),
    })
      .then((response) => response.json())
      .then((response) => {
        $("#deleteSubmitButton").attr("disabled", false)
        $("#deleteSubmitButton").html("Delete")
        if (response["statusCode"] == 200) {
          this.render()
          $('#deleteTeammateModal').modal('hide');

        } else {
          $("#deleteTeammateError").html("Failed to delete teammate with status code " + response['statusCode'] + ": " + response['error'])
          $("#deleteTeammateErrorDiv").show()
        }
      });
  }
  render() {
    fetch(this.apiurl + "project?project=" + this.projectid, {
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
        const { tasks, teammates } = project;
        $(this.topdiv).append("");
        for (const teammate of teammates) {
          this.teammates.push(teammate)
          const { id, name, tasks } = teammate;
          const teammateClass = new Teammate("team", id, name, tasks);
          $(this.topdiv).append(teammateClass.render());
        }
        for (const task of tasks) {
          this.tasks.push(task)
        }

        //$(this.header).html(response["project"]["name"]);
      });
  }

  setAddedTasks(teammateid) {
    const givenTasks = (this.teammates.find(t => t.id == teammateid)).tasks;
    const notGivenTasks = this.tasks.filter(t1 => !givenTasks.find(t2 => t1.title == t2.title))
    $("#givenSelect").empty()
    $("#notGivenSelect").empty()
    for (let task of givenTasks) {
      /*var repeated = "&nbsp"
      repeated = repeated.repeat(this.alltasks[i].depth * 2)*/
      $("#givenSelect").append("<option value='" + task.id + "'>" + task.title + "</option>")
    }
    for (let task of notGivenTasks) {
      $("#notGivenSelect").append("<option value='" + task.id + "'>" + task.title + "</option>")
    }
  }
}
