package Assignment1.Refactored;

import java.nio.file.Path;

/**
 * Shred stores information about a shred.
 * All shreds are square images.
 */
public record Shred(String filename, double size) {

    public Shred {
        assert filename != null : "Filename must not be null";
        assert !filename.isEmpty() : "Filename must not be empty";
        assert filename.endsWith(".png") : "Filename must end with .png";
        assert size > 0 : "Size must be positive";
        assert size <= 1000 : "Size must not exceed 1000 pixels";
    }
    
    Shred(Path dir, int id, double size) {
        this(dir.resolve(id + ".png").toString(), size);
    }

    public void draw(double left, double top, GraphicsAdapter ui) {
        ui.drawImage(filename, left, top, size, size);
    }
}
