package seafoamgreen.coms.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Series {

    //Mongo generated ID. Should not have a setter only getter
    @Id
    private String id;
    //Name of the Series
    private String seriesName;
    //Owner of the Series
    private String userID;
    //Owner of the series' username
    private String username;
    // List of comics within series
    private List<String> comicList;
    // List of collaborators in Series
    private List<String> collaboratorList;
    // List of subscribers
    private List<String> subscriberList;

    public Series(String seriesName, String username) {
        this.seriesName = seriesName;
        this.username = username;
        this.comicList = new ArrayList<String>();
        this.collaboratorList = new ArrayList<String>();
        this.subscriberList = new ArrayList<String>();
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void addComic(String comicID) { this.comicList.add(comicID); }

    public List<String> getComicList() {
        return comicList;
    }

    public void setComicList(List<String> comicList) {
        this.comicList = comicList;
    }

    public List<String> getCollaboratorList() {
        return collaboratorList;
    }

    public void setCollaboratorList(List<String> collaboratorList) {
        this.collaboratorList = collaboratorList;
    }

    public List<String> getSubscriberList() {
        return subscriberList;
    }

    public void setSubscriberList(List<String> subscriberList) {
        this.subscriberList = subscriberList;
    }

    @Override
    public String toString() {
        return "Series{" +
                "id='" + id + '\'' +
                ", seriesName='" + seriesName + '\'' +
                ", userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", comicList=" + comicList +
                ", collaboratorList=" + collaboratorList +
                ", subscriberList=" + subscriberList +
                '}';
    }
}