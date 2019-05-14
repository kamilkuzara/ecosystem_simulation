import java.util.List;

/**
 * A simple model of a mosquito.
 * Mosquitos age, move, feed on animals, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 *          extended by Kamil Kuzara and Hedi Gharbi
 * @version 2019.02.22
 */
public class Mosquito extends Predator
{
    // Characteristics shared by all mosquitos (class variables).
    
    // The age at which a mosquito can start to breed.
    private static final int REPRODUCTION_AGE = 2;
    // The age to which a mosquito can live.
    private static final int MAX_AGE = 30;
    // The likelihood of a mosquito breeding.
    private static final double REPRODUCTION_PROBABILITY = 0.09;
    // The maximum number of births.
    private static final int MAX_REPRODUCTION_SIZE = 8;
    // The food value of a single prey animal. In effect, this is the
    // number of steps a mosquito can go before it has to eat again.
    private static final int FOOD_VALUE = 5;
  

    /**
     * Create a mosquito. A mosquito can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the mosquito will have random age and hunger level.
     * @param MAX_AGE The maximum age of the mosquito.
     * @param FOOD_VALUE The food value of the mosquito.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param geneList The genes possessed by the mosquito.
     */
    public Mosquito(boolean randomAge, Field field, Location location, List<Gene> geneList)
    {
        super(randomAge, MAX_AGE, FOOD_VALUE, field, location, geneList);
    }
    
    /**
     * This method returns the minimum age for the mosquito to be able to breed.
     */
    public int getReproductionAge()
    {
        return REPRODUCTION_AGE;
    }
    
    /**
     * This method returns the maximum age the mosquito can live.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * This method returns the likelihood of the mosquito's reproduction.
     */
    public double getReproductionProbability()
    {
        return REPRODUCTION_PROBABILITY;
    }
    
    /**
     * This method returns the maximum number of births that the mosquito can give.
     */
    public int getMaxReproductionSize()
    {
        return MAX_REPRODUCTION_SIZE;
    }
    
    /**
     * This method returns the number of steps left before the mosquito has to eat again.
     */
    public int getFoodValue()
    {
        return FOOD_VALUE;
    }

    /**
     * Check whether or not this mosquito is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newMosquitos A list to return newly born mosquitos.
     */ 
    public void giveBirth(List<Organism> newMosquitos, List<Gene> childrenGenes, Field field, Location location)
    {
        Mosquito young = new Mosquito(false, field, location, childrenGenes);
        newMosquitos.add(young);
    }
    
    /**
     * This method checks if the animals around are of the same species of the mosquito : if they are, they are
     * added to the list "animalsAround". In fact, this method is used to verify one condition for 2 animals
     * to breed.
     */
    public List<Animal> getSameSpecies(List<Animal> animalsAround)
    {
        for(int i=0; i < animalsAround.size(); i++)
        {
            Animal animal = animalsAround.get(i);
            if(!(animal instanceof Mosquito))
                animalsAround.remove(animal);
        }
        return animalsAround;
    }
}
