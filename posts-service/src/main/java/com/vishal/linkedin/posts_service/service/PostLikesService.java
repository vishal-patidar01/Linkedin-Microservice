package com.vishal.linkedin.posts_service.service;

import com.vishal.linkedin.posts_service.entity.PostLike;
import com.vishal.linkedin.posts_service.exceptions.BadRequestException;
import com.vishal.linkedin.posts_service.exceptions.ResourceNotFoundException;
import com.vishal.linkedin.posts_service.repository.PostLikeRepository;
import com.vishal.linkedin.posts_service.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikesService {

    private final PostLikeRepository postLikeRepository;
    private final PostsRepository postsRepository;

    public void likePost(Long postId, Long userId) {
        log.info("Attempting to like the post with id: {}", postId);
        boolean exists = postsRepository.existsById(postId);
        if(!exists) throw  new ResourceNotFoundException("Post not found with id: "+ postId);

        boolean alreadyLinked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(alreadyLinked) throw new BadRequestException("can not like the same post again !");

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);
        log.info("Post with id: {} liked successfully", postId);

    }

    public void unlikePost(Long postId, Long userId) {
        log.info("Attempting to unlike the post with id: {}", postId);
        boolean exists = postsRepository.existsById(postId);
        if(!exists) throw  new ResourceNotFoundException("Post not found with id: "+ postId);

        boolean alreadyLinked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if(!alreadyLinked) throw new BadRequestException("can not unlike the post which is not liked!");

       postLikeRepository.deleteByUserIdAndPostId(userId, postId);
        log.info("Post with id: {} unliked successfully", postId);
    }
}
