package org.example.client;

import io.quarkus.arc.Unremovable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.ClientResponseContext;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.client.spi.ResteasyReactiveClientRequestContext;
import org.jboss.resteasy.reactive.client.spi.ResteasyReactiveClientResponseFilter;

import static org.example.client.HeaderRequestClientFactory.X_HEADER_SPECIFIC;

@ApplicationScoped
@Unremovable
public class FilterClientResponse implements ResteasyReactiveClientResponseFilter {
  private static final Logger LOGGER = Logger.getLogger(FilterClientResponse.class);

  @Override
  public void filter(final ResteasyReactiveClientRequestContext requestContext,
                     final ClientResponseContext responseContext) {
    final var value = responseContext.getHeaderString(X_HEADER_SPECIFIC);
    LOGGER.infof("Get header: %s", value);
  }
}
