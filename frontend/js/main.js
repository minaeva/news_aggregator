const BEARER = 'Bearer ';

function getCurrentUserId() {
    let tokenData = localStorage.getItem('tokenData');
    let jsonInside = JSON.parse(tokenData);
    if (jsonInside == null) {
        return null;
    }
    return jsonInside.id;
}

function showWarningModal(text) {
    document.getElementById("smallModalTitle").innerHTML = 'Warning';
    document.getElementById("smallModalBody").innerHTML = text;
    $('#smallModal').modal('show');
}

function showSuccessModal(text) {
    document.getElementById("smallModalTitle").innerHTML = 'Success';
    document.getElementById("smallModalBody").innerHTML = text;
    $('#smallModal').modal('show');
}

function getTokenFromLocalStorage() {
    let localStorageInfo = localStorage.getItem('tokenData');
    let jsonInsideLocalStorage = JSON.parse(localStorageInfo);
    return jsonInsideLocalStorage.jwt;
}

function addAuthorization(xhr) {
    let tokenString = getTokenFromLocalStorage();
    xhr.setRequestHeader('Authorization', 'Bearer ' + tokenString);
    xhr.setRequestHeader('Accept', 'application/json');
}

function logout() {
    clearRequestHeader();
//    googleSignOut();
    window.location.href = 'index.html';
    return false;
}

function loadIndex() {
    clearIndexForm();
    clearRequestHeader();
    attachAuthListenerOnEnter();
}

function clearIndexForm() {
    $("#reader_email").removeAttr('value');
    $('#reader_password').removeAttr('value');
}

function clearRequestHeader() {
    let savedLanguage = localStorage.getItem('language');
    localStorage.clear();
    localStorage.setItem('language', savedLanguage);

    let myHeaders = new Headers();
    myHeaders.delete('Authorization');
    return false;
}

function selectMenu(menuToSelect) {
    deselectActiveMenu();
    addClassToElement(menuToSelect, "active-page");

    return false;
}

function deselectActiveMenu() {
    let selected = document.getElementsByClassName("active-page");
    if (selected.length > 0) {
        selected[0].className = selected[0].className.replace("active-page", "");
    }
}

function addClassToElement(elementId, classToAdd) {
    let elementOnPage = document.getElementById(elementId);
    elementOnPage.classList.add(classToAdd);
}

function removeClassFromElement(elementId, classToRemove) {
    let elementToRemoveClassFrom = document.getElementById(elementId);
    if (elementToRemoveClassFrom.classList.contains(classToRemove)) {
        elementToRemoveClassFrom.classList.remove(classToRemove);
    }
}

function changeElementClass(elementId, classToRemove, classToAdd) {
    let element = document.getElementById(elementId);
    if (element.classList.contains(classToRemove)) {
        element.classList.remove(classToRemove);
    }
    element.classList.add(classToAdd);
}

function notNull(str) {
    if (str === null || str === "0" || str === '' || str === 0) {
        return '';
    }
    return str;
}

function isEmpty(str) {
    return str === null || str === '';
}

function setPageTitle(text) {
    document.getElementById("accordion_header").innerHTML = text;
}

function setPageSubtitle(text) {
    document.getElementById("accordion_subheader").innerHTML = text;
}

function clearContent() {
    document.getElementById("news_body").innerHTML = '';
}

function loadRegister() {
    clearRegisterForm();
    attachRegisterListenerOnEnter();
}

function clearRegisterForm() {
    $("#new_email").removeAttr('value');
    $("#new_name").removeAttr('value');
    $("#new_password").removeAttr('value');
}
