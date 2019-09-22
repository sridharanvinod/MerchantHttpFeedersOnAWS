# MerchantHttpFeeders

This is a sample to demonstrate loading of data from an RSS feed (which is an xml feed) to dynamodb and then provide the user with the option to view the data.

Following are the exposed APIs.
<pre>
1. http://localhost:8080/gobasket/feeds/{action}/merchant1 
  1a. action=load   [loads data from an rss feed]
    usage: http://localhost:8080/gobasket/feeds/load/merchant1
  1b. action=get    [retrieves all the coupons downloaded from the rss feed]
    usage: http://localhost:8080/gobasket/feeds/get/merchant1
  1c. action=count  [retrives the count of the coupons in the feed]
    usage: http://localhost:8080/gobasket/feeds/count/merchant1
  1d. action=delete  [deletes all the coupons loaded by the feed]
    usage: http://localhost:8080/gobasket/feeds/count/merchant1
    
2. http://localhost:8080/gobasket/coupons/id/{couponId}: 
    usage: http://localhost:8080/gobasket/coupons/id/20882185
    
3. http://localhost:8080/gobasket/coupons/brand/{retrieveDataWithBrandNameContainingThisSubString}
    usage: http://localhost:8080/gobasket/coupons/brand/Garde

Required Software:
    JDK1.8
    Maven
    
Deploy Instructions:
    Download code. 
    command to create executable: [maven clean install]
    
    The generated jar file is in the target directory.
    Command to run application: [java -jar HttpFeeder-0.0.1-SNAPSHOT.jar]

</pre>
