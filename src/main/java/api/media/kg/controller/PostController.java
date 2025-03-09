package api.media.kg.controller;

import api.media.kg.dto.post.PostDTO;
import api.media.kg.util.SpringSecurityUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    @PostMapping("/create")
    public String createPost(@RequestBody PostDTO postDTO) {
        System.out.println(SpringSecurityUtil.getCurrentProfile());
        System.out.println(SpringSecurityUtil.getCurrentUserId());
        return "post created";
    }
}
