class Task {
    constructor (id, parent, subtasks, title, depth, position, teammates, completed) {
        this.id = id;
        this.parent = parent;
        this.subtasks = subtasks;
        this.title = title;
        this.depth = depth;
        this.position = position;
        this.teammates = teammates;
        this.completed = completed;
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

    rerenderstate() {
        $("#markButton-" + this.id).html(this.completed ? "✅" : "🔄")
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

        let completedstring = this.completed ? "✅" : "🔄"

        let renderstring = ""
        if (this.subtasks.length > 0) {
            renderstring = `
            <div class="list-group-item">
                <span><b>${this.position}</b> ${this.title}</span>
                <button type="button" class="btn btn-secondary btn-sm float-end" id="renameButton-${this.id}" data-bs-target="#renameTaskModal" data-bs-toggle="modal" onclick="project.rename('${this.id}', '${this.title}')">Rename</button>
            </div>
            ${subtaskstring}
            `
        } else {
            renderstring = `
            <div class="list-group-item">
                <div style="margin-bottom: 0.6rem">
                <b style="margin-left: 0.2rem">${this.position}</b> ${this.title} 
                <button type="button" class="btn btn-secondary btn-sm float-end" id="renameButton-${this.id}" data-bs-target="#renameTaskModal" data-bs-toggle="modal" onclick="project.rename('${this.id}', '${this.title}')" style="margin-bottom: 2px">Rename</button>
                <button type="button" class="btn btn-dark btn-sm float-end" id="markButton-${this.id}" onclick="project.mark('${this.id}')" style="margin-right: 10px; margin-bottom: 2px; margin-right: 5px">${completedstring}</button>
                </div>
                <i class="bi bi-person-fill"></i> <span id="teammates-${this.id}-span">${assigneestring}</span>
                <div class="btn-group float-end">
                    <button class="btn btn-secondary btn-sm dropdown-toggle" type="button" data-bs-toggle="dropdown" data-bs-auto-close="outside" aria-expanded="false">
                    Assign teammates
                    </button>
                    <ul class="dropdown-menu" id="teammates-${this.id}">
                    </ul>
                </div>
                </div>
            </div>
            ${subtaskstring}
            `
        }

        $("#subTasks-" + this.parent).append(renderstring)
        for (let i = 0; i < teammates.length; i++) {
            var checked = this.teammates.includes(teammates[i]['id']) ? "checked" : ""
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