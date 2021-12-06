class Task {
    constructor (id, parent, subtasks, title, depth, position) {
        this.id = id;
        this.parent = parent;
        this.subtasks = subtasks;
        this.title = title;
        this.depth = depth;
        this.position = position;
    }

    render() {
        let subtaskstring = ""

        if (this.subtasks.length > 0) {
            subtaskstring = `<div class="list-group" style="margin-left: ${this.depth * 15}px" id="subTasks-${this.id}"></div>`
        }

        var renderstring = `
        <div class="list-group-item">
            <b>${this.position}</b> ${this.title}
        </div>
        ${subtaskstring}
        `
        $("#subTasks-" + this.parent).append(renderstring)
    }
}