package com.mycompany.itforum.controller;

import com.mycompany.itforum.entity.Account;
import com.mycompany.itforum.repository.AccountRepository;
import com.mycompany.itforum.repository.CategoryRepository;
import com.mycompany.itforum.service.PageService;
import com.mycompany.itforum.repository.PostRepository;
import com.mycompany.itforum.service.AccountService;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
public abstract class AccountController {
    
    final AccountRepository accountRepository;

    final AccountService accountService;
    final PageService pageService;

    // LOGOUT
    @GetMapping("/logout_confirm")
    public String logoutConfirmPage(
            Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        return "auth/logout";
    }

    @GetMapping("/auth/logout_success")
    public String logoutSuccessPage() {
        return "account/logout_success";
    }

    // LOGIN
    @GetMapping("/auth/login")
    public String loginPage() {
        return "account/login";
    }

    // REGISTER
    @GetMapping("/auth/register")
    public String registerPage(
            Model model
    ) {
        return "account/register";
    }

    @PostMapping("/auth/perform_register")
    public String perform_register(
            @ModelAttribute Account account,
            Model model,
            @RequestParam(name = "password_repeat") String password_repeat,
            RedirectAttributes redirectAttributes) {

        String error = null;

        if (accountRepository.existsByUsername(account.getUsername())) {
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
        accountService.createAccount(account);
        return "redirect:/auth/login";
    }

    // PROFILE
    @GetMapping("/account/profile")
    public String profilePage(Model model) {
        model.addAttribute("breadcrumbs", pageService.getPages("profile", ""));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Account user = accountRepository.findByUsername(auth.getName());
        model.addAttribute("roles", user.getRoles());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String date = user.getCreatedTime().format(formatter);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("registration_date", date);

        return "account/profile";
    }

    // CHANGE PASSWORD
    @GetMapping("/account/change-password")
    public String changePasswordPage(
            Model model,
            @ModelAttribute(name = "error") String error
    ) {

        model.addAttribute("breadcrumbs", pageService.getPages("change-password", ""));

        if (error.equals("diff")) {
            model.addAttribute("error", "true");
        }

        return "account/change-password";

    }

    @PostMapping("/account/change-password/confirm")
    public String changePassword(
            @RequestParam(name = "password") String password,
            @RequestParam(name = "password_repeat") String password_repeat,
            RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!password.equals(password_repeat)) {
            redirectAttributes.addFlashAttribute("error", "diff");
            return "redirect:/account/change-password";
        } else {
            accountService.changePassword(accountRepository.findByUsername(auth.getName()), password);
            redirectAttributes.addFlashAttribute("code", "200");
            redirectAttributes.addFlashAttribute("text", "Пароль от аккаунта " + auth.getName() + " успешно изменён.");
            return "redirect:/result";
        }

    }

}
