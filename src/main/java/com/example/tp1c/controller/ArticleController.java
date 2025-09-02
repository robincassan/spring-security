package com.example.tp1c.controller;

import com.example.tp1c.Article;
import com.example.tp1c.UserApp;
import com.example.tp1c.repository.ArticleRepository;
import com.example.tp1c.repository.UserAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    private UserAppRepository userAppRepository;


    @GetMapping("/list")
    public String listArticles(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "list-articles"; // correspond à list-articles.html
    }

    @PostMapping("/new")
    public String registerArticle(@ModelAttribute Article article, Authentication authentication) {
        // récupérer le username de l'utilisateur connecté
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        // récupérer l'objet UserApp depuis la BDD
        UserApp auteur = userAppRepository.findByEmail(username).orElseThrow();

        // assigner l'auteur à l'article
        article.setAuteur(auteur);

        // sauvegarder l'article
        articleRepository.save(article);

        return "redirect:/article/list"; // redirection vers la liste

    }

}

