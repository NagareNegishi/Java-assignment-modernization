package Assignment1.Refactored;

import java.util.ArrayList;
import java.util.List;

/**
 * DeShredder manages a collection of shreds
 */
public class DeShredder {
    
    private List<Shred> shreds = new ArrayList<>(); // Available shreds
    private List<Shred> working = new ArrayList<>(); // Strip currently being worked on
    private List<List<Shred>> completed = new ArrayList<>(); // Completed strips

    // Constants for the display and the mouse
    public static final double LEFT = 20;       // left side of the display
    public static final double TOP_ALL = 20;    // top of list of all shreds
    public static final double GAP = 5;         // gap between strips
    
    public final double SIZE;                   // size of a shred
    public final double TOP_WORKING;
    public final double TOP_STRIPS;

    private final ShredRenderer renderer; // Renderer for drawing shreds

    /**
     * Constructor for DeShredder
     * @param size Size of each shred
     * @param renderer Renderer for drawing shreds
     */
    public DeShredder(double size, ShredRenderer renderer) {
        this.SIZE = size;
        this.renderer = renderer;
        this.TOP_WORKING = TOP_ALL + SIZE + GAP;
        this.TOP_STRIPS = TOP_WORKING + (SIZE + GAP);
    }
}
