package com.virtual.poc.model.api;

import com.virtual.poc.model.Product;
import java.util.UUID;

public record UpdateRequest(UUID version, Product product) {}
