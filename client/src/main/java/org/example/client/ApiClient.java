package org.example.client;

import java.io.Closeable;
import java.io.InputStream;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.example.model.UploadResponse;
import org.jboss.resteasy.reactive.NoCache;

@RegisterRestClient(configKey = "apiclient")
@NoCache
@Path("/root")
@RegisterClientHeaders(HeaderRequestClientFactory.class)
@RegisterProvider(FilterClientResponse.class)
public interface ApiClient extends Closeable {
  @GET
  @Path("get/{name}")
  @Produces(MediaType.APPLICATION_JSON)
  UploadResponse get(@PathParam("name") final String name);

  @POST
  @Path("inputstream/{name}")
  @Consumes(MediaType.APPLICATION_OCTET_STREAM)
  @Produces(MediaType.APPLICATION_JSON)
  UploadResponse inputStream(@PathParam("name") final String name, final InputStream inputStream);

  @POST
  @Path("frombytes/{name}")
  @Consumes(MediaType.APPLICATION_OCTET_STREAM)
  @Produces(MediaType.APPLICATION_JSON)
  UploadResponse fromBytes(@PathParam("name") final String name, final byte[] bytes);

  @Override
  void close();
}
