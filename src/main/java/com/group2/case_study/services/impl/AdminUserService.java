package com.group2.case_study.services.impl;

import com.group2.case_study.models.User;
import com.group2.case_study.models.UserRole;
import com.group2.case_study.models.Role;
import com.group2.case_study.repositories.IAdminUserRepository;
import com.group2.case_study.repositories.IAdminRoleRepository;
import com.group2.case_study.repositories.IAdminUserRoleRepository;
import com.group2.case_study.services.IAdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AdminUserService implements IAdminUserService {

    @Autowired
    private IAdminUserRepository adminUserRepository;

    @Autowired
    private IAdminRoleRepository adminRoleRepository;

    @Autowired
    private IAdminUserRoleRepository adminUserRoleRepository;

    @Override
    public List<User> getAllUsers() {
        return adminUserRepository.findAllWithRoles();
    }

    @Override
    public User getUserById(Integer id) {
        return adminUserRepository.findByIdWithRoles(id);
    }

    @Override
    public void updateUserRole(Integer userId, String name) {
        User user = getUserById(userId);

        Optional<Role> roleOptional = adminRoleRepository.findByName(name);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            adminUserRoleRepository.save(userRole);
        } else {
            throw new RuntimeException("Vai trò không tồn tại: " + name);
        }
    }

    @Override
    public void updateUser(User user) {
        User existingUser = getUserById(user.getId());
        existingUser.setFullname(user.getFullname());
        existingUser.setEmail(user.getEmail());
        existingUser.setAddress(user.getAddress());
        existingUser.setPhone(user.getPhone());
        existingUser.setAvatar(user.getAvatar());
        adminUserRepository.save(existingUser);
    }

    @Override
    public java.util.List<Role> getAllRoles() {
        return adminRoleRepository.findAll();
    }
}