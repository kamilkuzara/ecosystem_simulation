import java.util.List;

/**
 * A simple model of a wolf.
 * Wolves age, move, eat prey animals, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 *          extended by Kamil Kuzara and Hedi Gharbi
 * @version 2019.02.22
 */
public class Wolf extends Predator
{
    // Characteristics shared by all wolves (class variables).
    
    // The age at which a wolves can start to breed.
    private static final int REPRODUCTION_AGE = 15;
    // The age to which a wolves can live.
    private static final int MAX_AGE = 140;
    // The likelihood of a wolves breeding.
    private static final double REPRODUCTION_PROBABILITY = 0.14;
    // The maximum number of births.
    private static final int MAX_REPRODUCTION_SIZE = 2;
    // The food value of a single prey animal. In effect, this is the
    // number of steps a wolf can go before it has to eat again.
    private static final int FOOD_VALUE = 11;
    
    /**
     * Create a wolf. A wolf can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the wolf will have random age and hunger level.
     * @param MAX_AGE The maximum age of the wolf.
     * @param FOOD_VALUE The food value of the wolf.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param geneList The genes possessed by the wolf.
     */
    public Wolf(boolean randomAge, Field field, Location location, List<Gene> geneList)
    {
        super(randomAge, MAX_AGE, FOOD_VALUE, field, location, geneList);
    }
    
    /**
     * This method returns the minimum age for the wolf to be able to breed.
     */
    public int getReproductionAge()
    {
        return REPRODUCTION_AGE;
    }
    
    /**
     * This method returns the maximum age the wolf can live.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * This method returns the likelihood of the wolf's reproduction.
     */
    public double getReproductionProbability()
    {
        return REPRODUCTION_PROBABILITY;
    }
    
    /**
     * This method returns the maximum number of births that the wolf can give.
     */
    public int getMaxReproductionSize()
    {
        return MAX_REPRODUCTION_SIZE;
    }
    
    /**
     * This method returns the number of steps left for the wolf to be able to eat again.
     */
    public int getFoodValue()
    {
        return FOOD_VALUE;
    }
    
    /**
     * Check whether or not this wolf is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newWolves A list to return newly born wolves.
     */
    public void giveBirth(List<Organism> newWolves, List<Gene> childrenGenes, Field field, Location location)
    {
        Wolf young = new Wolf(false, field, location, childrenGenes);
        newWolves.add(young);
    }
    
    /**
     * This method checks if the animals around are of the same species of the wolf : if they are, they are
     * added to the list "animalsAround". In fact, this method is used to verify one condition for 2 animals
     * to breed.
     */
    public List<Animal> getSameSpecies(List<Animal> animalsAround)
    {
        for(int i=0; i < animalsAround.size(); i++)
        {
            Animal animal = animalsAround.get(i);
            if(!(animal instanceof Wolf))
                animalsAround.remove(animal);
        }
        return animalsAround;
    }
}
