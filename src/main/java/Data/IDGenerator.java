package Data;

public class IDGenerator {
    private long ID;
    public IDGenerator(final long startingID){
        this.ID = startingID;
    }

    public long nextID(){
        return ID++;
    }
}
