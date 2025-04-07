package api.media.kg.service;

import api.media.kg.dto.FilterResultDTO;
import api.media.kg.dto.ProfileDTO;
import api.media.kg.dto.post.PostCreateDTO;
import api.media.kg.dto.post.PostDTO;
import api.media.kg.dto.post.PostFilterDTO;
import api.media.kg.entity.PostEntity;
import api.media.kg.enums.ProfileRole;
import api.media.kg.exception.BadRequestException;
import api.media.kg.repository.CustomRepository;
import api.media.kg.repository.PostRepository;
import api.media.kg.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AttachService attachService;
    private final CustomRepository customRepository;

    public PostDTO create(PostCreateDTO dto){
        PostEntity entity = new PostEntity();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setPhotoId(dto.getPhoto().getId());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        postRepository.save(entity);
        return toInfoDto(entity);
    }
    public List<PostDTO> getProfilePosts(){
        Long profileId = SpringSecurityUtil.getCurrentUserId();
        List<PostEntity> entities = postRepository.getAllByProfileAndVisibleTrue(profileId);
        return entities.stream().map(this::toInfoDto).toList();
    }
    public PostDTO getById(String id){
           PostEntity entity = findById(id);
           return toDto(entity);
    }

    public PostDTO update(String id, PostCreateDTO dto){
        PostEntity entity = findById(id);
        Long profileId = SpringSecurityUtil.getCurrentUserId();
        if(!SpringSecurityUtil.hasRole(ProfileRole.ROLE_ADMIN) && !entity.getProfileId().equals(profileId)){
            throw new BadRequestException("You don't have permission to edit this post");

        }
        String deletePhotoId = null;
        if(!dto.getPhoto().getId().equals(entity.getPhotoId())){
           deletePhotoId =entity.getPhoto().getId();
        }
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setPhotoId(dto.getPhoto().getId());
        postRepository.update(entity);
        if (deletePhotoId != null){
            attachService.delete(deletePhotoId);
        }
        return toInfoDto(entity);
    }

    public Boolean delete(String id){
        PostEntity entity = findById(id);
        Long profileId = SpringSecurityUtil.getCurrentUserId();
        if(!SpringSecurityUtil.hasRole(ProfileRole.ROLE_ADMIN) && !entity.getProfileId().equals(profileId)){
            throw new BadRequestException("You don't have permission to delete this post");
        }
        postRepository.delete(id);
        return true;
    }

    public Page<PostDTO> filter(PostFilterDTO dto, int page, int size){
        FilterResultDTO<PostEntity> result = customRepository.filter(dto, page, size);
        List<PostDTO> list = result.getList().stream()
                .map(this::toInfoDto).toList();
        return new PageImpl<>(list, PageRequest.of(page, size), result.getCount());
    }

    public PostDTO toDto(PostEntity entity){
        PostDTO dto = new PostDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setPhoto(attachService.attachDTO(entity.getPhotoId()));
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
    public PostDTO toInfoDto(PostEntity entity){
        PostDTO dto = new PostDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPhoto(attachService.attachDTO(entity.getPhotoId()));
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public PostEntity findById(String id){
        return postRepository.findById(id).orElseThrow(() -> new BadRequestException("Post not found "));
    }

}
