package seafoamgreen.coms.services;



import seafoamgreen.coms.model.Series;

import seafoamgreen.coms.repositories.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

//TODO: let user subscribe to series
@Service
public class SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    //Create
    public Series create(String seriesName, String username)
    {
        Series series = new Series(seriesName,  username);
        seriesRepository.save(series);
        return series;
    }

    //Update

    public Optional<Series> findByID(String id)
    {
        return seriesRepository.findById(id);
    }

    public Series addComic(String seriesID, String comicID)
    {
        //TODO: fix this!!
        Series series = seriesRepository.findById(seriesID).get();
        series.getComicList().add(comicID);
        seriesRepository.save(series);
        return series;
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


