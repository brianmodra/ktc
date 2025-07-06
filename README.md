# KTC

## URL handling

The mechanism for handling HTTP requests in an API service can be described as
a knowledge graph where the SPO (Subject, Predicate, Object) tuples can
be broken down using the following example:

method - GET/POST./etc to path ```/path/{with_bound_path}?query=value&parameter=values```
with an authorised user, and headers, e.g. ```"Header-context: values"```
handled by a controller and handler functions (one per method qualified by query parameters)

- Each endpoint (URL path) as an instance of api:Endpoint
- Each method (GET, POST, etc.) as an instance of api:Method
- Each handler as a resource (URI or function identifier)
- Each controller as a class or instance managing a set of handlers

Example of the associated ontology:

@prefix api: <http://ktc.com/api#> .
@prefix http: <http://www.w3.org/2011/http#> .
@prefix ktc: <http://ktc.com/resource#> .

ktc:Endpoint1 a api:Endpoint ;
    api:path "/path/{with_bound_path}" ;
    api:handledBy ktc:Controller1 ;
    api:requiresAuthentication true .

ktc:Controller1 a api:Controller ;
    api:hasMethod ktc:Method_GET .

ktc:Method_GET a api:Method ;
    api:httpMethod "GET" ;
    api:hasQueryParam "query=value" , "parameter=values" ;
    api:handledBy ktc:HandlerFunction1 ;
    api:hasHeader [ api:headerName "Header-context" ; api:headerValue "values" ] .

ktc:HandlerFunction1 a api:Handler ;
    api:functionName "getItems" .
