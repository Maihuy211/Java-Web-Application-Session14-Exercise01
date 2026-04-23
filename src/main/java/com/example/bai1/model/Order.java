package com.example.bai1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private String status; // Ví dụ: "PENDING", "PAID"

    @Column(name = "total_amount")
    private double totalAmount; // Số tiền của đơn hàng này

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet; // Ví của khách hàng
}
