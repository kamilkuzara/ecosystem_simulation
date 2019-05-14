/**
 * Class Day models a day, that is a period of
 * time during a simulation. Different times of
 * the day may affect the simulation.
 *
 * @author Kamil Kuzara and Hedi Gharbi
 * @version 2019.02.22
 */
public class Day
{
    public enum DayState
    {
        DAY,
        NIGHT;
    }
    
    private static final int TOTAL_LENGTH = 11;
    
    private int dayLength;
    private int time;
    private int dayNumber;
    private DayState state;
    
    /**
     * Constructor for objects of class Day
     */
    public Day()
    {
        dayLength = TOTAL_LENGTH / 2;
        time = 0;
        dayNumber = 1;
        state = DayState.DAY;
    }
    
    /**
     * Constructor for objects of class Day
     */
    public Day(int length, int time, int dayNo)
    {
        if(length > 0 && length <= TOTAL_LENGTH)
        {
            dayLength = length;
        }
        else
        {
            dayLength = TOTAL_LENGTH / 2;
        }
        
        if(time >= 0 && time <= TOTAL_LENGTH)
        {
            this.time = time;
            
            if(time > dayLength)
                state = DayState.NIGHT;
            else
                state = DayState.DAY;
        }
        else
        {
            time = 0;
            state = DayState.DAY;
        }
        
        if(dayNo > 0)
        {
            dayNumber = dayNo;
        }
        else
        {
            dayNumber = 1;
        }
    }
    
    /**
     * Step through a day. Increment the time during a day.
     */
    public void incrementTime()
    {
        if(time < TOTAL_LENGTH)
        {
            if(time == dayLength)
                changeState();
                
            time++;
        }
        else if(time == TOTAL_LENGTH)
        {
            time = 0;
            incrementDay();
            changeState();
        }
    }
    
    /**
     * Increment the day number.
     */
    public void incrementDay()
    {
        dayNumber++;
    }
    
    /**
     * Change from day to night and vice versa.
     */
    public void changeState()
    {
        if(state == DayState.DAY)
            state = DayState.NIGHT;
        else
            state = DayState.DAY;
    }
    
    /**
     * Return the day state.
     * 
     * @return DayState.DAY or DayState.NIGHT depending on the state value
     */
    public DayState getDayState()
    {
        return state;
    }
    
    /**
     * Reset the time.
     */
    public void reset()
    {
        time = 0;
        dayNumber = 1;
        state = DayState.DAY;
    }
    
    /**
     * Convert the day number to a string and return it.
     */
    public String getNumberString()
    {
        return "" + dayNumber;
    }
    
    /**
     * Convert the day state to a string and return it.
     */
    public String getStateString()
    {
        return "" + state;
    }
}
