# spring-myphotos
 
(Work in progress)

This is non-SPA/non-PWA version of "myphotos" project. This project uses Thymeleaf as a template engine for Spring MVC.

## Relationship to my other project on GitHub

[react-myphotos(frontend)](https://github.com/araobp/react-myphotos) --- REST API --- [express-myphotos(backend)](https://github.com/araobp/express-myphotos) --- Postgres SQL --- spring-myphotos(classical web app)

## Set up

Add "application.properties" file with the following properties:

```
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql:<... URL of your Postgres SQL database ...>
spring.datasource.username=vwbnmnunwiuopj
spring.datasource.password=8c06dcbffddd7cf0fd5e73496fea1ec20884885869c13298951f702d610d1a21
```
