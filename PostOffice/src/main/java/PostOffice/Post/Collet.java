package PostOffice.Post;

import java.time.LocalTime;
import java.util.Collections;
import java.util.Random;

public class Collet extends Post {
    private int length;
    private int width;
    private int height;
    private boolean isFragile;

    public Collet(){
        if (new Random().nextBoolean()){
            isFragile=true;
        }
        else{
            isFragile=false;
        }
    }

    @Override
    public double getTax() {
        double price=2;
        if (length>60||width>60||height>60){
            price+=price*0.5;
        }
        if (isFragile){
            price+=2*0.5;
        }
        return price;
    }

    @Override
    public String getType() {
        return "Collet";
    }

    @Override
    public boolean getFragility() {
        return isFragile;
    }
}
