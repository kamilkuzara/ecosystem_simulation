import java.util.List;

/**
 * A simple model of a sheep.
 * Sheep age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 *          extended by Kamil Kuzara and Hedi Gharbi
 * @version 2019.02.22
 */
public class Sheep extends Prey
{
    // Characteristics shared by all sheep (class variables).

    // The age at which a sheep can start to breed.
    private static final int REPRODUCTION_AGE = 7;
    // The age to which a sheep can live.
    private static final int MAX_AGE = 100;
    // The likelihood of a sheep breeding.
    private static final double REPRODUCTION_PROBABILITY = 0.10;
    // The maximum number of births.
    private static final int MAX_REPRODUCTION_SIZE = 8;
    // The food value of a single food portion. In effect, this is the
    // number of steps a sheep can go before it has to eat again.
    private static final int FOOD_VALUE = 15;
    
    /**
     * Create a new sheep. A sheep may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the sheep will have random age and hunger level.
     * @param MAX_AGE The maximum age of the sheep.
     * @param FOOD_VALUE The food value of the sheep.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param geneList The genes possessed by the sheep.
     */
    public Sheep(boolean randomAge, Field field, Location location, List<Gene> geneList)
    {
        super(randomAge, MAX_AGE, FOOD_VALUE, field, location, geneList);
    }
    
    /**
     * This method returns the minimum age for the sheep to be able to breed.
     */
    public int getReproductionAge()
    {
        return REPRODUCTION_AGE;
    }
    
    /**
     * This method returns the maximum age the sheep can live.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * This method returns the likelihood of the sheep's reproduction.
     */
    public double getReproductionProbability()
    {
        return REPRODUCTION_PROBABILITY;
    }
    
    /**
     * This method returns the maximum number of births that the sheep can give.
     */
    public int getMaxReproductionSize()
    {
        return MAX_REPRODUCTION_SIZE;
    }
    
    /**
     * This method returns the number of steps left before the sheep has to to eat again.
     */
    public int getFoodValue()
    {
        return FOOD_VALUE;
    }
    
    /**
     * Check whether or not this sheep is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newSheep A list to return newly born sheep.
     */ 
    public void giveBirth(List<Organism> newSheep, List<Gene> childrenGenes, Field field, Location location)
    {
        Sheep young = new Sheep(false, field, location, childrenGenes);
        newSheep.add(young);
    }
    
    /**
     * This method checks if the animals around are of the same species as the sheep : 
     * if they are, they are added to the list "animalsAround". In fact, this method 
     * is used to verify one condition for 2 animals to breed.
     */
    public List<Animal> getSameSpecies(List<Animal> animalsAround)
    {
        for(int i=0; i < animalsAround.size(); i++)
        {
            Animal animal = animalsAround.get(i);
            if(!(animal instanceof Sheep))
                animalsAround.remove(animal);
        }
        return animalsAround;
    }
}
