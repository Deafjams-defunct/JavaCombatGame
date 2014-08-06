/**
    @author: Dylan Foster
    @version: 12/14/12
    @description: Class representing an Action by a player
*/

public class Action implements Comparable
{
    
    /**
        Class method. Converts an Action in String form into an object of its proper class.
        @param String Representation of an Action as a String
        @return Action Converted Action
    */
    public static Action convertStringToAction ( String inAction )
    {
        
        //Splitting each part of given String into an array
        String[] splitAction = inAction.split( " " );
        
        //If this Action is a Move Action
        if ( splitAction[ 0 ].equals( "MOVE" ) )
        {
            
            //Return newly constructed Move Action with given properties
            return new Move( Integer.parseInt( splitAction[ 1 ] ), Integer.parseInt( splitAction[ 2 ] ) );
            
        }
        //If this Action is a HealthChange
        else if ( splitAction[ 0 ].equals( "HEALTH" ) )
        {
            
            //Return newly constructed HealthChange with given properties
            return new HealthChange( Integer.parseInt( splitAction[ 1 ] ), Integer.parseInt( splitAction[ 2 ] ), Integer.parseInt( splitAction[ 3 ] ) );
            
        }
        //If this Action is an Attack
        else if ( splitAction[ 0 ].equals( "ATTACK" ) )
        {
            
            //If this Action is a MeleeAttack
            if ( splitAction[ 1 ].equals( "MELEE" ) )
            {
                
                //Return newly constructed MeleeAttack with given properties
                return new MeleeAttack( Integer.parseInt( splitAction[ 2 ] ), Double.parseDouble( splitAction[ 5 ] ), Integer.parseInt( splitAction[ 2 ] ), Integer.parseInt( splitAction[ 3 ] ), Integer.parseInt( splitAction[ 6 ] ) );
                
            }
            
        }
        
        //Given String did not represent any known Actions. Unable to return an Action.
        return null;
        
    }
    
    //The priority of this Action, higher means more urgent.
    private int priority;
    
    //The player this Action is assigned to.
    private int player;

    /**
        Constructor, assigns only a priority to this Action
        @param int Priority of this Action
    */
    public Action ( int inPriority ) { this.priority = inPriority; }
    
    /**
        Constructor, assigns a priority and player to this Action
        @param int Priority of this Action
        @param int Player number
    */
    public Action ( int inPriority, int inPlayer )
    {
        
        //Lazily uses previous constructor
        this( inPriority );
        
        //Store this Action's player number
        this.player = inPlayer;
        
    }

    /**
        Sets the priority number of this Action
        @param int Priority to assign
    */
    public void setPriority ( int inPriority ) { this.priority = inPriority; }

    /**
        Gets the priority number of this Action
        @param int Priority number
    */
    public int getPriority() { return this.priority; }
    
    /**
        Sets the player number of this Action
        @param int Player number to assign
    */
    public void setPlayer ( int inPlayer ) { this.player = inPlayer; }
    
    /**
        Gets the player number of this Action
        @return int Player number of this Action
    */
    public int getPlayer() { return this.player; }

    /**
        Compares this Action to another
        @param Object Action to compare to
        @return int Value of this Action compared to the given
    */
    public int compareTo ( Object inObject )
    {

        Action inAction = ( Action ) inObject;
        
        //Priority of this Action is higher than that of the given. This Action is rated higher.
        if ( this.priority > inAction.getPriority() ) { return 1; }
        //Priority of this Action is lower than that of the given. This Acton is rated lower.
        else if ( this.priority < inAction.getPriority() ) { return -1; }
        //Priorities are the same. Both Actions are rated the same.
        else { return 0; }

    }

}