package org.example;

import java.io.ByteArrayInputStream;
import java.util.Random;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.example.client.ApiClient;
import org.example.client.ClientFactory;
import org.example.model.UploadResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class ClientServerTest {
  private static final byte[] bytes = new byte[120000];
  private static UploadResponse expected;
  @Inject
  ClientFactory factory;
  @RestClient
  ApiClient apiClient;

  @BeforeAll
  static void beforeAll() {
    final var random = new Random();
    random.nextBytes(bytes);
    expected = new UploadResponse("test", 120000);
  }

  @Test
  void allTests() {
    for (var i = 0; i < 10; i++) {
      testClientGet();
    }
    for (var i = 0; i < 10; i++) {
      testApiClientGet();
    }
    for (var i = 0; i < 10; i++) {
      testClientBytes();
    }
    for (var i = 0; i < 10; i++) {
      testApiClientBytes();
    }
    for (var i = 0; i < 10; i++) {
      testApiClientInputStream();
    }
    for (var i = 0; i < 10; i++) {
      testClientInputStream();
    }
  }

  @Test
  void testClientInputStream() {
    try (final var client = factory.getInstance()) {
      final var result = client.inputStream("test", new ByteArrayInputStream(bytes));
      assertEquals(expected, result);
    }
  }

  @Test
  void testClientBytes() {
    try (final var client = factory.getInstance()) {
      final var result = client.fromBytes("test", bytes);
      assertEquals(expected, result);
    }
  }

  @Test
  void testApiClientInputStream() {
    final var result = apiClient.inputStream("test", new ByteArrayInputStream(bytes));
    assertEquals(expected, result);
  }

  @Test
  void testApiClientBytes() {
    final var result = apiClient.fromBytes("test", bytes);
    assertEquals(expected, result);
  }

  @Test
  void testClientGet() {
    final var newExpected = new UploadResponse(expected.name(), -1);
    try (final var client = factory.getInstance()) {
      final var result = client.get("test");
      assertEquals(newExpected, result);
    }
  }

  @Test
  void testApiClientGet() {
    final var newExpected = new UploadResponse(expected.name(), -1);
    final var result = apiClient.get("test");
    assertEquals(newExpected, result);
  }
}
