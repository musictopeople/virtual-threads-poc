package com.virtual.poc.mapper;

import com.virtual.poc.model.Card;
import com.virtual.poc.model.Product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class ProductCardMapper implements RowMapper<Card> {
  @Override
  public Card mapRow(ResultSet rs, int rowNum) throws SQLException {
    return new Card(
        rs.getObject("id", UUID.class),
        rs.getObject("version", UUID.class),
        new Product(rs.getString("name")),
        rs.getObject("versioned_on", OffsetDateTime.class).toLocalDateTime());
  }
}
