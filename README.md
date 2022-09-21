<h2 align="center">ÜK Modul-223 - CoWorking Space application</h2>

  <p align="center">
    This CoWorking Space project is for module 223
    <br />
  </p>
<br>
<br>

### GitHub
The GitHub repository can be found under https://github.com/Pandouby/m223

### Built With

Major frameworks/libraries used to develop and run a demo of the application:

* [Docker](https://www.docker.com/)
* [Mysql](https://www.mysql.com/)
* [Postman](https://www.postman.com/)
* [Spring Boot](https://spring.io/projects/spring-boot)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

To start this project, you need to have the following tools installed on your machine:
<!-- Docker -->
* [Docker](https://www.docker.com/)
* Java (JDK 17)

### Starting the database server

To start the database server, you need to ensure that you have navigated to the docker directory of the project. If you are located in the docker directory run the command below.

```sh
docker compose up
  ```

### Starting the Springboot application

The Application is located in the m223 directory of the project. You can run the springboot application in the IDE of your choice. The application will most likely run on port 8080.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Startup info

The application will start with the following users:
* Peter Rutschmann (Admin) - login: peter.rutschmann@gmail.com - pw: test1234 
* Johannes Höffliger (Member) - login: johannes.höffliger@gmail.com - pw: test1234

### phpMyAdmin
On the port 8090 you can get acces to the database with the following credentials.
* username: root
* password: 123456

<!-- USAGE EXAMPLES -->
## Important Information
### Links
* API Documentation: http://localhost:8080/swagger-ui/index.html

### Links
The git history can be found in the log.text file in the root direcory of the project.

### Database structure
The database was designed with the assumption that the bookable locations are actual office workstations. For that reason the admin will handle any assignement of bookings to specific available Seats. 
Therefore, the database <b>does not feature a Table for the location of bookings. </b>

### Client Demo (Postman)
To be able to make requests to endpoints in a manner, corresponding to how an actual client would. There will be a Postman export in addition to the base Project
that can be used to make requests to the endpoints. The Postman export can be found in the postman directory of the project. To be able to test the endpoints the user musst first login either with Admin or Member with the coresponding postman call.

