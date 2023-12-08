**Thesis**

Application helps to subscribe to the keywords one is interested in, and to get an email notification as many times per day as requested as soon as a news containing a keyword in a title is posted.


**Workflow**

User: registers, subscribes to a keyword/set of keywords.

News-aggregator: fetches the news, saves the new ones to db, looks for the keywords, if any is found - it is produced to the Kafka broker.

Notification: consumes the event and sends emails to the users who have the subscription to the keyword the event contains.

-------
**Prerequisites**

JVM 17 should be installed 

Maven 3.9.2 should be installed

-------
**1. Start all the containers**
`````
docker-compose -f aggregator-service/docker-compose.yml up
docker-compose -f notification-service/docker-compose.yml up
docker-compose -f user-service/docker-compose.yml up
`````
-------
**2. Run eureka server**
The server will start at http://localhost:8761
-------
**3. Run user-service**
`mvn spring-boot:run`
The server will start at http://localhost:8073
-------
**4. Run notification-service**
`mvn spring-boot:run`
The server will start at http://localhost:8072
--------
**5. Run aggregator-service**

When running for the first time, go to _aggregator-service/src/main/resources/application.properties_
and provide values for GNEWS_API_KEY, BBC_API_KEY, and NY_TIMES_API_KEY.
File _keywords.txt_ contains the list of keywords to track

`mvn spring-boot:run`
The server will start at http://localhost:8071

Open API:
http://localhost:8071/swagger-ui/index.html

-----

**News providers limitations**

Gnews - https://gnews.io/#pricing 100 requests per day/ 10 records per request

NYTimes - https://developer.nytimes.com/docs/most-popular-product/1/overview

BBC - https://newsapi.org/account/manage-subscription/change-plan
