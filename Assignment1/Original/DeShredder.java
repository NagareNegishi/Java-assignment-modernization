// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2023T2, Assignment 1
 * Name:Negishi Nagare
 * Username:negishnaga
 * ID:300653779
 */

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import ecs100.UI;
import ecs100.UIFileChooser;

/**
 * DeShredder allows a user to sort fragments of a shredded document ("shreds") into strips, and
 * then sort the strips into the original document.
 * The program shows
 *   - a list of all the shreds along the top of the window,
 *   - the working strip (which the user is constructing) just below it.
 *   - the list of completed strips below the working strip.
 * The "rotate" button moves the first shred on the list to the end of the list to let the
 *  user see the shreds that have disappeared over the edge of the window.
 * The "shuffle" button reorders the shreds in the list randomly.
 * The user can use the mouse to drag shreds between the list at the top and the working strip,
 *  and move shreds around in the working strip to get them in order.
 * When the user has the working strip complete, they can move
 *  the working strip down into the list of completed strips, and reorder the completed strips.
 *
 */
public class DeShredder {

    // Fields to store the lists of Shreds and strips.  These should never be null.
    private List<Shred> allShreds = new ArrayList<Shred>();    //  List of all shreds
    private List<Shred> workingStrip = new ArrayList<Shred>(); // Current strip of shreds
    private List<List<Shred>> completedStrips = new ArrayList<List<Shred>>();

    // Constants for the display and the mouse
    public static final double LEFT = 20;       // left side of the display
    public static final double TOP_ALL = 20;    // top of list of all shreds
    public static final double GAP = 5;         // gap between strips
    public static final double SIZE = Shred.SIZE; // size of the shreds

    public static final double TOP_WORKING = TOP_ALL+SIZE+GAP;
    public static final double TOP_STRIPS = TOP_WORKING+(SIZE+GAP);

    //Fields for recording where the mouse was pressed  (which list/strip and position in list)
    // note, the position may be past the end of the list!
    private List<Shred> fromStrip;   // The strip (List of Shreds) that the user pressed on
    private int fromPosition = -1;   // index of shred in the strip

    /*# for completion */
    private int fromindex;  //index of completedStrips

    /**
     * Initialises the UI window, and sets up the buttons.
     */
    public void setupGUI() {
        UI.addButton("Load library",   this::loadLibrary);
        UI.addButton("Rotate",         this::rotateList);
        UI.addButton("Shuffle",        this::shuffleList);
        UI.addButton("Complete Strip", this::completeStrip);
        UI.addButton("Quit",           UI::quit);

        UI.setMouseListener(this::doMouse);
        UI.setWindowSize(1000,800);
        UI.setDivider(0);

        /*# for Challenge  */
        UI.addButton("Save Strips", this::saveStrips);//for challenge
        UI.addButton("Highlight Potential Next", this::highlightNext);
    }

    /**
     * Asks user for a library of shreds, loads it, and redisplays.
     * Uses UIFileChooser to let user select library
     * and finds out how many images are in the library
     * Calls load(...) to construct the List of all the Shreds
     */
    public void loadLibrary(){
        Path filePath = Path.of(UIFileChooser.open("Choose first shred in directory"));
        Path directory = filePath.getParent(); //subPath(0, filePath.getNameCount()-1);
        int count=1;
        while(Files.exists(directory.resolve(count+".png"))){ count++; }
        //loop stops when count.png doesn't exist
        count = count-1;
        load(directory, count);   // YOU HAVE TO COMPLETE THE load METHOD
        display();
    }

    /**
     * Empties out all the current lists (the list of all shreds,
     *  the working strip, and the completed strips).
     * Loads the library of shreds into the allShreds list.
     * Parameters are the directory containing the shred images and the number of shreds.
     * Each new Shred needs the directory and the number/id of the shred.
     */
    public void load(Path dir, int count) {
        /*# YOUR CODE HERE */
        if (dir != null){//wanted avoid error when dir was empty, but I need to modify loadLibrary for that
            //Empties out all the current lists
            this.allShreds.clear();
            this.workingStrip.clear();
            this.completedStrips.clear();
            for (int i = 1; i <= count; i++) {
                Shred shred = new Shred(dir, i);// Create a new Shred object with the directory and the shred id (fileName = i + ".png")
                this.allShreds.add(shred);// Add the shred to the list of all shreds
            }
        }
    }

    /**
     * Rotate the list of all shreds by one step to the left
     * and redisplay;
     * Should not have an error if the list is empty
     * (Called by the "Rotate" button)
     */
    public void rotateList(){
        /*# YOUR CODE HERE */
        int size = this.allShreds.size();
        //if list is not empty, get first shreds in the list and move it to the end
        if(size > 0){
            Shred temp = allShreds.get(0);//get first shreds
            this.allShreds.remove(0);
            this.allShreds.add(temp);//add it to the end
            display();
        }
    }

    /**
     * Shuffle the list of all shreds into a random order
     * and redisplay;
     */
    public void shuffleList(){
        /*# YOUR CODE HERE */
        //generate random number 0 to size of list, and switch the position of 2 shreds(say A and B)
        int max = this.allShreds.size();
        if(max > 0){
            Random rand = new Random();
            for (int i = 0; i < max; i++) {
                int randomNum = rand.nextInt(max);//0 to biggest index in the list
                Shred temp = this.allShreds.get(randomNum);//B
                this.allShreds.set(randomNum, this.allShreds.get(i));//copy A to random position B
                this.allShreds.set(i, temp);//add B to where A was  
            }
            display();
        }
    }

    /**
     * Move the current working strip to the end of the list of completed strips.
     * (Called by the "Complete Strip" button)
     */
    public void completeStrip(){
        /*# YOUR CODE HERE */
        if(!workingStrip.isEmpty()){//dont want empty comleteStrip
            List<Shred> templist = new ArrayList<Shred>();//make temporary list and copy workingStrip
            int size = workingStrip.size();
            for (int i = 0; i < size; i++){
                Shred temp = workingStrip.get(i);
                templist.add(temp);
            }
            //add copy of workingStrip to completedStrips
            this.completedStrips.add(templist);
            this.workingStrip.clear();
            display();
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
     * Moving a shred to a position past the end of a List should put it at the end.
     * You should create additional methods to do the different actions - do not attempt
     *  to put all the code inside the doMouse method - you will lose style points for this.
     * Attempting an invalid action should have no effect.
     * Note: doMouse uses getStrip and getColumn, which are written for you (at the end).
     * You should not change them.
     */
    public void doMouse(String action, double x, double y){
        if (action.equals("pressed")){
            fromStrip = getStrip(y);      // the List of shreds to move from (possibly null)
            fromPosition = getColumn(x);  // the index of the shred to move (may be off the end)
            fromindex = getIndex(y);      //the index of completedStrips to move from
        }
        if (action.equals("released")){
            List<Shred> toStrip = getStrip(y); // the List of shreds to move to (possibly null)
            int toPosition = getColumn(x);     // the index to move the shred to (may be off the end)
            int toindex = getIndex(y);         //the index of completedStrips to move to
            // perform the correct action, depending on the from/to strips/positions
            /*# YOUR CODE HERE */
            //move selected shred from allshreds to workingStrip
            if((fromStrip == allShreds)
            &&
            (toStrip == workingStrip)
            &&
            (!fromStrip.isEmpty())){
                this.alltowork(fromStrip, toStrip, fromPosition, toPosition);
            }

            //move selected shred from workingStrip to workingStrip
            else if((fromStrip == workingStrip)
            &&
            (toStrip == workingStrip)
            &&
            (!fromStrip.isEmpty())){
                this.worktowork(fromStrip, toStrip, fromPosition, toPosition);
            }

            //move selected shred from workingStrip to allshreds
            else if((fromStrip == workingStrip)
            &&
            (toStrip == allShreds)
            &&
            (!fromStrip.isEmpty())){
                this.worktoall(fromStrip, toStrip, fromPosition, toPosition);
            }

            //move selected completedStrip within completedStrips
            else if((completedStrips.contains(fromStrip))
            &&
            (completedStrips.contains(toStrip))
            &&
            (!completedStrips.isEmpty())
            &&
            (fromindex != -1)
            &&
            (toindex != -1)){
                this.reorder(fromStrip,fromindex, toindex);
            }

            //move selected completedStrip to workingStrip,if workingStrip is empty
            else if((completedStrips.contains(fromStrip))
            &&
            (toStrip == workingStrip)
            &&
            (!completedStrips.isEmpty())
            &&
            (workingStrip.isEmpty())){
                this.comptowork(fromStrip, toStrip, fromindex);
            }      
            display();
        }
    }

    // Additional methods to perform the different actions, called by doMouse

    /*# YOUR CODE HERE */
    /**
     * Move a Shred from allShreds to a position in the working strip
     */
    public void alltowork(List<Shred> from,List<Shred> to, int fromP, int toP){
        Shred temp = allShreds.get(fromP);//temporary Shred to store moving Shred
        if(toP >= workingStrip.size()){//if user try to move the selected Shred beyond the size of working strip, add it to the end of working strip
            workingStrip.add(temp);
        }
        else{
            workingStrip.add(toP, temp);
        }
        allShreds.remove(fromP);
    }

    /**
     * Move a Shred around within the working strip.
     */
    public void worktowork(List<Shred> from,List<Shred> to, int fromP, int toP){
        Shred temp = workingStrip.get(fromP);//temporary Shred to store moving Shred
        workingStrip.remove(fromP);
        if(toP >= workingStrip.size()){//if user try to move the selected Shred beyond the size of working strip, add it to the end of working strip
            workingStrip.add(temp);
        }
        else{
            workingStrip.add(toP, temp);
        }
    }

    /**
     * Move a Shred from the working strip back into allShreds
     */
    public void worktoall(List<Shred> from,List<Shred> to, int fromP, int toP){
        Shred temp = workingStrip.get(fromP);//temporary Shred to store moving Shred
        workingStrip.remove(fromP);
        if(toP >= allShreds.size()){//if user try to move the selected Shred beyond the size of allShreds, add it to the end of allShreds
            allShreds.add(temp);
        }
        else{
            allShreds.add(toP, temp);
        }
    }

    /**
     * Get index of selected completedStrip
     */
    public int getIndex(double y){
        int row = (int) ((y-TOP_ALL)/(SIZE+GAP));//calculate which raw was selected
        int index = 0;
        if (row-2<completedStrips.size()){
            index = row-2;//subtract raws for allShreds and working strip
            return index;
        }
        else {
            return -1;//selected raw was not completedStrip
        }
    }

    /**
     * Move a completed Strip around within the list of completed strips
     */
    public void reorder(List<Shred> from, int fromindex, int toindex){
        List<Shred> temp = completedStrips.get(fromindex);//temporary list to store moving list
        completedStrips.remove(fromindex);
        completedStrips.add(toindex,temp);
    }

    /**
     * Move a completed Strip back to become the working strip
     *    (but only if the working strip is currently empty)
     */
    public void comptowork(List<Shred> from, List<Shred> to, int fromindex){
        List<Shred> temp = from;//temporary list to store moving list
        completedStrips.remove(fromindex);
        workingStrip = temp;
    }

    /*# for challenge */
    /**
     * return the size of longest completedStrip
     * (use it for padding later)
     */
    public int getMax() {
        int max = 0;
        for (List<Shred> strip : completedStrips) {
            int length = strip.size();
            if (length > max) {
                max = length;
            }
        }
        return max;
    }

    /**
     * provided "loadImage" convert shred to the array comtain color of shred,
     * provided "saveImage" saves the array contain color of shred,
     * to use both method to save completedStrips, I need to combine return of "loadImage" to make one big picture.
     * this method use "loadImage" for every shreds in completedStrips to make a big array contain the color of the big picture.
     * 
     * also, the method checks length of each strips, if it's shorter than width of the big picture,"pad" right end with white squares.
     */
    private Color[][] ReconstructedImage() {
        //get the size of the reconstructed image
        int rows = completedStrips.size();
        int length = getMax();
        int width = length * (int)SIZE;
        int height = rows * (int)SIZE;

        Color[][] reconstructedImage = new Color[height][width];//to store the reconstructed image

        //convert completedStrips to the colors
        int row = 0;
        for (List<Shred> strip : completedStrips) {
            int col = 0;
            int stripLength = strip.size();

            int padding = length - stripLength;// check if this row needs padding
            for (Shred shred : strip) {
                //convert shreds to the colors
                Color[][] shredImage = loadImage(shred.getfilename());//getfilename() is new method in Shred Class, return filename of shred
                for (int r = 0; r < SIZE; r++) {
                    for (int c = 0; c < SIZE; c++) {
                        reconstructedImage[row + r][col + c] = shredImage[r][c];//transfer shred to reconstructed image
                    }
                }
                col = col + (int)SIZE;// Move to right
            }

            // Pad the strip with white squares on the right if needed
            if (padding > 0) {
                for (int r = 0; r < SIZE; r++) {
                    for (int c = 0; c < padding * SIZE; c++) {
                        reconstructedImage[row + r][col + c] = Color.WHITE;//white padding
                    }
                }
            }
            row = row + (int)SIZE;// Move down
        }
        return reconstructedImage;
    }

    /**
     * use "saveImage" to save the big picture constructed by "ReconstructedImage",
     * user chose name and location of saved image
     */
    public void saveStrips() {
        if(!completedStrips.isEmpty()){
            Color[][] bigpic = ReconstructedImage();// 2D array to store the colors of the completed strips
            String savePath = UIFileChooser.save("Save Reconstructed Image")+ ".png";//user choose a file name and location to save the image, save it as png

            // Save the image if the savePath is not null
            if (savePath != null) {
                saveImage(bigpic, savePath);
                //UI.println("Saved" + savePath);  could show this message, but "saveImage" may not work
            }
        }
        else{
        UI.println("Completed strips is empty");}
    }

    /** plan for "possible next"
     * 1, get the color of left side of shred 
     * 2, get the color of right side of shred 
     * 3, compare colors method
     * 4, compare colors loop
     * 5, if color is same to similar, highlight the shred
     */

    /**
     * Gets the colors of both side of a shred.
     * Returns 2D array of colors.
     */
    public Color[][] sideColors(Shred shred) {
        Color[][] colors = new Color[(int)SIZE][(int)SIZE];
        try {
            BufferedImage img = ImageIO.read(Files.newInputStream(Path.of(shred.getfilename())));
            // Get the colors on the left side
            for (int i = 0; i < SIZE; i++) {
                colors[i][0] = new Color(img.getRGB(1, i));
            }

            // Get the colors on the right side
            for (int i = 0; i < SIZE; i++) {
                colors[i][1] = new Color(img.getRGB((int)(SIZE - 1), i));//dont want over the shred
            }
        } catch(IOException e){UI.println("Reading Image failed: "+e);}
        return colors;
    }

    /**
     * Compares two colors next to each other,
     * if they are a close match, returns true 
     */
    public boolean ColorMatch(Color c1, Color c2) {
        boolean match = false;
        int difference = 10;//difference between colors, 0 is exact same color

        //compare each RGB values
        int red = Math.abs(c1.getRed() - c2.getRed());
        int green= Math.abs(c1.getGreen() - c2.getGreen());
        int blue = Math.abs(c1.getBlue() - c2.getBlue());
        if((red <= difference)
        && 
        (green <= difference)
        && 
        (blue <= difference)){
            match = true;//if RGB value are close enough, return true
        }
        return match;
    }

    /**
     * compare 2 shreds colors on same row, count the number colors matched,
     * if total matched number is high enough return true
     * (I chosed over 90% match is potential next)
     */
    public boolean PotentialNext(Shred work, Shred all) {
        boolean match = false;
        //count the number of matched raw
        int countLeft = 0;
        int countRight = 0;

        // Get color of both side of shred
        Color[][] workside = sideColors(work);
        Color[][] allside = sideColors(all);
        for (int i = 0; i < SIZE; i++) {
            if (ColorMatch(workside[i][0], allside[i][1])) {// Compare left of last working shred
                countLeft ++;
            }
            if (ColorMatch(workside[i][1], allside[i][0])) {// Compare right of last working shred
                countRight ++;
            }
        }
        //either left side or right side of shred in allshreds match to last workingshred, return true
        if ((countLeft >= SIZE*0.9)//by changing "SIZE*0.9", you can adjust threshold
        ||
        (countRight >= SIZE*0.9)){
            match = true;
        }
        return match;
    }

    /**
     * for the challenge part I added new fields and method for shred class,
     * "private boolean highlighted" contain information of if the shred shoud be highlighted,
     * "setHighlight" change the boolean value of "private boolean highlighted"
     * 
     * this method get the last working shred and allshreds, check if each shred in allshred is potential next to working shred,
     * if matched, set the shred ready to highlight
     * 
     * I also modified "display" method to highlight matched shred
     */
    public void highlightNext() {
        if (workingStrip.isEmpty()) {//method require shred in working strip
            UI.println("Working strip is empty.");
            return;
        }
        Shred lastShred = workingStrip.get(workingStrip.size() - 1);//get shred from right end of working strip
        for (Shred shred : allShreds) {
            if (PotentialNext(lastShred, shred)) {
                shred.setHighlight(true);
            } else {
                shred.setHighlight(false);
            }
        }
        display();
    }

    //=============================================================================
    // Completed for you. Do not change.
    // loadImage and saveImage may be useful for the challenge.

    /**
     * Displays the remaining Shreds, the working strip, and all completed strips
     */
    public void display(){
        UI.clearGraphics();

        // list of all the remaining shreds that haven't been added to a strip
        double x=LEFT;
        for (Shred shred : allShreds) {
            if (shred.isHighlighted()) {/*# for challenge */
                shred.highlight(x, TOP_ALL);//draw highlighted shred
                shred.setHighlight(false);
            } else {
                shred.drawWithBorder(x, TOP_ALL);
            }
            x += SIZE;
        }

        /** this is original code for allShred
         * 
         * for (Shred shred : allShreds){
        shred.drawWithBorder(x, TOP_ALL);
        x+=SIZE;
        }*/

        //working strip (the one the user is workingly working on)
        x=LEFT;
        for (Shred shred : workingStrip){
            shred.draw(x, TOP_WORKING);
            x+=SIZE;
        }
        UI.setColor(Color.red);
        UI.drawRect(LEFT-1, TOP_WORKING-1, SIZE*workingStrip.size()+2, SIZE+2);
        UI.setColor(Color.black);

        //completed strips
        double y = TOP_STRIPS;
        for (List<Shred> strip : completedStrips){
            x = LEFT;
            for (Shred shred : strip){
                shred.draw(x, y);
                x+=SIZE;
            }
            UI.drawRect(LEFT-1, y-1, SIZE*strip.size()+2, SIZE+2);
            y+=SIZE+GAP;
        }
    }

    /**
     * Returns which column the mouse position is on.
     * This will be the index in the list of the shred that the mouse is on,
     * (or the index of the shred that the mouse would be on if the list were long enough)
     */
    public int getColumn(double x){
        return (int) ((x-LEFT)/(SIZE));
    }

    /**
     * Returns the strip that the mouse position is on.
     * This may be the list of all remaining shreds, the working strip, or
     *  one of the completed strips.
     * If it is not on any strip, then it returns null.
     */
    public List<Shred> getStrip(double y){
        int row = (int) ((y-TOP_ALL)/(SIZE+GAP));
        if (row<=0){
            return allShreds;
        }
        else if (row==1){
            return workingStrip;
        }
        else if (row-2<completedStrips.size()){
            return completedStrips.get(row-2);
        }
        else {
            return null;
        }
    }

    /**
     * Load an image from a file and return as a two-dimensional array of Color.
     * From COMP 102 assignment 8&9.
     * Maybe useful for the challenge. Not required for the core or completion.
     */
    public Color[][] loadImage(String imageFileName) {
        if (imageFileName==null || !Files.exists(Path.of(imageFileName))){
            return null;
        }
        try {
            BufferedImage img = ImageIO.read(Files.newInputStream(Path.of(imageFileName)));
            int rows = img.getHeight();
            int cols = img.getWidth();
            Color[][] ans = new Color[rows][cols];
            for (int row = 0; row < rows; row++){
                for (int col = 0; col < cols; col++){                
                    Color c = new Color(img.getRGB(col, row));
                    ans[row][col] = c;
                }
            }
            return ans;
        } catch(IOException e){UI.println("Reading Image from "+imageFileName+" failed: "+e);}
        return null;
    }

    /**
     * Save a 2D array of Color as an image file
     * From COMP 102 assignment 8&9.
     * Maybe useful for the challenge. Not required for the core or completion.
     */
    public  void saveImage(Color[][] imageArray, String imageFileName) {
        int rows = imageArray.length;
        int cols = imageArray[0].length;
        BufferedImage img = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Color c =imageArray[row][col];
                img.setRGB(col, row, c.getRGB());
            }
        }
        try {
            if (imageFileName==null) { return;}
            ImageIO.write(img, "png", Files.newOutputStream(Path.of(imageFileName)));
        } catch(IOException e){UI.println("Image reading failed: "+e);}

    }

    /**
     * Creates an object and set up the user interface
     */
    public static void main(String[] args) {
        DeShredder ds =new DeShredder();
        ds.setupGUI();

    }

}