# iRODS Java HTTP API Wrapper

Java client library designed and developed to wrap the new iRODS HTTP API. It provides java classes to cleanly interact 
with all the functionality of the HTTP API and comes with tests and packaging.

## Usage Example 
The following example demonstrates how to create and remove a collection.

```java
String baseUrl = "http://<host>:<port>/irods-http-api/<version>";

// Create an instance of the IrodsHttpClient with the specified base URL.
IrodsHttpClient client = new IrodsHttpClient(baseUrl);

// All methods return a `Response` object, which holds the HTTP status code and the 
// body of the response (as JSON). Can be retrieved by using `.getHttpStatusCode()` 
// and `.getBody()`, respectively. You can parse the JSON response body using your
// preferred JSON parsing library or method.

// Authenticate a user. An authentication request has a response body containing the 
// bearer token that can be used to execute operations as the authenticated user.
Response res = client.authenticate(<username>, <password>);
String bearerToken = res.getBody();

// All methods can be called using a method chaining approach. On the client object, 
// you chain the endpoint name  (e.g. collections), and then chain the operation name
// (e.g. createCollection).

// We will create a collection. We don't want to use the createIntermediates field, 
// so we pass it an empty OptionalInt.
String logicalPath = "/tempZone/home/<username/testCollection";
res = client.collections().create(bearerToken, logicalPath, OptionalInt.empty());
if (res.getHttpStatusCode() != 200) {
    // Handle HTTP error.
    System.err.println("Request failed. Unable to create collection. " +
                       "HTTP Status Code: " + res.getHttpStatusCode());
} else {
    // Retrieving request was successful so response body will not be null.
    // Using Jackson to parse the JSON response.
    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode = null;
    try {
        rootNode = mapper.readTree(res.getBody());
        boolean created = rootNode.path("created").asBoolean();
        System.out.println("Created: " + rootNode.path("created").asBoolean());
    } catch (JsonProcessingException e) {
        // Handle exception.
    }
}

// Remove the collection. We want to permanently remove the collection, so we will 
// set the noTrash field. Since remove() has more than one optional field, we will 
// need to instantiate a class for that optional field.
CollectionsRemoveParams removeParams = new CollectionsRemoveParams();
removeParams.setNoTrash(1);
client.collections().remove(bearerToken, logicalPath, removeParams);
```