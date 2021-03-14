package PostOffice;

import PostOffice.Post.Letter;
import PostOffice.Post.Post;

import util.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class PostOffice {
    private String name;
    private CopyOnWriteArrayList<PostBox> postBoxes;
    private CopyOnWriteArrayList<Post> storage;
    private ConcurrentSkipListMap<LocalDate,CopyOnWriteArrayList<Post>> archive;
    private ArrayList<PostMan> postMans;
    private ArrayList<Collector> collectorsOfPosts;

    public PostOffice(String name) {
        this.name = name;
        this.storage=new CopyOnWriteArrayList<>();
        this.archive=new ConcurrentSkipListMap<>();
        for (int i = 0; i <50; i++) {
            Post post=new Letter();
            storage.add(post);
            addingToArchive(post);
            addPostToDb(post);
        }
        createPostBoxes();
        addPostMans(2);
        addCollectors(2);
        new CitizenGenerator(this).start();

    }

    public ArrayList<PostMan> getPostMans() {
        return postMans;
    }

    public ArrayList<Collector> getCollectorsOfPosts() {
        return collectorsOfPosts;
    }

    public void addingToArchive(Post post){
        if (!archive.containsKey(LocalDate.now())) {
            archive.put(LocalDate.now(), new CopyOnWriteArrayList<>());
        }
        else{
            archive.get(LocalDate.now()).add(post);
        }
    }
    public synchronized void submitPost(Post post){
        storage.add(post);
        addPostToDb(post);
        addingToArchive(post);
        post.setTime(LocalTime.now());
        notifyAll();
    }
    public void collectorSubmitPost(Collector collector){
        synchronized (this) {
            while (!collector.shouldIDoMyJob()) {
                try {
                    System.out.println(collector.getFirstName() + " is waiting");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Posts in the office are "+storage.size()+" "+collector.getFirstName()+" is going to search for posts from postboxes");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (PostBox postBox:postBoxes){
            postBox.collectPost(collector);
        }
        int gettedPosts=0;
        for (Post post:collector.getPostBag()) {
            gettedPosts++;
            submitPost(post);
        }
        System.out.println(collector.getFirstName()+"getted "+gettedPosts);
        collector.getPostBag().clear();
    }
    public void postManWork(PostMan postMan){
        synchronized (this) {
            while (!postMan.shouldIDoMyJob()) {
                try {
                    System.out.println(postMan.getFirstName() + " is waiting for posts");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Posts in the office are "+storage.size()+" "+postMan.getFirstName()+" is going to deliver posts ");
        int postToBeDelivered=0;
        for (int i = 0; i < getPostsToDeliver(); i++) {
            postToBeDelivered++;
            Post post = storage.remove(i);
            postMan.addPostToBag(post);
        }
        String sql = "UPDATE postmans SET count_posts=count_posts+? WHERE postman_name=?";
        Connection c = DBConnector.getInstance().getConnection();
        try (
                PreparedStatement ps = c.prepareStatement(sql);
        ) {
            ps.setInt(1, postToBeDelivered);
            ps.setString(2,postMan.getFirstName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(postMan.getFirstName()+" is going to deliver "+postToBeDelivered+" posts");
            for (Post post:postMan.getPostBag()) {
            try {
                if (post.getType().equals("Letter")){
                    Thread.sleep(1000);
                }
                else{
                    Thread.sleep(1500);
                }

            }
            catch (InterruptedException e){
                e.getMessage();
            }
        }
        postMan.getPostBag().clear();
    }
    private int getPostsToDeliver(){
        return storage.size()/postMans.size();
    }
    private void addPostMans(int count){
        postMans=new ArrayList<>();
        for (int i = 0; i <count ; i++) {
            PostMan postman=new PostMan("Postman",this);
            postMans.add(postman);
            String sql = "INSERT INTO postmans (postman_name, count_posts) VALUES (?, ?)";
            Connection c = DBConnector.getInstance().getConnection();
            try (
                    PreparedStatement ps = c.prepareStatement(sql);
            ) {
                ps.setString(1, postman.getFirstName());
                ps.setInt(2, 0);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void addCollectors(int count){
        collectorsOfPosts=new ArrayList<>();
        for (int i = 0; i <count ; i++) {
            collectorsOfPosts.add(new Collector("Collector",this));
        }
    }
    private void createPostBoxes(){
        postBoxes=new CopyOnWriteArrayList<>();
        for (int i = 0; i <25 ; i++) {
            postBoxes.add(new PostBox());
        }
    }

    public CopyOnWriteArrayList<Post> getStorage() {
        return storage;
    }

    public List<PostBox> getPostBoxes() {
        return Collections.unmodifiableList(postBoxes);
    }
    public void addPostToDb(Post post){
        String sql = "INSERT INTO posts (receiver, sender,type_post, isFragile) VALUES (?, ?,?,?)";
        Connection c = DBConnector.getInstance().getConnection();
        try (
                PreparedStatement ps = c.prepareStatement(sql);
        ) {
            ps.setString(1, post.getReceiverInfo());
            ps.setString(2, post.getSenderInfo());
            ps.setString(3,post.getType());
            ps.setBoolean(4,post.getFragility());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ConcurrentSkipListMap<LocalDate, CopyOnWriteArrayList<Post>> getArchive() {
        return archive;
    }

    public void displayStatistics(){
        System.out.println("-----------------Statistika------------------");
        Statistics.getPostmansSortedByPosts();
        System.out.println("Percentage Of letters: "+Statistics.getPercentageOfLetters(archive,LocalDate.now()));
        System.out.println("Percentage of fragile collets: "+Statistics.getPercentageOfFragiles(archive));
        System.out.println("----------------------------------------------");
    }
}
