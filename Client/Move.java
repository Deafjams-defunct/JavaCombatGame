/**
    @author: Dylan Foster
    @version: 12/14/12
    @description: Object representing the movement of a player
*/

public class Move extends Action
{

    //Direction of this movement
    private int direction;
    
    /**
        Constructor
        @param int Player number
    */
    public Move ( int inPlayer )
    {

        //Constructs using Action's constructor
        super( GameConstants.LOW_PRIORITY, inPlayer );
        
    }
    
    /**
        Constructor
        @param int Player number
        @param int Direction of movement
    */
    public Move ( int inPlayer, int inDirection )
    {

        //Constructs using Action's constructor
        super( GameConstants.LOW_PRIORITY, inPlayer );
        
        //Sets the direction of motion for this object
        this.direction = inDirection;
        
    }

    /**
        Sets direction of this movement
        @param int Direction of movement
    */
    public void setDirection ( int inDirection ) { this.direction = inDirection; }
    
    /**
        Gets direction of this movement
        @return int Direction of movement
    */
    public int getDirection() { return this.direction; }
    
    /**
        Checks if this movement is equal to another one, via direction.
        @param Move Movement to check against
        @return boolean Movements are the same
    */
    public boolean equals ( Move inMove )
    {
        
        //If direction of given and this movement are the same, the movements are the same.
        if ( this.direction == inMove.getDirection() ) { return true; }
        
        //Otherwise, they are not the same.
        return false;
        
    }
    
    /**
        Converts this Movement to its string representation
        @param String Representation of this movement
    */
    public String toString ()
    {
        
      return "MOVE " + super.getPlayer() + " " + this.direction;

    }

}