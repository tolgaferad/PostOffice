import PostOffice.*;

import java.time.LocalDate;

public class Demo {
    public static void main(String[] args) {
        PostOffice postOffice=new PostOffice("Поща Крумовград");
        for (int i = 0; i <postOffice.getPostMans().size() ; i++) {
            new Thread(postOffice.getPostMans().get(i)).start();
        }
        for (int i = 0; i <postOffice.getCollectorsOfPosts().size() ; i++) {
            new Thread(postOffice.getCollectorsOfPosts().get(i)).start();
        }
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Statistics.listAllPostsByDate(postOffice.getArchive(), LocalDate.now());
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        postOffice.displayStatistics();

    }
}
