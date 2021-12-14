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

    rerenderteammates() {
        let assigneearray = []
        if (this.teammates.length > 0) {
            for (let i = 0; i < this.teammates.length; i++) {
                try {
                    assigneearray.push(teammatemap[this.teammates[i]]['name'])
                } catch (e) {
                    assigneearray.push("Invalid teammate")
                }
            }
        } else {
            assigneearray.push("Nobody assigned")
        }
        assigneearray = assigneearray.sort()
        let assigneestring = assigneearray.join(", ")
        $("#teammates-" + this.id + "-span").html(assigneestring)
    }

    render() {
        let subtaskstring = ""

        if (this.subtasks.length > 0) {
            subtaskstring = `<div class="list-group" style="margin-left: 20px" id="subTasks-${this.id}"></div>`
        }

        let assigneearray = []
        if (this.teammates.length > 0) {
            for (let i = 0; i < this.teammates.length; i++) {
                try {
                    assigneearray.push(teammatemap[this.teammates[i]]['name'])
                } catch (e) {
                    assigneearray.push("Invalid teammate")
                }
            }
        } else {
            assigneearray.push("Nobody assigned")
        }
        assigneearray = assigneearray.sort()
        let assigneestring = assigneearray.join(", ")

        var renderstring = `
        <div class="list-group-item">
            <p style="margin-bottom: 0.2rem">
            <button type="button" class="btn btn-primary btn-sm" id="markButton-${this.id}" onclick="project.mark('${this.id}')" style="margin-right: 5px">🔄</button><b>${this.position}</b> ${this.title} 
            <button type="button" class="btn btn-secondary btn-sm float-end" id="renameButton-${this.id}" onclick="project.rename('${this.id}')" style="margin-bottom: 2px">Rename</button>
            </p>
            <div>
            <i class="bi bi-person-fill"></i> <span id="teammates-${this.id}-span">${assigneestring}</span>
            <div class="btn-group float-end">
                <button class="btn btn-secondary btn-sm dropdown-toggle" type="button" data-bs-toggle="dropdown" data-bs-auto-close="false" aria-expanded="false">
                Assign teammates
                </button>
                <ul class="dropdown-menu" id="teammates-${this.id}">
                </ul>
            </div>
            </div>
        </div>
        ${subtaskstring}
        `
        $("#subTasks-" + this.parent).append(renderstring)
        for (let i = 0; i < teammates.length; i++) {
            var checked = this.teammates.includes(teammates[i]['id']) ? "checked" : ""
            console.log(checked)
            $("#teammates-" + this.id).append(`
            <li class="dropdown-item">
                <div class="checkbox">
                    <label>
                        <input type="checkbox" id="teammatebox-${this.id}-${teammates[i]['id']}" onchange="project.teammate('${this.id}', '${teammates[i]['id']}')" ${this.teammates.includes(teammates[i]['id']) ? "checked": ""}>&nbsp ${teammates[i]['name']}
                    </label>
                <div class="checkbox">
            </li>`) 
        }
    }
}