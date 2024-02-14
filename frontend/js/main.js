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

function hideAllShowOne(elementId) {
    let news_body = document.getElementById("news_body");
    news_body.classList.add("hidden");
    let keywords_body = document.getElementById("keywords_body");
    keywords_body.classList.add("hidden");
    let about_body = document.getElementById("about_body");
    about_body.classList.add("hidden");

    document.getElementById("news_body").innerHTML = '';

    document.getElementById(elementId).classList.remove("hidden");
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
    // document.getElementById("page_title").innerHTML = text.toUpperCase();
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

function sanitizeHTML(str) {
    return str.replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

function formatDate(dateString) {
    const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

    const date = new Date(dateString);
    const month = months[date.getMonth()];
    const day = date.getDate();
    const hours = date.getHours();
    const minutes = date.getMinutes();

    let formattedDate;
    if (hours !== 0 && minutes !== 0) {
        formattedDate = `${month} ${day}, ${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
    } else {
        formattedDate = `${month} ${day}`;
    }
    return formattedDate;
}

