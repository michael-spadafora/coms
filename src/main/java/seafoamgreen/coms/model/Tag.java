package seafoamgreen.coms.model;

import org.springframework.data.annotation.Id;



public class Tag {

    @Id
    private String id;

    private String tagName;

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id='" + id + '\'' +
                ", tagName='" + tagName + '\'' +
                '}';
    }
}