package com.virtual.poc.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Card(UUID id, UUID version, Product product, LocalDateTime versionedOn) {}
