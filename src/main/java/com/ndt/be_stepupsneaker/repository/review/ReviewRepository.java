package com.ndt.be_stepupsneaker.repository.review;

import com.ndt.be_stepupsneaker.entity.auth.PasswordResetToken;
import com.ndt.be_stepupsneaker.entity.customer.Customer;
import com.ndt.be_stepupsneaker.entity.employee.Employee;
import com.ndt.be_stepupsneaker.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    public static final String NAME = "BaseReviewRepository";
}
