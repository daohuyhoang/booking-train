package com.group2.case_study.controllers;

import com.group2.case_study.models.User;
import com.group2.case_study.services.IAdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private IAdminUserService adminUserService;


    @GetMapping
    public String listUsers(Model model) {
        List<User> users = adminUserService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user/user-management";
    }

    @GetMapping("/view/{id}")
    public String viewUser(@PathVariable("id") Integer id, Model model) {
        User user = adminUserService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/user/user-view";
    }

    @PostMapping("/update-role")
    public String updateUserRole(@RequestParam Integer userId, @RequestParam String role, RedirectAttributes redirectAttributes) {
        adminUserService.updateUserRole(userId, role);
        redirectAttributes.addFlashAttribute("message", "Vai trò của người dùng đã được cập nhật thành công!");
        return "redirect:/admin/users";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        adminUserService.updateUser(user);
        redirectAttributes.addFlashAttribute("message", "Thông tin người dùng đã được cập nhật thành công!");
        return "redirect:/admin/users";
    }
}