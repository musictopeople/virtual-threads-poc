package com.virtual.poc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.virtual.poc.config.GlobalExceptionHandler;
import com.virtual.poc.model.Card;
import com.virtual.poc.model.Product;
import com.virtual.poc.model.api.Meta;
import com.virtual.poc.model.api.ResponseObject;
import com.virtual.poc.service.ProductService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @Mock private ProductService productService;

  @InjectMocks private ProductController productController;

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(productController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    objectMapper = JsonMapper.builder().build();
  }

  @Test
  void getProducts() throws Exception {
    Product product = new Product("Test Product");
    Card card = new Card(UUID.randomUUID(), UUID.randomUUID(), product, LocalDateTime.now());
    ResponseObject<List<Card>> response = new ResponseObject<>(List.of(card), new Meta(0, 10, 1));

    when(productService.findProducts("", 0, 10)).thenReturn(response);

    mockMvc
        .perform(get("/product"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].product.name").value("Test Product"))
        .andExpect(jsonPath("$.meta.count").value(1));
  }

  @Test
  void getWithFilterAndPagination() throws Exception {
    Product product = new Product("Filtered Product");
    Card card = new Card(UUID.randomUUID(), UUID.randomUUID(), product, LocalDateTime.now());
    ResponseObject<List<Card>> response = new ResponseObject<>(List.of(card), new Meta(5, 20, 1));

    when(productService.findProducts("filtered", 5, 20)).thenReturn(response);

    mockMvc
        .perform(
            get("/product")
                .param("filter.name", "filtered")
                .param("page.offset", "5")
                .param("page.size", "20"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].product.name").value("Filtered Product"))
        .andExpect(jsonPath("$.meta.offset").value(5))
        .andExpect(jsonPath("$.meta.size").value(20));
  }

  @Test
  void getById() throws Exception {
    UUID id = UUID.randomUUID();
    Product product = new Product("Single Product");
    Card card = new Card(id, UUID.randomUUID(), product, LocalDateTime.now());
    ResponseObject<Card> response = new ResponseObject<>(card, new Meta(0, 1, 1));

    when(productService.findById(id)).thenReturn(response);

    mockMvc
        .perform(get("/product/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.product.name").value("Single Product"));
  }

  @Test
  void createProduct() throws Exception {
    Product product = new Product("New Product");
    Card card = new Card(UUID.randomUUID(), UUID.randomUUID(), product, LocalDateTime.now());
    ResponseObject<Card> response = new ResponseObject<>(card, new Meta(0, 1, 1));

    when(productService.createProduct(any(Product.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.product.name").value("New Product"));
  }

  @Test
  void returnBadRequest() throws Exception {
    mockMvc
        .perform(
            post("/product").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void returnInternalServerErrorOnException() throws Exception {
    when(productService.findProducts("", 0, 10)).thenThrow(new RuntimeException("Database error"));

    mockMvc.perform(get("/product")).andExpect(status().isInternalServerError());
  }
}
