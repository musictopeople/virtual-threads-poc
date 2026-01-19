package com.virtual.poc.query;

import io.micrometer.common.util.StringUtils;
import java.util.ArrayList;
import lombok.Getter;

public enum ProductQueries {
  INSERT_BY_ID("INSERT INTO product.inventory (name) VALUES (?) RETURNING *"),
  BASE_SELECT("SELECT * FROM product.inventory WHERE 1=1"),
  BASE_COUNT("SELECT COUNT(*) FROM product.inventory WHERE 1=1"),
  SELECT_BY_ID("SELECT * FROM product.inventory WHERE id = ?"),
  FILTER_NAME(" AND LOWER(name) LIKE ?"),
  ORDER_PAGING(" ORDER BY name ASC LIMIT ? OFFSET ?");

  @Getter private final String sql;

  ProductQueries(String sql) {
    this.sql = sql;
  }

  public static Queries buildSearch(String nameFilter, int page, int size) {
    var sql = new StringBuilder(BASE_SELECT.sql);
    var params = new ArrayList<>();

    if (StringUtils.isNotBlank(nameFilter)) {
      sql.append(FILTER_NAME.sql);
      params.add("%" + nameFilter.toLowerCase() + "%");
    }

    sql.append(ORDER_PAGING.sql);
    params.add(size);
    params.add(page * size);
    return new Queries(sql.toString(), params.toArray());
  }

  public static Queries buildCount(String nameFilter) {
    var sql = new StringBuilder(BASE_COUNT.sql);
    var params = new ArrayList<>();

    if (StringUtils.isNotBlank(nameFilter)) {
      sql.append(FILTER_NAME.sql);
      params.add("%" + nameFilter.toLowerCase() + "%");
    }
    return new Queries(sql.toString(), params.toArray());
  }
}
