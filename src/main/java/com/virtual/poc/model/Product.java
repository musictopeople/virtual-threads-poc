package com.virtual.poc.model;

import jakarta.validation.constraints.NotBlank;

public record Product(@NotBlank(message = "Product name must not be null") String name) {}
