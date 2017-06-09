# lucene-services
RESTful services for searching Apache Lucene indexes.

## Dependencies:
* [JDK 1.8.x](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* [Maven 3.x](https://maven.apache.org/index.html)
* [Lucene 5.5.x](https://lucene.apache.org/core/5_5_0/) for Lucene Index
* Java IDE, [Spring Tool Suite](https://spring.io/tools) is heavily recommended for best Spring integration

## Setup:

1) Import the project into an IDE as "Existing Maven Project"

2) Create an application.properties file in the config folder with your Lucene index location and port number. Refer to [application.properties.template](config/application.properties.template)

3) Run -> mvn clean package

4) The build should run successfully and generate a runnable jar in the target folder. This can be run via terminal, or in Spring Tool Suite click Run As "Spring Boot App"

## Using Services
The services may be accessed via HTTP requests. They return data in JSON format.

### Lucene query for Index records
* Type: GET
* Path: /search?query=<Lucene_Query>&count=<150|all>
* Lucene Terms: username,text
* Example Lucene Query: username:jackie AND text:"i am" AND NOT text:RT  
* Details on Lucene Query syntax can be found [here](https://lucene.apache.org/core/2_9_4/queryparsersyntax.html).
