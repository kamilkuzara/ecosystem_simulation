import java.util.Random;
import java.util.List;

/**
 * Class Plant. The class extends the class Organism.
 * The class defines all the features of a plant.
 *
 * @author Kamil Kuzara and Hedi Gharbi
 * @version 2019.02.22
 */
public class Plant extends Organism
{
    // Characteristics shared by all plants (class variables).
    // The age at which a plant can start to reproduce.
    private static final int REPRODUCTION_AGE = 5;
    // The age to which a plant can live.
    private static final int MAX_AGE = 100;
    // The maximum height a plant can reach.
    private static final int MAX_HEIGHT = 5;
    // The likelihood of a plant reproducing.
    private static final double REPRODUCTION_PROBABILITY = 0.1;
    // The maximum number of new plants resulting from reproduction.
    private static final int MAX_REPRODUCTION_SIZE = 2;
    
    // The plant's height
    private int height;
    
    /**
     * Create a new plant at location in field.
     * 
     * @param randomAge Whether the plant has a random age or not.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plant(boolean randomAge, Field field, Location location)
    {
        super(randomAge, MAX_AGE, field, location);
        
        if(randomAge) {
            Random rand = new Random();
            height = rand.nextInt(MAX_HEIGHT);
        }
        else {
            height = 0;
        }
    }
    
    /**
     * This method returns the minimum age for the plant to be able to reproduce.
     */
    public int getReproductionAge()
    {
        return REPRODUCTION_AGE;
    }
    
    /**
     * This method returns the maximum age the plant can live.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * This method returns the likelihood of the plant's reproduction.
     */
    public double getReproductionProbability()
    {
        return REPRODUCTION_PROBABILITY;
    }
    
    /**
     * This method returns the maximum number of new plants that the plant can give.
     */
    public int getMaxReproductionSize()
    {
        return MAX_REPRODUCTION_SIZE;
    }
    
    /**
     * This is what the plant does - it grows. 
     * Sometimes it will reproduce or cease to exist of old age.
     * 
     * @param newPlants A list to return newly created plants.
     */
    public void act(List<Organism> newPlants, Day day)
    {
        incrementAge(MAX_AGE);
        if(isAlive()) {
            grow();
            reproduce(newPlants);
        }
    }
    
    /**
     * Make the plant reproduce
     * 
     * @param newPlants The list to store the newly created plants.
     */
    private void reproduce(List<Organism> newPlants)
    {
        // New plants are created into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation(), this);
        int births = 0;
        
        if(canBreed())
        {
            births = breed();
        }
        
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            createNew(newPlants, field, loc);
        }
    }
    
    /**
     * Create new plant.
     * 
     * @param newPlants A list to return newly created plants.
     */
    private void createNew(List<Organism> newPlants, Field field, Location location)
    {
        Plant young = new Plant(false, field, location);
        newPlants.add(young);
    }
    
    /**
     * Make the plant grow, i.e. increase its height.
     */
    private void grow()
    {
        if(height <= MAX_HEIGHT)
            height++;
    }
    
    /**
     * Decrease the plant's height when it is eaten.
     */
    public void decreaseHeight()
    {
        height -= 3;
        if(height <= 0) {
            setDead();
        }
    }
}
