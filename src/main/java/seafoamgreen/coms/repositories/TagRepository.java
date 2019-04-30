package seafoamgreen.coms.repositories;

import seafoamgreen.coms.model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TagRepository extends MongoRepository<Tag, String> {

}