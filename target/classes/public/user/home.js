const URL_USER = "/api/user/current"
const URL_LOGOUT = "/api/session/current";

function loadUserData() {
	getUserData()
}

function clearUserData() {
	$("#card-user #user-firstName").text("");
	$("#card-user #user-lastName").text("");
	$("#card-user #user-nickname").text("");
	$("#card-user #user-mobile").text("");
}

function populateUserData(user) {
	if (user) {
		clearUserData()
		$("#card-user #user-firstName").text(user.firstName);
		$("#card-user #user-lastName").text(user.lastName);
		$("#card-user #user-nickname").text(user.nickname);
		$("#card-user #user-mobile").text(user.mobile);

		$("#card-user").show();
	}
}

function onLoadUserFailure(e) {
	console.log("onLoadUserFailure(): Invoked")
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
