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

import org.example.model.UploadResponse;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import java.io.Closeable;
import java.io.InputStream;

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
