package seafoamgreen.coms.requestBodyTypes;

public class MessageBody {
    private String title;
    private String body;



    public MessageBody() {
        this.setTitle("TESTTTT");
        this.setBody("TESTTTTTTTT");
    }

    public MessageBody(String title, String body) {
        this.setTitle(title);
        this.setBody(body);
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
