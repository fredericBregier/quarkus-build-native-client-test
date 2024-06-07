package org.example.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record UploadResponse(String name, long size) {
}
