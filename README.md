**Thesis**

Application helps the user to subscribe to the keywords he is interested in, and to get notification as soon as news with a keyword in a title is posted.


**Workflow**

User: registers, subscribes to a keyword/set of keywords
News-aggregator: fetches the news, saves the new ones to db, looks for the keywords, if any is found - it is produced to the Kafka broker
Notification: consumes the event and sends emails to the users who have the subscription to the keyword the event contains

**To run/debug**

JVM 17 should be installed 

Maven should be installed

--------
**aggregator-service**

When running for the first time, go to 
aggregator-service/src/main/resources/application.properties

and uncomment 

_spring.jpa.hibernate.ddl-auto=create_

keywords.txt contains the list of keywords to track

To start Kafka,  
cd aggregator-service
docker-compose up -d


To start the aggregator-service, locate the folder, run 
_mvn spring-boot:run_

The server will start at http://localhost:8071/aggregator

To debug the application, go to AggregatorServiceApplication > right-click line with the class name > debug

spring.application.name=aggregator
server.port=8071

spring.application.name=notificator
server.port=8072

spring.application.name=user
server.port=8073
----------

**News providers limitations**

Gnews - https://gnews.io/#pricing 100 requests per day/ 10 records per request

NYTimes - https://developer.nytimes.com/docs/most-popular-product/1/overview

BBC - https://newsapi.org/account/manage-subscription/change-plan


