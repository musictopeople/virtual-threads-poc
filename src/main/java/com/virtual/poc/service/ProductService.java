package com.virtual.poc.service;

import com.virtual.poc.model.Card;
import com.virtual.poc.model.Product;
import com.virtual.poc.model.api.ResponseObject;
import com.virtual.poc.repository.ProductRepository;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductService {
  private final ProductRepository repository;

  public ProductService(ProductRepository repository) {
    this.repository = repository;
  }

  public ResponseObject<List<Card>> findProducts(String nameFilter, int page, int size) {
    return repository.find(nameFilter, page, size);
  }

  public ResponseObject<Card> findById(UUID id) {
    return repository.findById(id);
  }

  public ResponseObject<Card> createProduct(Product product) {
    return repository.save(product);
  }

  public void updateProduct(UUID id, UUID version, Product product) {
    repository.update(id, version, product);
  }

  public void deleteProduct(UUID id, UUID version) {
    repository.delete(id, version);
  }
}
