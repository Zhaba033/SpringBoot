package com.mycompany.itforum.controller;

import com.mycompany.itforum.entity.Account;
import com.mycompany.itforum.entity.Category;
import com.mycompany.itforum.entity.Comment;
import com.mycompany.itforum.entity.Post;
import com.mycompany.itforum.repository.AccountRepository;
import com.mycompany.itforum.repository.CategoryRepository;
import com.mycompany.itforum.repository.CommentRepository;
import com.mycompany.itforum.repository.PostRepository;
import com.mycompany.itforum.service.AccountService;
import com.mycompany.itforum.service.PageService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Slf4j
@Controller
public class ModeratorController {

    final PostRepository postRepository;
    final CategoryRepository categoryRepository;
    final CommentRepository commentRepository;
    final AccountRepository accountRepository;

    final AccountService accountService;
    final PageService pageService;

    @GetMapping("/moderator/panel")
    public String moderatorPanel(Model model) {
        model.addAttribute("accounts", accountRepository.findAllByOrderByUsernameAsc());
        return "moderator/moderator_panel";
    }

    @PostMapping("/moderator/manage/{u}/{a}")
    public String banUser(
            @PathVariable String u,
            @PathVariable String a,
            RedirectAttributes redirectAttributes,
            Model model) {

        model.addAttribute("breadcrumbs", pageService.getPages("moderator-panel", ""));

        Account account = accountRepository.findByUsername(u);
        redirectAttributes.addFlashAttribute("code", "200");
        redirectAttributes.addFlashAttribute("link", "/moderator/panel");
        
        return switch (a) {
            case "ban" -> {
                accountService.ban(account);
                redirectAttributes.addFlashAttribute("text", "Вы забанили пользователя " + u);
                yield "redirect:/result";
            }
            case "unban" -> {
                accountService.unban(account);
                redirectAttributes.addFlashAttribute("text", "Вы разбанили пользователя " + u);
                yield "redirect:/result";
            }
            case "giveModeratorRole" -> {
                accountService.op(account);
                redirectAttributes.addFlashAttribute("text", "Вы дали роль модератора пользователю " + u);
                yield "redirect:/result";
            }
            case "removeModeratorRole" -> {
                accountService.deop(account);
                redirectAttributes.addFlashAttribute("text", "Вы сняли роль модератора пользователя " + u);
                yield "redirect:/result";
            }
            default -> {
                yield "redirect:/moderator/panel";
            }
        };
    }

    @PostMapping("/moderator/categories/{c}/delete")
    public String removeCategory(@PathVariable String c) {
        categoryRepository.deleteByUid(c);
        return "redirect:/categories";
    }

    @PostMapping(value = {"/moderator/post/{id}/delete", "/moderator/post/{id}/delete/{from}"})
    public String removePost(@PathVariable Long id, @PathVariable Optional<String> from) {
        Post post = postRepository.findById(id).orElseThrow();
        Category postCategory = post.getCategory();
        postRepository.delete(post);
        log.info(from.get());
        if (from.isPresent() && from.get().equals("home")) {
            return "redirect:/";
        }
        return "redirect:/categories/" + postCategory.getUid();
    }

    @PostMapping("/moderator/comment/{commId}/delete")
    public String removeComm(@PathVariable Long commId) {
        Comment comment = commentRepository.findById(commId).orElseThrow();
        commentRepository.delete(comment);
        return "redirect:/post/" + comment.getPost().getId();
    }
}
