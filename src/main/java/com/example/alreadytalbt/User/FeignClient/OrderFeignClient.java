package com.example.alreadytalbt.User.FeignClient;
import com.example.alreadytalbt.Order.dto.*;
import org.bson.types.ObjectId;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "order-service", url ="${order.service.url}")
public interface OrderFeignClient {

    @PutMapping("/api/orders/{orderId}/status")
    OrderResponseDTO updateOrderStatus(@PathVariable("orderId") String orderId,
                                       @RequestParam("status") String status);


    //Order-related
    @PutMapping("/api/orders/assign-order/{orderId}/to-delivery/{deliveryGuyId}")
    void assignOrderToDeliveryGuy(@PathVariable("orderId") ObjectId orderId,
                                  @PathVariable("deliveryGuyId") ObjectId deliveryGuyId);

    @GetMapping("/api/orders/summary")
    List<OrderResponseDTO> getOrderSummaries();



    @GetMapping("/api/orders/customer/{customerId}")
    List<UpdateOrderDTO> getOrdersByCustomerId(@PathVariable("customerId") ObjectId customerId);

    // Cart-related
    @PostMapping("/api/carts")
    CartDTO createCart(@RequestBody CreateCartDTO dto);

    @PostMapping("/api/carts/add-items")
    CartDTO addItemsToCart(@RequestBody AddToCartRequestDTO dto);


    @PostMapping("/api/carts/remove-item")
    CartDTO removeItemFromCart(@RequestBody RemoveFromCartRequestDTO dto);



    @GetMapping("/api/carts/{customerId}")
    CartWithItemsDTO getCartByCustomerIdwithdetails(@PathVariable("customerId") String customerId);

    @GetMapping("/api/carts/{customerId}/ids-only")
    CartDTO getCartWithIdsOnly(@PathVariable("customerId") String customerId);

    @PutMapping("/api/carts/{customerId}")
    CartDTO updateCart(@PathVariable("customerId") String customerId,
                       @RequestBody UpdateCartDTO dto);

    @DeleteMapping("/api/carts/{customerId}")
    void deleteCart(@PathVariable("customerId") String customerId);


    @PostMapping("/api/carts/submit/{cartId}")
    CreateOrderDTO submitOrder(@PathVariable("cartId") String cartId,
                               @RequestParam("paymentMethod") String paymentMethod);

    @GetMapping("/api/orders/restaurant/{restaurantId}")
    List<OrderResponseDTO> getOrdersByRestaurantId(@PathVariable("restaurantId") String restaurantId);

    @GetMapping("/api/orders/{id}")
    OrderResponseDTO getOrderById(@PathVariable String id);

}

