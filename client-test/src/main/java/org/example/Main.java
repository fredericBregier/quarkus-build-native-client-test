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

package org.example;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.example.client.ApiClient;
import org.example.client.ClientFactory;
import org.jboss.logging.Logger;

import java.io.ByteArrayInputStream;
import java.util.Random;

@QuarkusMain
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }

    @ApplicationScoped
    public static class MyApp implements QuarkusApplication {

        @Inject
        ClientFactory factory;
        @Inject
        org.example.clientnoclose.ClientFactory factoryNoClose;

        @RestClient
        ApiClient apiClient;

        @Override
        public int run(String... args) throws Exception {
            final var config = ConfigProvider.getConfig();
            LOGGER.infof("Using HTTP Server at: %s",
                    config.getValue("quarkus.rest-client.apiclient.url", String.class));
            var action = ARGS.APIGET;
            if (args.length > 0) {
                try {
                    action = ARGS.valueOf(args[0]);
                } catch (final IllegalArgumentException e) {
                    LOGGER.warnf("Unrecognized command: %s", args[0]);
                }
            }
            final var bytes = new byte[120000];
            final var random = new Random();
            random.nextBytes(bytes);
            final var name = "test";
            switch (action) {
                case APIGET -> {
                    try {
                        for (var i = 0; i < 10; i++) {
                            LOGGER.infof("Result GET Native Client: %s", apiClient.get(name));
                        }
                    } catch (final Exception e) {
                        LOGGER.error("ERROR", e);
                    }
                }
                case APIPOST -> {
                    try {
                        for (var i = 0; i < 10; i++) {
                            LOGGER.infof("Result POST Native Client: %s", apiClient.fromBytes(name, bytes));
                        }
                    } catch (final Exception e) {
                        LOGGER.error("ERROR", e);
                    }
                }
                case APISTREAM -> {
                    try {
                        for (var i = 0; i < 10; i++) {
                            LOGGER.infof("Result INPUTSTREAM Native Client: %s",
                                    apiClient.inputStream(name, new ByteArrayInputStream(bytes)));
                        }
                    } catch (final Exception e) {
                        LOGGER.error("ERROR", e);
                    }
                }
                case GET -> {
                    try (final var client = factory.getInstance()) {
                        try {
                            for (var i = 0; i < 10; i++) {
                                LOGGER.infof("Result GET Client: %s", client.get(name));
                            }
                        } catch (final Exception e) {
                            LOGGER.error("ERROR", e);
                        }
                    }
                    try {
                        for (var i = 0; i < 10; i++) {
                            try (final var client = factory.getInstance()) {
                                LOGGER.infof("Result GET CLOSED Client: %s", client.get(name));
                            }
                        }
                    } catch (final Exception e) {
                        LOGGER.error("ERROR", e);
                    }
                }
                case POST -> {
                    try (final var client = factory.getInstance()) {
                        try {
                            for (var i = 0; i < 10; i++) {
                                LOGGER.infof("Result POST Client: %s", client.fromBytes(name, bytes));
                            }
                        } catch (final Exception e) {
                            LOGGER.error("ERROR", e);
                        }
                    }
                    try {
                        for (var i = 0; i < 10; i++) {
                            try (final var client = factory.getInstance()) {
                                LOGGER.infof("Result POST CLOSED Client: %s", client.fromBytes(name, bytes));
                            }
                        }
                    } catch (final Exception e) {
                        LOGGER.error("ERROR", e);
                    }
                }
                case STREAM -> {
                    try (final var client = factory.getInstance()) {
                        try {
                            for (var i = 0; i < 10; i++) {
                                LOGGER.infof("Result INPUTSTREAM Client: %s",
                                        client.inputStream(name, new ByteArrayInputStream(bytes)));
                            }
                        } catch (final Exception e) {
                            LOGGER.error("ERROR", e);
                        }
                    }
                    try {
                        for (var i = 0; i < 10; i++) {
                            try (final var client = factory.getInstance()) {
                                LOGGER.infof("Result INPUTSTREAM CLOSED Client: %s",
                                        client.inputStream(name, new ByteArrayInputStream(bytes)));
                            }
                        }
                    } catch (final Exception e) {
                        LOGGER.error("ERROR", e);
                    }
                }
                case NOCLOSEGET -> {
                    try (final var client = factoryNoClose.getInstance()) {
                        try {
                            for (var i = 0; i < 10; i++) {
                                LOGGER.infof("Result GET Client: %s", client.get(name));
                            }
                        } catch (final Exception e) {
                            LOGGER.error("ERROR", e);
                        }
                    }
                    try {
                        for (var i = 0; i < 10; i++) {
                            try (final var client = factoryNoClose.getInstance()) {
                                LOGGER.infof("Result GET CLOSED Client: %s", client.get(name));
                            }
                        }
                    } catch (final Exception e) {
                        LOGGER.error("ERROR", e);
                    }
                }
                case NOCLOSEPOST -> {
                    try (final var client = factoryNoClose.getInstance()) {
                        try {
                            for (var i = 0; i < 10; i++) {
                                LOGGER.infof("Result POST Client: %s", client.fromBytes(name, bytes));
                            }
                        } catch (final Exception e) {
                            LOGGER.error("ERROR", e);
                        }
                    }
                    try {
                        for (var i = 0; i < 10; i++) {
                            try (final var client = factoryNoClose.getInstance()) {
                                LOGGER.infof("Result POST CLOSED Client: %s", client.fromBytes(name, bytes));
                            }
                        }
                    } catch (final Exception e) {
                        LOGGER.error("ERROR", e);
                    }
                }
                case NOCLOSESTREAM -> {
                    try (final var client = factoryNoClose.getInstance()) {
                        try {
                            for (var i = 0; i < 10; i++) {
                                LOGGER.infof("Result INPUTSTREAM Client: %s",
                                        client.inputStream(name, new ByteArrayInputStream(bytes)));
                            }
                        } catch (final Exception e) {
                            LOGGER.error("ERROR", e);
                        }
                    }
                    try {
                        for (var i = 0; i < 10; i++) {
                            try (final var client = factoryNoClose.getInstance()) {
                                LOGGER.infof("Result INPUTSTREAM CLOSED Client: %s",
                                        client.inputStream(name, new ByteArrayInputStream(bytes)));
                            }
                        }
                    } catch (final Exception e) {
                        LOGGER.error("ERROR", e);
                    }
                }
            }
            Quarkus.waitForExit();
            return 0;
        }
    }
}