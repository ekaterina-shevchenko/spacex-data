### Usage
The project runs the Tomcat web server that serves requests to the URLs `/launches`, `/rockets`, and `/ships` and provides some statistical information on SpaceX launches, rockets and ships correspondingly. 

The information is obtained via API calls to SpaceX-API-v4 (https://github.com/r-spacex/SpaceX-API/tree/master/docs/v4).

Follow the instructions below to build and run the project, and enjoy the functionality it provides by sending requests to the web server using a REST client of your choice. For better visualization, you can use a web browser.

### Build
```sh
mvn clean package
```

### Run
```sh
cd target
java -jar spacex-data-1.0.jar
```