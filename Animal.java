import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 *          extended by Kamil Kuzara and Hedi Gharbi
 * @version 2019.02.22
 */
public abstract class Animal extends Organism
{
    // The animal's gender.
    private Gender sex;
    // The animal's food level, which is increased by eating.
    private int foodLevel;
    // The list of all the genes the animal has.
    private List<Gene> genes;
    // The list of all the diseasees the animal has.
    private List<Virus.VirusType> diseases;
    
    /**
     * Create a new animal at location in field.
     * 
     * @param randomAge If true, the animal will have random age and hunger level.
     * @param maxAge The maximum age of the animal.
     * @param foodValue The food value of the animal.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param geneList The genes possessed by the animal.
     */
    public Animal(boolean randomAge, int maxAge, int foodValue, Field field, Location location, List<Gene> geneList)
    {
        super(randomAge, maxAge, field, location);
        
        diseases = new ArrayList<>();
        
        sex = Gender.randomise();
        
        if(randomAge) {
            Random rand = new Random();
            foodLevel = rand.nextInt(foodValue);
            genes = Gene.randomise();
        }
        else {
            foodLevel = foodValue;
            genes = geneList;
        }
    }
    
    /**
     * Return the number of steps an animal can survive without eating.
     */
    abstract public int getFoodValue();
    
    /**
     * Create a new animal with the list of genes inherited from its parents.
     * 
     * @param newAnimal The list to which the new animal will be assigned.
     * @param childrenGenes The list of genes the new animal will posess.
     */
    abstract protected void giveBirth(List<Organism> newAnimals, List<Gene> childrenGenes, Field field, Location location);
    
    /**
     * Return the list of animals of the same species. Used for breeding.
     * 
     * @param animalsAround The list to be filtered.
     */
    abstract public List<Animal> getSameSpecies(List<Animal> animalsAround);
    
    /**
     * Return the list of animals of the opposite sex. Used for breeding.
     * 
     * @param animalsAround The list to be filtered.
     */
    public List<Animal> getOppositeSexes(List<Animal> animalsAround)
    {
        for(int i=0; i < animalsAround.size(); i++)
        {
            Animal animal = animalsAround.get(i);
            if(animal.getSex() == sex)
                animalsAround.remove(animal);
        }
        return animalsAround;
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * 
     * @param newAnimals A list to receive newly born animals.
     * @param day The current day.
     */
    public void act(List<Organism> newAnimals, Day day)
    {
        incrementAge(getMaxAge());
        incrementHunger();
        if(isAlive()) {
            if(day.getDayState() == Day.DayState.DAY) {
                reproduce(newAnimals);   
            }
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation(), this);
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
                findViruses(newLocation);   // if a move was made, check if the animal should get infected
            }
            else {
                // Overcrowding.
                setDead();
            }
        
        }
    }
    
    /**
     * Check for a virus in a given location. If a virus is found, try to infect the animal.
     * Method gets a list of all organisms in the specified location and filters it for viruses.
     * 
     * @param location The location to inspect for viruses.
     */
    private void findViruses(Location location)
    {
        List<Object> organisms = getField().getObjectsAt(location);
        
        for(Object organism : organisms)
        {
            if(organism instanceof Virus)
            {
                Virus virus = (Virus)organism;
                getInfected(virus.getType());
            }
        }
    }
    
    /**
     * Check if the animal will get infected by the specified virus type, 
     * i.e. if the animal is not immune to the virus. If the animal is not
     * immune, try to infect it. The animal is only infected if it did not
     * already have the disease.
     * 
     * @param virusType The type of the infecting virus.
     */
    public void getInfected(Virus.VirusType virusType)
    {
        if( isInfected(genes, virusType) )
        {
            if(!diseases.contains(virusType))
            {
                diseases.add(virusType);
                
                int stepDecrease = getStepDecrease(virusType);
                decreaseStepsLeft(stepDecrease);
            }
        }
    }
    
    /**
     * Look for food adjacent to the current location.
     * Only the first live form of food is eaten.
     * If the food is an infected animal, the predator gets the same disease.
     * Mosquitos do not kill other animals, they just feed on them 
     * as well as carry diseases.
     * 
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            List<Object> organisms = field.getObjectsAt(where);
            
            // different actions for different types of animals
            if(this instanceof Predator)    // predators only feed on other animals
            {
                for(Object organism : organisms)
                {
                    if(organism instanceof Prey)
                    {
                        Prey preyAnimal = (Prey) organism;
                        if(preyAnimal.isAlive()) { 
                            List<Virus.VirusType> preyDiseases = preyAnimal.getDiseases();  // retrieve the diseases the prey animal had
                            
                            // infect the animal with all the diseases
                            for(Virus.VirusType disease : preyDiseases)
                            {
                                getInfected(disease);
                            }
                            
                            if( !(this instanceof Mosquito) )   // mosquitos do not kill animals when they feed on them
                                preyAnimal.setDead();
                            
                            foodLevel = getFoodValue();
                            return where;
                        }
                    }
                }
            }
            else if(this instanceof Prey)   // prey animals only feed on plants
            {
                for(Object organism : organisms)
                {
                    if(organism instanceof Plant)
                    {
                        Plant plant = (Plant) organism;
                        if(plant.isAlive()) { 
                            plant.decreaseHeight();     // the plant is not eaten in its entirety
                            foodLevel = getFoodValue();
                            return where;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Make the animal breed. The methods receives the list of organisms around
     * and filters it for animals of the same species and opposite sex. It also checks
     * whether both animals can breed. If all the requirements are satisfied, the animals
     * breed.
     * 
     * @param newAnimals The list in which the newly created animals are put.
     */
    private void reproduce(List<Organism> newAnimals)
    {
        // New animals are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation(), this);
        
        // Get the list ofall animals around
        List<Object> sameType = field.getSameAdjecentOccupants(getLocation(), this);
        List<Animal> potentialPartners = new ArrayList<>(); 
        
        List<Gene> partnerGenes;    // containerfor the animal's partner's genes
        List<Gene> childrenGenes = genes;   // container for the childrens' genes
        
        int births = 0;
        
        for(Object next : sameType)
        {
            if(next instanceof Animal){
                potentialPartners.add((Animal)next);
            }
        }
        
        if(canBreed())
        { 
            if(!potentialPartners.isEmpty())
            {
                if( !getSameSpecies(potentialPartners).isEmpty())   // filter for the same species
                {
                    if( !getOppositeSexes(potentialPartners).isEmpty() )    // filter for opposite sexes
                    {
                        for(Animal animal : potentialPartners)
                        {
                            if(animal.canBreed())   // check if the partner can breed
                            {
                                births = breed();
                                if(births > 0)
                                {
                                    partnerGenes = animal.getGeneList();
                                    
                                    // Create a list of inherited genes.
                                    for(Gene animalGene : partnerGenes)
                                    {
                                        if(!childrenGenes.contains(animalGene))
                                            childrenGenes.add(animalGene);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            giveBirth(newAnimals, childrenGenes, field, loc);
        }
    }
    
    /**
     * Make this animal more hungry. This could result in the animal's death.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Return the list of the diseases the animal has.
     * 
     * @return The list of animal's diseases.
     */
    protected List<Virus.VirusType> getDiseases()
    {
        return diseases;
    }
    
    /**
     * Return the sex of the animal.
     * 
     * @return The animal's sex.
     */
    private Gender getSex()
    {
        return sex;
    }
    
    /**
     * Return the list of animal's genes.
     * 
     * @return The list of animal's genes.
     */
    private List<Gene> getGeneList()
    {
        return genes;
    }
    
    public enum Gender
    {
        MALE,
        FEMALE;
        
        public static Gender randomise()
        {
            Random rand = new Random();
            boolean isMale = rand.nextBoolean();
            
            if(isMale)
                return MALE;
            else
                return FEMALE;
        }
    }
    
    public enum Gene
    {
        TYPE1,
        TYPE2,
        TYPE3;
        
        public static List<Gene> randomise()
        {
            List<Gene> genes = new ArrayList<>();
            Random rand = new Random();
            Gene[] geneValues = values();
            
            for(Gene next : geneValues)
            {
                boolean enlist = rand.nextBoolean();
                if(enlist)
                    genes.add(next);
            }
            
            return genes;
        }
    }
}
