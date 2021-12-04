class Teammate {
  constructor(projform, errordiv, successdiv, errorspan, button, buttonspan) {
    this.projform = projform;
    this.errordiv = errordiv;
    this.successdiv = successdiv;
    this.errorspan = errorspan;
    this.button = button;
    this.buttonspan = buttonspan;
    this.apiurl = getApiUrl();
  }
  createTeammate() {
    const data = {
      name: this.projform.val(),
      projectid: getParameterByName("project"),
    };

    $(this.button).prop("disabled", true);
    $(this.buttonspan).html("Submitting...");

    fetch(this.apiurl + "project/teammates/createTeammate", {
      method: "POST",
      body: JSON.stringify(data),
    })
      .then((response) => response.json())
      .then((response) => {
        $(this.button).prop("disabled", false);
        $(this.buttonspan).html("Submit");

        if (response["statusCode"] != 200) {
          this.errorspan.html("");
          this.errordiv.show();
          this.successdiv.hide();
          if (response["statusCode"] == 422) {
            this.errorspan.html(
              "A teammate with the same name already exists."
            );
          } else if (response["statusCode"] == 400) {
            this.errorspan.html("Lambda returned a critical error (400).");
          }
          return;
        }

        $(this.errordiv).hide();
        $(this.successdiv).show();
      });
  }
}
