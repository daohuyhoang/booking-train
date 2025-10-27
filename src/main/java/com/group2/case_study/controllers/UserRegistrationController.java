package com.group2.case_study.controllers;

import com.group2.case_study.models.User;
import com.group2.case_study.services.impl.UserRegistrationService;
import com.group2.case_study.services.impl.OtpService;
import com.group2.case_study.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserRegistrationController {

    @Autowired
    private UserRegistrationService registrationService;

    @Autowired
    private OtpService otpService;
    
    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, 
                              @RequestParam("otpCode") String otpCode,
                              RedirectAttributes redirectAttributes) {
        try {
            if (otpCode == null || otpCode.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng nhập mã OTP!");
                return "redirect:/login";
            }

            boolean isValidOtp = otpService.verifyOtp(user.getEmail(), otpCode);
            if (!isValidOtp) {
                redirectAttributes.addFlashAttribute("error", "Mã OTP không hợp lệ hoặc đã hết hạn!");
                return "redirect:/login";
            }

            registrationService.registerNewUser(user, "ROLE_CUSTOMER");
            redirectAttributes.addFlashAttribute("success", "Đăng ký tài khoản thành công!");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đăng ký tài khoản thất bại: " + e.getMessage());
            return "redirect:/login";
        }
    }
    
    @PostMapping("/api/check-availability")
    @ResponseBody
    public Map<String, Object> checkAvailability(@RequestParam("username") String username, 
                                                 @RequestParam("email") String email) {
        Map<String, Object> response = new HashMap<>();
        
        User existingUserByUsername = userRepository.findByUsername(username);
        User existingUserByEmail = userRepository.findByEmail(email);
        
        boolean usernameExists = existingUserByUsername != null;
        boolean emailExists = existingUserByEmail != null;

        // Check if both username and email are available
        boolean isAvailable = !usernameExists && !emailExists;
        
        response.put("success", isAvailable);
        
        if (!isAvailable) {
            // Determine which field has the issue
            if (usernameExists && emailExists) {
                response.put("message", "Tên đăng nhập và email đã tồn tại!");
                response.put("field", "both");
            } else if (usernameExists) {
                response.put("message", "Tên đăng nhập đã tồn tại!");
                response.put("field", "username");
            } else if (emailExists) {
                response.put("message", "Email đã tồn tại!");
                response.put("field", "email");
            }
        }
        
        return response;
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
        return "redirect:/login";
    }
}
