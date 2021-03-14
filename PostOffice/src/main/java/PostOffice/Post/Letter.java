package PostOffice.Post;

import PostOffice.PostMan;

import java.time.LocalTime;

public class Letter extends Post {

    @Override
    public double getTax() {
        return 0.5;
    }

    @Override
    public String getType() {
        return "Letter";
    }

    @Override
    public boolean getFragility() {
        return false;
    }
}
