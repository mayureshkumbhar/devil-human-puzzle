# Human and Devil Game-Puzzle

This application contains REST APIs for Human and Devil game.
There are 3 devils and 3 Humans. They all have to cross a river in a boat. Boat can only carry two people at a time. As long as there are equal number of devils and Humans,then devils will not eat Human. If the number of devils are greater than the number of Humans on the same side of the river then devils will eat the Humans

## Prerequisites
1. maven 3.3.9
2. oracle-jdk 1.8

# Build
Run "mvn clean package" to build the project. The build artifacts will be stored in the "/target" directory.

# Running unit tests
Run "mvn test" to execute the unit tests and create JaCoCo report under /target/site/jacoco/ directory. 

# Running application
Run "mvn spring-boot:run" or execute jar file under "/target" directory using Java "java -jar target/puzzle-game-0.jar".

# Accessing application
Hit "http://localhost:8080/swagger-ui.html" to get documentation for API usage.
