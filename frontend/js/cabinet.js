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
    showNews();
}

function showNews() {
    if (getCurrentUserId() == null) {
        logout();
    }
    setPageTitle('News');

    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {

        if (this.readyState === 4) {

            if (this.status === 404) {
                let subHeader =
                    '<br/><h4 class="panel-title">Add one or more keywords of your interest</h4>\n' +
                    '     <button type="button" class="btn btn-info" data-toggle="modal" data-target="#addKeywordModal">Add</button>\n';
                setPageSubtitle(subHeader);

            } else if (this.status === 200) {
                let articles = JSON.parse(this.responseText);
                console.log(articles);
                if (articles === null || articles.length === 0) {
                    clickKeywords();
                } else {
                    let html =
                        '<div class="row">\n' +
                        '<div class="col-md-12">\n' +
                        '<div class="panel panel-white">\n' +
                        '<div class="panel-heading clearfix">\n' +
                        '  <h4 class="panel-title" id="news_table_title"></h4>\n' +
                        '</div>\n' +
                        '<div class="panel-body">\n' +
                        '  <div class="table-responsive">\n' +
                        '   <table class="table" id="news_table">' +
                        '<thead>\n' +
                        '   <tr>\n' +
                        '     <th>Date</th>\n' +
                        '     <th>Title</th>\n' +
                        '     <th>Link</th>\n' +
                        '     <th>Source</th>\n' +
                        '     <th>Content</th>\n' +
                        '   </tr>\n' +
                        ' </thead>\n' +
                        ' <tbody>';
                    for (let i = 0; i < articles.length; i++) {
                        let article = articles[i];
                        console.log(article);
                        // article.title;
                        // article.description;
                        // article.content;
                        // article.url;
                        // article.dateCreated;
                        // article.sourceDto.name;
                        // article.keywordDtos;
                        html = html +
                            '<tr>\n' +
                            '<th scope="row">' + article.dateCreated +'</th>\n' +
                            '<td>' + article.title + '</td>\n' +
                            '<td><a href="' + article.url + '" target="_blank">link</a></td>\n' +
                            '<td>' + article.sourceDto.name + '</td>\n' +
                            '<td>'+ article.content + '</td>\n' +
                            '</tr>';
                    }
                    html = html +
                        '</table>\n' +
                        ' </div>\n' +
                        ' </div>\n' +
                        ' </div>';
                    document.getElementById("news_body").innerHTML = html;
                }

            }
        }
    }

    let getNewsUrl = AGGREGATOR_SERVICE + "/articles";
    xhr.open("GET", getNewsUrl, true);
    addAuthorization(xhr);
    xhr.send();

    return false;
}

function clickKeywords() {
    hideLeftMenu();
    activateCabinet();
    selectMenu('menu_keywords');
    setPageTitle('Keywords');
    setPageSubtitle('');
    clearContent();
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
    removeClassFromElement("news_body", "hidden");
}

function activateAbout() {
    addClassToElement("news_body", "hidden");
    removeClassFromElement("about_body", "hidden");
}

function showKeywords() {
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
            //to do
            // if no keywords are there, show button
            if (this.status === 404) {
                showWarningModal('Something went wrong');
            } else if (this.status === 200) {
                let subscription = JSON.parse(this.responseText);
                console.log(subscription);
                if (subscription === null) {
                    let subHeader =
                        '<br/><h4 class="panel-title">You do not have any subscription. Please add one or more keywords of your interest</h4>\n' +
                        '     <button type="button" class="btn btn-info" data-toggle="modal" data-target="#addKeywordModal">Add</button>\n';
                    setPageSubtitle(subHeader);
                } else {

                }

                let html = '';
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
    $('#addKeywordModal').modal('show');

    $('#addKeywordModal').on('shown.bs.modal', function () {
        $('#book_title').focus();
    })

    document.getElementById("book_title").value = '';
    return false;
}

function closeAddKeywordModal() {
    $('#addKeywordModal').modal('hide');
    if ($('#book_title_group').hasClass('has-error')) {
        $('#book_title_group').removeClass('has-error');
    }
    return false;
}

function saveKeyword() {
    let keyword = document.getElementById("keyword_title").value.trim();
    if (!validateKeyword(keyword)) {
        return false;
    }
    closeAddKeywordModal();

    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 500) {
                closeAddKeywordModal();
                showWarningModal('Keyword ' + keyword + ' cannot be added');
                return false;
            } else if (this.status === 200) {
                let response = JSON.parse(this.responseText);
                showSuccessModal('Keyword ' + keyword + ' has been added');
                showKeywords();
            }
        }
    };

    let requestUrl = USER_SERVICE + "/subscription";
    // let currentUserId = getCurrentUserId();

    const requestBody = {
        "keywordNames": [keyword],
        "timesPerDay": 2
    };
    console.log(requestBody);

    xhr.open("POST", requestUrl);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    addAuthorization(xhr);
    xhr.send(JSON.stringify(requestBody));

    return false;
}