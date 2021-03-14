package PostOffice;

import PostOffice.Post.Collet;
import PostOffice.Post.Letter;
import PostOffice.Post.Post;
import PostOffice.PostOffice;

import java.time.LocalTime;
import java.util.Random;

public class Citizen {
    private String firstName;
    private String lastName;
    private String address;
    private PostOffice postOffice;
    private Post post;
    private static int counter=0;

    public Citizen(String firstName,String lastName,String address,PostOffice postOffice){
        this.firstName=firstName+"-"+counter;
        this.lastName=lastName+"-"+counter;
        this.address=address+"-"+counter;
        this.postOffice=postOffice;
        if (new Random().nextBoolean()) {
            this.post = new Letter();
        }
        else{
            this.post=new Collet();
        }
        counter++;
    }

    public Post getPost() {
        return post;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
