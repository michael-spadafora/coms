package seafoamgreen.coms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import seafoamgreen.coms.model.Message;
import seafoamgreen.coms.model.Series;
import seafoamgreen.coms.model.User;
import seafoamgreen.coms.repositories.MessageRepository;
import seafoamgreen.coms.repositories.SeriesRepository;
import seafoamgreen.coms.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;


@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    //Create
    public String sendMessage(String title, String body, String fromUsername, String toUsername) {
        Message mes = new Message (title, body, fromUsername, toUsername, false);
        messageRepository.save(mes);
        User from = userRepository.findByUsername(fromUsername);
        User to = userRepository.findByUsername(toUsername);
        if (from == null) {
            return "invalid from";
        }
        if (to == null) {
            return "The user you are sending to does not exist";
        }
        from.getMessagesSent().add(mes.getId());
        to.getMessagesReceived().add(mes.getId());
        userRepository.save(from);
        userRepository.save(to);
        return "successfully sent message to " + toUsername;
    }

    public void delete(String username, String messageID) {
        messageRepository.deleteById(messageID);
        User user = userRepository.findByUsername(username);
        ArrayList<String> messages = user.getMessagesReceived();
        messages.remove(messageID);
        user.setMessagesRecieved(messages);
        userRepository.save(user);

    }

   
    public Message viewMessage(String msgID) {
        Message msg = messageRepository.findById(msgID).get();

        if (msg == null) {
            return null;
        }

        msg.setRead(true);
        return msg;

    }

    //TODO: change to viewing sessions rather than params
    //TODO: test this
	public String sendSystemMessage(String toUsername, String title, String body) {
        String fromUsername = "system";
        Message mes = new Message (title, body, fromUsername, toUsername, false);
        messageRepository.save(mes);
        // User from = userRepository.findByUsername(fromUsername);
        User to = userRepository.findByUsername(toUsername);
        // if (from == null) {
        //     return "invalid from";
        // }
        if (to == null) {
            return "The user you are sending to does not exist";
        }
        // from.getMessagesSent().add(mes.getId());
        to.getMessagesReceived().add(mes.getId());
        // userRepository.save(from);
        userRepository.save(to);
        return "successfully sent message to " + toUsername;
	}

	public void sendSubscriptionUpdate(String toN, String seriesId) {
        Series s = seriesRepository.findById(seriesId).get();
        String seriesName = s.getSeriesName();
        String title = "New comic from " + seriesName;
        String body = "A new comic from your subscribed series, " + seriesName + " has been released!";
        
        sendSystemMessage(toN, title, body);



	}
}
    

