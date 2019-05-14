import java.util.Random;
import java.util.List;
import java.util.HashMap;

/**
 * Abstract class Organism. The class defines all
 * the common features of organisms in the simulation.
 *
 * @author Kamil Kuzara and Hedi Gharbi
 * @version 2019.02.22
 */
public abstract class Organism
{
    // Characteristics shared by all organisms (class variables).
    // A shared random number generator to control reproduction.
    private static final Random rand = Randomizer.getRandom();
    // A shared mapping of the virus types to their gene equivalences.
    private static HashMap<Virus.VirusType, Animal.Gene> diseaseMap;
    // The number by which the stepsLeft variable decreases when an organism is infected with the TYPE1 virus.
    private static final int TYPE1_STEPS_DECREASE = 2;
    // The number by which the stepsLeft variable decreases when an organism is infected with the TYPE2 virus.
    private static final int TYPE2_STEPS_DECREASE = 5;
    // The number by which the stepsLeft variable decreases when an organism is infected with the TYPE3 virus.
    private static final int TYPE3_STEPS_DECREASE = 8;
    
    // Individual characteristics (instance fields).
    // Whether the organism is alive or not.
    private boolean alive;
    // The organism's field.
    private Field field;
    // The organism's position in the field.
    private Location location;
    // The organism's age.
    private int age;
    // The number of steps left for the organism to live. It may be affected by a disease.
    private int stepsLeft;
    
    /**
     * Constructor for the new objects of type Organism.
     * 
     * @param randomAge Whether the plant has a random age or not.
     * @param maxAge The maximum age to which the animal may live.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Organism(boolean randomAge, int maxAge, Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
        
        if(randomAge) {
            age = rand.nextInt(maxAge);
            stepsLeft = maxAge - age;
        }
        else {
            age = 0;
            stepsLeft = maxAge;
        }
        
        if(diseaseMap == null)
        {
            diseaseMap = new HashMap<>();
            diseaseMap.put(Virus.VirusType.TYPE1, Animal.Gene.TYPE1);
            diseaseMap.put(Virus.VirusType.TYPE2, Animal.Gene.TYPE2);
            diseaseMap.put(Virus.VirusType.TYPE3, Animal.Gene.TYPE3);
        }
    }
    
    /**
     * This method returns the minimum age for the organism to be able to reproduce.
     */
    abstract public int getReproductionAge();
    
    /**
     * This method returns the maximum age the organism can live.
     */
    abstract public int getMaxAge();
    
    /**
     * This method returns the likelihood of the organism's reproduction.
     */
    abstract public double getReproductionProbability();
    
    /**
     * This method returns the maximum number of new organisms that the organism can give.
     */
    abstract public int getMaxReproductionSize();
    
    /**
     * Make this organism act - that is: make it do
     * whatever it wants/needs to do.
     * 
     * @param newOrganisms A list to receive newly created organisms.
     * @param day The current day.
     */
    abstract public void act(List<Organism> newOrganisms, Day day);
    
    /**
     * Check whether the organism is alive or not.
     * @return true if the organism is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }
    
    /**
     * Increase the age. This could result in the organism's death.
     */
    protected void incrementAge(int maxAge)
    {
        age++;
        stepsLeft--;
        if(age > maxAge || stepsLeft < 0) {
            setDead();
        }
    }
    
    /**
     * Indicate that the organism is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
    
    /**
     * Return the organism's location.
     * @return The organism's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the organism at the new location in the given field.
     * @param newLocation The organism's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the organism's field.
     * @return The organism's field.
     */
    protected Field getField()
    {
        return field;
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can reproduce.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(rand.nextDouble() <= getReproductionProbability()) {
            births = rand.nextInt(getMaxReproductionSize()) + 1;
        }
        return births;
    }

    /**
     * An organism can breed if it has reached the breeding age.
     * @return true if the organism can breed, false otherwise.
     */
    protected boolean canBreed()
    {
        return age >= getReproductionAge();
    }
    
    /**
     * Return the step decrease after getting infected by a virus.
     */
    protected int getStepDecrease(Virus.VirusType virusType)
    {
        if(virusType == Virus.VirusType.TYPE1)
            return TYPE1_STEPS_DECREASE;
        else if(virusType == Virus.VirusType.TYPE2)
            return TYPE2_STEPS_DECREASE;
        else if(virusType == Virus.VirusType.TYPE3)
            return TYPE3_STEPS_DECREASE;
        else
            return 0;
    }
    
    /**
     * Decrease the number of the steps left.
     * 
     * @param value The number by which we want to decrease.
     */
    protected void decreaseStepsLeft(int value)
    {
        stepsLeft -= value;
    }
    
    /**
     * Return true if the organism with the specified set of genes would be
     * infected by the specified virus type.
     * 
     * @param genes The list of the organism's genes.
     * @param virusType The type of the virus that would infect the organism.
     */
    protected boolean isInfected(List<Animal.Gene> genes, Virus.VirusType virusType)
    {
        Animal.Gene virusEquivalence = diseaseMap.get(virusType);
        
        for(Animal.Gene next : genes)
            if(next == virusEquivalence)
                return false;
                
        return true;
    }
    
    /**
     * Return true if the organism is higher in the class hierarchy
     * than the other specified organism. When comparing two organisms of the
     * same type true is returned.
     * 
     * @param organism The organism to compare with.
     * @param organismHierarchy The hierarchy of the organisms in the simulation.
     */
    public boolean isHigher(Organism organism, List<Class> organismHierarchy)
    {
        int indexOfThis = organismHierarchy.indexOf(this.getClass());
        int indexOfOther = organismHierarchy.indexOf(organism.getClass());
        
        if(indexOfThis <= indexOfOther){
            return true;
        }
        else {
            return false;
        }
        
    }
}
