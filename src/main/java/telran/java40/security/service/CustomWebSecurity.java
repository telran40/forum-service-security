package telran.java40.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.java40.forum.dao.PostRepository;
import telran.java40.forum.model.Post;

@Service("customSecurity")
public class CustomWebSecurity {
	PostRepository postRepository;

	@Autowired
	public CustomWebSecurity(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	public boolean checkPostAuthority(String postId, String userName) {
		Post post = postRepository.findById(postId).orElse(null);
		return post != null && userName.equals(post.getAuthor());
	}

}
