# Installing

Whispir's SDK allows Java Developers to get up and running sending messages with Whispir's API quickly.

Whispir's SDK is built using [Maven](http://maven.apache.org/).  To create the JAR files, simply execute the install maven target (skip tests unless you want to add the static parameters in the tests file first).

```shell
mvn clean install -DskipTests=true
```

This will produce two JAR fies:

```shell
/target/WhispirSDK-1.0.0-with-dependencies.jar
/target/WhispirSDK.jar
```

The JAR with dependencies should be used as a standalone import into any project.

If you are using other Apache HTTP Client libraries, the WhispirSDK.jar may suit your project better, so import that with your other HTTP Client libraries.

# Examples

```java
package com.whispir.sdk.examples;

import com.whispir.sdk.WhispirSDK;
import com.whispir.sdk.exceptions.WhispirSDKException;

public class SDKExample {
  
  public static final String API_KEY = "...";
  public static final String USERNAME = "...";
  public static final String PASSWORD = "...";

  public static void main(String[] args) {
    try {
      
      //INIT SDK object
      WhispirSDK sdk = new WhispirSDK(API_KEY, USERNAME, PASSWORD);
      
      //Send the message
      int status = sdk.sendMessage("61400000000", "This is the subject of my SMS", "This is the content of my SMS");
      
      //Status should be 202 Accepted
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


