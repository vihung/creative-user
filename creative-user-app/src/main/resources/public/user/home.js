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

function enableAndShowButton($button) {
  console.log("enableAndShowButton(): Invoked", $button);
  $button.prop("disabled", false);
  $button.show();
}

function disableAndHideButton($button) {
  console.log("disableAndHideButton(): Invoked", $button);
  $button.prop("disabled", true);
  $button.hide();
}

function disableFormField(i, field) {
  var $field = $(field);
  console.log(i, $field)
  $field.addClass("form-control-plaintext").removeClass("form-control").attr("readonly", true);
}

function enableFormField(i, field) {
  var $field = $(field);
  console.log(i, $field)
  $field.addClass("form-control").removeClass("form-control-plaintext").attr("readonly", false);
}

function populateFormField($field, value, placeholder) {
  $field.val(value);
  $field.prop("defaultValue", value);
  $field.attr("placeholder", placeholder);

}
function clearUserData() {
  $("#card-user #user-firstName").val("");
  $("#card-user #user-lastName").val("");
  $("#card-user #user-nickname").val("");
  $("#card-user #user-mobile").val("");
}

function clearCredentialsData() {
  $("#card-creds #cred-email").text("");
}

function populateUserData(user) {
  if (user) {
    clearUserData()
    populateFormField($("#card-user #user-firstName"), user.firstName, "first name")
    populateFormField($("#card-user #user-lastName"), user.lastName, "last name")
    populateFormField($("#card-user #user-nickname"), user.nickname, "nickname");
    populateFormField($("#card-user #user-mobile"), user.mobile, "mobile");
    $("#card-user").show();
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
  if (formDirty) $("#buttonUpdateProfile").prop("disabled", false);
  else
    $("#buttonUpdateProfile").prop("disabled", true);

}

function onEditProfile(pEvent) {
  console.log("onEditProfile(): Invoked", pEvent);
  console.log(this);
  pEvent.preventDefault();

  // enable form fields
  // $("#card-user input.form-control-plaintext").removeClass("form-control-plaintext").addClass("form-control").attr("readonly", false);
  $("#card-user input.form-control-plaintext").each(enableFormField);

  $("#buttonEditProfile").prop("disabled", true);
  $("#buttonEditProfile").hide();
  $("#buttonUpdateProfile").show();

  $("#buttonCancelEditProfile").prop("disabled", false);
  $("#buttonCancelEditProfile").show();

}

function onEditProfileCancel(pEvent) {
  console.log("onEditProfileCancel(): Invoked", pEvent);
  console.log(this);
  pEvent.preventDefault();

  // disable form fields
  $("#card-user input.form-control").each(disableFormField);

  pEvent.target.form.reset();

  disableAndHideButton($("#buttonUpdateProfile"));
  disableAndHideButton($("#buttonCancelEditProfile"));
  enableAndShowButton($("#buttonEditProfile"));
}

function onUpdateProfileSuccess(pUser) {
  console.log("onUpdateProfileSuccess(): Invoked");
  console.log(pUser);
  populateFormField($("#user-firstName"), pUser.firstName);
  populateFormField($("#user-lastName"), pUser.lastName);
  populateFormField($("#user-nickname"), pUser.nickname);
  populateFormField($("#user-mobile"), pUser.mobile);
  
  disableAndHideButton($("#buttonUpdateProfile"));
  disableAndHideButton($("#buttonCancelEditProfile"));
  enableAndShowButton($("#buttonEditProfile"));
  $("#card-user input.form-control").each(disableFormField);
}

function onUpdateProfileFailure(pEvent) {
  console.log("onUpdateProfileFailure(): Invoked");
  console.log(pEvent);
}

function onUpdateProfileFormSubmit(pEvent) {
  console.log("onUpdateProfileFormSubmit(): Invoked");
  console.log(pEvent);
  pEvent.preventDefault();

  var $form = $(this);

  // Extract form fields
  var firstName = $form.find('#user-firstName').val();
  console.log("onUpdateProfileFormSubmit(): firstName=" + firstName);

  var lastName = $form.find('#user-lastName').val();
  console.log("onUpdateProfileFormSubmit(): lastName=" + lastName);

  var nickname = $form.find('#user-nickname').val();
  console.log("onUpdateProfileFormSubmit(): nickname=" + nickname);

  var mobile = $form.find('#user-mobile').val();
  console.log("onUpdateProfileFormSubmit(): mobile=" + mobile);

  var updateProfileRequest = {
    "firstName" : firstName,
    "lastName" : lastName,
    "nickname" : nickname,
    "mobile" : mobile
  }

  // AJAX POST to update profile URL
  $.ajax({
    headers : {
      'Accept' : 'application/json',
      'Content-Type' : 'application/json'
    },
    url : URL_USER,
    type : "PUT",
    data : JSON.stringify(updateProfileRequest),
    statusCode : {
      200 : onUpdateProfileSuccess,
    }
  }).fail(onUpdateProfileFailure);

}
