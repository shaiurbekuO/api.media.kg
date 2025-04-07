package api.media.kg.controller;

import api.media.kg.dto.post.PostCreateDTO;
import api.media.kg.dto.post.PostDTO;
import api.media.kg.dto.post.PostFilterDTO;
import api.media.kg.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@Tag(name = "PostController", description = "Api set for work with Posts")
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("")
    @Operation(summary = "Create post", description = "Create post")
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostCreateDTO dto) {
        postService.create(dto);
        return ResponseEntity.ok(postService.create(dto));
    }

    @GetMapping("/profile")
    @Operation(summary = "Get profile posts", description = "Get all profile post list")
    public ResponseEntity<List<PostDTO>> getProfilePosts() {
        return ResponseEntity.ok(postService.getProfilePosts());
    }

    @PostMapping("public/{id}")
    @Operation(summary = "Get post by id", description = "Api return post by id")
    public ResponseEntity<PostDTO> getPostById(@PathVariable String id) {
        return ResponseEntity.ok(postService.getById(id));
    }
    @PutMapping("/{id}")
    @Operation(summary = "Update post", description = "Api used for update post")
    public ResponseEntity<PostDTO> updatePost(@PathVariable("id") String id,@Valid @RequestBody PostCreateDTO dto) {
        return ResponseEntity.ok(postService.update(id, dto));
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete post", description = "Api used for delete post")
    public ResponseEntity<Boolean> deletePost(@PathVariable("id") String id) {
        return ResponseEntity.ok(postService.delete(id));
    }

    @PostMapping("/public/filter")
    @Operation(summary = "Filter posts", description = "Api used for filter posts")
    public ResponseEntity<Page<PostDTO>> filter(@Valid @RequestBody PostFilterDTO dto,
                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.filter(dto, page-1, size));
    }
}
