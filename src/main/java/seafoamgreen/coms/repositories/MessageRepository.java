package seafoamgreen.coms.repositories;


import seafoamgreen.coms.model.Message;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface MessageRepository extends MongoRepository<Message, String> {

    @Query
    Optional<Message> findById(String id);

}