package api.media.kg.repository;

import api.media.kg.dto.FilterResultDTO;
import api.media.kg.dto.post.PostFilterDTO;
import api.media.kg.entity.PostEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomRepository {
    private final EntityManager entityManager;

    public FilterResultDTO filter(PostFilterDTO filter, int page, int size) {
        StringBuilder queryBuilder = new StringBuilder("where p.visible = true ");
        Map<String, Object> params = new HashMap<>();

        if(filter.getQuery() != null && !filter.getQuery().isEmpty()) {
            queryBuilder.append(" and lower(p.title) like :query ");
            params.put("query", "%" + filter.getQuery().toLowerCase() + "%");
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
}

