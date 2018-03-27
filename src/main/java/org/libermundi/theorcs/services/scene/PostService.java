package org.libermundi.theorcs.services.scene;

import org.libermundi.theorcs.domain.jpa.scene.Post;
import org.libermundi.theorcs.domain.jpa.scene.Scene;
import org.libermundi.theorcs.forms.PostForm;
import org.libermundi.theorcs.services.base.BaseService;

import java.util.List;

public interface PostService extends BaseService<Post> {
    List<Post> getAllPosts(Scene scene);
    void sendPost(PostForm postForm);
    void updatePost(PostForm postForm);
}
