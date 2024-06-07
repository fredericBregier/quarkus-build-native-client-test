package org.example;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@ApplicationScoped
@Path("/command")
public class PseudoCommandServer {
  private static final Logger LOGGER = Logger.getLogger(PseudoCommandServer.class);

  @Inject
  Main.MyApp myApp;

  @GET
  @Path("{action}")
  @Produces(MediaType.APPLICATION_JSON)
  @Blocking
  public Uni<Response> get(@PathParam("action") final String action) {
    return Uni.createFrom().emitter(em -> {
      LOGGER.infof("ACTION %s", action);
      try {
        myApp.launch(action);
        em.complete(Response.status(200).build());
      } catch (final Exception e) {
        em.fail(e);
      }
    });
  }

}
