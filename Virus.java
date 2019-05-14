import java.util.Random;
import java.util.List;

/**
 * Class Virus. The class extends the class Organism.
 * The class defines all the features of a virus.
 *
 * @author Kamil Kuzara and Hedi Gharbi
 * @version 2019.02.22
 */
public class Virus extends Organism
{
    public enum VirusType
    {
        TYPE1,
        TYPE2,
        TYPE3;
        
        public static VirusType randomise()
        {
            Random rand = new Random();
            VirusType[] types = values();
            return types[rand.nextInt(types.length)];
        }
    }
    
    // Characteristics shared by all viruses (class variables).
    // The age at which a virus can start to spread.
    private static final int SPREADING_AGE = 15;
    // The age to which a virus can exist.
    private static final int MAX_AGE = 100;
    // The likelihood of a virus spreading.
    private static final double SPREADING_PROBABILITY = 0.1;
    // The maximum number of new viruses resulting from spreading.
    private static final int MAX_SPREAD_SIZE = 2;
    // The type of the virus.
    private VirusType type;
    
    /**
     * Constructor for new objects of type Virus.
     * 
     * @param randomAge Whether the plant has a random age or not.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param type The type of the virus.
     */
    public Virus(boolean randomAge, Field field, Location location, VirusType type)
    {
        super(randomAge, MAX_AGE, field, location);
        
        if(randomAge)
        {
             type = VirusType.randomise();
        }
        else
        {
            this.type = type;
        }
    }
    
    /**
     * This method returns the minimum age for the virus to be able to spread.
     */
    public int getReproductionAge()
    {
        return SPREADING_AGE;
    }
    
    /**
     * This method returns the maximum age the virus can exist.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * This method returns the likelihood of the virus's spreading.
     */
    public double getReproductionProbability()
    {
        return SPREADING_PROBABILITY;
    }
    
    /**
     * This method returns the maximum spread number for the virus.
     */
    public int getMaxReproductionSize()
    {
        return MAX_SPREAD_SIZE;
    }
    
    /**
     * Return the type of the virus.
     */
    public VirusType getType()
    {
        return type;
    }
    
    /**
     * This is what the virus does - it spreads around the field. 
     * Sometimes it will cease to exist of old age.
     * 
     * @param newViruses A list to return newly created viruses.
     */
    public void act(List<Organism> newViruses, Day day)
    {
        incrementAge(MAX_AGE);
        if(isAlive()) {
            spread(newViruses);
        }
    }
    
    /**
     * Make the virus spread around the field. The method is
     * equivalent to the reproduce(...) methods in other classes.
     */
    private void spread(List<Organism> newViruses)
    {
        // New viruses are created into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation(), this);
        int spreadNumber = 0;
        
        if(canBreed())
        {
            spreadNumber = breed();
        }
        
        for(int b = 0; b < spreadNumber && free.size() > 0; b++) {
            Location loc = free.remove(0);
            createNew(newViruses, field, loc);
        }
    }
    
    /**
     * Check whether or not this virus is to spread at this step.
     * If it "meets" an animal in its new location it tries to infect it.
     * New viruses will be made into free adjacent locations.
     * 
     * @param newViruses A list to return newly created viruses.
     */
    private void createNew(List<Organism> newViruses, Field field, Location location)
    {
        Virus young = new Virus(false, field, location, type);
        newViruses.add(young);
        
        // Check if there is an animal to infect at the new location
        List<Object> organisms= getField().getObjectsAt(location);
        
        for(Object organism : organisms)
        {
            if(organism instanceof Animal)
                ((Animal)organism).getInfected(type);   // infect the animal
        }
    }
}
