## Updates 29/06/2015

### Released to Maven Central

Installation steps updated to reference Maven dependency instead of building from source.

## Updates 26/06/2015 - Release 2.2.0

### Added the following:

* Added getSingleMessage and getSingleWorkspace functions
* Refactored some of the tests to make them run when no Test Workspace has been defined

## Updates 7/06/2015 - Release 2.1.0

### Added the following:

* Support for Basic Proxy Authentication (thanks to a pull request by vlsinitsyn)
* Support for Creating Scenarios
* Support for Creating Workspaces

### Refactored:

* I changed the default response type to WhispirResponse from int for POST requests.  This was because I needed to get the header information for managing Scenarios and it's just a generally cleaner approach.

## Bugs/Known Issues:

* Currently, you can't create a Scenario in the default workspace, you need to specify a workspace ID.  This is due to a bug in Whispir that will be fixed in an upcoming release.
* After creating workspaces via the API, a login to Whispir is required. This rebuilds the cache and allows the workspace to be used. A subseqent release of Whispir will resolve this issue.

Let me know if you've got any questions: @jordwalsh

# Installing with Maven

Users can easily include the Whispir dependency in their projects using [Maven](http://maven.apache.org/).

The dependency information is as follows:

```xml
<dependency>
    <groupId>com.whispir</groupId>
    <artifactId>sdk</artifactId>
    <version>2.2.0</version>
</dependency>
```
See the examples section below for usage instructions.

# Building/Installing the SDK from Source

Whispir's SDK allows Java Developers to get up and running sending messages with Whispir's API quickly.

Whispir's SDK is built using [Maven](http://maven.apache.org/).  To create the JAR files, simply execute the install maven target (skip tests unless you want to add the static parameters in the tests file first).

```shell
mvn clean install -DskipTests=true
```

This will produce two JAR fies:

```shell
/target/WhispirSDK-2.1.0-with-dependencies.jar
/target/WhispirSDK.jar
```

The JAR with dependencies should be used as a standalone import into any project.

If you are using other Apache HTTP Client libraries, the WhispirSDK.jar may suit your project better, so import that with your other HTTP Client libraries.

# Examples

The API can perform three activities at the moment:

- Sending messages
- Retrieving Workspaces
- Sending scenarios

These are described below:

```java
package com.whispir.sdk.examples;

import java.util.Map;
import java.util.TreeMap;

import com.whispir.sdk.WhispirResponse;
import com.whispir.sdk.WhispirSDK;
import com.whispir.sdk.exceptions.WhispirSDKException;

public class SDKExample {

  //APIKEY, Username and Password available from whispir.io
  public static final String API_KEY = "...";
  public static final String USERNAME = "...";
  public static final String PASSWORD = "...";

  public static void main(String[] args) {
    try {

      // INIT API object
      WhispirSDK sdk = new WhispirSDK(API_KEY, USERNAME, PASSWORD);

      // Send the message
      WhispirResponse response = sdk.sendMessage("61400000000",
          "This is the subject of my SMS",
          "This is the content of my SMS");

      // Status should be 202 Accepted
      System.out.println("Status: " + response.getStatusCode());

      //------------------------------------------------------
      
      // Retrieve a list of workspaces
      WhispirResponse workspaceResponse = sdk.getWorkspaces();

      TreeMap<String, String> workspaces = (TreeMap<String, String>) workspaceResponse
          .getResponse();

      // Iterate through the workspaces, printing the names and IDs out
      for (Map.Entry<String, String> entry : workspaces.entrySet()) {
        String workspaceName = entry.getKey();
        String workspaceId = entry.getValue();

        System.out.println(workspaceName + " => " + workspaceId);
      }
      
      //------------------------------------------------------
      
      //Retrieve a list of scenarios
      
      WhispirResponse scenariosResponse = sdk.getScenarios();
      
      TreeMap<String, String> scenarios = (TreeMap<String, String>) scenariosResponse
          .getResponse();

      // Iterate through the workspaces, printing the names and IDs out
      for (Map.Entry<String, String> entry : scenarios.entrySet()) {
        String scenarioName = entry.getKey();
        String scenarioId = entry.getValue();

        System.out.println(scenarioName + " => " + scenarioId);
      }
      
      //execute the scenario
      
      String scenarioId = "...";
      String workspaceId = "...";
      
      status = sdk.sendScenario(workspaceId, scenarioId);
      
      // Status should be 202 Accepted
      System.out.println("Status: " + status);

    } catch (WhispirSDKException e) {
      e.printStackTrace();
    }
  }
}

```

# Support

This SDK is provided for use within your projects to simplify the use of Whispir's API. 

The support of this code (and any potential bugs within) is not included under the SLA or contractual obligations within customers agreements with Whispir.

Whispir will provide a best effort to ensure this repository is bug free.

Please submit any issues via GitHub, or alternatively, fix the code and submit a pull request.

# Terms and Conditions

See the LICENSE.txt and refer to [Whispir's Terms of Service](http://whispir.com/terms-of-service).


