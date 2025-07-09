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

    /**
     * Loads shreds from a directory chosen by the user.
     */
    private void loadShreds(){
        Path path = fileAdapter.open("Choose first shred in directory");
        Path dir = path.getParent();
        int count = 1;
        while (Files.exists(dir.resolve(count + ".png"))) { count++; }
        count--;
        load(dir, count);
        display();
    }

    /**
     * Loads shreds from a specified directory and count.
     * Clears existing shreds, working strip, and completed strips before loading new shreds.
     * @param dir Directory containing the shreds
     * @param count Number of shreds to load
     */
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

    /**
     * Rotates the list of shreds.
     * Moves the first shred to the end of the list.
     */
    private void rotateList(){
        if(!shreds.isEmpty()) {
            Collections.rotate(shreds, -1);
            display();
        }
    }

    /**
     * Shuffles the list of shreds.
     */
    private void shuffleList(){
        if(!shreds.isEmpty()) {
            Collections.shuffle(shreds);
            display();
        }
    }

    /**
     * Completes the current working strip and adds it to the completed strips.
     */
    public void completeStrip() {
        if (!working.isEmpty()) {
            completed.add(new ArrayList<>(working)); // Shallow copy
            working.clear();
            display();
        }
    }

    /**
     * Displays the current state of the DeShredder.
     */
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

    /**
     * Draws a strip of shreds at a specified y-coordinate.
     * @param strip
     * @param y
     * @param border
     */
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

    /**
     * Moves an item from one list to another at specified indices.
     * @param <T> the type of the item to move (expected to be Shred or List<Shred>)
     * @param from the list to move the item from
     * @param to the list to move the item to
     * @param fromIndex the index of the item to move in the from list
     * @param toIndex the index to insert the item in the to list
     */
    private <T> boolean moveItem(List<T> from, List<T> to, int fromIndex, int toIndex){
        if (fromIndex < 0 || fromIndex >= from.size()) return false;
        T item = from.remove(fromIndex);

        /*
        * Still need to handle the case toIndex < 0 !!!!!!!!!!!!!
        * return or max(0, toIndex) ?
        */
        if(toIndex >= to.size()){ // If toIndex is out of bounds, add to the end
            to.add(item);
            return true;
        } else{
            to.add(toIndex, item);
            return true;
        }
    }


    /**
     * Simple Mouse actions to move shreds and strips
     *  User can
     *  - move a Shred from allShreds to a position in the working strip
     *  - move a Shred from the working strip back into allShreds
     *  - move a Shred around within the working strip.
     *  - move a completed Strip around within the list of completed strips
     *  - move a completed Strip back to become the working strip
     *    (but only if the working strip is currently empty)
     */


    private int fromColumn = -1;
    private int fromCompletedIndex = -1; // index of completedStrips to move from
    private List<Shred> fromStrip;

     // old code let refactor
    public void doMouse(String action, double x, double y){
        if (action.equals("pressed")){
            fromStrip = getStrip(y);      // the List of shreds to move from (possibly null)
            fromColumn = getColumn(x);  // the index of the shred to move (may be off the end)
            fromCompletedIndex = getIndex(y);      //the index of completedStrips to move from
        }
        if (action.equals("released")){
            List<Shred> toStrip = getStrip(y); // the List of shreds to move to (possibly null)
            int toColumn = getColumn(x);     // the index to move the shred to (may be off the end)

            if (!handleShredToShred(fromStrip, toStrip, fromColumn, toColumn)) {
                int toCompletedIndex = getIndex(y); //the index of completedStrips to move to
                handleStripToStrip(fromStrip, toStrip, fromCompletedIndex, toCompletedIndex);
            }
            display();
        }
    }


    /**
     * Handles moving shreds between strips.
     * Moves a shred from one strip to another based on the specified columns.
     * Returns true if the action was successful, false otherwise.
     * @param fromStrip the strip to move from
     * @param toStrip the strip to move to
     * @param fromColumn the column index in the fromStrip to move from
     * @param toColumn the column index in the toStrip to move to
     * @return true if the action was successful, false otherwise
     */
    public boolean handleShredToShred(List<Shred> fromStrip, List<Shred> toStrip, int fromColumn, int toColumn) {
        if (fromStrip == null || toStrip == null || fromStrip.isEmpty()) return false;
        if((fromStrip == shreds) && (toStrip == working)){ // move from all shreds to working strip
            return moveItem(fromStrip, toStrip, fromColumn, toColumn);
        }
        else if((fromStrip == working) && (toStrip == working)){ // move within working strip
            return moveItem(fromStrip, toStrip, fromColumn, toColumn);
        }
        else if((fromStrip == working) && (toStrip == shreds)){ // move from working strip to all shreds
            return moveItem(fromStrip, toStrip, fromColumn, toColumn);
        }
        return false;
    }

    /**
     * Handles moving strips between completed strips and the working strip.
     * @param fromStrip
     * @param toStrip
     * @param fromIndex
     * @param toIndex
     */
    public void handleStripToStrip(List<Shred> fromStrip, List<Shred> toStrip, int fromIndex, int toIndex) {
        if (fromStrip == null || toStrip == null || fromStrip.isEmpty()) return;
        if (completed.contains(fromStrip) && completed.contains(toStrip) && fromIndex != -1 && toIndex != -1) {
            moveItem(completed, completed, fromIndex, toIndex);
        } else if (completed.contains(fromStrip) && toStrip == working && working.isEmpty()) {
            working = new ArrayList<>(fromStrip);
            completed.remove(fromIndex);
        }
    }


    /**
     * Returns which column the mouse position is on.
     * This will be the index in the list of the shred that the mouse is on,
     * (or the index of the shred that the mouse would be on if the list were long enough)
     */
    public int getColumn(double x){
        return (int) ((x - LEFT)/(SIZE));
    }

    public int getRow(double y){
        return (int) ((y - TOP_ALL)/(SIZE + GAP));
    }

    /**
     * Returns the strip that the mouse position is on.
     * This may be the list of all remaining shreds, the working strip, or
     *  one of the completed strips.
     * If it is not on any strip, then it returns null.
     */
    public List<Shred> getStrip(double y){
        int row = getRow(y);
        if (row<=0){
            return shreds;
        }
        else if (row==1){
            return working;
        }
        else if (row-2<completed.size()){
            return completed.get(row-2);
        }
        else {
            return null;
        }
    }

    /**
     * Get index of selected completedStrip
     */
    public int getIndex(double y){
        int row = getRow(y);
        int index = 0;
        if (row-2<completed.size()){
            index = row-2;//subtract raws for allShreds and working strip
            return index;
        }
        else {
            return -1;//selected raw was not completedStrip
        }
    }

}
