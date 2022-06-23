package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.Article;
import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MagazineFacadeImpl implements MagazineFacade {

    private final MagazineService magazineService;
    private final UserService userService;
    private final ArticleService articleService;
    private final ArticleFacade articleFacade;

    @Autowired
    public MagazineFacadeImpl(MagazineService magazineService, ArticleService articleService, ArticleFacade articleFacade,
                              UserService userService) {
        this.magazineService = magazineService;
        this.articleService = articleService;
        this.articleFacade = articleFacade;
        this.userService = userService;
    }

    @Override
    public void deleteMagazine(Long id) {
        Magazine magazine = magazineService.getMagazineById(id);
        List<Article> articles = articleService.getAllArticleByMagazine(magazine);
        for (Article article : articles) {
            articleFacade.deleteArticle(article.getArticleId());
        }
        Set<User> users = magazine.getFollowedUsers();
        for (User user : users) {
            user.getFollowedMagazines().remove(magazine);
        }
        magazineService.delete(magazine);
    }
}
