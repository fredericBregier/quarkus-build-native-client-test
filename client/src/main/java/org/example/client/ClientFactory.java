package org.example.client;

import java.net.URI;
import java.util.NoSuchElementException;

import io.quarkus.arc.Unremovable;
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

@ApplicationScoped
@Unremovable
public class ClientFactory {
  public static final String DEFAULT_TEST_URL = "http://localhost:8080";
  public static final String ACTIVE_URI;
  private static final Logger LOGGER = Logger.getLogger(ClientFactory.class);

  static {
    String tmp;
    try {
      tmp = ConfigProvider.getConfig().getValue("quarkus.rest-client.apiclient.url", String.class);
    } catch (final NoSuchElementException e) {
      tmp = DEFAULT_TEST_URL;
    }
    ACTIVE_URI = tmp;
  }

  @RestClient
  ApiClient client;

  public Client getInstance() {
    LOGGER.infof("URI: %s", ACTIVE_URI);
    return new Client(QuarkusRestClientBuilder.newBuilder().baseUri(URI.create(ACTIVE_URI)).build(ApiClient.class));
  }

  public Client getInstanceNative() {
    LOGGER.infof("URI: %s", ACTIVE_URI);
    return new Client(client);
  }
}
