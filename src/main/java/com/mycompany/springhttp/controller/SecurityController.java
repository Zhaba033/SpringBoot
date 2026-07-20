package com.mycompany.springhttp.controller;

import com.mycompany.springhttp.dto.AccountDTO;
import com.mycompany.springhttp.service.AccountService;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class SecurityController {

    @Autowired
    private AccountService servA;

    // LOGOUT
    @GetMapping("/logout_confirm")
    public String logoutConfirmPage(
            Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        return "logout";
    }

    @GetMapping("/auth/logout_success")
    public String logoutSuccessPage() {
        return "logout_success";
    }

    // LOGIN
    @GetMapping("/auth/login")
    public String loginPage() {
        return "login";
    }

    // REGISTER
    @GetMapping("/auth/register")
    public String registerPage(
            Model model
    ) {
        return "register";
    }

    @PostMapping("/auth/perform_register")
    public String perform_register(
            @ModelAttribute AccountDTO account,
            Model model,
            @RequestParam(name = "password_repeat") String password_repeat,
            RedirectAttributes redirectAttributes) {

        String error = null;

        if (servA.getUserData().keySet().contains(account.getUsername())) {
            error = "userAlreadyExists";
        } else if (account.getPassword().length() < 3) {
            error = "tooShortPassw";
        } else if (!account.getPassword().equals(password_repeat)) {
            error = "diffPassw";
        }

        if (error != null) {
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/auth/register";
        }

        log.info("New account: " + account.toString());
        servA.createNewAccount(account.getUsername(), account);
        return "redirect:/auth/login";
    }

    // PROFILE
    @GetMapping("/profile")
    public String profilePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountDTO user = servA.getUserByName(auth.getName());
        model.addAttribute("roles", user.getRoles());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String date = user.getCreatedTime().format(formatter);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("registration_date", date);

        return "profile";
    }

}
