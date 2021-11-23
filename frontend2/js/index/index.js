class Index {
    constructor(projform, errordiv, successdiv, urlspan, errorspan, urlhref, button, buttonspan) {
        this.projform = projform
        this.errordiv = errordiv
        this.successdiv = successdiv
        this.urlspan = urlspan
        this.errorspan = errorspan
        this.urlhref = urlhref
        this.button = button
        this.buttonspan = buttonspan

        this.apiurl = "https://2xt2u5o36m.execute-api.us-east-2.amazonaws.com/api/"
    }

    createProject() {
        var data = {"projectName": this.projform.val()}

        $(this.button).prop("disabled", true)
        $(this.buttonspan).html("Submitting...")

        fetch(this.apiurl + "project/createProject", {
            method: "POST",
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(response => {
            $(this.button).prop("disabled", false)
            $(this.buttonspan).html("Submit")

            if (response['statusCode'] != 200) {
                this.errorspan.html("")
                this.errordiv.show()
                this.successdiv.hide()
                if (response['statusCode'] == 422) {
                    this.errorspan.html("A project with the same name already exists.")
                } else if (response['statusCode'] == 400) {
                    this.errorspan.html("Lambda returned a critical error (400).")
                }
                return
            }

            $(this.errordiv).hide()
            $(this.successdiv).show()
            $(this.urlspan).html(response['url'].substring(1))
            // Issue in the API where the URL is padded with 1 extra character.
            $(this.urlhref).attr("href", response['url'].substring(1))
        })

    }

}