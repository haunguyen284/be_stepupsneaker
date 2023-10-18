package com.ndt.be_stepupsneaker.repository.employee;

import com.ndt.be_stepupsneaker.entity.employee.Employee;
import org.hibernate.boot.archive.internal.JarProtocolArchiveDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository(EmployeeRepository.NAME)
public interface EmployeeRepository  extends JpaRepository<Employee, UUID> {
    public static final String NAME ="BaseEmployeeRepository";
}
