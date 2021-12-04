class Teammate {
  constructor(taskVar, id, name, tasks) {
    this.taskVar = taskVar;
    this.id = id;
    this.name = name;
    this.tasks = tasks;
    this.apiurl = getApiUrl();
  }

  render() {
    let html = `
    <div class="row mb-3">
      <hr>
      <div class="col s12">
        <h4>${this.name}</h4>
        Tasks: ${this.tasks.map((task) => task.title)} <br/> <br/>
        <button type="button" class="btn btn-danger" id="taskDeletionButton${
          this.id
        }"onclick="${this.taskVar}.deleteTask('${this.id}', 'taskDeletionError${
      this.id
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
        `;
    return html;
  }
}
