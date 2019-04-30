package seafoamgreen.coms.repositories;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import seafoamgreen.coms.model.User;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);

    @Query("{ 'username' : { $regex: ?0 } }")
    List<User> findAllByUsernameRegex(String regex);
}