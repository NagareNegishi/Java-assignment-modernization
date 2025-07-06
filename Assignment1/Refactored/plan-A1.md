Looking at the code, you're exactly right!

## What the program does:

**Shred class:**
- Represents a single puzzle piece (40x40 pixel image)
- Just stores filename and ID
- Has drawing methods (normal, with border, highlighted)

**DeShredder class:**
- **Document reconstruction tool** - like putting together shredded paper
- Manages **3 lists**:
  1. `allShreds` - all unused pieces at the top
  2. `workingStrip` - current row being assembled
  3. `completedStrips` - finished rows

**User workflow:**
1. Load a library of shred images (1.png, 2.png, etc.)
2. Drag pieces from `allShreds` to `workingStrip` to build a row
3. When row is complete, move it to `completedStrips`
4. Repeat until document is reconstructed
5. Save the final reconstructed image

**Key features:**
- Mouse drag & drop between lists
- "Rotate" button cycles through unused pieces
- "Shuffle" randomizes unused pieces
- "Highlight Potential Next" suggests matching pieces
- Can reorder completed strips

Simply put:
- **Shred** = puzzle piece data
- **DeShredder** = list management + UI logic

## Plan for Refactoring

1. remove ecs100 dependency from Shred class
2. remove ecs100 dependency from DeShredder class
3. refactor Shred class
4. refactor DeShredder class
5. Implement the interface which replaced the ecs100 dependency
6. Add unit tests for Shred class
7. Add unit tests for DeShredder class

## Refactoring Opportunities

- **Shred class**:
    - The Puzzle piece should be immutable -> it should be record
    - Shred class should not have any drawing methods, it should only store data
    - Create a separate class for drawing operations

- **DeShredder class**:
    - Use of Stream, Collections methods for list management
    - Method extraction for clarity
    - Remove redundant validation checks


## ECS100 Dependency Removal

### Shred class
- drawImage
- drawRect
- setColor
- fillRect

### DeShredder class
- addButton
- setMouseListener
- setWindowSize
- setDivider
- UIFileChooser.open/save
- println
- clearGraphics
- drawRect
- setColor
- quit

Those methods can be categorized into 3 groups:
- Graphics methods
- File I/O methods
- UI methods

Therefore, we will create 3 interfaces to replace the ecs100 dependency:
1. `GraphicsAdapter` - for drawing operations
2. `FileChooserAdapter` - for file operations
3. `UIAdapter` - for UI operations

1. **GraphicsAdapter**:
    - drawImage
    - drawRect
    - setColor
    - fillRect
    - clearGraphics

2. **FileChooserAdapter**:
    - open
    - save

3. **UIAdapter**:
    - addButton
    - setMouseListener
    - setWindowSize
    - setDivider
    - println (Can be just System.out.println)
    - quit
