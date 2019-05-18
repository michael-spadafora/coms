package seafoamgreen.coms.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document
public class Comment {

    //Mongo generated ID. Should not have a setter only getter
    @Id
    private String id;
    //Owner of comic
    @Field
    private String userId;
    // Name of comic
    @Field
    private String commentBody;
    // Series that comic belongs to
    @Field
    private String comicId;
    // List of Panels within the comic
    @Field
    private String dateTime;

    public Comment(String userId, String comicId, String commentBody, String dateTime) {
        this.userId = userId;
        this.comicId = comicId;
        this.commentBody = commentBody;
        this.dateTime = dateTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getComicId() {
        return comicId;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public String getDateTime() {
        return dateTime;
    }
}