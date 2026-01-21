package com.virtual.poc.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.virtual.poc.query.ProductQueries;
import com.virtual.poc.query.Queries;
import org.junit.jupiter.api.Test;

class ProductQueriesTest {

  @Test
  void enumSqlValuesAreCorrect() {
    assertEquals(
        "INSERT INTO product.inventory (name) VALUES (?) RETURNING *",
        ProductQueries.INSERT_BY_ID.getSql());
    assertEquals("SELECT * FROM product.inventory WHERE 1=1", ProductQueries.BASE_SELECT.getSql());
    assertEquals(
        "SELECT COUNT(*) FROM product.inventory WHERE 1=1", ProductQueries.BASE_COUNT.getSql());
    assertEquals(
        "SELECT * FROM product.inventory WHERE id = ?", ProductQueries.SELECT_BY_ID.getSql());
    assertEquals(" AND LOWER(name) LIKE ?", ProductQueries.FILTER_NAME.getSql());
    assertEquals(" ORDER BY name ASC LIMIT ? OFFSET ?", ProductQueries.ORDER_PAGING.getSql());
  }

  @Test
  void buildSearchWithNameFilterIncludesFilterClause() {
    Queries result = ProductQueries.buildSearch("test", 0, 10);

    assertTrue(result.sql().contains("AND LOWER(name) LIKE ?"));
    assertTrue(result.sql().contains("ORDER BY name ASC LIMIT ? OFFSET ?"));
    assertEquals(3, result.params().length);
    assertEquals("%test%", result.params()[0]);
    assertEquals(10, result.params()[1]);
    assertEquals(0, result.params()[2]);
  }

  @Test
  void buildSearchWithNullFilterExcludesFilterClause() {
    Queries result = ProductQueries.buildSearch(null, 0, 10);

    assertFalse(result.sql().contains("AND LOWER(name) LIKE ?"));
    assertTrue(result.sql().contains("ORDER BY name ASC LIMIT ? OFFSET ?"));
    assertEquals(2, result.params().length);
    assertEquals(10, result.params()[0]);
    assertEquals(0, result.params()[1]);
  }

  @Test
  void buildSearchWithBlankFilterExcludesFilterClause() {
    Queries result = ProductQueries.buildSearch("   ", 0, 10);

    assertFalse(result.sql().contains("AND LOWER(name) LIKE ?"));
    assertEquals(2, result.params().length);
  }

  @Test
  void buildSearchWithEmptyFilterExcludesFilterClause() {
    Queries result = ProductQueries.buildSearch("", 0, 10);

    assertFalse(result.sql().contains("AND LOWER(name) LIKE ?"));
    assertEquals(2, result.params().length);
  }

  @Test
  void buildSearchCalculatesOffsetCorrectly() {
    Queries result = ProductQueries.buildSearch(null, 3, 25);

    assertEquals(25, result.params()[0]);
    assertEquals(75, result.params()[1]);
  }

  @Test
  void buildSearchConvertsFilterToLowercase() {
    Queries result = ProductQueries.buildSearch("UPPERCASE", 0, 10);

    assertEquals("%uppercase%", result.params()[0]);
  }

  @Test
  void buildCountWithNameFilterIncludesFilterClause() {
    Queries result = ProductQueries.buildCount("test");

    assertTrue(result.sql().contains("SELECT COUNT(*)"));
    assertTrue(result.sql().contains("AND LOWER(name) LIKE ?"));
    assertEquals(1, result.params().length);
    assertEquals("%test%", result.params()[0]);
  }

  @Test
  void buildCountWithNullFilterExcludesFilterClause() {
    Queries result = ProductQueries.buildCount(null);

    assertTrue(result.sql().contains("SELECT COUNT(*)"));
    assertFalse(result.sql().contains("AND LOWER(name) LIKE ?"));
    assertEquals(0, result.params().length);
  }

  @Test
  void buildCountWithBlankFilterExcludesFilterClause() {
    Queries result = ProductQueries.buildCount("   ");

    assertFalse(result.sql().contains("AND LOWER(name) LIKE ?"));
    assertEquals(0, result.params().length);
  }

  @Test
  void buildCountWithEmptyFilterExcludesFilterClause() {
    Queries result = ProductQueries.buildCount("");

    assertFalse(result.sql().contains("AND LOWER(name) LIKE ?"));
    assertEquals(0, result.params().length);
  }

  @Test
  void buildCountConvertsFilterToLowercase() {
    Queries result = ProductQueries.buildCount("UPPERCASE");

    assertEquals("%uppercase%", result.params()[0]);
  }
}
