package seafoamgreen.coms.services;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import seafoamgreen.coms.model.Comic;
import seafoamgreen.coms.model.Series;
import seafoamgreen.coms.model.User;
import seafoamgreen.coms.repositories.ComicRepository;
import seafoamgreen.coms.repositories.SeriesRepository;
import seafoamgreen.coms.repositories.UserRepository;

@Service
public class SearchService {
    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeriesRepository seriesRepository;


    public List<Comic> findAllByTag(String tag) {
        List<Comic> comics = comicRepository.findAllByTag(tag);
        List<Comic> publishedComics = new ArrayList<Comic>();

        //wont display unpublished comics
        for(Comic c: comics) {
            if (c.isPublished()) {
                publishedComics.add(c);
            }
        }
        return publishedComics;
    }

    public List<User> findAllByUsername(String username) {
        // List<User> users = userRepository.findAll();
        String usernameRegex = ".*"+username +".*";
        List<User> users = userRepository.findAllByUsernameRegex(usernameRegex);
        return users;
    }

    public List<Comic> findAllByComicTitle(String comicName) {
        String comicNameRegex = ".*"+ comicName +".*";
        List<Comic> comics = comicRepository.findAllByComicNameRegex(comicNameRegex);

        List<Comic> publishedComics = new ArrayList<Comic>();

        //wont display unpublished comics
        for(Comic c: comics) {
            if (c.isPublished()) {
                publishedComics.add(c);
            }
        }

        return publishedComics;
    }

    public List<Series> findAllBySeriesName(String seriesName) {
        String seriesNameRegex = ".*"+seriesName +".*";
        List<Series> series = seriesRepository.findAllBySeriesNameRegex(seriesNameRegex);

        return series;
    }

    public List<Comic> findAllComicsByUsername(String username) {
        List<Comic> comics = comicRepository.findAllByUsername(username);
        return comics;
    }

}

