package primedsoft.com.salvation.Model;

public class MyMessages {

    private String messageTitle,messageImageUrl,messageDownloadUrl,messageKey,type,extension;

    public MyMessages() {
    }

    public MyMessages(String messageTitle, String messageImageUrl, String messageDownloadUrl, String messageKey) {
        this.messageTitle = messageTitle;
        this.messageImageUrl = messageImageUrl;
        this.messageDownloadUrl = messageDownloadUrl;
        this.messageKey = messageKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageImageUrl() {
        return messageImageUrl;
    }

    public void setMessageImageUrl(String messageImageUrl) {
        this.messageImageUrl = messageImageUrl;
    }

    public String getMessageDownloadUrl() {
        return messageDownloadUrl;
    }

    public void setMessageDownloadUrl(String messageDownloadUrl) {
        this.messageDownloadUrl = messageDownloadUrl;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
}
