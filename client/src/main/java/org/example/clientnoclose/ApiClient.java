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

package org.example.clientnoclose;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.example.model.UploadResponse;
import org.jboss.resteasy.reactive.NoCache;

import java.io.InputStream;

@RegisterRestClient(configKey = "apiclient")
@NoCache
@Path("/root")
public interface ApiClient {
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

}
