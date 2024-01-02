## Choose the News

1. User subscribes to the keywords he/she is interested in.
2. Job fetches the news from the outside news providers, checks every article if it needs to be saved into DB, if yes, saves it and extracts the keywords from the title.
3. Kafka producer sends the keywords, Kafka Broker consumes them and stores in the cache.
4. Job to send emails is triggered 3 times a day to collect all the users who have interest in the keywords in the cache.
5. On getting the email, user clicks the link to the website, where he/she will be shown only the articles he/she has subscribed to.

### Workflow

User-service: accepts user's registration and subscription to the keywords.

Aggregator-service: fetches the news, saves the new ones to db, looks for the keywords, if any is found - it is produced to the Kafka broker.

Notification-service: consumes the event and sends emails to the users who have the subscription to the keyword the event contains. 

Aggregator-service: gets the users request with the JWT token, requests users' keywords, responds with all the articles having the keywords user has subscribed to.

[To Do] Statistics-service: provides reports on keywords, users, articles.

---
### Technology stack

Java 17, Spring Boot 3, Spring Cloud, Spring Family (Batch, Security, Data, Validation, Mail, Test),
Maven, Gradle, Docker, PostgreSQL, FlywayDB, Kafka, Redis, Eureka, Feign Client, Circuit breaker,
Open API, Oauth2.0, Lombok, Mockito, Wiremock, Testcontainers

---
### Prerequisites

JVM 17 should be installed 

Maven 3.9.2 should be installed

---
### 1. Start all the containers

`````
docker-compose -f aggregator-service/docker-compose.yml up
docker-compose -f notification-service/docker-compose.yml up
docker-compose -f user-service/docker-compose.yml up
`````
---
### 2. Run eureka server
The server will start at http://localhost:8761

---
### 3. Run user-service
`mvn spring-boot:run`
The server will start at http://localhost:8073

---
### 4. Run notification-service
`mvn spring-boot:run`
The server will start at http://localhost:8072

---
### 5. Run aggregator-service

When running for the first time, go to _aggregator-service/src/main/resources/application.properties_
and provide values for GNEWS_API_KEY, BBC_API_KEY, and NY_TIMES_API_KEY.
File _keywords.txt_ contains the list of keywords to track

`mvn spring-boot:run`
The server will start at http://localhost:8071

---
### Open API
http://localhost:8071/swagger-ui/index.html

---

### News providers limitations

- Gnews - https://gnews.io/#pricing 100 requests per day/ 10 records per request

- NYTimes - https://developer.nytimes.com/docs/most-popular-product/1/overview

- BBC - https://newsapi.org/account/manage-subscription/change-plan
