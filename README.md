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
**1. notification-service**

To start the notification-service, locate the folder, run
>mvn spring-boot:run

The server will start at http://localhost:8072

--------
**2. aggregator-service**

>cd aggregator-service 
>
>docker-compose up -d

When running for the first time, go to _aggregator-service/src/main/resources/application.properties_ and uncomment 

>_spring.jpa.hibernate.ddl-auto=create_

keywords.txt contains the list of keywords to track

To start the aggregator-service, locate the folder, run 
>mvn spring-boot:run

The server will start at http://localhost:8071

To debug the application, go to AggregatorServiceApplication > right-click line with the class name > debug

-------
**3. user-service**

>cd user-service 
> 
>docker-compose up -d

When running for the first time, go to _user-service/src/main/resources/application.properties_ and uncomment
>spring.jpa.hibernate.ddl-auto=create

To start the aggregator-service, locate the folder, run
>mvn spring-boot:run 
 
The server will start at http://localhost:8073

----------

**News providers limitations**

Gnews - https://gnews.io/#pricing 100 requests per day/ 10 records per request

NYTimes - https://developer.nytimes.com/docs/most-popular-product/1/overview

BBC - https://newsapi.org/account/manage-subscription/change-plan


