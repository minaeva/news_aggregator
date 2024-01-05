function validateEmailPassword(email, password) {
    let isValidated = true;
    let errorText = '';

    if (!validateNotEmptyField(email, 'login_email_group')) {
        errorText += 'Email cannot be empty.<br>';
        isValidated = false;
    }
    if (!validateNotEmptyField(password, 'login_password_group')) {
        errorText += 'Password cannot be empty.<br>';
        isValidated = false;
    }

    if (isValidated) {
        return true;
    }

    showWarningModal(errorText);
    return false;
}


function validateNameEmailPassword(name, email, password) {
    let isValidated = true;
    let errorText = '';

    if (!validateNotEmptyField(name, 'register_name_group')) {
        errorText += 'Name cannot be empty.<br>';
        isValidated = false;
    }

    if (!validateNotEmptyField(email, 'register_email_group')) {
        errorText += 'Email cannot be empty.<br>';
        isValidated = false;
    }

    if (!validateNotEmptyField(password, 'register_password_group')) {
        errorText += 'Password cannot be empty.<br>';
        isValidated = false;
    }

    if (isValidated) {
        return true;
    }

    showWarningModal(errorText);
    return false;
}

function validateKeyword(title) {
    let isValidated = true;
    if (!validateNotEmptyField(title, 'book_title_group')) {
        isValidated = false;
    }
    return isValidated;
}

function validateEditBook(book_title, author_name, author_surname, year) {
    let isValidated = true;
    // let errorText = "";
    if (!validateNotEmptyField(book_title, 'edit_book_title_group')) {
        // errorText += application_language.titleCannotBeEmpty_title + '<br>';
        isValidated = false;
    }
    if (!validateNotEmptyField(author_name, 'edit_author_name_group')) {
        // errorText += application_language.authorNameCannotBeEmpty_title + '<br>';
        isValidated = false;
    }
    if (!validateNotEmptyField(author_surname, 'edit_author_surname_group')) {
        // errorText += application_language.authorSurnameCannotBeEmpty_title + '<br>';
        isValidated = false;
    }
    if (!yearIsEmptyOrGoodNumber(year, 'edit_year_group')) {
        isValidated = false;
    }
    if (isValidated) {
        return true;
    }

//    showWarningModal(errorText);
    return false;
}

function validateNotEmptyField(field, groupControlId) {
    let group = document.getElementById(groupControlId);
    if (field == null || field === "") {
        group.classList.add('has-error');
        return false;
    }
    if (group.classList.contains('has-error')) {
        group.classList.remove('has-error');
    }
    return true;
}

function validateFieldLength(groupId, fieldValue, valueMaximum) {
    let group = document.getElementById(groupId);
    if (fieldValue.length > valueMaximum) {
        group.classList.add('has-error');
        return false;
    }
    if (group.classList.contains('has-error')) {
        group.classList.remove('has-error');
    }
    return true;
}

