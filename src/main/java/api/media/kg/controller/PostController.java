package api.media.kg.controller;

import api.media.kg.dto.SimpleResponse;
import api.media.kg.dto.post.*;
import api.media.kg.enums.AppLanguage;
import api.media.kg.service.PostService;
import api.media.kg.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        log.info("Create post request: {}", dto);
        return ResponseEntity.ok(postService.create(dto));
    }

    @GetMapping("/profile")
    @Operation(summary = "Get profile posts", description = "Get all profile post list")
    public ResponseEntity<Page<PostDTO>> getProfilePosts(@RequestParam(value = "page", defaultValue = "1") int page,
                                                         @RequestParam(value = "size", defaultValue = "12") int size) {
        return ResponseEntity.ok(postService.getProfilePosts(PageUtil.getPage(page), size));
    }

    @GetMapping("public/{id}")
    @Operation(summary = "Get post by id", description = "Api return post by id")
    public ResponseEntity<PostDTO> getPostById(@PathVariable String id) {
        return ResponseEntity.ok(postService.getById(id));
    }
    @PutMapping("/{id}")
    @Operation(summary = "Update post", description = "Api used for update post")
    public ResponseEntity<SimpleResponse> updatePost(@PathVariable("id") String id,@Valid @RequestBody PostCreateDTO dto,
                                                     @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
        return ResponseEntity.ok(postService.update(id, dto, lang));
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete post", description = "Api used for delete post")
    public ResponseEntity<SimpleResponse> deletePost(@PathVariable("id") String id,
                                                     @RequestHeader(value = "Accept-Language", defaultValue = "EN") AppLanguage lang) {
      return ResponseEntity.ok(postService.delete(id, lang));
    }

    @PostMapping("/public/filter")
    @Operation(summary = "Filter posts", description = "Api used for filter posts")
    public ResponseEntity<Page<PostDTO>> filter(@Valid @RequestBody PostFilterDTO dto,
                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.filter(dto, PageUtil.getPage(page), size));
    }

    @PostMapping("/public/similar")
    @Operation(summary = "Get similar post list", description = "Api used for get similar post list")
    public ResponseEntity<List<PostDTO>> similarPostList(@Valid @RequestBody SimilarPostListDTO dto) {
        return ResponseEntity.ok(postService.getSimilarPostList(dto));
    }

    @PostMapping("/filter")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Admin filter", description = "Api used for admin filter")
    public ResponseEntity<Page<PostDTO>> filter(@Valid @RequestBody PostAdminFilterDTO dto,
                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<PostDTO> postDTOPage = postService.adminFilter(dto, PageUtil.getPage(page), size);
        return ResponseEntity.ok(postDTOPage);
    }
}
