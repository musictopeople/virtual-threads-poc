package com.virtual.poc.model.api;

public record ResponseObject<T>(T data, Meta meta) {}
