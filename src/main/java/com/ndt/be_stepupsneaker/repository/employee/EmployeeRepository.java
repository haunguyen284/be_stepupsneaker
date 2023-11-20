package com.ndt.be_stepupsneaker.repository.employee;

import com.ndt.be_stepupsneaker.entity.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(EmployeeRepository.NAME)
public interface EmployeeRepository  extends JpaRepository<Employee, String> {
    public static final String NAME ="BaseEmployeeRepository";
}
