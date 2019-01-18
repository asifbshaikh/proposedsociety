
function setupHome() {
	$('#get_auth_code').click(function (event) {
		event.preventDefault();
		requestAuthCode();
	});
	
	$('#visitor_login_submit').click(function (event) {
		event.preventDefault();
		authenticate();
	});
	
	$('#visitor_show_forget_code_page').click(function(event){
		event.preventDefault();
		$("#enterCodeDiv").hide();
		var notify = $('#existing_visitor_notify');
		notify.text('Please enter your registered email id then click submit button to get authcode, check your email inbox for that.');
		$("#forgetButtonGroup").hide();
		$("#forgetSubmitButoon").show();
	});
	
	$('#visitor_forget_code_submit').click(function(event){
		event.preventDefault();
		recoverAuthcode();
	});

	$('#current_member_submit').click(function (event) {
		event.preventDefault();
		authenticateMember();
	});
	
	$('#current_member_show_forget_password_page').click(function(event){
		event.preventDefault();
		$("#enterPasswordDiv").hide();
		$('#existing_member_notify').text('Please enter your registered email id then click submit button to get password, check your email inbox for that.');
		$("#forgetButtonGroupApplicant").hide();
		$("#forgetSubmitButtonApplicant").show();
	});
	
	$('#current_member_forget_password_submit').click(function(event){
		event.preventDefault();
		recoverApplicantPassword();
	});
	
	$('#agent_submit').click(function(event){
		event.preventDefault();
		authenticateAgent();
	});
	
	$('#agent_show_forget_password_page').click(function(event){
		event.preventDefault();
		$("#enterPasswordDivAgent").hide();
		$('#agent_notify').text('Please enter your registered email id then click submit button to get password, check your email inbox for that.');
		$("#forgetButtonGroupAgent").hide();
		$("#forgetSubmitButtonAgent").show();
	});
	
	$('#agent_forget_password_submit').click(function(event){
		event.preventDefault();
		recoverAgentPassword();
	});
	
}

function requestAuthCode() {
	var name = $('#new_visitor #name').val();
	var mobile = $('#new_visitor #mobile').val();
	var email = $('#new_visitor #email').val();

	var visitor = {
		'name': name,
		'mobile': mobile,
		'email': email
	};

	$.ajax({
		url: '/api/visitor/authcode/generate',
		type: 'POST',
		data: JSON.stringify(visitor),
		contentType: 'application/json',
		beforeSend: function() {
			$('#new_visitor input').attr('disabled', 'disabled');
			$('#get_auth_code').attr('disabled', 'disabled');
			$('#new_visitor_notify').removeClass("text-info notify-error")
									.addClass("text-info")
									.text('Please wait as we request for an authentication code...');
		},

		success: function() {	
			var notify = $('#new_visitor_notify');
			var fillEmail =  $('#existing_visitor #email');
			notify.text('An authentication code is successfully generated and sent message to your mobile, and emailed to you. Please use this authentication code to access the website by clicking on existing visitor tab.');
			$('#vTab a[href="#existing_visitor"]').tab('show');
			fillEmail.val(email);
		},

		error: function(response) {
			if(response.responseText) {
				var answer = jQuery.parseJSON(response.responseText);

				if (answer && answer.errorcode) {
					var notify = $("#new_visitor_notify").removeClass("text-info").addClass("notify-error")
					if (answer.errorcode == "VALIDATION_ERRORS") {
						notify.text("Please provide valid name, mobile number and e-mail. All the fields are mandatory.");
					} else if (answer.errorcode == "EMAIL_OR_MOBILE_EXISTS") {
						notify.text("Either mobile number or e-mail is already registered with us. To request for authentication code again, please provide same mobile number / e-mail pair.");
					} else {
						notify.text("We were unable to generate the authentication code, please try after some time.");
					}
				}
			}
		},

		complete: function() {
			$('#new_visitor input').removeAttr('disabled');
			$('#get_auth_code').removeAttr('disabled');
		}
	});
}

function authenticate() {
	var email = $('#existing_visitor #email').val();
	var authcode = $('#existing_visitor #authcode').val();

	var login = {
		'email': email,
		'password': authcode
	};
	$.ajax({
		url: '/api/visitor/authenticate',
		type: 'POST',
		data: JSON.stringify(login),
		contentType: 'application/json',
		beforeSend: function() {
			$('#existing_visitor input').attr('disabled', 'disabled');
			$('#visitor_login_submit').attr('disabled', 'disabled');
			$('#existing_visitor_notify').text('Please wait as we authenticate...');
		},
		success: function() {
			$('#block_screen').fadeOut('slow');
			var flashMovie=getFlashMovieObject("simplemovie");
			flashMovie. StopPlay();
			flashMovie. Rewind();
			flashMovie. Play();
			location.reload(true);
		},
		error: function() {
			var notify = $('#existing_visitor_notify');
			notify.text('Invalid login id or password.');
		},
		complete: function() {
			$('#existing_visitor input').removeAttr('disabled');
			$('#visitor_login_submit').removeAttr('disabled');
		}
	});
}

function recoverAuthcode() {
	var email = $('#existing_visitor #email').val();

	var forgetAuthcode = {
		'email': email
	};
	
	$.ajax({
		url: '/api/visitor/forget/authcode',
		type: 'POST',
		data: JSON.stringify(forgetAuthcode),
		contentType: 'application/json',
		beforeSend: function() {
			$('#existing_visitor input').attr('disabled');
			$('#visitor_forget_code_submit').attr('disabled');
			$('#existing_visitor_notify').text('Please wait as we are processing your request...');
		},
		success: function() {
			$("#enterCodeDiv").show();
			$('#existing_visitor_notify').text('If you have visited this website before, please enter the authentication code that you received on your email or mobile, otherwise click on the Visitor tab to generate an authentication code.');
			$("#forgetButtonGroup").show();
			$("#forgetSubmitButoon").hide();
		},
		error: function() {
			$('#existing_visitor_notify').text('Email id is invalid, please enter your registered email id.');
		},
		complete: function() {
			$('#existing_visitor input').removeAttr('disabled');
			$('#visitor_forget_code_submit').removeAttr('disabled');
		}
	});
}

function authenticateMember() {
	var email = $('#current_member #email').val();
	var password = $('#current_member #password').val();

	var login = {
		'email': email,
		'password': password
	};
	
	$.ajax({
		url: '/api/member/authenticate',
		type: 'POST',
		data: JSON.stringify(login),
		contentType: 'application/json',
		beforeSend: function() {
			$('#current_member input').attr('disabled', 'disabled');
			$('#current_member_submit').attr('disabled', 'disabled');
			$('#current_member_notify').text('Please wait as we authenticate...');
			
		},
		success: function() {
			window.location = '/applicant/dashboard';
		},
		error: function() {
			var notify = $('#current_member_notify');
			notify.text('Invalid login id or password.');
		},
		complete: function() {
			$('#current_member input').removeAttr('disabled');
			$('#current_member_submit').removeAttr('disabled');
		}
	});
}

function recoverApplicantPassword() {
	var email = $('#current_member #email').val();

	var forgetAuthcode = {
		'email': email
	};
	
	$.ajax({
		url: '/api/member/forget/password',
		type: 'POST',
		data: JSON.stringify(forgetAuthcode),
		contentType: 'application/json',
		beforeSend: function() {
			$('#current_member input').attr('disabled');
			$('#current_member_forget_password_submit').attr('disabled');
			$('#current_member_notify').text('Please wait as we are processing your request...');
		},
		success: function() {
			$("#enterPasswordDiv").show();
			$('#current_member_notify').text('If you are registered paid applicant by procedure, please enter your login id and password you received at the time of registration to access the dashboard or website.');
			$("#forgetButtonGroupApplicant").show();
			$("#forgetSubmitButtonApplicant").hide();
		},
		error: function() {
			$('#current_member_notify').text('Email id is invalid, please enter your registered email id.');
		},
		complete: function() {
			$('#current_member input').removeAttr('disabled');
			$('#current_member_forget_password_submit').removeAttr('disabled');
		}
	});
}

function authenticateAgent() {
	var email = $('#agent #email').val();
	var password = $('#agent #password').val();

	var login = {
		'email': email,
		'password': password
	};
	
	$.ajax({
		url: '/agent/login',
		type: 'POST',
		data: JSON.stringify(login),
		contentType: 'application/json',
		beforeSend: function() {
			$('#agent input').attr('disabled', 'disabled');
			$('#agent_submit').attr('disabled', 'disabled');
			$('#agent_notify').text('Please wait as we authenticate...');
			
		},
		success: function() {
			window.location = '/agent/dashboard';
		},
		error: function() {
			var notify = $('#agent_notify');
			notify.text('Invalid email or password.');
		},
		complete: function() {
			$('#agent input').removeAttr('disabled');
			$('#agent_submit').removeAttr('disabled');
		}
	});
}

function recoverAgentPassword() {
	var email = $('#agent #email').val();

	var forgetAuthcode = {
		'email': email
	};
	
	$.ajax({
		url: '/api/agent/forget/password',
		type: 'POST',
		data: JSON.stringify(forgetAuthcode),
		contentType: 'application/json',
		beforeSend: function() {
			$('#agent input').attr('disabled');
			$('#agent_forget_password_submit').attr('disabled');
			$('#agent_notify').text('Please wait as we are processing your request...');
		},
		success: function() {
			$("#enterPasswordDivAgent").show();
			$('#agent_notify').text('If you are registered paid applicant by procedure, please enter your login id and password you received at the time of registration to access the dashboard or website.');
			$("#forgetButtonGroupAgent").show();
			$("#forgetSubmitButtonAgent").hide();
		},
		error: function() {
			$('#agent_notify').text('Email id is invalid, please enter your registered email id.');
		},
		complete: function() {
			$('#agent input').removeAttr('disabled');
			$('#agent_forget_password_submit').removeAttr('disabled');
		}
	});
}

function getFlashMovieObject(movieName)
{
	  if (window.document[movieName]) 
	  {
	    return window.document[movieName];
	  }
	  if (navigator.appName.indexOf("Microsoft Internet")==-1)
	  {
	    if (document.embeds && document.embeds[movieName])
	      return document.embeds[movieName]; 
	  }
	  else // if (navigator.appName.indexOf("Microsoft Internet")!=-1)
	  {
	    return document.getElementById(movieName);
	  }
}


