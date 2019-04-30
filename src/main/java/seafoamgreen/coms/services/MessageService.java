package seafoamgreen.coms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import seafoamgreen.coms.model.Message;
import seafoamgreen.coms.model.User;
import seafoamgreen.coms.repositories.MessageRepository;
import seafoamgreen.coms.repositories.UserRepository;




@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

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

    public void delete(String Id) {
        Message message = messageRepository.findById(Id).get();
        messageRepository.delete(message);
    }


    public Message viewMessage(String msgID) {
        Message msg = messageRepository.findById(msgID).get();

        if (msg == null) {
            return null;
        }

        msg.setRead(true);
        return msg;

    }
}


