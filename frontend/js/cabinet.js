function hideLeftMenu() {
    $('#sidebar-toggle-button-close').click();
    return false;
}

function clickHome() {
    hideLeftMenu();
    activateHomeMenu();
}

function activateHomeMenu() {
    activateCabinet();
    selectMenu('menu_home');
    setPageTitle('News');
    setPageSubtitle('');
    clearContent();
    showReadersKeywords();
}

function clickAllKeywords() {
    hideLeftMenu();
    activateCabinet();
    selectMenu('menu_keywords');
    setPageTitle('Keywords');
    setPageSubtitle('');
    clearContent();

    // showSearchBooksHeader();
}

function clickAbout() {
    hideLeftMenu();
    selectMenu('menu_about');
    setPageTitle('About');
    setPageSubtitle('');
    activateAbout();
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
    showReadersKeywords();
    activateHomeMenu();
}

function showReadersKeywords() {
    if (getCurrentUserId() == null) {
        logout();
    }
    setPageTitle('News');

    let addKeywordsButton =
        '<div class="row"><div class="col-sm-12"> <button class="btn btn-default right" onclick="openAddKeywordModal(); return false" data-toggle="modal">' +
        // '<i class="menu-icon icon-books"></i>' +
        '<i class="menu-icon fa fa-book"></i>&nbsp;&nbsp;' +
        '<span id="menu_add_keyword_title">Add keywords</span>' +
        '</button></div> </div>';

    setPageSubtitle(addKeywordsButton);

    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {

        if (this.readyState === 4) {
            if (this.status === 404) {
                let subHeader =
                    '<br/><h4 class="panel-title">Add one or more keywords you want to read the news</h4>\n' +
                    '     <button type="button" class="btn btn-info" data-toggle="modal" data-target="#addBookModal">Add</button>\n';
                setPageSubtitle(subHeader);

            } else if (this.status === 200) {
                // let articles = JSON.parse(this.responseText);
                // let html = '';
                // for (let i = 0; i < articles.length; i++) {
                //     let article = articles[i];
                //     console.log(article);
                //     html = html +
                //         '<div class="panel panel-default">\n' +
                //         '    <div class="panel-heading" role="tab" id="heading' + article.id + '">\n' +
                //         '        <h4 class="panel-title">\n' +
                //         '            <a data-toggle="collapse" onclick="showBookDetails(' + article.id + ', ' + article.ownerId + '); return false;" data-parent="#accordion" href="#collapse' + article.id + '"\n' +
                //         '               aria-expanded="true" aria-controls="collapse' + article.id + '">\n' + article.title +
                //         '            <h5 class="text-muted">' + notNull(article.authorName) + ' ' + notNull(article.authorSurname) + '</h5>\n' +
                //         '            </a>\n' +
                //         '        </h4>\n' +
                //         '    </div>\n' +
                //         '    <div id="collapse' + article.id + '" class="panel-collapse collapse" role="tabpanel"\n' +
                //         '         aria-labelledby="heading' + article.id + '">\n' +
                //         '    </div>\n' +
                //         '</div>';
                // }
                // document.getElementById("accordion").innerHTML = html;
                document.getElementById("accordion").innerHTML = 'HELLO';
            }
        }
    }

    let getSubscriptionUrl = USER_SERVICE + "/subscription/jwt";
    xhr.open("GET", getSubscriptionUrl, true);
    addAuthorization(xhr);
    xhr.send();

    return false;
}


function openAddKeywordModal() {
    $('#addBookModal').modal('show');

    $('#addBookModal').on('shown.bs.modal', function () {
        $('#book_title').focus();
    })

    document.getElementById("book_title").value = '';    return false;
}

function closeAddKeywordModal() {
    $('#addBookModal').modal('hide');
    if ($('#book_title_group').hasClass('has-error')) {
        $('#book_title_group').removeClass('has-error');
    }
    return false;
}

function saveKeyword() {

    let book_title = document.getElementById("book_title").value.trim();
    if (!validateKeyword(book_title)) {
        return false;
    }

    closeAddKeywordModal();

    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 500) {
                closeAddKeywordModal();
                showWarningModal('Keyword ' + book_title + ' cannot be added');
                return false;
            } else if (this.status === 200) {
                let response = JSON.parse(this.responseText);
                let bookId = response.id;
                showSuccessModal('Keyword ' + book_title + ' has been added');

                showReadersKeywords();
            }
        }
    };

    let requestUrl = USER_SERVICE + "/subscription";
    // let currentUserId = getCurrentUserId();

    const requestBody = {
        "keywordNames": [book_title],
        "timesPerDay": 2
    };
    console.log(requestBody);

    xhr.open("POST", requestUrl);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    addAuthorization(xhr);
    xhr.send(JSON.stringify(requestBody));

    return false;
}