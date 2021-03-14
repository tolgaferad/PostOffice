package PostOffice;

public class Collector extends PostMan {
    public Collector(String name,PostOffice postOffice) {
        super(name,postOffice);
    }

    @Override
    public void run() {
        while(true){
            postOffice.collectorSubmitPost(this);
        }
    }

    public boolean shouldIDoMyJob(){
        return postOffice.getPostBoxes().size() < 50;
    }


}
