package seafoamgreen.coms.repositories;

import seafoamgreen.coms.model.Comment;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {

    @Query("{ 'comicID' : ?0 }")
	List<Comment> findAllByComicId(String comicId, Sort sort);
}