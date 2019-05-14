import java.util.List;

/**
 * Abstract class Predator
 * This class regroups the animals that share the same features : the animals that can eat the prey ones and 
 * that cannot be eaten. 
 * @author Kamil Kuzara and Hedi Gharbi
 * @version 2019.02.22.
 */
public abstract class Predator extends Animal
{
    /**
     * Create a predator. A predator can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the predator will have random age and hunger level.
     * @param maxAge The maximum age of the predator.
     * @param foodValue The food value of the predator.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param geneList The genes possessed by the predator.
     */
    public Predator(boolean randomAge, int maxAge, int foodValue, Field field, Location location, List<Gene> geneList)
    {
        super(randomAge, maxAge, foodValue, field, location, geneList);
    }
}
