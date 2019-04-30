package seafoamgreen.coms.repositories;


import seafoamgreen.coms.model.ComicReport;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ComicReportRepository extends MongoRepository<ComicReport, String> {

}
