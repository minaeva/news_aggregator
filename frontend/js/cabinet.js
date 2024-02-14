function hideLeftMenu() {
    $('#sidebar-toggle-button-close').click();
    return false;
}

function clickHome() {
    if (getCurrentUserId() == null) {
        logout();
    }
    selectMenu('menu_home');
    setPageTitle('News');
    hideAllShowOne("news_body");
    showNews();
}

async function showNews() {
    const getNewsUrl = AGGREGATOR_SERVICE + "/articles";

    try {
        const response = await fetch(getNewsUrl, {
            method: "GET",
            headers: {
                'Authorization': 'Bearer ' + getTokenFromLocalStorage(),
                'Accept': 'application/json'
            }
        });

        if (response.status === 404) {
            showWarningModal("Something went wrong");
            console.error(response);

        } else if (response.status === 200) {
            const articles = await response.json();
            console.log(articles);
            if (!articles || articles.length === 0) {
                document.getElementById("noneOfYourKeywordsHitTheNews").classList.remove("hidden");
                clickKeywords();
            } else {
                document.getElementById("noneOfYourKeywordsHitTheNews").classList.add("hidden");
                displayArticles(articles);
            }
        }
    } catch (error) {
        console.error('Error fetching news:', error);
    }
}

function displayArticles(articles) {
    document.getElementById("news_body").innerHTML = `
        <div class="row">
            <div class="col-md-12">
                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table" id="news_table">
                            <tbody>
                                ${articles.map(article => `
                                    <tr>
                                        <td><a class="article-link" href="${sanitizeHTML(article.url)}" target="_blank">${sanitizeHTML(article.title)}</a>
                                        <p class="article-date">${sanitizeHTML(formatDate(article.dateCreated))}, ${sanitizeHTML(article.sourceDto.name)}</p>
                                        <p>${sanitizeHTML(article.content)}</p></td>
                                    </tr>
                                `).join('')}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function clickKeywords() {
    // hideLeftMenu();
    selectMenu('menu_keywords');
    setPageTitle('Keywords');
    hideAllShowOne("keywords_body");
    fetchAndSetKeywords();
}

function clickAbout() {
    // hideLeftMenu();
    selectMenu('menu_about');
    setPageTitle('About');
    hideAllShowOne("about_body");
}

function fetchAndSetKeywords() {
    let getSubscriptionUrl = USER_SERVICE + "/subscription/jwt";
    const tokenString = getTokenFromLocalStorage();

    fetch(getSubscriptionUrl, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + tokenString,
            'Accept': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            const keywords = data.keywordNames.join(',');
            console.log(keywords);
            $('.panel-body #keywordsInput').tagsinput('removeAll');
            data.keywordNames.sort().forEach(keyword => {
                $('.panel-body #keywordsInput').tagsinput('add', keyword);
            });
        })
        .catch(error => console.error('Error:', error));
}

function handleSaveClick() {
    const inputField = document.getElementById('keywordsInput');
    const keywords = inputField.value.split(',').map(keyword => keyword.trim());
    if (keywords.length === 0 || keywords.every(keyword => keyword === '')) {
        showWarningModal('Please enter at least one keyword.');
        return;
    }

    let postSubscriptionUrl = USER_SERVICE + "/subscription";
    const tokenString = getTokenFromLocalStorage();

    fetch(postSubscriptionUrl, {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + tokenString,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            "keywordNames": keywords,
            "timesPerDay": 2
        })
    })
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);
            fetchAndSetKeywords();
        })
        .catch(error => console.error('Error:', error));

    clickHome();
}
