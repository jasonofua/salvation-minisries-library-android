package salvation.com.salvationministries.Model;


import ir.mirrajabi.searchdialog.core.Searchable;

public class MessageModel implements Searchable {

    private String type, imageUrl, exetension, downloadUrl, title, genre, key, priceDollar, priceNaira,author,id;
    private int points;

    public MessageModel() {
    }

    public MessageModel(String type,String id,String author, String imageUrl, String exetension, String downloadUrl, String title, String genre, String key, String priceDollar, String priceNaira,int points) {
        this.type = type;
        this.imageUrl = imageUrl;
        this.exetension = exetension;
        this.downloadUrl = downloadUrl;
        this.title = title;
        this.genre = genre;
        this.key = key;
        this.priceDollar = priceDollar;
        this.priceNaira = priceNaira;
        this.author = author;
        this.id = id;
        this.points = points;

    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPriceDollar() {
        return priceDollar;
    }

    public void setPriceDollar(String priceDollar) {
        this.priceDollar = priceDollar;
    }

    public String getPriceNaira() {
        return priceNaira;
    }

    public void setPriceNaira(String priceNaira) {
        this.priceNaira = priceNaira;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getExetension() {
        return exetension;
    }

    public void setExetension(String exetension) {
        this.exetension = exetension;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
