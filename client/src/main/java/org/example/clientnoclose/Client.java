package org.example.clientnoclose;

import java.io.Closeable;
import java.io.InputStream;

import org.example.model.UploadResponse;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

public class Client implements Closeable {
  private static final Logger LOGGER = Logger.getLogger(Client.class);
  private final ApiClient apiClient;


  Client(final ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public UploadResponse inputStream(final String name, final InputStream inputStream)
      throws ClientWebApplicationException {
    LOGGER.infof("Send InputStream %s", name);
    return apiClient.inputStream(name, inputStream);
  }

  public UploadResponse fromBytes(final String name, final byte[] bytes) throws ClientWebApplicationException {
    LOGGER.infof("Send Bytes %s", name);
    return apiClient.fromBytes(name, bytes);
  }

  public UploadResponse get(final String name) throws ClientWebApplicationException {
    LOGGER.infof("GET");
    return apiClient.get(name);
  }

  @Override
  public void close() {
    LOGGER.infof("PSEUDO CLOSING");
  }
}
