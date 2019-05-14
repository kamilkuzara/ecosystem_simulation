import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Represent a rectangular grid of field positions.
 * Each position is able to store a single animal.
 * 
 * @author David J. Barnes and Michael Kölling
 *          extended by Kamil Kuzara and Hedi Gharbi
 * @version 2016.02.29
 */
public class Field
{
    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();
    
    // The depth and width of the field.
    private int depth, width, capacity;
    // Storage for the animals.
    private Object[][][] field;

    /**
     * Represent a field of the given dimensions.
     * @param depth The depth of the field.
     * @param width The width of the field.
     * @param locationCapacity The maximum number of organisms that can occupy the same location.
     */
    public Field(int depth, int width, int locationCapacity)
    {
        this.depth = depth;
        this.width = width;
        this.capacity = locationCapacity;
        field = new Object[locationCapacity][depth][width];
    }
    
    /**
     * Empty the field.
     */
    public void clear()
    {
        for(int row = 0; row < depth; row++) {
            for(int col = 0; col < width; col++) {
                for(int counter = 0; counter < capacity; counter++)
                    field[counter][row][col] = null;
            }
        }
    }
    
    /**
     * Clear the given location.
     * @param location The location to clear.
     */
    public void clear(Location location)
    {
        for(int counter = 0; counter < capacity; counter++)
            field[counter][location.getRow()][location.getCol()] = null;
    }
    
    /**
     * Place an organism at the given location.
     * If there is already an organism of the same type 
     * at the location it will be lost.
     * 
     * @param animal The animal to be placed.
     * @param row Row coordinate of the location.
     * @param col Column coordinate of the location.
     */
    public void place(Object animal, int row, int col)
    {
        place(animal, new Location(row, col));
    }
    
    /**
     * Place an organism at the given location.
     * If there is already an organism of the same type 
     * at the location it will be lost.
     * 
     * @param animal The animal to be placed.
     * @param location Where to place the animal.
     */
    public void place(Object organism, Location location)
    {
        int row = location.getRow();
        int col = location.getCol();
        for(int counter = 0; counter < capacity; counter++)
        {
            if(field[counter][row][col] == null || field[counter][row][col].getClass() == organism.getClass())
            {
                field[counter][row][col] = organism;
                break;
            }    
        }
    }
    
    /**
     * Return the list of organisms at the given location, if any.
     * @param location Where in the field.
     * @return The list of organisms at the given location.
     */
    public List<Object> getObjectsAt(Location location)
    {
        return getObjectsAt(location.getRow(), location.getCol());
    }
    
    /**
     * Return the list of organisms at the given location, if any.
     * @param row The desired row.
     * @param col The desired column.
     * @return The list of organisms at the given location.
     */
    public List<Object> getObjectsAt(int row, int col)
    {
        List<Object> organisms = new ArrayList<>();
        
        for(int counter = 0; counter < capacity; counter++)
        {
            if(field[counter][row][col] != null)
                organisms.add(field[counter][row][col]);
        }
        
        return organisms;
    }
    
    /**
     * Generate a random location that is adjacent to the
     * given location, or is the same location.
     * The returned location will be within the valid bounds
     * of the field.
     * @param location The location from which to generate an adjacency.
     * @return A valid location within the grid area.
     */
    public Location randomAdjacentLocation(Location location)
    {
        List<Location> adjacent = adjacentLocations(location);
        return adjacent.get(0);
    }
    
    /**
     * Get a shuffled list of the free adjacent locations 
     * from the point of view of the specified organism.
     * F.e. from an animal's perspective, a location is not
     * free is if contains another animal, because two organisms
     * of the same type cannot occupy the same location.
     * 
     * @param location Get locations adjacent to this.
     * @param testedOrganism The organism for which the locations are tested.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location, Object testedOrganism)
    {
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = adjacentLocations(location);
        for(Location next : adjacent) {
            List<Object> organisms = getObjectsAt(next);
            
            if(organisms.isEmpty()) {
                free.add(next);
            }
            else
            {
                boolean containsSame = false;
                for(Object organism : organisms)
                {
                    if(organism.getClass() == testedOrganism.getClass())
                    {
                        containsSame = true;
                        break;
                    }
                }
                
                if(!containsSame)
                    free.add(next);
            }
        }
        return free;
    }
    
    /**
     * Get a shuffled list of the occupied adjacent locations.
     * @param location Get locations adjacent to this.
     * @return A list of occupied adjacent locations.
     */
    public List<Object> getSameAdjecentOccupants(Location location, Object testedOrganism)
    {
        List<Object> organisms = new ArrayList<>();
        List<Location> adjacent = adjacentLocations(location);
        for(Location next : adjacent) {
            List<Object> occupants = getObjectsAt(next);
            
            for(Object occupant : occupants)    // if the list is empty the loop is not executed
                if(occupant.getClass() == testedOrganism.getClass())
                    organisms.add(occupant);
        }
        
        return organisms;
    }
    
    /**
     * Try to find a free location that is adjacent to the
     * given location. If there is none, return null.
     * The returned location will be within the valid bounds
     * of the field.
     * @param location The location from which to generate an adjacency.
     * @return A valid location within the grid area.
     */
    public Location freeAdjacentLocation(Location location, Object organism)
    {
        // The available free ones.
        List<Location> free = getFreeAdjacentLocations(location, organism);
        if(free.size() > 0) {
            return free.get(0);
        }
        else {
            return null;
        }
    }

    /**
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> adjacentLocations(Location location)
    {
        assert location != null : "Null location passed to adjacentLocations";
        // The list of locations to be returned.
        List<Location> locations = new LinkedList<>();
        if(location != null) {
            int row = location.getRow();
            int col = location.getCol();
            for(int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth) {
                    for(int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }
            
            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }
    
    

    /**
     * Return the depth of the field.
     * @return The depth of the field.
     */
    public int getDepth()
    {
        return depth;
    }
    
    /**
     * Return the width of the field.
     * @return The width of the field.
     */
    public int getWidth()
    {
        return width;
    }
}
