**Thesis**
Application helps the user to get notification as soon as news with a keyword in a title is posted.

**Workflow**
User: registers, subscribes to a keyword.
News-aggregator: fetches the news, saves the new ones to db, looks for the keywords, if any is found, it is produced to Kafka broker
Notification: consumes event and sends email to the users with the subscriptions to the keyword the event contains

**To run/debug**
keywords.txt contains the list of keywords to track
first run - uncomment spring.jpa.hibernate.ddl-auto=create
mvn spring-boot:run
or AggregatorServiceApplication => run/debug

**News providers limitations**
Gnews - https://gnews.io/#pricing 100 requests per day/ 10 records per request
NYTimes - https://developer.nytimes.com/docs/most-popular-product/1/overview
BBC - https://newsapi.org/account/manage-subscription/change-plan


