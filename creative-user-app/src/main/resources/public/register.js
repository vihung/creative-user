/**
 * register.js
 * 
 * Client-side logic for registration form
 */
const URL_REGISTRATION = "/api/user/new";

var bDeriveNickname = true;

function deriveNickname(e) {
	console.log("deriveNickname(): Invoked");

	if (bDeriveNickname) {
		var $inputFirstName = e.data.inputFirstName;
		var firstName = $inputFirstName.val();
		firstName = firstName.trim().replace(/[^a-z0-9+]+/gi, '');
		firstname = firstName.substring(0, 7).toLowerCase();
		console.log("deriveNickname(): firstName=" + firstName);

		var $inputLastName = e.data.inputLastName;
		var lastName = $inputLastName.val();
		lastName = lastName.trim().replace(/[^a-z0-9+]+/gi, '');
		var lastInitial = lastName.substring(0, 1).toLowerCase();
		console.log("deriveNickname(): lastInitial=" + lastInitial);

		var nickname = firstname + lastInitial;
		console.log("deriveNickname(): nickname=" + nickname);

		var $inputNickname = e.data.inputNickname;
		$inputNickname.val(nickname);
	}
}

function onFirstNameChange(e) {
	console.log("onFirstNameChange(): Invoked");
	deriveNickname(e);
}

function onLastNameChange(e) {
	console.log("onLastNameChange(): Invoked");
	deriveNickname(e);
}

function onNicknameChange(e) {
	console.log("onNicknameChange(): Invoked");

	var $inputNickname = e.data.inputNickname;
	var nickname = $inputNickname.val();
	if (nickname.trim() == "") {
		bDeriveNickname = true;
		deriveNickname(e);
	} else {
		bDeriveNickname = false;
	}
}

function onRegistrationSuccess(e) {
	console.log("onRegistrationSuccess(): Invoked")
	console.log(e);
	window.location.href = "./user/home.html"
}

function onRegistrationFailure(e) {
	console.log("onRegistrationFailure(): Invoked")
	console.log(e);
	var errorMessage = e.statusText;
}

function onRegisterFormSubmit(e) {
	console.log("onRegisterFormSubmit(): Invoked")
	e.preventDefault();

	// jQuery object version of target object
	var $form = $(this);

	// Extract form fields
	var firstName = $form.find('#inputFirstName').val();
	console.log("onRegisterFormSubmit(): firstName=" + firstName);

	var lastName = $form.find('#inputLastName').val();
	console.log("onRegisterFormSubmit(): lastName=" + lastName);

	var nickname = $form.find('#inputNickname').val();
	console.log("onRegisterFormSubmit(): nickname=" + nickname);

	var mobile = $form.find('#inputMobile').val();
	console.log("onRegisterFormSubmit(): mobile=" + mobile);

	var email = $form.find('#inputEmail').val();
	console.log("onRegisterFormSubmit(): email=" + email);

	var password = $form.find('#inputPassword').val();
	console.log("onRegisterFormSubmit(): password=" + password);

	var registrationRequest = {
		"firstName" : firstName,
		"lastName" : lastName,
		"nickname" : nickname,
		"mobile" : mobile,
		"email" : email,
		"password" : password
	}

	// AJAX PUT to register URL
	$.ajax({
		headers: { 
	        'Accept': 'application/json',
	        'Content-Type': 'application/json' 
	    },
	    url: URL_REGISTRATION, 
		type: "PUT",
		data: JSON.stringify(registrationRequest),
		statusCode: {
			200: onRegistrationSuccess,
			201: onRegistrationSuccess
		}
	})
	.fail(onRegistrationFailure);

}
