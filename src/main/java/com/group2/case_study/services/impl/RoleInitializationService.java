package com.group2.case_study.services.impl;

import com.group2.case_study.models.Role;
import com.group2.case_study.repositories.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RoleInitializationService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RoleInitializationService.class);

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
    }

    private void initializeRoles() {
        logger.info("Initializing roles...");
        
        // Kiểm tra và tạo role ADMIN
        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setDescription("Quản trị viên hệ thống");
            roleRepository.save(adminRole);
            logger.info("Created ROLE_ADMIN");
        }
        
        // Kiểm tra và tạo role EMPLOYEE
        if (roleRepository.findByName("ROLE_EMPLOYEE") == null) {
            Role employeeRole = new Role();
            employeeRole.setName("ROLE_EMPLOYEE");
            employeeRole.setDescription("Nhân viên");
            roleRepository.save(employeeRole);
            logger.info("Created ROLE_EMPLOYEE");
        }
        
        // Kiểm tra và tạo role CUSTOMER
        if (roleRepository.findByName("ROLE_CUSTOMER") == null) {
            Role customerRole = new Role();
            customerRole.setName("ROLE_CUSTOMER");
            customerRole.setDescription("Khách hàng");
            roleRepository.save(customerRole);
            logger.info("Created ROLE_CUSTOMER");
        }
        
        logger.info("Role initialization completed");
    }
}
