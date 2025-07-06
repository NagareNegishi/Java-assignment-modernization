package Assignment1.Refactored;

import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
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
    private final UIAdapter uiAdapter; // UI adapter for user interface operations
    private final FileChooserAdapter fileAdapter; // File chooser adapter for file operations
    private final GraphicsAdapter graphicsAdapter; // Graphics adapter for drawing operations

    /**
     * Constructor for DeShredder
     * @param size Size of each shred
     * @param renderer Renderer for drawing shreds
     */
    public DeShredder(double size, ShredRenderer renderer, UIAdapter uiAdapter, FileChooserAdapter fileAdapter, GraphicsAdapter graphicsAdapter) {
        this.SIZE = size;
        this.renderer = renderer;
        this.uiAdapter = uiAdapter;
        this.fileAdapter = fileAdapter;
        this.graphicsAdapter = graphicsAdapter;
        this.TOP_WORKING = TOP_ALL + SIZE + GAP;
        this.TOP_STRIPS = TOP_WORKING + (SIZE + GAP);
    }

    private void loadShreds(){
        Path path = fileAdapter.open("Choose first shred in directory");
        Path dir = path.getParent();
        int count = 1;
        while (Files.exists(dir.resolve(count + ".png"))) { count++; }
        count--;
        load(dir, count);
        display();
    }

    private void load(Path dir, int count) {
        assert dir != null : "Directory cannot be null";
        assert count > 0 : "Count must be greater than zero";
        shreds.clear();
        working.clear();
        completed.clear();
        for (int i = 1; i <= count; i++) {
            Shred shred = new Shred(dir, i, SIZE);
            shreds.add(shred);
        }
    }

    private void rotateList(){
        if(!shreds.isEmpty()) {
            Collections.rotate(shreds, -1);
            display();
        }
    }

    private void shuffleList(){
        if(!shreds.isEmpty()) {
            Collections.shuffle(shreds);
            display();
        }
    }

    public void completeStrip() {
        if (!working.isEmpty()) {
            completed.add(new ArrayList<>(working)); // Shallow copy
            working.clear();
            display();
        }
    }

    public void display(){
        graphicsAdapter.clearGraphics();

        drawStrip(shreds, TOP_ALL, null); // all shreds
        drawStrip(working, TOP_WORKING, Color.red); // working strip
        double y = TOP_STRIPS;
        for (List<Shred> strip : completed){ // completed strips
            drawStrip(strip, y, Color.black);
            y += SIZE + GAP;
        }
        
    }

    private void drawStrip(List<Shred> strip, double y, Color border) {
        double x = LEFT;
        for (Shred shred : strip) {
            renderer.drawShred(shred, x, y);
            x += SIZE;
        }
        if (border != null) {
            graphicsAdapter.setColor(border);
            graphicsAdapter.drawRect(LEFT - 1, y - 1, SIZE * strip.size() + 2, SIZE + 2);
            graphicsAdapter.setColor(Color.black);
        }
    }



    // Additional methods to perform the different actions, called by doMouse

    /*# YOUR CODE HERE */
    /**
     * Move a Shred from allShreds to a position in the working strip
     */
    private <T> void moveItem(List<T> from, List<T> to, int fromIndex, int toIndex){
        if (fromIndex < 0 || fromIndex >= from.size()) return;
        T item = from.remove(fromIndex);

        if(toIndex >= to.size()){ // If toIndex is out of bounds, add to the end
            to.add(item);
        } else{
            to.add(toIndex, item);
        }
    }










}
