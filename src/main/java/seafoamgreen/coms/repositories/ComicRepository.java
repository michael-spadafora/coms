package seafoamgreen.coms.repositories;

import seafoamgreen.coms.model.Comic;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ComicRepository extends MongoRepository<Comic, String> {


    @Query("{ 'id' : ?0 }")
    Comic findByComicId(String comicID);

    @Query("{ $text : { $search : ?0 } }")
    List<Comic> findAllByKeyword(String keyword);

    @Query(" { 'tags' : ?0 } ")
    List<Comic> findAllByTag(String tag);


    //Returns all comics under a series ID
    @Query("{ 'seriesID' : ?0 }")
    List<Comic> findBySeriesID(String seriesID);

    @Query("{ 'username' : ?0 }")
    List<Comic> findByUsername(String username);

    @Query("{ 'comicName' : { $regex: ?0 } }")
    List<Comic> findAllByComicNameRegex(String comicNameRegex);

    @Query("{ 'dateTime' : ?0 }")
    List<Comic>  findByDateTime(String dateTime);

    @Query("{ 'username' : ?0 }")
    List<Comic> findAllByUsername(String username);

    @Query("{ }")
	List<Comic> findMostPopular(Sort sort);

}