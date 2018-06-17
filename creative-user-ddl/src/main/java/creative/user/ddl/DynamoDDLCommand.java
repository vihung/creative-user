/**
 *
 */
package creative.user.ddl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

/**
 * @author vihung
 *
 */
public abstract class DynamoDDLCommand {
  // Logger for this class
  private static final Log log = LogFactory.getLog(DynamoDDLCommand.class);

  protected AmazonDynamoDB client;

  private final String CONFIG_FILE = "/DynamoDBConfig.properties";
  protected boolean initialised = false;

  /**  */
  public DynamoDDLCommand() {
    super();
  }

  public final boolean clean() {
    boolean success;
    try {
      preClean();
      doClean();
      postClean();

      success = true;
    } catch (final Exception e) {
      log.fatal(e);
      success = false;
    }
    return success;
  }

  private AWSCredentialsProvider createAmazonAWSCredentialsProvider(final String amazonAWSAccessKey,
      final String amazonAWSSecretKey) {
    final AWSCredentialsProvider provider = new AWSCredentialsProvider() {
      private AWSCredentials mCredentials = new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);

      public AWSCredentials getCredentials() {
        return mCredentials;

      }

      public void refresh() {
        mCredentials = new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
      }
    };
    return provider;
  }

  private AmazonDynamoDB createAmazonDynamoDB(final String amazonDynamoDBEndpoint, final String amazonAWSAccessKey,
      final String amazonAWSSecretKey) {
    final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
        .withCredentials(createAmazonAWSCredentialsProvider(amazonAWSAccessKey, amazonAWSSecretKey))
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, Regions.DEFAULT_REGION.getName()))
        .build();

    return client;
  }

  protected abstract void doClean();

  protected abstract void doRun();

  public abstract String getName();

  public void init() throws IOException {
    final Properties dynamoDBConfig = loadDynamoDBConfig();

    final String amazonAWSAccessKey = dynamoDBConfig.getProperty("amazonAWSAccessKey");
    final String amazonAWSSecretKey = dynamoDBConfig.getProperty("amazonAWSSecretKey");
    final String amazonDynamoDBEndpoint = dynamoDBConfig.getProperty("amazonDynamoDBEndpoint");

    client = createAmazonDynamoDB(amazonDynamoDBEndpoint, amazonAWSAccessKey, amazonAWSSecretKey);
    initialised = true;
  }

  private Properties loadDynamoDBConfig() throws IOException {
    final Properties dynamoDBConfig = new Properties();
    final InputStream stream = getClass().getResourceAsStream(CONFIG_FILE);
    dynamoDBConfig.load(stream);
    return dynamoDBConfig;
  }

  protected void postClean() {
  }

  private final void postRun() {
  }

  private final void preClean() throws IOException {
    if (!initialised) {
      init();
    }
  }

  protected void preRun() throws IOException {
    if (!initialised) {
      init();
    }

  }

  public final boolean run() {
    boolean success;
    try {
      preRun();
      doRun();
      postRun();

      success = true;
    } catch (final IOException e) {
      log.fatal(e);
      success = false;
    }
    return success;
  }

}
