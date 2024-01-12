# Microservices for Clients and Credit Cards

## Description
Development of a microservices architecture using Spring Boot and Spring Cloud.

## Project Architecture
Separate APIs were built that communicate with each other synchronously through OpenFeign and asynchronously using RabbitMQ. There are 5 independent microservices:
- **Microservices-msclients**: Manages the creation and registration of clients.
- **Microservices-mscards**: Manages the creation and registration of credit cards.
- **Microservices-mscreditevaluator**: Evaluates the client's situation and assigns available credit cards.
- **Microservices-EurekaServer**: Maintains the instances of different microservices.
- **Microservices-mscloudgateway**: Manages and distributes the requests.


<img src="Screenshot_20231215-213035_Samsung%20Internet.jpg" width="500"> <!-- Altere o valor de width para o tamanho desejado -->


## Technologies Used
- **Spring Cloud**: For building some of the most common patterns in distributed systems.
- **Service Discovery**: Using Eureka Server for locating services for load balancing and failover.
- **API Gateway**: Managing and routing requests.
- **Spring Cloud Open Feign**: For synchronous communication between microservices.
- **Messaging Services with RabbitMQ**: For asynchronous communication.
- **Authorization Server with Keycloak**: For secure authentication and authorization.
- **Docker**: Building images and creating containers for deployment.
