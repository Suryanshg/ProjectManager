class Index {
    constructor(projform, errordiv, successdiv, urlspan) {
        this.projform = projform
        this.errordiv = errordiv
        this.successdiv = successdiv
        this.urlspan = urlspan

        this.apiurl = "https://2xt2u5o36m.execute-api.us-east-2.amazonaws.com/api/"
    }

    createProject() {
        var request = new XMLHttpRequest();
        var data = {"projectName": $(this.projform).val()}

        fetch(this.apiurl + "project/createProject", {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(response => {
            console.log(response)
            if (response['statusCode'] != 200) {
                console.log(this.errordiv)
                this.errordiv.show()
                this.successdiv.hide()
                return
            }

            $(this.errordiv).hide()
            $(this.successdiv).show()
            $(this.urlspan).html(response['url'])
        })

    }

}