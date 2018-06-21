package creative;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

@Configuration
public class DynamoDBConfig {

    @Value("${AWS_ACCESS_KEY_ID}")
    private String amazonAWSAccessKey;

    @Value("${AWS_SECRET_ACCESS_KEY}")
    private String amazonAWSSecretKey;

    @Value("${AWS_DYNAMODB_ENDPOINT}")
    private String amazonDynamoDBEndpoint;

    @Value("${AWS_REGION}")
    private String amazonRegion; // = Regions.DEFAULT_REGION.getName();

    @Bean
    public AWSCredentialsProvider amazonAWSCredentialsProvider() {
        final AWSCredentialsProvider provider = new AWSCredentialsProvider() {
            private AWSCredentials mCredentials = new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);

            @Override
            public AWSCredentials getCredentials() {
                return mCredentials;

            }

            @Override
            public void refresh() {
                mCredentials = new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
            }
        };
        return provider;
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(amazonAWSCredentialsProvider())
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, amazonRegion)).build();

        return client;
    }
}