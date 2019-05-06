package seafoamgreen.coms.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {
 
    @Id
    private String id;
    private String username;
    private String password;

    private ArrayList<String> messagesSentIds = new ArrayList<String>();
    private ArrayList<String> messagesReceivedIds= new ArrayList<String>();

    private ArrayList<String> upvotedComicIds = new ArrayList<String>();
    private ArrayList<String> downvotedComicIds = new ArrayList<String>();

    private boolean isAdmin;
    private ArrayList<String> subscriptions = new ArrayList<String>();

    private ArrayList<String> comicIdHistory = new ArrayList<String>();

    public User() {
    }

    /**
     * @return the comicIdHistory
     */
    public ArrayList<String> getComicIdHistory() {
        return comicIdHistory;
    }

    /**
     * @param comicIdHistory the comicIdHistory to set
     */
    public void setComicIdHistory(ArrayList<String> comicIdHistory) {
        this.comicIdHistory = comicIdHistory;
    }

    public void addComicToHistory(String comicId){
        comicIdHistory.add(0, comicId);
        if (comicIdHistory.size() > 5) {
            comicIdHistory.remove(5);
        }
    }

    /**
     * @return the subscriptions
     */
    public ArrayList<String> getSubscriptions() {
        return subscriptions;
    }

    /**
     * @param subscriptions the subscriptions to set
     */
    public void setSubscriptions(ArrayList<String> subscriptions) {
        this.subscriptions = subscriptions;
    }

    /**
     * @return the downvotedComicIds
     */
    public ArrayList<String> getDownvotedComicIds() {
        return downvotedComicIds;
    }

    /**
     * @param downvotedComicIds the downvotedComicIds to set
     */
    public void setDownvotedComicIds(ArrayList<String> downvotedComicIds) {
        this.downvotedComicIds = downvotedComicIds;
    }

    /**
     * @return the upvotedComicIds
     */
    public ArrayList<String> getUpvotedComicIds() {
        return upvotedComicIds;
    }

    /**
     * @param upvotedComicIds the upvotedComicIds to set
     */
    public void setUpvotedComicIds(ArrayList<String> upvotedComicIds) {
        this.upvotedComicIds = upvotedComicIds;
    }

    /**
     * @return the isAdmin
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * @param isAdmin the isAdmin to set
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * @return the messagesRecieved
     */
    public ArrayList<String> getMessagesReceived() {
        return messagesReceivedIds;
    }

    /**
     * @param messagesRecieved the messagesRecieved to set
     */
    public void setMessagesRecieved(ArrayList<String> messagesRecieved) {
        this.messagesReceivedIds = messagesRecieved;
    }

    

    /**
     * @return the messages
     */
    public ArrayList<String> getMessagesSent() {
        return messagesSentIds;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessagesSentIds(ArrayList<String> messages) {
        this.messagesSentIds = messages;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%s, firstName='%s', lastName='%s']",
                id, username, password);
    }
}