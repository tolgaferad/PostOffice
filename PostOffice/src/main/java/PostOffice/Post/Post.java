package PostOffice.Post;

import PostOffice.PostOffice;

import java.time.LocalTime;

public abstract class Post implements Comparable<Post>{
    protected String senderInfo;
    protected String receiverInfo;
    protected String address;
    protected LocalTime time;
    protected String type;
    protected static int counter= 1;
    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Post() {

        this.senderInfo = "Tolga-"+counter;
        this.receiverInfo = "Valentin-"+counter;
        this.type = getType();
        counter++;
    }

    public abstract double getTax();
    public abstract String getType();

    public String getSenderInfo() {
        return senderInfo;
    }

    public String getReceiverInfo() {
        return receiverInfo;
    }

    public String getAddress() {
        return address;
    }

    public LocalTime getTime() {
        return time;
    }
    public abstract boolean getFragility();
    public int compareTo(Post o) {
        return this.time.compareTo(o.time);
    }
}
