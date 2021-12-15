class Project {
  constructor(header, topdiv) {
    this.header = header;
    this.topdiv = topdiv;
    this.alltasks = [];

    this.apiurl = getApiUrl();
    this.render()
  }

  newtask() {
    $("#taskSubmitButton").attr("disabled", true)
    $("#taskSubmitButton").html("Submitting...")
    $("#addTaskErrorDiv").hide()
    var requestbody = {"projectid": getParameterByName("project"),
                "title": $("#taskName").val()}
    
    if ($("#fileUnderSelect").val() != "root") {
      requestbody['parentTask'] = $("#fileUnderSelect").val()
    }

    fetch(this.apiurl + "project/tasks/createTask", {
      method: "POST",
      body: JSON.stringify(requestbody)
    })
    .then((response => response.json()))
    .then((response) => {
      $("#taskSubmitButton").attr("disabled", false)
      $("#taskSubmitButton").html("Submit")
      if (response["statusCode"] != 200) {
        $("#addTaskErrorDiv").show()
        $("#addTaskError").html("Failed to add task with error code " + response['statusCode'] + ": " + response['error'])
      } else {
        $("#taskName").val("")
        this.render()
      }


    })
  }

  render() {
    fetch(this.apiurl + "project?project=" + getParameterByName("project"), {
      method: "GET",
    })
      .then((response) => response.json())
      .then((response) => {
        if (response["statusCode"] != 200) {
          $("#yourTeamButton").attr("disabled", true)
          $("#addTaskButton").attr("disabled", true)
          if (response["statusCode"] == 422) {
            $(this.header).html("A project with this ID doesn't exist.");
            $("#subTasks-root").empty()
            $("#subTasks-root").removeClass("placeholder-glow")
          } else {
            $(this.header).html("Couldn't load project due to an error.");
            $("#subTasks-root").empty()
            $("#subTasks-root").removeClass("placeholder-glow")
          }

          return;
        }

        // Map teammates properly
        for (var i = 0; i < response["project"]["teammates"].length; i++) {
          let workingid = response['project']['teammates'][i]['id']
          let workingobject = response['project']['teammates'][i]
          teammatemap[workingid] = {}
          teammatemap[workingid] = {"name": workingobject['name']}
        }

        teammates = response["project"]["teammates"]
        $(this.header).removeClass("placeholder-glow")
        $(this.header).html(response["project"]["name"]);
        $("#subTasks-root").empty()
        $("#subTasks-root").removeClass("placeholder-glow")
        this.alltasks = []
        let tasks = response["project"]["tasks"];
        console.log(tasks)
        this.recursivecreate(tasks, 0, "root", "")
        this.renderselect()

        if (response['project']['isArchived']) {
          $("#projectArchivedText").show()
          $("#addTaskButton").attr("disabled", true)
        }
      });
  }

  recursivecreate(tasks, depth, parent, outline) {
    for (let i = 0; i < tasks.length; i++) {
      let newtask = new Task(tasks[i]['id'], parent, tasks[i]['subTasks'], tasks[i]['title'], depth + 1, outline + tasks[i]['outlineNumber'] + ".", tasks[i]['assignees'])
      newtask.render()
      this.alltasks.push(newtask)
      if (tasks[i]['subTasks'].length > 0) {
        this.recursivecreate(tasks[i]['subTasks'], depth + 1, tasks[i]['id'], outline + tasks[i]['outlineNumber'] + ".", tasks[i]['assignees'])
      }
    }
  }

    teammate(taskid, teammateid) {
      let url = ""
      if ($("#teammatebox-" + taskid + "-" + teammateid).prop("checked")) {
        url = "assignTeammate"
      } else {
        url = "unassignTeammate"
      }

      let reqbody = {
        "taskid": taskid,
        "teammateid": teammateid,
        "projectid": getParameterByName("project")
      }

      $("#teammatebox-" + taskid + "-" + teammateid).prop("disabled", true)
      fetch(this.apiurl + "project/tasks/" + url, {
        body: JSON.stringify(reqbody),
        method: "POST"
      })
      .then((response) => response.json())
      .then((response) => {
        if (response['statusCode'] != 200) {
          $("#teammatebox-" + taskid + "-" + teammateid).prop("disabled", false)
          // TODO - Throw modal when this fails
          return
        }

        // Find object for the task in alltasks, then do modifications
        var taskobj = this.alltasks.find(obj => {
          return obj.id === taskid
        })

        if (url == "unassignTeammate") {
          taskobj.teammates = taskobj.teammates.filter(item => item !== teammateid)
        } else if (url == "assignTeammate") {
          taskobj.teammates.push(teammateid)
        }

        // Sort the teammates to ensure consistent behavior.
        taskobj.teammates = taskobj.teammates.sort()
        
        // Rerender JUST the teammates
        taskobj.rerenderteammates()
        // Then disable the box.
        $("#teammatebox-" + taskid + "-" + teammateid).prop("disabled", false)
      })

      
    }

  rename(taskid) {
    $("#renameSubmitButton").prop("onclick", "project.renameconfirm('" + taskid + "')")
  }

  renameconfirm(taskid) {
    let reqbody = {
      "taskid": taskid,
      "name": $("#renameTaskName").val()
    }
    $("#renameTaskErrorDiv").hide()
    $("#renameSubmitButton").html("Submitting...")
    $("#renameSubmitButton").prop("disabled", true)
    fetch(this.apiurl + "project/tasks/renameTask", {
      body: JSON.stringify(reqbody),
      method: "POST"
    })
    .then((response) => response)
    .then((response) => {
      $("#renameSubmitButton").html("Submit")
      $("#renameSubmitButton").prop("disabled", false)
      if (response['statusCode'] != 200) {
        $("#renameTaskErrorDiv").show()
        $("#renameTaskError").html("Failed to rename task with status code " + response['statusCode'] + ": " + response['error'])
        return
      }

      this.render()
    })
  }

  renderselect() {
    $("#fileUnderSelect").empty()
    $("#fileUnderSelect").append("<option value='root' selected>File as top level task</option>")
    for (let i = 0; i < this.alltasks.length; i++) {
      var repeated = "&nbsp"
      repeated = repeated.repeat(this.alltasks[i].depth * 2)
      $("#fileUnderSelect").append("<option value='" + this.alltasks[i].id + "'>" + repeated + this.alltasks[i].position + " " + this.alltasks[i].title + "</option>")
    }
  }
}
