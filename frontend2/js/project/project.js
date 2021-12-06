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
          console.log(response["statusCode"]);
          if (response["statusCode"] == 422) {
            $(this.header).html("A project with this ID doesn't exist.");
          } else {
            $(this.header).html("Couldn't load project due to an error.");
          }

          return;
        }

        $(this.header).removeClass("placeholder-glow")
        $(this.header).html(response["project"]["name"]);
        $("#subTasks-root").empty()
        $("#subTasks-root").removeClass("placeholder-glow")
        this.alltasks = []
        let tasks = response["project"]["tasks"];
        console.log(tasks)
        this.recursivecreate(tasks, 0, "root", "")
        this.renderselect()
      });
  }

  recursivecreate(tasks, depth, parent, outline) {
    for (let i = 0; i < tasks.length; i++) {
      let newtask = new Task(tasks[i]['id'], parent, tasks[i]['subTasks'], tasks[i]['title'], depth + 1, outline + tasks[i]['outlineNumber'] + ".")
      newtask.render()
      this.alltasks.push(newtask)
      if (tasks[i]['subTasks'].length > 0) {
        this.recursivecreate(tasks[i]['subTasks'], depth + 1, tasks[i]['id'], outline + tasks[i]['outlineNumber'] + ".")
      }
    }


  }

  renderselect() {
    $("#fileUnderSelect").empty()
    $("#fileUnderSelect").append("<option value='root' selected>File as top level task</option>")
    for (let i = 0; i < this.alltasks.length; i++) {
      
      $("#fileUnderSelect").append("<option value='" + this.alltasks[i].id + "'>" + this.alltasks[i].position + " " + this.alltasks[i].title + "</option>")
    }
  }
}
