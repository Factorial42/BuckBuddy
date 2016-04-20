# BuckBuddy

## Pre-requisites
1. Install RethinkDB 2.3.0 from https://rethinkdb.com/docs/install/
2. Install Java 8
3. Install maven 3

## Set Up
1. start rethinkdb on a terminal and create database & tables on your local installation of RethinkDB by running the commands listed in https://github.com/Factorial42/BuckBuddy/blob/master/config/datastore/schema under # create database and # create tables
2. compile core project and install in local maven repo:

   ```   
   cd core
   mvn clean install
   ```
3. compile all API micro services

   ```   
   cd ../api/
   mvn clean package
   ```
4. run user micro service

   ```   
   cd user
   java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar target/user-1.0.jar
   ```
   Now user Spark-Java app will be listening on port 4567 for API calls and 8000 for remote debug connectors
5. open another terminal for running campaign micro service

   ```   
   cd ../campaign
   java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8001,suspend=n -jar target/campaign-1.0.jar
   ```
   Now campaign Spark-Java app will be listening on port 4568 for API calls and 8001 for remote debug connectors
   
