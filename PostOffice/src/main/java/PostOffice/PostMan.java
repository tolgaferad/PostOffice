package PostOffice;

import PostOffice.Post.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostMan extends Thread{
    protected String firstName;
    protected int age;
    protected int yearsExperience;
    protected List<Post> postBag;
    protected PostOffice postOffice;
    private static int counter=1;
    protected int countPosts=0;

    public PostMan(String name, PostOffice postOffice) {
        this.firstName = name+"-"+counter++;
        this.postBag=new ArrayList<>();
        this.postOffice = postOffice;
    }

    public String getFirstName() {
        return firstName;
    }

    public void addPostToBag(Post post){
        postBag.add(post);
    }
    public boolean shouldIDoMyJob(){
        return postOffice.getStorage().size() > 50;
    }
    public List<Post> getPostBag() {
        return postBag;
    }

    @Override
    public void run() {
        while(true){
            postOffice.postManWork(this);
        }
    }
}
