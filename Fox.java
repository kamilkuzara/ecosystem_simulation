import java.util.List;

/**
 * A simple model of a fox.
 * Foxes age, move, eat prey animals, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 *          extended by Kamil Kuzara and Hedi Gharbi
 * @version 2019.02.22
 */
public class Fox extends Predator
{
    // Characteristics shared by all foxes (class variables).
    
    // The age at which a fox can start to breed.
    private static final int REPRODUCTION_AGE = 12;
    // The age to which a fox can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a fox breeding.
    private static final double REPRODUCTION_PROBABILITY = 0.12;
    // The maximum number of births.
    private static final int MAX_REPRODUCTION_SIZE = 2;
    // The food value of a single prey animal. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int FOOD_VALUE = 20;
    
    
    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param MAX_AGE The maximum age of the fox.
     * @param FOOD_VALUE The food value of the fox.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param geneList The genes possessed by the fox.
     */
    public Fox(boolean randomAge, Field field, Location location, List<Gene> geneList)
    {
        super(randomAge, MAX_AGE, FOOD_VALUE, field, location, geneList);
    }
    
    /**
     * This method returns the minimum age for the fox to be able to breed.
     */
    public int getReproductionAge()
    {
        return REPRODUCTION_AGE;
    }
    
    /**
     * This method returns the maximum age the fox can live.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * This method returns the likelihood of the fox's reproduction.
     */
    public double getReproductionProbability()
    {
        return REPRODUCTION_PROBABILITY;
    }
    
    /**
     * This method returns the maximum number of births that the fox can give.
     */
    public int getMaxReproductionSize()
    {
        return MAX_REPRODUCTION_SIZE;
    }
    
    /**
     * This method returns the number of steps left for the fox to be able to eat again.
     */
    public int getFoodValue()
    {
        return FOOD_VALUE;
    }
    
    /**
     * Check whether or not this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newFoxes A list to return newly born foxes.
     */ 
    public void giveBirth(List<Organism> newFoxes, List<Gene> childrenGenes, Field field, Location location)
    {
        Fox young = new Fox(false, field, location, childrenGenes);
        newFoxes.add(young);
    }
    
    /**
     * This method checks if the animals around are of the same species of the fox : if they are, they are
     * added to the list "animalsAround". In fact, this method is used to verify one condition for 2 animals
     * to breed.
     */
    public List<Animal> getSameSpecies(List<Animal> animalsAround)
    {
        for(int i=0; i < animalsAround.size(); i++)
        {
            Animal animal = animalsAround.get(i);
            if(!(animal instanceof Fox))
                animalsAround.remove(animal);
        }
        return animalsAround;
    }
}
