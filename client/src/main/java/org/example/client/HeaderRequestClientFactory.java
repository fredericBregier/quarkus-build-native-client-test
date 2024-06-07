package org.example.client;

import io.quarkus.arc.Unremovable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;
import org.jboss.logging.Logger;

@ApplicationScoped
@Unremovable
public class HeaderRequestClientFactory implements ClientHeadersFactory {
  public static final String X_HEADER_SPECIFIC = "X-Header-Specific";
  private static final Logger LOGGER = Logger.getLogger(HeaderRequestClientFactory.class);

  @Override
  public MultivaluedMap<String, String> update(final MultivaluedMap<String, String> multivaluedMap,
                                               final MultivaluedMap<String, String> multivaluedMap1) {
    final MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
    result.add(X_HEADER_SPECIFIC, "default-value");
    LOGGER.infof("Added Header: %s", result);
    return result;
  }
}
