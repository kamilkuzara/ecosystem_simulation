import java.util.List;

/**
 * Abstract class Prey 
 * This class regroups the animals that share the same features : the animals that can only eat plants and 
 * that can be eaten by the predators.
 * @author Kamil Kuzara and Hedi Gharbi
 * @version 2019.02.22.
 */
public abstract class Prey extends Animal
{
    /**
     * Create a new prey animal. A prey animal may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the prey animal will have random age and hunger level.
     * @param maxAge The maximum age of the prey animal.
     * @param foodValue The food value of the prey animal.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param geneList The genes possessed by the prey animal.
     */
    public Prey(boolean randomAge, int maxAge, int foodValue, Field field, Location location, List<Gene> geneList)
    {
        super(randomAge, maxAge, foodValue, field, location, geneList);
    }
}
