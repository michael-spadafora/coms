package seafoamgreen.coms.repositories;

import seafoamgreen.coms.model.UserReport;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserReportRepository extends MongoRepository<UserReport, String> {

}