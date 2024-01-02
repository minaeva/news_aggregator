function authenticateRegisterWithoutValidation(email, password) {

    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {

        if (this.readyState === 4) {
            document.getElementById("new_password").value = '';
            if (this.status === 404) {
                showWarningModal('Reader with email ' + email + ' cannot be found.');
                document.getElementById("new_email").value = '';
                return false;
            }
            if (this.status === 401) {
                showWarningModal('Incorrect password for a reader with email ' + email);
                return false;
            }
            if (this.status === 200) {
                let token = JSON.parse(this.responseText);
                localStorage.setItem('tokenData', JSON.stringify(token));
                document.getElementById("new_email").value = '';
                window.location.href = 'cabinet.html';
            }
        }
    };

    let requestUrl = USER_SERVICE + "/auth";
    const requestBody = {
        "email": email,
        "password": password
    };
    xhr.open("POST", requestUrl);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(requestBody));

    return false;
}

function authenticateLoginWithoutValidation(email, password) {

    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {

        if (this.readyState === 4) {
            document.getElementById("reader_password").value = '';
            if (this.status === 400) {
                showWarningModal('Reader with email ' + email + ' and password provided cannot be found.');
                document.getElementById("reader_email").value = '';
                return false;
            }
            if (this.status === 200) {
                let token = JSON.parse(this.responseText);
                localStorage.setItem('tokenData', JSON.stringify(token));
                document.getElementById("reader_email").value = '';
                window.location.href = 'cabinet.html';
            }
        }
    };

    let requestUrl = USER_SERVICE + "/auth";
    const requestBody = {
        "email": email,
        "password": password
    };
    xhr.open("POST", requestUrl);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(requestBody));

    return false;
}

function authenticate(email, password) {
    if (!validateEmailPassword(email, password)) {
        return false;
    }
    authenticateLoginWithoutValidation(email, password);
}

function registerAReader() {
    let new_email = document.getElementById("new_email").value;
    let new_password = document.getElementById("new_password").value;
    let new_name = document.getElementById("new_name").value;

    if (!validateNameEmailPassword(new_name, new_email, new_password)) {
        return false;
    }

    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {

        if (this.readyState === 4) {
            document.getElementById("new_email").value = '';
            document.getElementById("new_password").value = '';
            document.getElementById("new_name").value = '';
            if (this.status === 400) {
                showWarningModal('Reader with email ' + new_email + ' cannot be created.');
                return false;
            }
            if (this.status === 409) {
                showWarningModal('Reader with email ' + new_email + '  already exists.');
                return false;
            } else if (this.status === 200) {
                authenticateRegisterWithoutValidation(new_email, new_password);
            }
        }
    };

    let requestUrl = USER_SERVICE + "/registration";
    const requestBody = {
        "email": new_email,
        "password": new_password,
        "name": new_name,
        "registrationSource": 'ONSITE'
    };
    xhr.open("POST", requestUrl);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(requestBody));

    return false;
}

function registerGoogleReader(name, surname, email, token) {
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {

        if (this.readyState === 4) {
            clearRegisterForm();
            clearIndexForm();
            if ((this.status === 500) || (this.status === 400)) {
                showWarningModal("Something is wrong with logging " + name + " " + surname);
                return false;
            } else if (this.status === 200) {
                let token = JSON.parse(this.responseText);
                localStorage.setItem('tokenData', JSON.stringify(token));
                window.location.href = 'cabinet.html';
            }
        }
    };

    let requestUrl = HOME_PAGE + "/users/google";
    const requestBody = {
        "email": email,
        "name": name,
        "surname": surname,
        "registrationType": 'GOOGLE',
        "googleIdToken": token
    };
    xhr.open("POST", requestUrl);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(requestBody));

    return false;
}

function attachAuthListenerOnEnter() {
    $(window).off();

    $(window).on('keydown', e => {
        switch (e.which) {
            case 13: // enter
                authenticate(document.getElementById('reader_email').value, document.getElementById('reader_password').value);
                break;
            default:
                return; // exit this handler for other keys
        }
        e.preventDefault(); // prevent the default action (scroll / move caret)
    });
}

function attachRegisterListenerOnEnter() {
    $(window).off();

    $(window).on('keydown', e => {
        switch (e.which) {
            case 13: // enter
                registerAReader();
                break;
            default:
                return; // exit this handler for other keys
        }
        e.preventDefault(); // prevent the default action (scroll / move caret)
    });
}


