package org.libermundi.theorcs.repositories;

import org.libermundi.theorcs.domain.jpa.scene.Post;
import org.libermundi.theorcs.domain.jpa.scene.Scene;
import org.libermundi.theorcs.repositories.base.UndeletableRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends UndeletableRepository<Post, Long> {
    List<Post> findAllByScene(Scene scene);
}
