<h2 align="center">ÜK Modul-223 - Co-working Space application</h2>

  <p align="center">
    An application for a co-working space. The application is developed as the final practial assignement of the Modul 223 course at ZLI.:
    <br />
  </p>
<br>
<br>

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

### Starting the database server

To start the database server, you need to ensure that you have the docker-compose file as well as an "init" folder in the same directory. The "init" folder should include an SQL file.
Then, you can start the database server by running the following command in the root directory of the project:

```sh
docker compose up
  ```

### Starting the Springboot application

You can run the springboot application in the IDE of your choice. The application will most likely run on port 8080.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Startup info

The application will start with the following users:
* Peter Rutschmann (Admin) - login: peter.rutschmann@gmail.com - pw: test1234 
* Johannes Höffliger (Member) - login: johannes.höffliger@gmail.com - pw: test1234



<!-- USAGE EXAMPLES -->
## Important Information
### Links
* API Documentation: http://localhost:8080/swagger-ui/index.html

### Database structure
The database was designed with the assumption that the bookable locations are actual office workstations. For that reason the admin will handle any assignement of bookings to specific available Seats. 
Therefore, the database <b>does not feature a Table for the location of bookings. </b>

### Client Demo (Postman)
To be able to make requests to endpoints in a manner, corresponding to how an actual client would. There will be a Postman export in addition to the base Project
that can be used to make requests to the endpoints. The Postman export can be found in the root directory of the project.

