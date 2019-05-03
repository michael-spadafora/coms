package seafoamgreen.coms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import seafoamgreen.coms.model.Comic;
import seafoamgreen.coms.model.Series;
import seafoamgreen.coms.model.User;
import seafoamgreen.coms.repositories.ComicRepository;
import seafoamgreen.coms.repositories.SeriesRepository;
import seafoamgreen.coms.repositories.UserRepository;

@Service
public class InteractionService {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    
	public void addVote(String comicId, int val, String username) {
        User user = userRepository.findByUsername(username);
        Comic comic = comicRepository.findByComicId(comicId);

        if (val == 1) {
            user.getDownvotedComicIds().remove(comicId);
            comic.getDownvoters().remove(username);   

            user.getUpvotedComicIds().add(comicId);
            comic.getUpvoters().add(username);
        }
        else {
            user.getUpvotedComicIds().remove(comicId);
            comic.getUpvoters().remove(username);

            user.getDownvotedComicIds().add(comicId);
            comic.getDownvoters().add(username);
        }

        userRepository.save(user);
        comicRepository.save(comic);
	}


	public void removeVote(String comicId, String username) {
        User user = userRepository.findByUsername(username);
        Comic comic = comicRepository.findByComicId(comicId);

        user.getUpvotedComicIds().remove(comicId);
        comic.getUpvoters().remove(username);
        
        user.getDownvotedComicIds().remove(comicId);
        comic.getDownvoters().remove(username);

        userRepository.save(user);
        comicRepository.save(comic);
        
    }

    public int checkVote(String comicId, String username) {
        User user = userRepository.findByUsername(username);
        if (user.getDownvotedComicIds().contains(comicId)) {
            return -1;
        }
        if (user.getUpvotedComicIds().contains(comicId)) {
            return 1;
        }
        return 0;
    }


	public void vote(String comicId, int val, String username) {
        int currVote = checkVote(comicId, username);
        if (val == currVote) {
            removeVote(comicId, username);
        }
        else addVote(comicId, val, username);
    }


	public void subscribeByComic(String comicId, String username) {
        Comic c = comicRepository.findByComicId(comicId);
        //get series
        Series s = seriesRepository.findById(c.getSeriesID()).get();
        s.getSubscriberList().add(username);

        User user = userRepository.findByUsername(username);
        user.getSubscriptions().add(c.getSeriesID());

        //get 
	}


	public void unsubscribeByComic(String comicId, String username) {
        Comic c = comicRepository.findByComicId(comicId);
        //get series
        Series s = seriesRepository.findById(c.getSeriesID()).get();
        s.getSubscriberList().add(username);

        User user = userRepository.findByUsername(username);
        user.getSubscriptions().remove(c.getSeriesID());

	}

	public void subscribe(String seriesId, String username) {
        //get series
        Series s = seriesRepository.findById(seriesId).get();
        s.getSubscriberList().add(username);

        User user = userRepository.findByUsername(username);
        user.getSubscriptions().add(seriesId);

        //get 
	}

    public void unsubscribe(String seriesId, String username) {
        //get series
        Series s = seriesRepository.findById(seriesId).get();
        s.getSubscriberList().remove(username);

        User user = userRepository.findByUsername(username);
        user.getSubscriptions().remove(seriesId);

        //get 
	}
}