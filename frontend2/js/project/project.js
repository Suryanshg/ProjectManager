class Project {
    constructor (header, topdiv) {
        this.header = header
        this.topdiv = topdiv

        this.apiurl = "https://2xt2u5o36m.execute-api.us-east-2.amazonaws.com/api/"

        this.render()
    }

    render() {
        fetch(this.apiurl + "project?project=" + getParameterByName("project"), {
            method: "GET"
        })
        .then(response => response.json())
        .then(response => {
            if (response['statusCode'] != 200) {
                console.log(response['statusCode'])
                if (response['statusCode'] == 422) {
                    $(this.header).html("A project with this ID doesn't exist.")
                } else {
                    $(this.header).html("Couldn't load project due to an error.")
                }

                return
            }

            $(this.header).html(response['project']['name'])


        })
    }
}