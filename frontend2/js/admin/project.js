class Project {
    constructor (projectid, name, archived, adminvar, tasks, teammates) {
        this.projectid = projectid
        this.name = name
        this.archived = archived
        this.adminvar = adminvar
        this.tasks = tasks
        this.teammates = teammates
    }

    render() {
        var archivedtext = ""
        if (this.archived) {
            archivedtext = "disabled"
        }
        var html = `
            <div class="row mb-3">
                <hr>
                <div class="col s12">
                    <h4>${this.name}</h4>
                    ${this.tasks.length}/${this.tasks.length} tasks complete <br>
                    ${this.teammates.length} teammates <br><br>
                    <button type="button" class="btn btn-warning" onclick="${this.adminvar}.archiveProject('${this.projectid}')" ${archivedtext}>Archive</button>
                    <button type="button" class="btn btn-danger" onclick="${this.adminvar}.deleteProject('${this.projectid}')">Delete</button>
                </div>
            </div>
        `

        return html
    }
}