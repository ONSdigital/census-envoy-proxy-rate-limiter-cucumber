# Envoy Proxy Rate Limiter Cucumber
Cucumber integration tests for the Envoy Proxy Rate Limiter


This project comprises 3 features

1.  Limiter Test (limiter-test.feature)
    This feature runs through all the requirements listed in CR-1324
    It runs through with no cross-over with any other tests in this Feature
    
1.  Roll-Over Test (roll-forward.feature)
    This feature re-runs the above tests except with the clock run into the next hour segment.
    It achieves this by either waiting on the real limiter, or if it's > 15 minutes to the hour it throws Pending Exceptions
    Or if running against the MockClient (export mock_client=true) locally, it rolls the MockClient forward an hour, so these tests can be run.

3. Cross-Over Tests 
        
        - CONTINUATION-POST.feature
        - LARGE-PRINT-POST-HH.feature
        - LARGE-PRINT-POST-SPG.feature
        - QUESTIONNAIRE-POST-HH.feature
        - QUESTIONNAIRE-POST-SPG.feature
        - UAC-POST-CE.feature
        - UAC-POST-HH.feature
        - UAC-POST-SPG.feature
        - UAC-SMS-CE.feature
        - UAC-SMS-HH.feature
        - UAC-SMS-SPG.feature
        
    These tests test multiple rates together in a combination of tests. So limits run over into the next scenario with an Outline and feature
    
Mock Client and Mock Limiter

   Mock CLient
    The MockClient class in this project replaces the real RateLimiterClient and can be switched on using (export mock_client=true) 
    This enables the developer to "test the tests". It returns actual results based on what the real limiter would provide without having to spin services up.
    The MockClient class calls a class called MockLimiter, which does the number crunching and returns a response to the client 
    Posts to this are held inside hashmaps with a date/time stamp of day+hour. There's a method which allows the dev to roll it forward to the next hour.
    It actually does this by pushing all held transactions back an hour giving the effect of aging them. 
    
  Mock Limiter
    the project census-int-mock-envoy-limiter will contain the MockLimiter class and will be called by the MockClient class
    
  MockLimiter stand alone
    the project census-int-mock-envoy-limiter can be run stand alone by setting the env var emulate-limiter=true and spinning it up.
    before running the cucumber set export mock_client=false and it will use the RateLimiterClient to call the limiter emulator on port 8181. 
    
This project is a Cucumber / Spring boot project and is driven by Maven. 

mvn clean install will cause a maven task to run the glue classes which drive the features
    Cucumber then "sees" the Spring boot class in its path and calls the "init()" hook - firing up Spring Boot Test class the first time and keeping it alive
    Spring Boot Test creates its beans which are used when the Java Steps are called by the Cucumber engine via the features.
    Context Classes can have Scope @Scope(SCOPE_CUCUMBER_GLUE) which means they get initialised for every test

   RateLimiterClientConfig delivers a TestClient for use - either the RateLimiterClient for use with an external limiter or MockClient which interactes 
   with the MockLimiter. This depends on the mock_client env var 
   
Added Webform test that checks for 100 requests per IP address in an hour
Added Webform endpoint to MockClient and MockLimiter   

Added tests for large print fulfilments

     


