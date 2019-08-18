# Junk Microservice

Dummy microservice written using the Micronaut framework that supplies Junk-related information and takes reservation requests.


###Setup

- Install Docker

- Start up redis and mongo instances by running `docker run -p 6379:6379 redislabs/rejson:latest`
and `docker run -p 27017:27017 mongo` in the command line _(you can test if these have started by trying `redis-cli ping` and `mongo` in separate command line instances)_

- Clone this repo and open in your IDE of choice (preferably Intellij)

- If using Intellij, go to Preferences -> Build, Execution, Deployment -> Compiler -> Annotation Processors, and ensure that the option "Enable Annotation Processing" is ticked. (If using some other IDE, you'll have to look up a similar setting)

- run `./gradlew build -x test` from the command line while at the root project directory (your IDE might have already done this but never hurts - also, the `-x test` is skipping the test phase, so don't do this when you're testing!)

- navigate to the `mongo-load` file and run the `populate-mongo` script

- go to the Application class and run it

- open a browser and navigate to `http://localhost:8080/health` - you should see `"status":"UP!"`

- next, try `http://localhost:8080/trips?limit=2&harbour=VICTORIA_HARBOUR&date=2019-08-25&type=BOOZE_CRUISE` - you should get back a response object titled Jim's Summer Getaway