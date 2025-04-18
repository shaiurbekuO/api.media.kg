package api.media.kg.service;

import api.media.kg.dto.FilterResultDTO;
import api.media.kg.dto.ProfileDTO;
import api.media.kg.dto.SimpleResponse;
import api.media.kg.dto.post.*;
import api.media.kg.entity.PostEntity;
import api.media.kg.enums.AppLanguage;
import api.media.kg.enums.GeneralStatus;
import api.media.kg.enums.ProfileRole;
import api.media.kg.exception.BadRequestException;
import api.media.kg.repository.CustomPostRepository;
import api.media.kg.repository.PostRepository;
import api.media.kg.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AttachService attachService;
    private final CustomPostRepository customRepository;
    private final ResourceBundleService bundleService;

    public PostDTO create(PostCreateDTO dto){
        PostEntity entity = new PostEntity();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setPhotoId(dto.getPhoto().getId());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        entity.setStatus(GeneralStatus.NOT_ACTIVE);
        postRepository.save(entity);
        return toInfoDto(entity);
    }
    public Page<PostDTO> getProfilePosts(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Long profileId = SpringSecurityUtil.getCurrentUserId();
        Page<PostEntity> result = postRepository.getAllByProfileAndVisibleTrueOrderByCreatedDateDesc(profileId, pageRequest);
        List<PostDTO> list = result.getContent().stream().map(this::toInfoDto).toList();
        result.getTotalElements();
        return new PageImpl<>(list, pageRequest, result.getTotalElements());
    }
    public PostDTO getById(String id){
           PostEntity entity = findById(id);
           return toDto(entity);
    }

    public SimpleResponse update(String id, PostCreateDTO dto, AppLanguage lang){
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
        return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("post.update.successful", lang));
    }

    public SimpleResponse delete(String id, AppLanguage lang){
        PostEntity entity = findById(id);
        Long profileId = SpringSecurityUtil.getCurrentUserId();
        if(!SpringSecurityUtil.hasRole(ProfileRole.ROLE_ADMIN) && !entity.getProfileId().equals(profileId)){
            throw new BadRequestException("You don't have permission to delete this post");
        }
        postRepository.delete(id);
        return new SimpleResponse(HttpStatus.OK, bundleService.getMessage("post.delete.successful", lang));
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

    public List<PostDTO> getSimilarPostList(SimilarPostListDTO dto) {
        List<PostEntity> list = postRepository.getSimilarPostList(dto.getExceptId());
        return list.stream().map(this::toInfoDto).toList();
    }


    public Page<PostDTO> adminFilter(PostAdminFilterDTO dto, int page, int size) {
        FilterResultDTO<Object[]> resultDTO = customRepository.filter(dto, page, size);
        List<PostDTO> dtoList = resultDTO.getList().stream()
                .map(this::toDto).toList();
        return new PageImpl<>(dtoList, PageRequest.of(page, size), resultDTO.getCount());
    }


    public PostDTO toDto(Object[] obj) {
        PostDTO post = new PostDTO();
        post.setId((String) obj[0]); // ID
        post.setTitle((String) obj[1]); // Title

        if (obj[2] != null) {
            post.setPhoto(attachService.attachDTO((String) obj[2])); // Photo
        }
        post.setCreatedDate((LocalDateTime) obj[3]); // CreatedDate
        post.setStatus((GeneralStatus) obj[4]);  // Status
        ProfileDTO profile = new ProfileDTO();
        profile.setId((Long) obj[5]); // Profile ID
        profile.setName((String) obj[6]); // Profile Name
        profile.setUsername((String) obj[7]); // Profile Username

        post.setProfile(profile);
        return post;
    }



}
