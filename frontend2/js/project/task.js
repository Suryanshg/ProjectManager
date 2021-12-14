class Task {
    constructor (id, parent, subtasks, title, depth, position, teammates) {
        this.id = id;
        this.parent = parent;
        this.subtasks = subtasks;
        this.title = title;
        this.depth = depth;
        this.position = position;
        this.teammates = teammates;
    }

    render() {
        let subtaskstring = ""

        if (this.subtasks.length > 0) {
            subtaskstring = `<div class="list-group" style="margin-left: 20px" id="subTasks-${this.id}"></div>`
        }

        let assigneearray = []
        if (this.teammates.length > 0) {
            for (let i = 0; i < this.teammates.length; i++) {
                console.log(this.teammates[i])
                console.log(teammatemap['9d118e43-e808-4f65-89f9-3927d6fa9960']['name'])
                assigneearray.push(teammatemap[this.teammates[i]]['name'])
            }
        } else {
            assigneearray.push("Nobody assigned")
        }

        let assigneestring = assigneearray.join(", ")

        var renderstring = `
        <div class="list-group-item">
            <button type="button" class="btn btn-primary btn-sm">Mark</button><b>${this.position}</b> ${this.title} <br>
            <i class="bi bi-person-fill"></i> ${assigneestring} 
            <div class="btn-group float-end">
                <button class="btn btn-secondary btn-sm dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                Assign teammates
                </button>
                <ul class="dropdown-menu">
                ...
                </ul>
            </div>
        </div>
        ${subtaskstring}
        `
        $("#subTasks-" + this.parent).append(renderstring)
    }
}