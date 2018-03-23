/**
 * login.js
 * 
 * Client-side logic for login form
 */
const URL_LOGIN = "/api/session/new";

function onLoginSuccess(e) {
	console.log("onLoginSuccess(): Invoked")
	console.log(e);
	window.location.href = "./user/home.html"
}

function onLoginFailure(e) {
	console.log("onLoginFailure(): Invoked")
	console.log(e);
	var errorMessage = e.statusText;
}

function onLoginFormSubmit(e) {
	console.log("onLoginFormSubmit(): Invoked")
	e.preventDefault();

	// jQuery object version of target object
	var $form = $(this);

	// Extract form fields
	var email = $form.find('#inputEmail').val();
	console.log("onLoginFormSubmit(): email=" + email);

	var password = $form.find('#inputPassword').val();
	console.log("onLoginFormSubmit(): password=" + password);

	var loginRequest = {
		"email" : email,
		"password" : password
	}

	// AJAX PUT to register URL
	$.ajax({
		headers: { 
	        'Accept': 'application/json',
	        'Content-Type': 'application/json' 
	    },
	    url: URL_LOGIN, 
		type: "PUT",
		data: JSON.stringify(loginRequest),
		statusCode: {
			200: onLoginSuccess,
			201: onLoginSuccess
		}
	})
	.fail(onLoginFailure);

}
