import java.util.List;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 *          extended by Kamil Kuzara and Hedi Gharbi
 * @version 2019.02.22
 */
public class Rabbit extends Prey
{
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static final int REPRODUCTION_AGE = 5;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 50;
    // The likelihood of a rabbit breeding.
    private static final double REPRODUCTION_PROBABILITY = 0.24;
    // The maximum number of births.
    private static final int MAX_REPRODUCTION_SIZE = 6;
    // The food value of a single food portion. In effect, this is the
    // number of steps a rabbit can go before it has to eat again.
    private static final int FOOD_VALUE = 25;
    
    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have random age and hunger level.
     * @param MAX_AGE The maximum age of the rabbit.
     * @param FOOD_VALUE The food value of the rabbit.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param geneList The genes possessed by the rabbit.
     */
    public Rabbit(boolean randomAge, Field field, Location location, List<Gene> geneList)
    {
        super(randomAge, MAX_AGE, FOOD_VALUE, field, location, geneList);
    }
    
    /**
     * This method returns the minimum age for the rabbit to be able to breed.
     */
    public int getReproductionAge()
    {
        return REPRODUCTION_AGE;
    }
    
    /**
     * This method returns the maximum age the rabbit can live.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * This method returns the likelihood of the rabbit's reproduction.
     */
    public double getReproductionProbability()
    {
        return REPRODUCTION_PROBABILITY;
    }
    
    /**
     * This method returns the maximum number of births that the rabbit can give.
     */
    public int getMaxReproductionSize()
    {
        return MAX_REPRODUCTION_SIZE;
    }
    
    /**
     * This method returns the number of steps left before the rabbit has to eat again.
     */
    public int getFoodValue()
    {
        return FOOD_VALUE;
    }
    
    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    public void giveBirth(List<Organism> newRabbits, List<Gene> childrenGenes, Field field, Location location)
    {
        Rabbit young = new Rabbit(false, field, location, childrenGenes);
        newRabbits.add(young);
    }
    
    /**
     * This method checks if the animals around are of the same species of the rabbit : if they are, they are
     * added to the list "animalsAround". In fact, this method is used to verify one condition for 2 animals
     * to breed.
     */
    public List<Animal> getSameSpecies(List<Animal> animalsAround)
    {
        for(int i=0; i < animalsAround.size(); i++)
        {
            Animal animal = animalsAround.get(i);
            if(!(animal instanceof Rabbit))
                animalsAround.remove(animal);
        }
        return animalsAround;
    }
}
