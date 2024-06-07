package org.example.server;

import java.io.IOException;
import java.io.InputStream;

import io.quarkus.resteasy.reactive.server.Closer;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.model.UploadResponse;
import org.jboss.logging.Logger;

import static org.example.client.HeaderRequestClientFactory.X_HEADER_SPECIFIC;

@ApplicationScoped
@Path("/root")
public class ApiService {
  private static final Logger LOGGER = Logger.getLogger(ApiService.class);

  @GET
  @Path("get/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<Response> get(@PathParam("name") final String name,
                           @HeaderParam(X_HEADER_SPECIFIC) final String specific) {
    return Uni.createFrom().emitter(em -> {
      LOGGER.infof("GET");
      try {
        final var uploadResponse = new UploadResponse(name, -1);
        em.complete(Response.status(201).header(X_HEADER_SPECIFIC, specific).entity(uploadResponse).build());
      } catch (final RuntimeException e) {
        em.fail(e);
      }
    });
  }

  @POST
  @Path("inputstream/{name}")
  @Blocking
  @Consumes(MediaType.APPLICATION_OCTET_STREAM)
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<Response> inputStream(@PathParam("name") final String name,
                                   @HeaderParam(X_HEADER_SPECIFIC) final String specific, @Context final Closer closer,
                                   final InputStream inputStream) {
    return Uni.createFrom().emitter(em -> {
      LOGGER.infof("POST");
      try {
        closer.add(inputStream);
        final long size = inputStream.readAllBytes().length;
        final var uploadResponse = new UploadResponse(name, size);
        em.complete(Response.status(201).header(X_HEADER_SPECIFIC, specific).entity(uploadResponse).build());
      } catch (final RuntimeException | IOException e) {
        em.fail(e);
      }
    });
  }

  @POST
  @Path("frombytes/{name}")
  @Blocking
  @Consumes(MediaType.APPLICATION_OCTET_STREAM)
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<Response> inputStreamFromBytes(@PathParam("name") final String name,
                                            @HeaderParam(X_HEADER_SPECIFIC) final String specific,
                                            @Context final Closer closer, final InputStream inputStream) {
    return inputStream(name, specific, closer, inputStream);
  }
}
