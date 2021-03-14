package PostOffice;

import PostOffice.Post.Letter;
import PostOffice.Post.Post;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class PostBox {
    private CopyOnWriteArrayList<Post> posts;

    public PostBox() {
        this.posts=new CopyOnWriteArrayList<>();
    }

    public synchronized void submitPost(Post letter){
        posts.add(letter);
        letter.setTime(LocalTime.now());
    }
    public void collectPost(Collector collector){
        if (!posts.isEmpty()) {
            for (Post post : posts) {
                posts.remove(post);
                collector.addPostToBag(post);
            }
        }
    }

}
