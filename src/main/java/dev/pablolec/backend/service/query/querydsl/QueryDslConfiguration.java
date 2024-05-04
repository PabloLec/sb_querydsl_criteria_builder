package dev.pablolec.backend.service.query.querydsl;

import com.querydsl.core.types.dsl.EntityPathBase;
import dev.pablolec.backend.db.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class QueryDslConfiguration {
    private final Map<String, EntityPathBase<?>> mappings = new HashMap<>();

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
