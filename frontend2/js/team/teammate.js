class Teammate {
  constructor(taskVar, id, name, tasks) {
    this.taskVar = taskVar;
    this.id = id;
    this.name = name;
    this.tasks = tasks;
    this.apiurl = getApiUrl();
  }

  render() {
    const html = `<div class="col-sm-12 col-md-6 col-lg-4">
    <div class="card mt-3 mb-3">
      <div class="card-body">
        <h6 class="card-subtitle mb-2 text-muted">${this.name}</h6>
        <div style="margin-bottom: 0.125rem">Tasks: ${this.tasks.map((task) => task.title)} <br/> <br/></div>
        <button type="button" class="btn btn-danger" id="taskDeletionButton${this.id
      }"onclick="${this.taskVar}.deleteTeammate('${this.id}', 'taskDeletionError${this.id
      }', 'taskDeletionButton${this.id}')">Delete</button>
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
