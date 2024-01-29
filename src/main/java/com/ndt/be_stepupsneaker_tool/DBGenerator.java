package com.ndt.be_stepupsneaker_tool;

import com.ndt.be_stepupsneaker.core.admin.repository.order.AdminOrderDetailRepository;
import com.ndt.be_stepupsneaker.entity.order.OrderDetail;
import com.ndt.be_stepupsneaker.repository.order.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author SonPT
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.ndt.be_stepupsneaker.repository")
public class DBGenerator implements CommandLineRunner {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DBGenerator.class);
        ctx.close();
    }

    @Override
    public void run(String... args) throws Exception {
        OrderDetail orderDetail = orderDetailRepository.findById("7489db75-6659-47a2-9fc5-7d4b594e1abf").orElseThrow();
        orderDetail.setQuantity(12);
        orderDetailRepository.save(orderDetail);
    }
}
