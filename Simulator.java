import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes and Michael Kölling
 *          extended by Kamil Kuzara and Hedi Gharbi
 * @version 2019.02.22
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The maximum number of organisms at one location.
    private static final int MAX_ORGANISMS_AT_LOCATION = 3;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.05;
    // The probability that a wolf will be created in any given grid position.
    private static final double WOLF_CREATION_PROBABILITY = 0.04;
    // The probability that a mosquito will be created in any given grid position.
    private static final double MOSQUITO_CREATION_PROBABILITY = 0.01;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;
    // The probability that a sheep will be created in any given grid position.
    private static final double SHEEP_CREATION_PROBABILITY = 0.05;
    // The probability that a plant will be created in any given grid position.
    private static final double PLANT_CREATION_PROBABILITY = 0.25;
    // The probability that a virus will be created in any given grid position.
    private static final double VIRUS_CREATION_PROBABILITY = 0.02;

    // List of organisms in the field.
    private List<Organism> organisms;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    // The current day.
    private Day day;
    // List for maintaining the organism hierarchy.
    private List<Class> organismHierarchy;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        organisms = new ArrayList<>();
        field = new Field(depth, width, MAX_ORGANISMS_AT_LOCATION);
        day = new Day();
        
        //Create the organism hierarchy.
        organismHierarchy = new ArrayList<>();
        organismHierarchy.add(Animal.class);
        organismHierarchy.add(Plant.class);
        organismHierarchy.add(Virus.class);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Rabbit.class, Color.ORANGE);
        view.setColor(Fox.class, Color.BLUE);
        view.setColor(Wolf.class, Color.BLACK);
        view.setColor(Mosquito.class, Color.MAGENTA);
        view.setColor(Sheep.class, Color.RED);
        view.setColor(Plant.class, Color.GREEN);
        view.setColor(Virus.class, Color.CYAN);
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            delay(60);
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;
        day.incrementTime();

        // Provide space for newborn organisms.
        List<Organism> newOrganisms = new ArrayList<>();        
        // Let all organisms act.
        for(Iterator<Organism> it = organisms.iterator(); it.hasNext(); ) {
            Organism organism = it.next();
            organism.act(newOrganisms, day);
            if(! organism.isAlive()) {
                it.remove();
            }
        }
               
        // Add the newly created organisms to the main lists.
        organisms.addAll(newOrganisms);

        view.showStatus(step, day, field, organismHierarchy);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        day.reset();
        organisms.clear();
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, day, field, organismHierarchy);
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Fox fox = new Fox(true, field, location, Animal.Gene.randomise());
                    organisms.add(fox);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Rabbit rabbit = new Rabbit(true, field, location, Animal.Gene.randomise());
                    organisms.add(rabbit);
                }
                else if(rand.nextDouble() <= WOLF_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Wolf wolf = new Wolf(true, field, location, Animal.Gene.randomise());
                    organisms.add(wolf);
                }
                else if(rand.nextDouble() <= MOSQUITO_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Mosquito mosquito = new Mosquito(true, field, location, Animal.Gene.randomise());
                    organisms.add(mosquito);
                }
                else if(rand.nextDouble() <= SHEEP_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Sheep sheep = new Sheep(true, field, location, Animal.Gene.randomise());
                    organisms.add(sheep);
                }
                else if(rand.nextDouble() <= PLANT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Plant plant = new Plant(true, field, location);
                    organisms.add(plant);
                }
                else if(rand.nextDouble() <= VIRUS_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Virus virus = new Virus(true, field, location, Virus.VirusType.randomise());
                    organisms.add(virus);
                }
                // else leave the location empty.
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}
