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
        List<Comic> allComics = comicRepository.findAll();
        List<Comic> comics = new ArrayList<Comic>();

        for(Comic comic: allComics)
            for(String t: comic.getTags())
                if(t.toLowerCase().contains(tag.toLowerCase()) ||
                        tag.toLowerCase().contains(t.toLowerCase()))
                    comics.add(comic);

        return comics;
    }

    public List<User> findAllByUsername(String username) {
        List<User> allUsers = userRepository.findAll();
        List<User> users = new ArrayList<User>();
        for(User user: allUsers)
            if(user.getUsername().equalsIgnoreCase(username))
                users.add(user);
        return users;
    }

    public List<Comic> findAllByComicTitle(String comicName) {
        List<Comic> allComics = comicRepository.findAll();
        List<Comic> comics = new ArrayList<Comic>();

        for(Comic comic: allComics)
            if(comic.getComicName().toLowerCase().contains(comicName.toLowerCase()) ||
                    comicName.toLowerCase().contains(comic.getComicName().toLowerCase()))
                comics.add(comic);

        return comics;
    }

    public List<Comic> findAllBySeriesName(String seriesName) {
        List<Comic> allComics = comicRepository.findAll();
        List<Comic> comics = new ArrayList<Comic>();
        for(Comic comic: allComics)
            if(comic.getSeriesName().toLowerCase().contains(seriesName.toLowerCase()) ||
                    seriesName.toLowerCase().contains(comic.getSeriesName().toLowerCase()))
                comics.add(comic);

        return comics;
    }

    public List<Comic> findAllComicsByUsername(String username) {
        List<Comic> allComics = comicRepository.findAll();
        List<Comic> comics = new ArrayList<Comic>();

        for(Comic comic: allComics)
            if(comic.getUsername().toLowerCase().contains(username.toLowerCase()) ||
                    username.toLowerCase().contains(comic.getUsername().toLowerCase()))
                comics.add(comic);

        return comics;
    }
}

