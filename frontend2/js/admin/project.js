class Project {
  constructor(projectid, name, archived, adminvar, tasks, teammates) {
    this.projectid = projectid;
    this.name = name;
    this.archived = archived;
    this.adminvar = adminvar;
    this.tasks = tasks;
    this.teammates = teammates;

    this.percentcomplete = 0;
    this.taskscomplete = 0;
    this.totaltasks = 0;
  }

  calculatetaskscomplete(tasks) {
    for (let i = 0; i < tasks.length; i++) {
      this.totaltasks++;
      if (tasks[i]['completed']) {
        this.taskscomplete++;
      }

      if (tasks[i]['subTasks'].length > 0) {
        this.calculatetaskscomplete(tasks[i]['subTasks'])
      }
    }
  }

  render() {
    var archivedtext = "";
    if (this.archived) {
      archivedtext = "disabled";
    }

    this.calculatetaskscomplete(this.tasks)

    if (this.totaltasks > 0) {
      this.percentcomplete = (this.taskscomplete / this.totaltasks) * 100
    } else {
      this.percentcomplete = 0
    }




    var html = `
    <div class="col-sm-12 col-md-6 col-lg-4">
    <div class="card mt-3 mb-3">
      <div class="card-body">
        <h5 class="card-title"><a href="./project.html?project=${this.projectid}" target="_blank">${this.name}</a></h5>
        <h6 class="card-subtitle mb-2 text-muted">ID: ${this.projectid}</h6>
        <p class="card-text" style="margin-bottom: 1rem"><i class="bi bi-person-fill"></i> ${this.teammates.length} teammates</p>
        <div class="progress" style="margin-bottom: 0.125rem">
          <div class="progress-bar" role="progressbar" style="width: ${this.percentcomplete}%;" aria-valuenow="${this.percentcomplete}" aria-valuemin="0" aria-valuemax="100">${this.percentcomplete.toFixed(0)}%</div>
        </div>
        <small class="text-muted" style="margin-top: 0.5rem">${this.taskscomplete}/${this.totaltasks} tasks complete</small><br><br>
        <button type="button" class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#archiveProjectModal" onclick="${this.adminvar}.archiveProject('${this.projectid}')" ${archivedtext}>${this.archived ? "Archived" : "Archive"}</button>
        <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteProjectModal" onclick="${this.adminvar}.deleteProject('${this.projectid}')">Delete</button>
      </div>
    </div>
    </div>
    `

    return html;
  }
}
