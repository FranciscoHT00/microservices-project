package example.microservices.orders.services;

import example.microservices.orders.entities.Order;
import example.microservices.orders.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order saveOrder(Order order){
        return orderRepository.save(order);
    }

    public List<Order> findAll(){
        return (List<Order>) orderRepository.findAll();
    }

    public Optional<Order> findById(Long id){
        return orderRepository.findById(id);
    }

    public void deleteById(Long id){
        orderRepository.deleteById(id);
    }
}
