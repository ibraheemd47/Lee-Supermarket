package domainLayer.Inventory;

public class Location {
    private String aisle;
    private String shelf;
    public Location(String aisle, String shelf) {
        this.aisle = aisle;
        this.shelf = shelf;
    }
    public String getAisle() {
        return aisle;
    }
    public String getShelf() {
        return shelf;
    }
    public void setAisle(String aisle) {
        this.aisle = aisle;
    }
    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    
}
