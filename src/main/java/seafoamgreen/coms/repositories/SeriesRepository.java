package seafoamgreen.coms.repositories;

import seafoamgreen.coms.model.Series;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SeriesRepository extends MongoRepository<Series, String> {

    @Query("{ 'seriesName' : ?0 }")
    public Series findBySeriesName(String seriesName);

    @Query("{ 'userID' : ?0 }")
    List<Series> findByUserID(String userID);

    @Query("{ 'seriesName' : { $regex: ?0 } }")
    public List<Series> findAllBySeriesNameRegex(String seriesNameRegex);

    @Query("{ 'username' : ?0 }")
    public List<Series> findByUsername(String username);

}