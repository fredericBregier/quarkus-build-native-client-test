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

package org.example.client;

import io.quarkus.arc.Unremovable;
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.net.URI;
import java.util.NoSuchElementException;

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
