package seafoamgreen.coms.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Comic {

    //Mongo generated ID. Should not have a setter only getter
    @Id
    private String id;
    //Owner of comic
    private String username;
    //Name of comic
    private String comicName;
    //Series that comic belongs to
    private String seriesID;
    //List of Panels within the comic
    private List<String> panelList;
    //List of Tags within the comic
    private List<String> tags;
    //is published
    private boolean isPublished;
    //aws url of the picture
    private String thumbnailBlob;
    //DateTime in format MM-dd-yyyy hh:mm a
    private String dateTime;

    private List<String> upvoters;
    private List<String> downvoters;
    private int score;

    // Panel and Tags are initalized as empty lists. They can be added within the
    // Service layer
    public Comic(String username, String comicName, String seriesID) {
        this.username = username;
        this.comicName = comicName;
        this.seriesID = seriesID;
        this.panelList = new ArrayList<String>();
        this.tags = new ArrayList<String>();
        this.upvoters =  new ArrayList<String>();
        this.downvoters = new ArrayList<String>();
        this.score = 0;
    }


    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return the downvoters
     */
    public List<String> getDownvoters() {
        return downvoters;
    }

    /**
     * @param downvoters the downvoters to set
     */
    public void setDownvoters(List<String> downvoters) {
        this.downvoters = downvoters;
    }

    /**
     * @return the upvoters
     */
    public List<String> getUpvoters() {
        return upvoters;
    }

    /**
     * @param upvoters the upvoters to set
     */
    public void setUpvoters(List<String> upvoters) {
        this.upvoters = upvoters;
    }

    /**
     * @return the dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     * @param dateTime the dateTime to set
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * @return the aWSURL
     */
    public String getThumbnailBlob() {
        return thumbnailBlob;
    }

    public void setThumbnailBlob(String thumbnailBlob) {
        this.thumbnailBlob = thumbnailBlob;
    }

    /**
     * @return the isPublished
     */
    public boolean isPublished() {
        return isPublished;
    }

    /**
     * @param isPublished the isPublished to set
     */
    public void setPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userID) {
        this.username = userID;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

    public String getSeriesID() {
        return seriesID;
    }

    public void setSeriesID(String seriesID) {
        this.seriesID = seriesID;
    }

    public List<String> getPanelList() {
        return panelList;
    }

    public void setPanelList(List<String> panelList) {
        this.panelList = panelList;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tagList) {
        this.tags = tagList;
    }

    public void addPanel(String panelId)
    {
        panelList.add(panelId);
    }

    public void addTag(String tag)
    {
        tags.add(tag);
    }

    @Override
    public String toString() {
        return "Comic{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", comicName='" + comicName + '\'' +
                ", seriesID='" + seriesID + '\'' +
                ", panelList=" + panelList +
                ", tags=" + tags +
                ", isPublished=" + isPublished +
                ", thumbnailBlob='" + thumbnailBlob + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", upvoters=" + upvoters +
                ", downvoters=" + downvoters +
                ", score=" + score +
                '}';
    }

    public void calculateScore() {
        this.score = upvoters.size() - downvoters.size();
    }
}