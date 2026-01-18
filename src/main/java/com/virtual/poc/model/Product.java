package com.virtual.poc.model;

import jakarta.validation.constraints.NotBlank;

public record Product(@NotBlank String name) {}
