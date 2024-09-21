package example.microservices.orders.controllers;

import example.microservices.orders.controllers.dto.OrderDTO;
import example.microservices.orders.entities.Order;
import example.microservices.orders.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/save")
    public ResponseEntity<?> saveOrder(@RequestBody OrderDTO order){

        Order newOrder = Order.builder()
                .userId(order.getUserId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .status(order.getStatus())
                .build();

        return ResponseEntity.ok(orderService.saveOrder(newOrder));
    }

    @GetMapping
    public ResponseEntity<?> findAll(){
        List<OrderDTO> orderList = orderService.findAll()
                .stream()
                .map(order -> OrderDTO.builder()
                        .id(order.getId())
                        .userId(order.getUserId())
                        .productId(order.getProductId())
                        .quantity(order.getQuantity())
                        .status(order.getStatus())
                        .build())
                .toList();

        if(orderList.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(orderList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<Order> optional = orderService.findById(id);
        if(optional.isPresent()){
            Order order = optional.get();
            OrderDTO orderDTO = OrderDTO.builder()
                    .id(order.getId())
                    .userId(order.getUserId())
                    .productId(order.getProductId())
                    .quantity(order.getQuantity())
                    .status(order.getStatus())
                    .build();

            return ResponseEntity.ok(orderDTO);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO){

        Optional<Order> storedOrder = orderService.findById(id);

        if(storedOrder.isPresent()){
            Order order = storedOrder.get();
            order.setUserId(orderDTO.getUserId());
            order.setProductId(orderDTO.getProductId());
            order.setQuantity(orderDTO.getQuantity());
            order.setStatus(orderDTO.getStatus());

            orderService.saveOrder(order);
            return ResponseEntity.ok("Orden actualizada correctamente.");
        }
        return  ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateOrderByFields(@PathVariable Long id, Map<String, Object> fields){

        Optional<Order> storedOrder = orderService.findById(id);
        if(storedOrder.isPresent()){
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Order.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, storedOrder.get(), value);
            });

            orderService.saveOrder(storedOrder.get());
            return ResponseEntity.ok("Orden actualizada correctamente.");
        }
        return  ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){

        if(id != null){
            orderService.deleteById(id);
            return ResponseEntity.ok("Orden eliminada correctamente.");
        }

        return ResponseEntity.badRequest().build();
    }
}
