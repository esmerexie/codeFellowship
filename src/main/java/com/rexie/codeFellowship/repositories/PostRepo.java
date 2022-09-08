package com.rexie.codeFellowship.repositories;

import com.rexie.codeFellowship.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {
}
