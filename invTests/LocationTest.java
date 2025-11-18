// File: Inventory/BusinessLayer/LocationTest.java

package invTests;

import domainLayer.Inventory.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class LocationTest {

    private Location location;

    @BeforeEach
    void setUp() {
        location = new Location("A1", "S1");
    }

    @Test
    void getAisle() {
        assertEquals("A1", location.getAisle());
    }

    @Test
    void getShelf() {
        assertEquals("S1", location.getShelf());
    }

    @Test
    void setAisle() {
        location.setAisle("A2");
        assertEquals("A2", location.getAisle());
    }

    @Test
    void setShelf() {
        location.setShelf("S2");
        assertEquals("S2", location.getShelf());
    }
}