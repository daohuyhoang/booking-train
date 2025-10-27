package com.group2.case_study.services;

import com.group2.case_study.models.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface IAdminUserService {

    List<User> getAllUsers();

    User getUserById(Integer id);

    void updateUserRole(Integer userId, String name);

    void updateUser(User user);
}