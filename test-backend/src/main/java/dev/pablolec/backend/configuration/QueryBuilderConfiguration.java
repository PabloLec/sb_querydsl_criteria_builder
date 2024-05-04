package dev.pablolec.backend.configuration;

import com.querydsl.core.types.dsl.EntityPathBase;
import dev.pablolec.querybuilder.CriteriaQueryBuilder;
import dev.pablolec.querybuilder.EntityPathResolver;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.pablolec.backend.db.model.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class QueryBuilderConfiguration {
    private final Map<String, EntityPathBase<?>> mappings = new HashMap<>();

    @Bean
    public EntityPathResolver queryDslClassMapper(Map<String, EntityPathBase<?>> entityMappings) {
        return new EntityPathResolver(entityMappings);
    }

    @Bean
    public CriteriaQueryBuilder dynamicQueryService(EntityManager entityManager, EntityPathResolver entityPathResolver) {
        return new CriteriaQueryBuilder(entityManager, entityPathResolver);
    }

    @Bean
    public Map<String, EntityPathBase<?>> entityMappings() {
        mappings.put("library", QLibrary.library);
        mappings.put("address", QAddress.address);
        mappings.put("author", QAuthor.author);
        mappings.put("book", QBook.book);
        mappings.put("tag", QTag.tag);
        mappings.put("bookTag", QBookTag.bookTag);
        mappings.put("user", QUser.user);
        mappings.put("borrowedBook", QBorrowedBook.borrowedBook);
        mappings.put("review", QReview.review);
        mappings.put("publisher", QPublisher.publisher);
        mappings.put("bookPublisher", QBookPublisher.bookPublisher);
        mappings.put("staff", QLibraryStaff.libraryStaff);
        mappings.put("membership", QMembership.membership);
        mappings.put("event", QLibraryEvent.libraryEvent);
        mappings.put("participant", QEventParticipant.eventParticipant);

        return mappings;
    }
}
