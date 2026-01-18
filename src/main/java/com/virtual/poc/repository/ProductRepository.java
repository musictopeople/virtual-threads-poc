package com.virtual.poc.repository;

import com.virtual.poc.mapper.ProductCardMapper;
import com.virtual.poc.model.Card;
import com.virtual.poc.model.Product;
import com.virtual.poc.model.api.Meta;
import com.virtual.poc.model.api.ResponseObject;
import com.virtual.poc.query.ProductQueries;
import com.virtual.poc.query.Queries;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ProductRepository {
  private final JdbcTemplate jdbcTemplate;
  private final ProductCardMapper productCardMapper = new ProductCardMapper();

  public ProductRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @SuppressWarnings("SqlSourceToSinkFlow")
  public ResponseObject<List<Card>> find(String nameFilter, int page, int size) {
    final Queries dataQuery = ProductQueries.buildSearch(nameFilter, page, size);
    final Queries countQuery = ProductQueries.buildCount(nameFilter);

    List<Card> products =
        jdbcTemplate.query(dataQuery.sql(), productCardMapper, dataQuery.params());

    Long total = jdbcTemplate.queryForObject(countQuery.sql(), Long.class, countQuery.params());

    long totalElements = (total != null) ? total : 0L;

    return new ResponseObject<>(products, new Meta(page, size, totalElements));
  }

  public ResponseObject<Card> findById(java.util.UUID id) {
    return new ResponseObject<>(
        jdbcTemplate.queryForObject(ProductQueries.SELECT_BY_ID.getSql(), productCardMapper, id),
        null);
  }

  public ResponseObject<Card> save(Product product) {
    return new ResponseObject<>(
        jdbcTemplate.queryForObject(
            ProductQueries.INSERT_BY_ID.getSql(), productCardMapper, product.name()),
        null);
  }

  public void update(UUID id, UUID version, Product product) {
    log.info("need to implement updating id {} version {} with product {}", id, version, product);
  }

  public void delete(UUID id, UUID version) {
    log.info("need to implement deleting id {} version {}", id, version);
  }
}
