package com.sean.newspapersproject.service;

import com.sean.newspapersproject.entity.Comment;
import com.sean.newspapersproject.entity.Like;
import com.sean.newspapersproject.entity.Magazine;
import com.sean.newspapersproject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final CommentService commentService;
    private final MagazineService magazineService;
    private final LikeService likeService;
    private final MagazineFacade magazineFacade;

    @Autowired
    public UserFacadeImpl(UserService userService, CommentService commentService, MagazineService magazineService,
                          LikeService likeService, MagazineFacade magazineFacade) {
        this.userService = userService;
        this.commentService = commentService;
        this.magazineService = magazineService;
        this.likeService = likeService;
        this.magazineFacade = magazineFacade;
    }

    @Override
    public void deleteUser(Long id) {
        User user = userService.getUserById(id);

        List<Comment> comments = commentService.getCommentsByUser(user);
        for (Comment comment : comments) {
            commentService.delete(comment);
        }

        Magazine magazineByAuthor = magazineService.getMagazineByAuthor(userService.getUserById(id));
        if (magazineByAuthor != null) {
            magazineFacade.deleteMagazine(magazineByAuthor.getMagazineId());
        }

        List<Like> likes = likeService.getLikesOfUser(user);
        likes.forEach(likeService::delete);

        userService.delete(user);
    }
}
