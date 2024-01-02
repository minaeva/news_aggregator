const NAME_SURNAME_EMAIL_HERO_50 = 50;
const COUNTRY_CITY_DISTRICT_TELEGRAM_VIBER_30 = 30;
const FB_PAGE_100 = 100;
const TITLE_MOON_BOOK_YEAR_HOBBY_POWER_200 = 200;

function validateImage(field, groupControlId) {
    let group = document.getElementById(groupControlId);
    if (field == null || field == "") {
        group.classList.add('has-error');
        return false;
    }
    if (group.classList.contains('has-error')) {
        group.classList.remove('has-error');
    }
    return true;
}

function validateBookSearch(search_year_from, search_year_to) {
    if (yearIsEmptyOrGoodNumber(search_year_from, 'year_from_group') &
        yearIsEmptyOrGoodNumber(search_year_to, 'year_to_group')) {
        return true;
    }
    return false;
}

function yearIsNonEmpty(year) {
    if (year == null || year == "") {
        return false;
    }
    return true;
}

function yearIsEmptyOrGoodNumber(year, groupId) {
    let element = document.getElementById(groupId);

    if (year == null || year == "") {
        if (element.classList.contains('has-error')) {
            element.classList.remove('has-error');
        }
        return true;
    }
    if (isNaN(year)) {
        element.classList.add('has-error');
        return false;
    }
    if (element.classList.contains('has-error')) {
        element.classList.remove('has-error');
    }
    return true;
}

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

function validateProfileInfo(name, yearOfBirth) {
    let isValidated = true;
    let errorText = "";
    if (!validateNotEmptyField(name, 'profile_name_group')) {
        errorText += application_language.nameCannotBeEmpty_title + '<br>';
        isValidated = false;
    }
/*
    if (!validateYearOfBirth(yearOfBirth)) {
        errorText += 'It is supposed the year of birth is more than 1919 and less than the current one<br>';
        isValidated = false;
    }
*/
    if (isValidated) {
        return true;
    }

    showWarningModal(errorText);
    return false;
}

function validateProfileInfoWithLengths(name, surname, bookToTheMoon, hero, yearOfBirth, gender, superPower, bookOfTheYear, hobby,
                             country, city, district, fb, telegram, viber) {
    let isValidated = true;
    let errorText = "";
    let nameIsEmpty = false;
    if (!validateNotEmptyField(name, 'profile_name_group')) {
        errorText += 'Name cannot be empty<br>';
        isValidated = false;
        nameIsEmpty = true;
    }
    if (!nameIsEmpty && !validateFieldLength('profile_name_group', name, NAME_SURNAME_EMAIL_HERO_50)) {
        errorText += 'Name cannot be longer that 50 symbols<br>';
        isValidated = false;
    }

    if (!validateFieldLength('profile_surname_group', name, NAME_SURNAME_EMAIL_HERO_50)) {
        errorText += 'Surname cannot be longer that 50 symbols<br>';
        isValidated = false;
    }
    if (!validateFieldLength('profile_book_to_the_moon_group', bookToTheMoon, TITLE_MOON_BOOK_YEAR_HOBBY_POWER_200)) {
        errorText += "Book you'd take to the Mars cannot be longer that 200 symbols<br>";
        isValidated = false;
    }
    if (!validateFieldLength('profile_hero_group', hero, NAME_SURNAME_EMAIL_HERO_50)) {
        errorText += "Hero you are similar to cannot be longer that 50 symbols<br>";
        isValidated = false;
    }
    if (!validateYearOfBirth(yearOfBirth)) {
        errorText += 'It is supposed the year of birth is more than 1919 an less than the current one<br>';
        isValidated = false;
    }
    if (!validateFieldLength('profile_super_power_group', superPower, TITLE_MOON_BOOK_YEAR_HOBBY_POWER_200)) {
        errorText += "Superpower cannot be longer that 200 symbols<br>";
        isValidated = false;
    }
    if (!validateFieldLength('profile_book_of_the_year_group', bookOfTheYear, TITLE_MOON_BOOK_YEAR_HOBBY_POWER_200)) {
        errorText += "Book of the last year cannot be longer that 200 symbols<br>";
        isValidated = false;
    }
    if (!validateFieldLength('profile_hobby_group', hobby, TITLE_MOON_BOOK_YEAR_HOBBY_POWER_200)) {
        errorText += "What you do when there's nothing to do cannot be longer that 200 symbols<br>";
        isValidated = false;
    }
    if (!validateFieldLength('profile_country_group', country, COUNTRY_CITY_DISTRICT_TELEGRAM_VIBER_30)) {
        errorText += "Country cannot be longer that 30 symbols<br>";
        isValidated = false;
    }
    if (!validateFieldLength('profile_city_group', city, COUNTRY_CITY_DISTRICT_TELEGRAM_VIBER_30)) {
        errorText += "City cannot be longer that 30 symbols<br>";
        isValidated = false;
    }
    if (!validateFieldLength('profile_district_group', district, COUNTRY_CITY_DISTRICT_TELEGRAM_VIBER_30)) {
        errorText += "District cannot be longer that 30 symbols<br>";
        isValidated = false;
    }
    if (!validateFieldLength('profile_facebook_group', fb, FB_PAGE_100)) {
        errorText += "Facebook page value cannot be longer that 100 symbols<br>";
        isValidated = false;
    }
    if (!validateFieldLength('profile_telegram_group', telegram, COUNTRY_CITY_DISTRICT_TELEGRAM_VIBER_30)) {
        errorText += "Telegram value cannot be longer that 30 symbols<br>";
        isValidated = false;
    }
    if (!validateFieldLength('profile_viber_group', viber, COUNTRY_CITY_DISTRICT_TELEGRAM_VIBER_30)) {
        errorText += "Viber value cannot be longer that 30 symbols<br>";
        isValidated = false;
    }

    if (isValidated) {
        return true;
    }

    showWarningModal(errorText);
    return false;
}

function validateBook(book_title, author_name, author_surname, year) {
    let isValidated = true;
    if (!validateNotEmptyField(book_title, 'book_title_group')) {
        isValidated = false;
    }
    if (!validateNotEmptyField(author_name, 'author_name_group')) {
        isValidated = false;
    }
    if (!validateNotEmptyField(author_surname, 'author_surname_group')) {
        isValidated = false;
    }
    if (!yearIsEmptyOrGoodNumber(year,'year_group')) {
        isValidated = false;
    }

    if (isValidated) {
        return true;
    }

    return false;
}

function validateBookWithLengths(book_title, author_name, author_surname, year) {
    let isValidated = true;
    let errorText = "";
    let titleIsEmpty = false;
    let nameIsEmpty = false;
    let surnameIsEmpty = false;
    if (!validateNotEmptyField(book_title, 'book_title_group')) {
        errorText += 'Title cannot be empty<br>';
        isValidated = false;
        titleIsEmpty = true;
    }
    if (!titleIsEmpty && !validateFieldLength('book_title_group', book_title, TITLE_MOON_BOOK_YEAR_HOBBY_POWER_200)) {
        errorText += 'Title cannot be longer that 200 symbols<br>';
        isValidated = false;
    }

    if (!validateNotEmptyField(author_name, 'author_name_group')) {
        errorText += 'Author name cannot be empty<br>';
        isValidated = false;
        nameIsEmpty = true;
    }
    if (!nameIsEmpty && !validateFieldLength('author_name_group', author_name, NAME_SURNAME_EMAIL_HERO_50)) {
        errorText += 'Author name cannot be longer that 50 symbols<br>';
        isValidated = false;
    }

    if (!validateNotEmptyField(author_surname, 'author_surname_group')) {
        errorText += 'Author surname cannot be empty<br>';
        isValidated = false;
        surnameIsEmpty = true;
    }
    if (!surnameIsEmpty && !validateFieldLength('author_surname_group', author_name, NAME_SURNAME_EMAIL_HERO_50)) {
        errorText += 'Author surname cannot be longer that 50 symbols<br>';
        isValidated = false;
    }

    if (!yearIsEmptyOrGoodNumber(year,'year_group')) {
        isValidated = false;
    } else if (year < 1454) {
        errorText += "The Gutenberg Bible, also known as the 42-line Bible, is listed by the Guinness Book of World records as the world's oldest mechanically printed book – the first copies of which were printed in 1454-1455 AD<br>";
        isValidated = false;
    } else if (year > new Date().getFullYear()) {
        errorText += 'Boobook is expected to be used for the already existent books only<br>';
        isValidated = false;
    }

    if (isValidated) {
        return true;
    }

    showWarningModal(errorText);
    return false;
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
    if (field == null || field == "") {
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

