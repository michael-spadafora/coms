package seafoamgreen.coms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import seafoamgreen.coms.model.Comic;
import seafoamgreen.coms.model.Message;
import seafoamgreen.coms.model.User;
import seafoamgreen.coms.repositories.ComicRepository;
import seafoamgreen.coms.repositories.MessageRepository;
import seafoamgreen.coms.repositories.UserRepository;

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
        String encrypted = encrypt(password);
        User user = new User(username, encrypted);
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


        List<Message> ret = new ArrayList<>();

        for (String  s : msgIds) {
            ret.add(messageRepository.findById(s).get());
        }

        return ret;

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


}


