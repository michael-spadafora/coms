package seafoamgreen.coms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import seafoamgreen.coms.model.Comic;
import seafoamgreen.coms.model.Message;
import seafoamgreen.coms.model.Panel;
import seafoamgreen.coms.model.User;
import seafoamgreen.coms.repositories.ComicRepository;
import seafoamgreen.coms.repositories.MessageRepository;
import seafoamgreen.coms.repositories.UserRepository;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private PanelService panelService;

    // Create
    public User create(String username, String password) {
        username = username.toLowerCase();
        if (userRepository.findByUsername(username) != null) {
            return null;
        }

        String encrypted = encrypt(password);
        User user = new User(username, encrypted);
        user.setProfilePictureUrl("https://s3.us-east-2.amazonaws.com/seafoamgreen/person+1.png");
        user.setProfilePictureBlob(this.getBlob(username));
        //use default for now
        userRepository.save(user);
        return user;
    }

    private String encrypt(String password)  {
        // StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            String generatedPassword = sb.toString();


            // String encrypted = passwordEncryptor.encryptPassword(password);
            return generatedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User login(String username, String password) {
        username = username.toLowerCase();
        User usr = userRepository.findByUsername(username);
        if (usr == null) {
            return null;
        }

        String entered = encrypt(password);
        // StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        if (entered.equals(usr.getPassword())) {
            return usr;
        }

        else return null;

    }
    //Retrieve... use this when
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() { return userRepository.findAll(); }

    //Update
    public User update(String username, String password) {
        User user = userRepository.findByUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }

    //Delete
    public void deleteAll() { userRepository.deleteAll(); }

    public void delete(String username) {
        User user = userRepository.findByUsername(username);
        userRepository.delete(user);
    }


    public List<Message> getInbox(String username){
        User usr = userRepository.findByUsername(username);
        if(usr == null) {
            return null;
        }

        List<String> msgIds = usr.getMessagesReceived();

        System.out.println("User messages " + msgIds);


        List<Message> ret = new ArrayList<>();

        for (String  s : msgIds) {
            ret.add(messageRepository.findById(s).get());
        }

        return ret;

    }
    public User update(User user)
    {
        userRepository.save(user);
        return user;

    }

    public List<Message> getSent(String username){
        User usr = userRepository.findByUsername(username);
        if(usr == null) {
            return null;
        }
        List<String> msgIds = usr.getMessagesSent();


        List<Message> ret = new ArrayList<>();
 
        for (String  s : msgIds) {
            ret.add(messageRepository.findById(s).get());
        }

        return ret;
    }

    public List<Comic> getPopular() {
        Sort sort = new Sort(Sort.Direction.DESC, "score");
        List<Comic> popular = comicRepository.findMostPopular(sort);
        List<Comic> ret = getPublished(popular);

        return ret;
    }

    public List<Comic> getPublished(List<Comic> lst) {
        List<Comic> ret = new ArrayList<Comic>();
        for (Comic c : lst) {
            if (c.isPublished()) {
                ret.add(c);
            }
        }
        return ret;

    }

    public List<Comic> getRecommended(String username) {
        User user = userRepository.findByUsername(username);
        List<String> readComics = user.getComicIdHistory();
        List<String> tags = new ArrayList<>();
        for(String comicID: readComics)
        {
            Comic comic = comicRepository.findByComicId(comicID);
            for(String tag: comic.getTags())
                tags.add(tag);
        }
        List<Comic> recommended = new ArrayList<>();
        List<Comic> allComics = comicRepository.findAll();
        for(Comic comic: allComics)
        {
            if(comic.isPublished() && !readComics.contains(comic.getId()))
            {
                for(String tag: comic.getTags())
                    if(tags.contains(tag) && !recommended.contains(comic))
                        recommended.add(comic);
            }
        }
        
        return recommended;
    }

    public List<Comic> getUserHistory(String username) {
        List<Comic> comics = new ArrayList<>();

        User user = userRepository.findByUsername(username);
        List<String> comicIds = user.getComicIdHistory();
 
        for (String id : comicIds) {
            Comic c = comicRepository.findByComicId(id);
            comics.add(c);
        }

        return comics;
    }


    public List<String> getThumbnails(List<Comic> comicList) {
        List<String> thumbs = new ArrayList<>();
        for (Comic c: comicList) {
            List<String> panelIds = c.getPanelList();
            if (panelIds.size() == 0) continue;
            String panel1 = panelIds.get(0);
            if (panel1 == null) continue;
            String blob  = panelService.getBlob(panel1);
            thumbs.add(blob);
        }
        return thumbs;
    }


    public List<Comic> getUsersComics(String username) {
        List<Comic> allComics = comicRepository.findAllByUsername(username);
        List<Comic> comics = new ArrayList<Comic>();
        for(Comic comic: allComics)
            if(comic.isPublished())
                comics.add(comic);
        return comics;
    }

    public List<Comic> getUsersMyList(String username) {
        User user = userRepository.findByUsername(username);
        List<String> myList = user.getMyList();
        List<Comic> ret = new ArrayList<Comic>();
        for (String s:myList) {
            if(comicRepository.findByComicId(s).isPublished())
                ret.add(comicRepository.findByComicId(s));
        }
        return ret;
    }

    public void addToMyList(String username, String comicId) {
        User user = userRepository.findByUsername(username);

        ArrayList<String> myList = user.getMyList();
        if(!myList.contains(comicId))
        {
            myList.add(comicId);
        }
        user.setMyList(myList);
        userRepository.save(user);
    }

    public void removeFromMyList(String username, String comicId) {
        User user = userRepository.findByUsername(username);

        ArrayList<String> myList = user.getMyList();
        if(myList.contains(comicId))
        {
            myList.remove(comicId);
        }
        user.setMyList(myList);
        userRepository.save(user);
    }

	public List<Comic> getComicsFromSubscriptions(User user) {
        List<String> subscriptions = user.getSubscriptions();
        List<Comic> ret = new ArrayList<Comic>();
        for (String subKey: subscriptions) {
            List<Comic> coms = comicRepository.findBySeriesID(subKey);
            for(Comic comic: coms)
                if(comic.isPublished())
                    ret.add(comic);
        }
		return ret;
    }
    
    public List<Comic> getComicsFromSubscriptions(String username) {
        User user = userRepository.findByUsername(username);
        List<String> subscriptions = user.getSubscriptions();
        List<Comic> ret = new ArrayList<Comic>();
        for (String subKey: subscriptions) {
            List<Comic> coms = comicRepository.findBySeriesID(subKey);
            ret.addAll(coms);
        }
		return ret;
	}

	public void addProfilePicture(String username, String blob) {
        User user = userRepository.findByUsername(username);
        if (user == null) return;
        String url = storeBlobInAWS(username, blob);
        user.setProfilePictureUrl(url);
        user.setProfilePictureBlob(this.getBlob(username));
        userRepository.save(user);
    }
    
    private String storeBlobInAWS(String username, String blob) {
        AWSCredentials credentials = new BasicAWSCredentials("AKIAJIKZPRZSWRVS6SLQ",
                "2IZ4gI/pxi8L82qeIWFl2txPIE1eslMxdbrHpYjq ");

        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();

        String bucketName = "coms.com-comics";

        // String key = "key";

        //key = seriesId/comicId/panelNumber
        String key = "profilePictures/" + username;

        s3client.putObject(bucketName, key, blob); //saves
        String urlstring = s3client.getUrl(bucketName, key).toString();
        System.out.println(urlstring);


        return urlstring;
    }

    public String getBlob(String username) {
        AWSCredentials credentials = new BasicAWSCredentials("AKIAJIKZPRZSWRVS6SLQ",
                "2IZ4gI/pxi8L82qeIWFl2txPIE1eslMxdbrHpYjq ");

        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();

        String bucketName = "coms.com-comics";
        String key = "profilePictures/" + username;

        // String key = "5cca2508519e228d91fd3bfa/5cca2508519e228d91fd3bfb/5cca250b519e228d91fd3bfc";

        S3Object obj = s3client.getObject(bucketName, key);

        S3ObjectInputStream s = obj.getObjectContent();
        InputStream stream = s.getDelegateStream();

        String ret = convertStreamToString(stream);
        return ret;
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

