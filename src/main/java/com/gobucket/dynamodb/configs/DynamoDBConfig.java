package com.gobucket.dynamodb.configs;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;



@Configuration
@EnableDynamoDBRepositories
(basePackages = "com.gobucket.dynamodb.repositories")
public class DynamoDBConfig {
	
	@Value("${amazon.dynamodb.endpoint}")
    private String dynamoDbEndpoint;

	@Value("${amazon.dynamodb.region}")
    private String dynamoDbRegion;
	
    @Value("${amazon.aws.accessKey}")
    private String awsAccessKey;

    @Value("${amazon.aws.secretKey}")
    private String awsSecretKey;
	
	//@PostConstruct
//	@Bean
//	public DynamoDBMapper dynamoDBMapper() {
//		DynamoDBMapper objDynamoDBMapper = new DynamoDBMapper(amazonDynamoDB());
//		return objDynamoDBMapper;
//	}

	//@PostConstruct
	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		AmazonDynamoDB dynamodb = AmazonDynamoDBClientBuilder.standard()
									.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(dynamoDbEndpoint, dynamoDbRegion))
									.withCredentials(new AWSStaticCredentialsProvider(amazonAWSCredentials()))
									.build();		
	
		// use the DynamoDB API over HTTP
		 listTables(dynamodb.listTables(), "DynamoDB over HTTP");
		return dynamodb;
	}
	@Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(awsAccessKey, awsSecretKey);
    }
	private void listTables(ListTablesResult result, String method) {
		System.out.println("found " + Integer.toString(result.getTableNames().size()) + " tables with " + method);
		for (String table : result.getTableNames()) {
			System.out.println(table);
		}
	}
}
