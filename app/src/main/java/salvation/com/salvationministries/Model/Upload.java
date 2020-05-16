package salvation.com.salvationministries.Model;


public class Upload {
    private String firstName;

    private String mKey;
    private int wallet;
    private String title;
    private String mImageUrl;
    private String region;
    private String currency;
    private String lastName;
    private String status;


    public Upload( ) {
        //empty constructor needed
    }

    public Upload(String firstName,String lastName,  String title, String region,String mImageUrl,String currency,String status,int wallet ) {
        this.firstName = firstName;

        this.title = title;
        this.lastName = lastName;
        this.mImageUrl = mImageUrl;
        this.region = region;
        this.currency = currency;
        this.status = status;
        this.wallet = wallet;



    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getKey() {
        return mKey;
    }


    public void setKey(String key) {
        mKey = key;
    }
}