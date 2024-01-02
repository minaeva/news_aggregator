function hideLeftMenu() {
    $('#sidebar-toggle-button-close').click();
    return false;
}

function clickHome() {
    hideLeftMenu();
    activateMyBooksMenu();
}

function clickHomeForTheFirstTime() {
    activateMyBooksMenu();
}

function activateMyBooksMenu() {
    activateCabinet();
    selectMenu("menu_home");
    setPageTitle('News');
    setPageSubtitle('');
    clearContent();
    showReadersNews();
}

function clickAllKeywords() {
    hideLeftMenu();
    activateCabinet();
    selectMenu("menu_keywords");
    setPageTitle('Keywords');
    setPageSubtitle('');
    clearContent();

    // showSearchBooksHeader();
}

function clickAbout() {
    hideLeftMenu();
    selectMenu('profile_about_title');
    activateAbout();

    setPageTitle('Choose the News - Workflow');
    setPageSubtitle('');
    clearContent();
}

function activateCabinet() {
    addClassToElement("about_body", "hidden");
    removeClassFromElement("books_readers_body", "hidden");
}


function activateAbout() {
    addClassToElement("books_readers_body", "hidden");
    removeClassFromElement("about_body", "hidden");
}

function openCabinet() {
    showReadersNews();
    clickHomeForTheFirstTime();
}

function showReadersNews() {
    if (getCurrentUserId() == null) {
        logout();
    }
    setPageTitle('News');

    let addBookButton =
        '<div class="row"><div class="col-sm-12"> <button class="btn btn-default right" onclick="openAddBookModal(); return false" data-toggle="modal">' +
        // '<i class="menu-icon icon-books"></i>' +
        '<i class="menu-icon fa fa-book"></i>&nbsp;&nbsp;' +
        '<span id="menu_add_book_title">Add news?</span>' +
        '</button></div> </div>';

    setPageSubtitle(addBookButton);

    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {

        if (this.readyState === 4) {
            if (this.status === 404) {
                let subHeader =
                    '<br/><h4 class="panel-title">' + application_language.goodDayToStartAddingBooks_title + '</h4>\n' +
                    '     <button type="button" class="btn btn-info" data-toggle="modal" data-target="#addBookModal">Add news?</button>\n';
                setPageSubtitle(subHeader);

            } else if (this.status === 200) {
                let articles = JSON.parse(this.responseText);
                let html = '';
                for (let i = 0; i < articles.length; i++) {
                    let article = articles[i];
                    console.log(article);
                    html = html +
                        '<div class="panel panel-default">\n' +
                        '    <div class="panel-heading" role="tab" id="heading' + article.id + '">\n' +
                        '        <h4 class="panel-title">\n' +
                        '            <a data-toggle="collapse" onclick="showBookDetails(' + article.id + ', ' + article.ownerId + '); return false;" data-parent="#accordion" href="#collapse' + article.id + '"\n' +
                        '               aria-expanded="true" aria-controls="collapse' + article.id + '">\n' + article.title +
                        '            <h5 class="text-muted">' + notNull(article.authorName) + ' ' + notNull(article.authorSurname) + '</h5>\n' +
                        '            </a>\n' +
                        '        </h4>\n' +
                        '    </div>\n' +
                        '    <div id="collapse' + article.id + '" class="panel-collapse collapse" role="tabpanel"\n' +
                        '         aria-labelledby="heading' + article.id + '">\n' +
                        '    </div>\n' +
                        '</div>';
                }
                document.getElementById("accordion").innerHTML = html;
            }
        }
    }

    let getOwnNewsUrl = AGGREGATOR_SERVICE + "/articles";
    xhr.open("GET", getOwnNewsUrl, true);
    addAuthorization(xhr);
    xhr.send();

    return false;
}