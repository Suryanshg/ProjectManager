class Administrator {
    constructor(topdiv, adminheader) {
        this.topdiv = topdiv
        this.adminheader = adminheader
        this.apiurl = "https://2xt2u5o36m.execute-api.us-east-2.amazonaws.com/api/"

        this.render()
    }

    render() {
        fetch(this.apiurl + "admin", {
            method: "GET"
        })
        .then(response => response.json())
        .then(response => {
            $(this.topdiv).empty()
            if (response['statusCode'] != 200) {
                $(this.adminheader).html("Failed to load projects.")
                return
            } else {
                $(this.adminheader).html("Here's all the projects in the system.")
            }

            for (var i = 0; i < response['projects'].length; i++) {
                var projectobject = response['projects'][i]
                var project = new Project(projectobject['id'], projectobject['name'], projectobject['isArchived'], "adminobject", projectobject['tasks'], projectobject['teammates'])
                $(this.topdiv).append(project.render())
            }
        })
    }

    archiveProject(projectid) {
        console.log(`Attempting to archive project with id ${projectid}`)
    }

    deleteProject(projectid) {
        console.log(`Attempting to delete project with id ${projectid}`)
    }
}