package PostOffice;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Logger extends Thread {
    private PostOffice postOffice;
    private static int counter=0;
/*
    private void saveToFile(TreeMap<>) {
        try(PrintWriter pr = new PrintWriter("history-"+counter+++ ".txt" ) ){
            for(Map.Entry<Integer, TreeSet<Shipment>> e : stats.entrySet()){
                pr.println("Dock " + e.getKey() + ":");
                for(Shipment s : e.getValue()){
                    pr.println(s);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

 */
}
