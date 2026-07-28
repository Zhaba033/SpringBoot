package com.mycompany.itforum.controller;

import com.mycompany.itforum.repository.AccountRepository;
import com.mycompany.itforum.repository.CategoryRepository;
import com.mycompany.itforum.repository.PostRepository;
import com.mycompany.itforum.service.AccountService;
import com.mycompany.itforum.service.PageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DefaultController {

    final PostRepository postRepository;
    final CategoryRepository categoryRepository;

    final PageService pageService;

    // HOME
    @GetMapping("/")
    public String HomePage(Model model) {

        model.addAttribute("breadcrumbs", pageService.getPages("home", ""));
        model.addAttribute("posts", postRepository.findRecent(PageRequest.of(0, 5)));
        model.addAttribute("categories", categoryRepository.findTop().subList(0, 3));

        return "default/home";
    }

    @GetMapping("/result")
    public String result(
            @ModelAttribute(name = "code") String code,
            @ModelAttribute(name = "text") String text,
            Model model
    ) {
        if (code.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("code", code);
        model.addAttribute("text", text);
        return "default/result";
    }
}
