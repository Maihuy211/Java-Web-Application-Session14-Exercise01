package com.example.bai1.service;

import com.example.bai1.model.Order;
import com.example.bai1.model.Wallet;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentService {
    private final SessionFactory sessionFactory;
    public void processPayment(Long orderId, Long walletId, double totalAmount) {
        // 1. Mở phiên session
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        // 2. Tìm đối tượng order và wallet
        Order order = session.get(Order.class, orderId);
        Wallet wallet = session.get(Wallet.class, walletId);

        // 3. Kiểm tra điều kiện
        if (totalAmount < 0) {
            System.err.printf("Lỗi: Số tiền thanh toán không hợp lệ: %.2f%n", totalAmount);
            transaction.rollback();
            return;
        }

        if (wallet.getBalance() < totalAmount) {
            System.err.printf("Lỗi: Số dư ví không đủ! Số dư hiện tại: %.2f, Yêu cầu: %.2f%n", wallet.getBalance(), totalAmount);
            transaction.rollback();
            return;
        }

        // 4. Cập nhật trạng thái đơn hàng và trừ tiền ví
        order.setStatus("PAID");
        wallet.setBalance(wallet.getBalance() - totalAmount);

        // 5. Lưu vào database
        session.merge(order);
        session.merge(wallet);

        // 6. Commit transaction
        transaction.commit();
        System.out.printf("Thanh toán thành công! Đơn hàng ID: %d, Số tiền: %.2f%n", orderId, totalAmount);
    }
}
