/**
    @author: Dylan Foster
    @version: 12/14/12
    @description: Object representing an adjustment of health for a player.
*/

public class HealthChange extends Action
{
    
    //Counter to give each health change a unique identifier
    private static int identifierCounter = 0;
    
    //The amount of health this change represents; this change's unique identifier
    int healthChange, id;

    /**
        Constructor
        @param int Player number
        @param int Amount of health change
    */
    public HealthChange ( int inPlayer, int inChange )
    {
        
        //Use Action's constructor
        super( GameConstants.HIGHEST_PRIORITY, inPlayer );
        
        //Store the health change 
        this.healthChange = inChange;
        
        //Store ID and increment the ID counter.
        this.id = identifierCounter++;
        
    }
    
    /**
        Constructor
        @param int Player number
        @param int Amount of health change
        @param int ID of this HealthChange
    */
    public HealthChange ( int inPlayer, int inChange, int inID )
    {
        
        //Use Action's constructor
        super( GameConstants.HIGHEST_PRIORITY, inPlayer );
        
        //Store the health change
        this.healthChange = inChange;
        
        //Set this object's ID
        //This is used when reconstructing health changes from the server.
        this.id = inID;
        
    }
    
    /**
        Gets the amount of health this change represents
        @return int Amount of health
    */
    public int getHealthChange () { return this.healthChange; }
    
    /**
        Return the ID of this change
        @return int ID
    */
    public int getID () { return this.id; }
    
    /**
        Checks if this change is equal to another
        @param HealthChange Change to check against
        @return boolean Changes are the same
    */
    public boolean equals ( HealthChange inHealthChange )
    {
        
        if ( this.id == inHealthChange.getID() ) { return true; }
        
        return false;
        
    }
    
    /**
        Converts this HealthChange into String form
        @return String Representation of this HealthChange as a String
    */
    public String toString () { return "HEALTH " + this.getPlayer() + " " + this.healthChange + " " + this.id; }
    
}