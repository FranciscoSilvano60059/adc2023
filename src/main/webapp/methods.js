function getURL(resourcePath) {
	return `${window.location.protocol}//${window.location.host}/rest/${resourcePath}`
}
function submitRegisterFetch(event) {
	event.preventDefault();
	const form = document.querySelector('form');
	const formData = new FormData(form);

	const name = formData.get("name");
	const username = formData.get("username");
	const password = formData.get("password");
	const password2 = formData.get("password2");
	const email = formData.get("email");
	const telefoneFixo = formData.get("telefoneFixo");
	const telefoneMovel = formData.get("telefoneMovel");
	const visibilidade = formData.get("visibilidade");
	const morada = formData.get("morada");
	const codigoPostal = formData.get("codigoPostal");
	const ocupacao = formData.get("ocupacao");
	const nif = formData.get("nif");


	const json = {
		name: name,
		username: username,
		email: email,
		password: password,
		password2: password2,
		telefoneFixo: telefoneFixo,
		telefoneMovel: telefoneMovel,
		visibilidade: visibilidade,
		morada: morada,
		codigoPostal: codigoPostal,
		ocupacao: ocupacao,
		nif: nif
	};
	console.log(json)


	var url = '/rest/register/';
	console.log();
	fetch(url, {
		method: "POST",
		headers: {
			"Content-Type": "application/json;charset=UTF-8"
		},
		body: JSON.stringify(json)
	})
		.then(response => {
			if (response.ok) {
				console.log("Registration successful!");
				window.location.href = '/index.html';

			} else {
				console.log("Registration failed.");
			}
		})
		.catch(error => {
			console.log("An error occurred while registering.");
		});
}

function getToken() {

}
function submitLoginFetch(event) {
	event.preventDefault();

	const form = document.querySelector('form');
	const formData = new FormData(form);

	const username = formData.get("username");
	const password = formData.get("password");

	const json = {
		username: username,
		password: password
	};

	console.log(json)

	var url = '/rest/login/v1';
	console.log();
	fetch(url, {
		method: "POST",
		headers: {
			"Content-Type": "application/json;charset=UTF-8"
		},
		body: JSON.stringify(json)
	})
		.then(response =>{// response.json())
		//.then(JsonData => {
			if (response.ok) {
				//localStorage.setItem("role", JSON.stringify(responseData.data));
				//localStorage.setItem("userId", username);
				console.log("Login successful!");
				window.location.href = '/page.html';
			} else {
				console.log("Login failed.");
			}
		})
		.catch(error => {
			console.log("An error occurred while login.");
		});
}

function logout(event) {
	event.preventDefault();

	username = localStorage.getItem("userId");
	
	const json = {username:username};
	
	fetch('/rest/logout/', {
	method: 'POST',
	headers: {
		'Content-Type': 'application/json;charset=UTF-8'
	},
	body: JSON.stringify(json)
})
	.then(response => {
		if (response.ok) {
			console.log('Logout successful!');
			localStorage.clear();
			window.location.href = '/login.html';
		} else {
			console.log('Logout failed.');
		}
	})
	.catch(error => {
		console.log('An error occurred while logging out.');
	});
}


function tokenFetch(event) {
	event.preventDefault(); // Prevent default form submission

	const token = localStorage.getItem('token');
	if (token) {
		console.log("Token found:", token);

		var url = '/rest/tokens/info';
		fetch(url, {
			method: "POST",
			headers: {
				"Content-Type": "application/json;charset=UTF-8",
				"Authorization": "Bearer " + token
			}
		})
			.then(response => {
				if (response.ok) {
					console.log("Info received successfully!");
				} else {
					console.log("Info not received.");
				}
			})
			.catch(error => {
				console.log("An error occurred while getting token info.");
			});

	} else {
		console.log("Token not found.");
	}
}
