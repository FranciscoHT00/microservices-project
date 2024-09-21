package example.microservices.products.controllers;

import example.microservices.products.controllers.dto.ProductDTO;
import example.microservices.products.entities.Product;
import example.microservices.products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/save")
    public ResponseEntity<?> saveProduct(@RequestBody ProductDTO productDTO){
        Product newProduct = Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .stock(productDTO.getStock())
                .build();

        return ResponseEntity.ok(productService.saveProduct(newProduct));
    }

    @GetMapping
    public ResponseEntity<?> findAll(){
        List<ProductDTO> productList = productService.findAll()
                .stream()
                .map(product -> ProductDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .stock(product.getStock())
                        .build())
                .toList();

        if(productList.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(productList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id){
        Optional<Product> optional = productService.findById(id);
        if(optional.isPresent()){
            Product product = optional.get();
            ProductDTO productDTO = ProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .stock(product.getStock())
                    .build();

            return ResponseEntity.ok(productDTO);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable String id, @RequestBody ProductDTO productDTO){

        Optional<Product> storedOrder = productService.findById(id);

        if(storedOrder.isPresent()){
            Product product = storedOrder.get();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setStock(productDTO.getStock());

            productService.saveProduct(product);
            return ResponseEntity.ok("Producto actualizado correctamente.");
        }
        return  ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id){

        if(id != null){
            productService.deleteById(id);
            return ResponseEntity.ok("Producto eliminado correctamente.");
        }

        return ResponseEntity.badRequest().build();
    }
}
