package org.shopping.service;

import lombok.RequiredArgsConstructor;
import org.shopping.common.ConflictException;
import org.shopping.common.RecordNotfoundException;
import org.shopping.entity.Account;
import org.shopping.entity.Order;
import org.shopping.entity.OrderDetails;
import org.shopping.entity.Product;
import org.shopping.model.*;
import org.shopping.repository.AccountRepository;
import org.shopping.repository.OrderDetailRepository;
import org.shopping.repository.OrderRepository;
import org.shopping.repository.ProductRepository;
import org.shopping.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final AccountService accountService;

    @Transactional
    public void addOrder(CartInfo cartInfo) {
        Integer maxOrderNum = orderRepository.maxOrderNum();
        int orderNum = (maxOrderNum == null) ? 1 : maxOrderNum + 1;

        Order order = new Order();
        order.setOrderNum(orderNum);
        order.setOrderDate(new Date());
        order.setAmount(cartInfo.getAmountTotal());

        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        order.setName(customerInfo.getName());
        order.setEmail(customerInfo.getEmail());
        order.setPhone(customerInfo.getPhone());
        order.setAddress(customerInfo.getAddress());
        order.setIsDeleted(true);
        order.setStatus(Constants.StatusOrder.ORDER_PENDING_CONFIRMATION);
        order.setUserId(accountService.findIdByUserDetail());

        System.out.println("OrderNum : "+order.getOrderNum());

        orderRepository.save(order);

        List<CartLineInfo> lines = cartInfo.getCartLines();
        for (CartLineInfo line : lines) {
            Integer quantity = productRepository.findQuantityById(line.getProductInfo().getId());
            if (quantity <= 0){
                throw new ConflictException("Quantity of " + line.getProductInfo().getCode() + " is(are) out of stock");
            }else if (quantity < line.getQuantity()){
                throw new ConflictException("Quantity of " + line.getProductInfo().getCode() + " is only " + quantity);
            }
            OrderDetails detail = new OrderDetails();
            detail.setOrder(order);
            detail.setAmount(line.getAmount());
            detail.setPrice(line.getProductInfo().getPrice());
            detail.setQuantity(line.getQuantity());
            int id = line.getProductInfo().getId();
            Optional<Product> productOpt = productRepository.findById(id);
            Product product = productOpt.get();
            product.setQuantity(product.getQuantity() - line.getQuantity());
            productRepository.save(product);
            detail.setProduct(product);
            orderDetailRepository.save(detail);
        }
    }

    public Page<Order> getOrders(String likeName, int currentPage, int size, Integer userId, Boolean isActive) {
        Pageable pageable = PageRequest.of(currentPage - 1, size);
        Page<Order> orders = orderRepository.search(likeName, userId, isActive, pageable);
        return orders;
    }

    public OrderInfo findOrderDetailById(Integer orderId) {
        OrderInfo orderInfo = null;
        Optional<Order> existOrder = orderRepository.findById(orderId);
        if (existOrder.isEmpty()){
            throw new RecordNotfoundException("Order");
        }
        Order order = existOrder.get();
        orderInfo = new OrderInfo(orderId, order.getOrderDate(), order.getOrderNum(), order.getAmount(),
                order.getName(), order.getAddress(), order.getEmail(),
                order.getPhone());
        List<OrderDetailInfo> orderDetailInfos = getOrderDetailInfos(order);
        orderInfo.setDetails(orderDetailInfos);
        return orderInfo;
    }

    private List<OrderDetailInfo> getOrderDetailInfos(Order order) {
        List<OrderDetailInfo> orderDetailInfos = new ArrayList<>();
        List<OrderDetails> orderDetails = order.getOrderDetailsList();
        for (OrderDetails orderDetail: orderDetails){
            OrderDetailInfo orderDetailInfo = new OrderDetailInfo(orderDetail.getId(), orderDetail.getProduct().getCode(),
                    orderDetail.getProduct().getName(), orderDetail.getQuantity(), orderDetail.getPrice(),
                    orderDetail.getAmount());
            orderDetailInfos.add(orderDetailInfo);
        }
        return orderDetailInfos;
    }

    public void updateOrderStatus(Integer orderId, String status, Account account) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RecordNotfoundException("Order"));

        // Nếu người dùng là khách hàng, chỉ cho phép cập nhật thành DELIVERED khi trạng thái là SHIPPED
        if (account.getRole().equals("ROLE_EMPLOYEE")) {
            if (order.getStatus().equals(Constants.StatusOrder.ORDER_SHIPPED) && status.equals(Constants.StatusOrder.ORDER_DELIVERED)) {
                order.setStatus(Constants.StatusOrder.ORDER_DELIVERED);
            } else {
                throw new ConflictException("Only 'Delivered' status can be updated by customer.");
            }
        }

        if (account.getRole().equals("ROLE_MANAGER")) {
            if (order.getStatus().equals(Constants.StatusOrder.ORDER_PENDING_CONFIRMATION) && status.equals(Constants.StatusOrder.ORDER_CONFIRMED)) {
                order.setStatus(Constants.StatusOrder.ORDER_CONFIRMED);
            } else if (order.getStatus().equals(Constants.StatusOrder.ORDER_CONFIRMED) && status.equals(Constants.StatusOrder.ORDER_SHIPPED)) {
                order.setStatus(Constants.StatusOrder.ORDER_SHIPPED);
            } else {
                throw new ConflictException("Invalid status update for manager.");
            }
        }
        orderRepository.save(order);
    }
}
