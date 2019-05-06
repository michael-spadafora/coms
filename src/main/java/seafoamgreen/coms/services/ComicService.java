package seafoamgreen.coms.services;

import seafoamgreen.coms.model.Comic;

import seafoamgreen.coms.model.Panel;
import seafoamgreen.coms.model.Series;
import seafoamgreen.coms.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import seafoamgreen.coms.repositories.ComicRepository;
import seafoamgreen.coms.repositories.PanelRepository;
import seafoamgreen.coms.repositories.SeriesRepository;
import seafoamgreen.coms.repositories.UserRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;



@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private PanelRepository panelRepository;

    @Autowired
    private PanelService panelService;

    @Autowired
    private UserRepository userRepository;

    public Comic create(String Username, String comicName, String SeriesID, String tagString, String publishDate) //need to find a way to implement time
    {
        Comic comic = new Comic(Username, comicName, SeriesID);
        comic.setTags(tokenizeTagString(tagString));
        comic.setDateTime(publishDate);
        Comic newComic = comicRepository.save(comic);
        Panel firstPanel = new Panel(Username, newComic.getId(), "{\"version\":\"2.7.0\",\"objects\":[]}");
        panelRepository.save(firstPanel);
        return comic;
    }

    public List<Comic> getEditableComicsByUsername(String username) {
        List<Comic> comics = findAllByUsername(username);
        List<Comic> ret = new ArrayList<Comic>();
        for (Comic c: comics) {
            if (!c.isPublished()) {
                ret.add(c);
            }
        }
        return ret;
    }





    //Update

    //Id or comic name? idk yet
    public void addPanel(String comicID, String panel)
    {

        Comic comic = comicRepository.findByComicId(comicID);
        comic.addPanel(panel);
        comicRepository.save(comic);
    }

    public Comic findById(String id) { return comicRepository.findByComicId(id);}

    public List<Comic> findAllByUsername(String username) {

        System.out.println("COMIC SERVICE FIND ALL COMICS BY USERNAME: " + username);
        List<Comic> list = comicRepository.findByUsername(username);
        return list;

    }

    public List<Comic> findAllByTag(String tag) {
        List<Comic> comics = comicRepository.findAllByTag(tag);
        return comics;
    }

    public void deleteById(String id) { comicRepository.deleteById(id) ;}

    public void deleteAll() { comicRepository.deleteAll(); }


    public void addTag(String comicID, String tag)
    {
        Comic comic = comicRepository.findByComicId(comicID);
        comic.addTag(tag);
        comicRepository.save(comic);
    }

    public Comic getRandomComic() {
        List<Comic> comics = comicRepository.findAll();
        List<Comic> publishedComics = new ArrayList<Comic>();

        //wont display unpublished comics
        for(Comic c: comics) {
            if (c.isPublished()) {
                publishedComics.add(c);
            }
        }
        int size = publishedComics.size();
        int random = (int)Math.random()*size;
        Comic comic = publishedComics.get(random);
        return comic;
    }

    public List<String> tokenizeTagString(String tagString) {
        StringTokenizer multiTokenizer = new StringTokenizer(tagString, ", ");
        ArrayList<String> tagStrings = new ArrayList<>();

        while (multiTokenizer.hasMoreTokens()) {
            tagStrings.add(multiTokenizer.nextToken());
        }
        return tagStrings;
    }

    public void addTags(String comicId, String tagString) {
        List<String> tagStrings = tokenizeTagString(tagString);

        Comic c = comicRepository.findByComicId(comicId);
        for (String tag : tagStrings) {
            c.addTag(tag);
        }

    }

    public Comic publishComic(String comicId, String seriesName, String tagList, String dateTime) {
        Comic c= publishComic(comicId, seriesName, tagList);
        c.setDateTime(dateTime);
        return c;
    }

    public Comic publishComic(String comicId, String seriesName, String tagList) {
        Comic c = comicRepository.findById(comicId).get();
        List<String> tags = tokenizeTagString(tagList);
        c.setTags(tags);
        Series s = seriesRepository.findBySeriesName(seriesName);
        c.setSeriesID(s.getId());

        AWSCredentials credentials = new BasicAWSCredentials(
                "AKIAJIKZPRZSWRVS6SLQ",
                "2IZ4gI/pxi8L82qeIWFl2txPIE1eslMxdbrHpYjq "
        );

        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2)
                .build();

        String bucketName = "coms.com-comics";
        String key = "key";
        String content = "content";

        s3client.putObject(bucketName, "key", content);

        String urlstring = s3client.getUrl(bucketName, key).toString();

        c.setAWSURL(urlstring);
        comicRepository.save(c);

        // s3client.putObject(
        //     bucketName,
        //     "Document/hello.txt",
        //     new File("/Users/user/Document/hello.txt")
        // );



        return c;



    }


    //this will be checking if we have any comics ready to be published. if so, we set published to true.
    //will check every 15 minutes
    @Scheduled(cron = "0 * * ? * *")
    public void scheduleCheck() {
        Date date = new Date();
        // String strDateTimeFormat = "MM-dd-yyyy hh:mm a";
        String strDateTimeFormat = "yyyy-MM-dd";
        DateFormat dateTimeFormat = new SimpleDateFormat(strDateTimeFormat);

        String currDateTime = dateTimeFormat.format(date);

        // System.out.print(currDateTime);

        //do getByDateTime
        List<Comic> comics = comicRepository.findByDateTime(currDateTime);

        for (Comic c: comics) {
            c.setPublished(true);
            comicRepository.save(c);
        }

    }

    public Comic getEditComic(String username, String comicId) {
        Comic c = comicRepository.findByComicId(comicId);
        if (c.getUsername() != username) {
            return null;
        }


        return c;
    }



    public List<String> getPanelObjects(Comic c) {
        ArrayList<String> urls = new ArrayList<>();
        List<String> panels = c.getPanelList();
        for (String s: panels) {
            urls.add(panelService.getBlob(s));
        }

        return urls;
    }

    public List<Comic> findAllBySeriesId(String id) {

        return comicRepository.findBySeriesID(id);

    }

    public void addToHistory(String username, String comicID) {
        User user = userRepository.findByUsername(username);
        user.addComicToHistory(comicID);

    }
}

