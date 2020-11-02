# Envoy Proxy Rate Limiter Cucumber
Cucumber integration tests for the Envoy Proxy Rate Limiter

This project comprises 3 features

1.  Limiter Test (limiter-test.feature)
    This feature runs through all the requirements listed in CR-
    It runs through with no cross-over with any other tests in this Feature
    
1.  Roll-Over Test (roll-forward.feature)
    This feature re-runs the above tests except with the clock run into the next hour segment.
    It achieves this by either waiting on the real limiter, or if it's > 15 minutes to the hour it throws Pending Exceptions
    Or if running against the MockClient (export USE_MOCK_CLIENT=TRUE) locally, it rolls the MockClient forward an hour, so these tests can be run.

3. Cross-Over Test (limiter-test-combinations.feature)
    This test runs all of the requirements, but maintains values to the next test(s) in order to test multiple limits together.
    
Mock Client and Mock Limiter

    Mock CLient
    
    The MockClient class in this project replaces the real RateLimiterClient and can be switched on using (export USE_MOCK_CLIENT=TRUE) 
    This enables the developer to "test the tests". It returns actual results based on what the real limiter would provide without having to spin services up.
    
    Mock Limiter
    I have created a branch in the project https://github.com/ONSdigital/census-int-mock-envoy-limiter called Andy Test Branch
    https://github.com/ONSdigital/census-int-mock-envoy-limiter/tree/Andy-Test-Branch
    This project is designed to support RH-Service currently, but I wanted to have a copy of the rate limiter that would actually perform like
    the real one in order to test the client and some of the Cucumber functions here, e.g. Waiting for the next hour segment, etc.
    So this branch can be spun up as a Spring Boot service - mvn spring-boot:run and will respond to client requests on localhost:8081
    It will give responses akin to the real rate limiter client so that tests and waits can be checked, along with any client dependency changes.
    (export USE_MOCK_CLIENT=FALSE) and spin the "rate limiter" up and it will perfom like the live one in your local environment.
    
This project is a Cucumber / Spring boot project and is driven by Maven. 

mvn clean install will cause a maven task to run the glue classes which drive the features
Cucumber then "sees" the Spring boot class in its path and calls the "init()" hook - firing up Spring Boot Test class the first time and keeping it alive
Spring Boot Test creates its beans which are used when the Java Steps are called by the Cucumber engine via the features.
Context Classes have Scope @Scope(SCOPE_CUCUMBER_GLUE) which means they get initialised for every test
The RateLimiterClientProvider is app scoped and lives for the entire run, managing the RateLimiterClient and the MockClient as beans
     


