# MerchantHttpFeeders

This is a sample to demonstrate loading of data from an RSS feed (which is an xml feed) to dynamodb configured in AWS and then provide the user with the option to view the data. The ideal way of running this would be to deploy this in EC2.

This project has been deployed in AWS using EBS. 

Note: The free tier is only available if we use the server port running at 5000. The change has been added to the application.properties

Following are the exposed APIs.
<pre>
1. http://httpfeederonaws-env-1.eqpuucbxsd.us-east-2.elasticbeanstalk.com/gobasket/feeds/{action}/merchant1 
  1a. action=load   [loads data from an rss feed]
    usage: http://httpfeederonaws-env-1.eqpuucbxsd.us-east-2.elasticbeanstalk.com/gobasket/feeds/load/merchant1
  1b. action=get    [retrieves all the coupons downloaded from the rss feed]
    usage: http://httpfeederonaws-env-1.eqpuucbxsd.us-east-2.elasticbeanstalk.com/gobasket/feeds/get/merchant1
  1c. action=count  [retrives the count of the coupons in the feed]
    usage: http://httpfeederonaws-env-1.eqpuucbxsd.us-east-2.elasticbeanstalk.com/gobasket/feeds/count/merchant1
  1d. action=delete  [deletes all the coupons loaded by the feed]
    usage: http://httpfeederonaws-env-1.eqpuucbxsd.us-east-2.elasticbeanstalk.com/gobasket/feeds/count/merchant1
    
2. http://httpfeederonaws-env-1.eqpuucbxsd.us-east-2.elasticbeanstalk.com/gobasket/coupons/id/{couponId}: 
    usage: http://httpfeederonaws-env-1.eqpuucbxsd.us-east-2.elasticbeanstalk.com/gobasket/coupons/id/20882185
    
3. http://httpfeederonaws-env-1.eqpuucbxsd.us-east-2.elasticbeanstalk.com/gobasket/coupons/brand/{retrieveDataWithBrandNameContainingThisSubString}
    usage: http://httpfeederonaws-env-1.eqpuucbxsd.us-east-2.elasticbeanstalk.com/gobasket/coupons/brand/Garde

Required Software:
    JDK1.8
    Maven
    
Deploy Instructions:
    Log in to AWS. 
    In the IAM view, create a group with admin access. create a user and add to the group.
    This will generate the credentials (Access Key, Secret Key)
    This needs to be used in the application.properties file for the value of access key / secret key.
    
    Run maven->install to get the jar file. This jar file is to be uploaded in the java mode in EBS to get the application up and running.

</pre>
