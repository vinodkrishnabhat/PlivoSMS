# Plivo SMS API (Test assignment)

## Getting Started
These instructions will get you a copy of the project up and running for development and testing purposes.

### Prerequisites
* Postgres server 9.6 (with plivo db loaded)
* JDK 1.8
* Apache Ant

### Installing
Follow these steps to get environment up and running.
* Run modifications in database
```
SQL in db/dbChangesV1.sql
```
* Build the project from the code base directory
```
ant -f plivosms.xml all
```
* Navigate to out/artifacts/server
```
cd out/artifacts/server
```
* Change property files to reflect reality
```
Set Postgres properties in config.properties
Set log4j.appender.FILE.File in log4j.properties
```
* Start the server from out/artifacts/server
```
java -cp .:PlivoSMS.jar:lib/* com.vkb.plivosms.RestServer
```

## Running the tests
* Unit tests are already run as part of "all" ant target. Use the "runUnitTests" target to run only Unit tests.
```
ant -f plivosms.xml runUnitTests
```

* Automated tests can be run from the out/artifacts/automationTests folder. Ensure the server has been started as mentioned above. Provide the server URL in the command line appropriately.
```
java -cp PlivoSMS_IT.jar:lib/* com.vkb.plivosmsit.TestAll http://localhost:8080
```

### Overview of the end to end tests
* com.vkb.plivosmsit.TestAll runs all the tests mentioned below 
* com.vkb.plivosmsit.TestInbound runs all the tests for /inbound/sms requests
* com.vkb.plivosmsit.TestOutbound runs all the tests for /outbound/sms requests
* com.vkb.plivosmsit.TestCombined runs tests which require sequencing requests - Testing the STOP functionality