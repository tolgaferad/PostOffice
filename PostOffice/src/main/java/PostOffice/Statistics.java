package PostOffice;

import PostOffice.Post.Post;
import util.DBConnector;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Statistics {

    private  static int counter=0;

    public static void listAllPostsByDate(ConcurrentSkipListMap<LocalDate,
            CopyOnWriteArrayList<Post>> archive, LocalDate date) {
            CopyOnWriteArrayList<Post> posts = archive.get(date);

        try(PrintWriter pr = new PrintWriter("history-"+counter+++ ".txt" ) ) {
            for (Post post : posts) {
                String info = post.getSenderInfo() + " " + post.getReceiverInfo() + " " + post.getAddress() + " " + post.getType() + post.getTax();
                pr.println(info);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static double getPercentageOfLetters(ConcurrentSkipListMap<LocalDate,
            CopyOnWriteArrayList<Post>> archive, LocalDate date) {
        CopyOnWriteArrayList<Post> postsDate = archive.get(date);
        int countOfLetters = 0;
        for (Post post : postsDate) {
            if (post.getType().equals("Letter")) {
                countOfLetters++;
            }
        }
        return (double)(countOfLetters * 100) / postsDate.size();
    }

    public static double getPercentageOfFragiles(ConcurrentSkipListMap<LocalDate,
            CopyOnWriteArrayList<Post>> archive) {
        int countFragility = 0;
        int countCollets = 0;
        for (Map.Entry<LocalDate, CopyOnWriteArrayList<Post>> e : archive.entrySet()
        ) {
            for (Post post : e.getValue()
            ) {
                if (post.getType().equals("Collet")) {
                    countCollets++;
                    if (post.getFragility()) {
                        countFragility++;
                    }
                }
            }
        }
        return (double)countFragility * 100 / countCollets;
    }

    public static void getPostmansSortedByPosts() {
        String sql = "SELECT postman_name, count_posts, id FROM postmans ORDER BY count_posts DESC";
        Connection c = DBConnector.getInstance().getConnection();
        try (
                PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rows = ps.executeQuery();
            while (rows.next()) {
                System.out.println(rows.getString("postman_name") + " " + rows.getInt("count_posts"));
                System.out.println();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
