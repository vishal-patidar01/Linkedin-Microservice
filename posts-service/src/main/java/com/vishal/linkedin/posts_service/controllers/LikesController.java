package com.vishal.linkedin.posts_service.controllers;

import com.vishal.linkedin.posts_service.service.PostLikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikesController {

    private final PostLikesService postLikesService;

    @PostMapping("/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {
        postLikesService.likePost(postId, 1L);
        return  ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId) {
        postLikesService.unlikePost(postId, 1L);
        return  ResponseEntity.noContent().build();
    }
}
