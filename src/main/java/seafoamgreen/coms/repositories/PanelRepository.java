package seafoamgreen.coms.repositories;


import seafoamgreen.coms.model.Panel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PanelRepository extends MongoRepository<Panel, String> {

    

    //This is a custom query where you can search for a Panel based on the Comic name
    //The Panel object has a nested Comic object ("comic"), and a comic object has "comicName" field

    //Returns all Panels under a comic name
    @Query("{ 'comicID' : ?0 }")
    List<Panel> findByComicID(String comicID);

    @Query("{ 'userID' : ?0 }")
    List<Panel> findByUserID(String userID);


    @Query(value="{'comicID': ?0}", delete = true)
    List<Panel> deleteByComicId(String id);
}