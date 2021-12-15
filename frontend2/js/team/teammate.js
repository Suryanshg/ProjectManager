class Teammate {
  constructor(teammateVar, id, name, tasks) {
    this.teammateVar = teammateVar;
    this.id = id;
    this.name = name;
    this.tasks = tasks;
    this.apiurl = getApiUrl();
  }

  render() {
    const html = `<div class="col-sm-12 col-md-6 col-lg-4">
    <div class="card mt-3 mb-3">
      <div class="card-body">
        <h4 class="card-title mb-2">${this.name}</h6>
        <div style="margin-bottom: 1rem">Tasks: 
        <ul>
          ${this.tasks.length > 0 ? 
              this.tasks.map(function (task) {
              return "<li>" + (task.completed ? "✅" : "🔄") + "&nbsp;<b>" + task.position + "</b> " + task.title + "</li>"
          }).join("") : "<li>No tasks assigned to this teammate.</li>"}
        </ul></div>
        <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteTeammateModal" onclick="${this.teammateVar}.updateDeleteTeammate('${this.id}')">Delete</button>
        <div class="row" id="taskDeletionError${this.id}" style="display: none">
        <div class="col s12">
          <hr />
          <p>
            There was an error deleting your teammate.
            <span id="projectCreationErrorDetailed"></span>
          </p>
        </div>
      </div>
        </div>
    </div>
    </div>`
    return html;
  }
}
