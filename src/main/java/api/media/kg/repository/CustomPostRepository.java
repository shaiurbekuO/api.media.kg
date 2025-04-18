package api.media.kg.repository;

import api.media.kg.dto.FilterResultDTO;
import api.media.kg.dto.post.PostAdminFilterDTO;
import api.media.kg.dto.post.PostFilterDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomPostRepository {
    private final EntityManager entityManager;
    public FilterResultDTO filter(PostFilterDTO filter, int page, int size) {
        StringBuilder queryBuilder = new StringBuilder("where p.visible = true and p.status = 'ACTIVE' ");
        Map<String, Object> params = new HashMap<>();

        // Проверка на наличие фильтра по названию
        if (filter.getQuery() != null && !filter.getQuery().isEmpty()) {
            queryBuilder.append(" and lower(p.title) like :query ");
            params.put("query", "%" + filter.getQuery().toLowerCase() + "%");
        }

        // Проверка на наличие фильтра по exceptId
        if (filter.getExceptId() != null) {
            queryBuilder.append(" and p.exceptId != :exceptId ");
            params.put("exceptId", filter.getExceptId());
        }

        String baseQuery = "Select p from PostEntity p ";
        String countQuery = "Select count(p) from PostEntity p ";

        // Получение данных
        Query selectQuery = entityManager.createQuery(baseQuery + queryBuilder + "order by p.createdDate desc");
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);
        params.forEach(selectQuery::setParameter);

        // Получение общего количества
        Query countQueryObj = entityManager.createQuery(countQuery + queryBuilder.toString());
        params.forEach(countQueryObj::setParameter);

        return new FilterResultDTO<>(
                selectQuery.getResultList(),
                (Long) countQueryObj.getSingleResult()
        );
    }


    public FilterResultDTO<Object[]> filter(PostAdminFilterDTO filter, int page, int size) {
        StringBuilder queryBuilder = new StringBuilder(" where p.visible = true ");
        Map<String, Object> params = new HashMap<>();

        if (filter.getProfileQuery() != null && !filter.getProfileQuery().isBlank()) {
            queryBuilder.append(" and (lower(pr.name) like :profileQuery or lower(pr.username) like :profileQuery) ");
            params.put("profileQuery", "%" + filter.getProfileQuery().toLowerCase() + "%");
        }

        if (filter.getPostQuery() != null && !filter.getPostQuery().isBlank()) {
            queryBuilder.append(" and (lower(p.title) like :postQuery or p.id = :postId ");
            params.put("postQuery", "%" + filter.getPostQuery().toLowerCase() + "%");
            params.put("postId", filter.getPostQuery().toLowerCase());
            try {
                Long postId = Long.parseLong(filter.getPostQuery());
                queryBuilder.append(" or p.id = :postQueryExact ");
                params.put("postQueryExact", postId);
            } catch (NumberFormatException e) {
                // Ignore if not a number
            }
            queryBuilder.append(") ");
        }

        String baseQuery = "select p.id, p.title, p.photoId, p.createdDate, p.status, pr.id, pr.name, pr.username " +
                "from PostEntity p inner join p.profile pr";

        String countQuery = "select count(p) from PostEntity p inner join p.profile pr";

        Query selectQuery = entityManager.createQuery(baseQuery + queryBuilder + " order by p.createdDate desc");
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);
        params.forEach(selectQuery::setParameter);
        List<Object[]> entityList = selectQuery.getResultList();

        Query countQueryObj = entityManager.createQuery(countQuery + queryBuilder);
        params.forEach(countQueryObj::setParameter);
        Long count = (Long) countQueryObj.getSingleResult();

        return new FilterResultDTO<>(entityList, count);
    }






}

