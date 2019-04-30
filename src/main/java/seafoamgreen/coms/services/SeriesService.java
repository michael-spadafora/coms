package seafoamgreen.coms.services;



import seafoamgreen.coms.model.Series;

import seafoamgreen.coms.repositories.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

//TODO: let user subscribe to series
//TODO: notify all users on new comic post in series
@Service
public class SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    //Create
    public Series create(String seriesName, String UserID, String username)
    {
        Series series = new Series(seriesName, UserID, username);
        seriesRepository.save(series);
        return series;
    }

    //Update

    public Optional<Series> findByID(String id)
    {
        return seriesRepository.findById(id);
    }

    //Add comic to series list

    public void deleteAll() { seriesRepository.deleteAll(); }


    public List<Series> findByUserId(String userId) {
        return seriesRepository.findByUserID(userId);
    }

    public List<Series> findByUsername(String username) {
        return seriesRepository.findByUsername(username);
    }



}


