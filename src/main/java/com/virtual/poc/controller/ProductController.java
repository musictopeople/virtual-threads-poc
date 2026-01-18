package com.virtual.poc.controller;

import com.virtual.poc.model.Card;
import com.virtual.poc.model.Product;
import com.virtual.poc.model.api.ResponseObject;
import com.virtual.poc.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ProductController {
  private final ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }

  @GetMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObject<List<Card>> get(
      @RequestParam(name = "filter.name", defaultValue = "") String name,
      @RequestParam(name = "page.offset", defaultValue = "0") int offset,
      @RequestParam(name = "page.size", defaultValue = "10") int size) {
    log.info("Handling request for '{}' on {}", name, Thread.currentThread());
    return service.findProducts(name, offset, size);
  }

  @GetMapping(value = "/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObject<Card> getById(@PathVariable UUID id) {
    log.info("Handling get by id request for '{}' on {}", id, Thread.currentThread());
    return service.findById(id);
  }

  @PostMapping(
      value = "/product",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseObject<Card> create(@NotNull @Valid @RequestBody Product product) {
    log.info("Handling create request for '{}' on {}", product.name(), Thread.currentThread());
    return service.createProduct(product);
  }

  @PutMapping("/product/{id}/{version}")
  public void update(
      @PathVariable("id") UUID productId,
      @PathVariable("version") UUID productVersion,
      @NonNull @Valid @RequestBody Product product) {
    service.updateProduct(productId, productVersion, product);
  }

  @DeleteMapping("/product/{id}/{version}")
  public void delete(
      @PathVariable("id") UUID productId, @PathVariable("version") UUID productVersion) {
    service.deleteProduct(productId, productVersion);
  }
}
