package PostOffice;

import PostOffice.PostOffice;
import java.*;
import java.util.Random;

public class CitizenGenerator extends Thread {
    private  PostOffice postOffice;

    public CitizenGenerator(PostOffice postOffice) {
        this.postOffice=postOffice;
    }

    @Override
    public void run() {
        while (true){
            Citizen citizen=new Citizen("Citizen","lastname","address",postOffice);
            if(new Random().nextBoolean()&&citizen.getPost().getType().equals("Letter")){
                postOffice.getPostBoxes().get(new Random().nextInt(25)).submitPost(citizen.getPost());
                System.out.println(citizen.getFirstName()+" "+citizen.getLastName()+" added post to postbox");

            }
            else{
                postOffice.submitPost(citizen.getPost());
                System.out.println(citizen.getFirstName()+" "+citizen.getLastName()+" added post to postoffice");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
/*
public static Port port;

    @Override
    public void run() {
        int cnt = 1;
        while(true){
            Ship ship = new Ship("Ship" + cnt++);
            port.addShip(ship);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
 */