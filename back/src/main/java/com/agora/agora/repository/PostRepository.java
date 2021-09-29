package com.agora.agora.repository;

import com.agora.agora.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Integer> {
}
