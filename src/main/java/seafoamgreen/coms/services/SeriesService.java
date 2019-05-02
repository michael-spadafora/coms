package seafoamgreen.coms.services;



import seafoamgreen.coms.model.Comic;
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
        Optional<Series> series = seriesRepository.findById(seriesID);
        series.get().addComic(comicID);
        System.out.println("Adding to series " + series.get().getSeriesName());
        seriesRepository.save(series.get());
        return series.get();

    }

    public void deleteById(String id)
    {
        seriesRepository.deleteById(id);
    }

    //Add comic to series list

    public void deleteAll() { seriesRepository.deleteAll(); }


    public List<Series> findByUserId(String userId) {
        return seriesRepository.findByUserID(userId);
    }

    public List<Series> findAllByUsername(String username) {
        return seriesRepository.findByUsername(username);
    }



}


