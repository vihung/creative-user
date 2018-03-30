const
URL_USER = "/api/user/current";
const
URL_CREDS = "/api/user/current/credentials";
const
URL_LOGOUT = "/api/session/current";

function loadUserData() {
  getUserData();
  getCredentialsData();
}

function clearUserData() {
  $("#card-user #user-firstName").val("");
  $("#card-user #user-lastName").val("");
  $("#card-user #user-nickname").val("");
}

function clearContactData() {
  $("#card-contact #user-mobile").text("");
}

function clearCredentialsData() {
  $("#card-creds #cred-email").text("");
}

function populateUserData(user) {
  if (user) {
    clearUserData()
    $("#card-user #user-firstName").val(user.firstName);
    $("#card-user #user-firstName").prop("defaultValue", user.firstName);
    $("#card-user #user-firstName").attr("placeholder", "first name");
    
    $("#card-user #user-lastName").val(user.lastName);
    $("#card-user #user-lastName").prop("defaultValue", user.lastName);
    $("#card-user #user-lastName").attr("placeholder", "last name");
    
    $("#card-user #user-nickname").val(user.nickname);
    $("#card-user #user-nickname").prop("defaultValue", user.nickname);
    $("#card-user #user-nickname").attr("placeholder", "nickname");
    $("#card-user").show();

    clearContactData()
    $("#card-contact #contact-mobile").text(user.mobile);
    $("#card-contact").show();
  }
}

function populateCredentialsData(creds) {
  if (creds) {
    clearCredentialsData()
    $("#card-creds #cred-email").text(creds.email);
    $("#card-creds").show();
  }
}

function onLoadUserFailure(e) {
  console.log("onLoadUserFailure(): Invoked")
  console.log(e);
  window.location.href = "/login.html"
}

function onLoadCredentialsFailure(e) {
  console.log("onLoadCredentialsFailure(): Invoked")
  console.log(e);
  window.location.href = "/login.html"
}

function getUserData() {
  $("#card-user").hide();
  $.ajax({
    headers : {
      'Accept' : 'application/json',
      'Content-Type' : 'application/json'
    },
    url : URL_USER,
    type : "GET",
    statusCode : {
      200 : populateUserData,
      401 : onLoadUserFailure
    }
  })
}

function getCredentialsData() {
  $("#card-creds").hide();
  $.ajax({
    headers : {
      'Accept' : 'application/json',
      'Content-Type' : 'application/json'
    },
    url : URL_CREDS,
    type : "GET",
    statusCode : {
      200 : populateCredentialsData,
      401 : onLoadCredentialsFailure
    }
  })
}

function onLogoutSuccess(e) {
  console.log("onLogoutSuccess(): Invoked")
  console.log(e);
  window.location.href = "/login.html"
}

function onLogoutFailure(e) {
  console.log("onLogoutFailure(): Invoked")
  console.log(e);
  var errorMessage = e.statusText;

}

function onSignOut() {
  $.ajax({
    headers : {
      'Accept' : 'application/json',
      'Content-Type' : 'application/json'
    },
    url : URL_LOGOUT,
    type : "DELETE",
    statusCode : {
      200 : onLogoutSuccess
    }
  }).fail(onLogoutFailure);
}

function onFormFieldChange(pEvent) {
  var $form = pEvent.data.form;
  var formDirty;
  $form.find(":input:not(:button):not([type=hidden])").each(function() {
    if ((this.type == "text" || this.type == "textarea" || this.type == "hidden") && this.defaultValue != this.value) {
      console.log(this)
      console.log("onFormFieldChange(): Form is dirty");
      formDirty = true;
    }
  });
  if(formDirty)
    $("#buttonUpdateProfile").prop("disabled", false);
  else
    $("#buttonUpdateProfile").prop("disabled", true);

}

function onEditProfile(pEvent) {
  console.log("onEditProfile(): Invoked", pEvent);
  console.log(this);
  pEvent.preventDefault();
  // enable form fields
  $("#card-user input.form-control-plaintext").removeClass("form-control-plaintext").addClass("form-control").attr("readonly", false);
  $("#buttonEditProfile").prop("disabled", true);
  $("#buttonEditProfile").hide();
  $("#buttonUpdateProfile").show();

}

function onUpdateProfileFormSubmit(pEvent) {
  console.log("onUpdateProfileFormSubmit(): Invoked");
  console.log(pEvent);
  pEvent.preventDefault();
}
