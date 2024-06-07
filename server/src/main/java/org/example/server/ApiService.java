/*
 * Copyright (c) 2024. SIV, Contributors from Sopra Steria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example.server;

import io.quarkus.resteasy.reactive.server.Closer;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.model.UploadResponse;

import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
@Path("/root")
public class ApiService {
    @GET
    @Path("get/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> get(@PathParam("name") final String name) {
        return Uni.createFrom().emitter(em -> {
            try {
                final var uploadResponse = new UploadResponse(name, -1);
                em.complete(Response.status(201).entity(uploadResponse).build());
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
    public Uni<Response> inputStream(@PathParam("name") final String name, @Context final Closer closer,
                                     final InputStream inputStream) {
        return Uni.createFrom().emitter(em -> {
            try {
                closer.add(inputStream);
                final long size = inputStream.readAllBytes().length;
                final var uploadResponse = new UploadResponse(name, size);
                em.complete(Response.status(201).entity(uploadResponse).build());
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
    public Uni<Response> inputStreamFromBytes(@PathParam("name") final String name, @Context final Closer closer,
                                              final InputStream inputStream) {
        return inputStream(name, closer, inputStream);
    }
}
