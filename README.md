# Empire-State-Elevator

                     |   
                     |   
                     A   
                    | |  
                   /   \ 
                  /_____\
                 _|_____|_                       
                /         \         _____  ___  ___  _____  _____  _____  _____   
              _|  * * * *  |_      |  ___||   \/   ||  _  ||_   _||  _  ||  ___|  
            _|___ * * * * ___|_    | |___ | |\  /| || |_| |  | |  | |_| || |___   
           |* * *|* * * *|* * *|   |  ___|| | \/ | ||  ___|  | |  |    _||  ___|  
           |* * *|* * * *|* * *|   | |___ | |    | || |     _| |_ | |\ \ | |___   
           |* * *|* * * *|* * *|   |_____||_|    |_||_|    |_____||_| \_\|_____|  
           |* * *|* * * *|* * *|                                                  
          _|_____|_     _|_____|_   _____  _____   ___   _____  _____             
         |* * * * *|* *|* * * * *| |  ___||_   _| /   \ |_   _||  ___|            
         |* * * * *|* *|* * * * *| | |__    | |  | /_\ |  | |  | |___             
         |* * * * *|* *|* * * * *|  \__ \   | |  |  _  |  | |  |  ___|            
         |* * * * *|* *|* * * * *|  ___| |  | |  | | | |  | |  | |___             
         |* * * * *|* *|* * * * *| |_____|  |_|  |_| |_|  |_|  |_____|            
         |* * * * *|* *|* * * * *|                                                
         |* * * * *|* *|* * * * *|  _____  _    _____  _   _   ___   _____  _____ 
         |* * * * *|* *|* * * * *| |  ___|| |  |  ___|| | | | /   \ |_   _||  ___|
         |* * * * *|* *|* * * * *| | |___ | |  | |___ | | | || /_\ |  | |  | |___ 
         |* * * * *|* *|* * * * *| |  ___|| |  |  ___|\ \ / /|  _  |  | |  |  ___|
         |* * * * *|* *|* * * * *| | |___ | |_ | |___  \ V / | | | |  | |  | |___ 
         |* * * * *|* *|* * * * *| |_____||___||_____|  \ /  |_| |_|  |_|  |_____|
         |* * * * *|* *|* * * * *|
        _|* * * * *|___|* * * * *|_
       |*|* * * * * * * * * * * *|*|
       |*|* * * * * * * * * * * *|*|      
      _|_|________* * * *________|_|_       
     |* * * * * * | * * | * * * * * *|     
    _|* * * * * * | * * | * * * * * *|_    
   |*|* * * * * * | * * | * * * * * *|*|   
   |*|* * * * * * | * * | * * * * * *|*|   
   |*|* * * * * * | * * | * * * * * *|*|   
   |*|* * * * * * | * * | * * * * * *|*|   
 __|_|____________|_____|____________|_|__ 
|* * * * *|*|* * * * * * * * *|*|* * * * *|
|* * * * *|*|* * * * * * * * *|*|* * * * *|
|_________|_|_________________|_|_________|

Usage:
- This is a CLI based application. All user input will be collected from the command line
- Main Menu
    - Start: 
        - This option will run the elevator simulation with the current settings and allow the user to input elevator requests at the command line
        - A GUI will be generated for each elevator to track its progress
        - Depending on the settings, a pre-seed request file will be injected into the application to automatically move the elevators
    - Settings: This option allows the user to view and update the application's settings
        - Default_Floor: Integer indicating the floor the elevators will start at and return to if no there are no outstanding requests (Default 1)
	    - Number_Of_Elevators: Integer indicating the number of elevators to generate (Default 1)
	    - Stop_Floor_Wait: Integer indicating the number of seconds the elevators will wait when stopping at a floor (Default 3)
	    - Pass_Floor_Wait: Integer indicating the number of seconds the elevators will wait when passing a floor (Default 1)
	    - Same_Floor_Wait: Integer indicating the number of seconds the elevators will wait at the same floor (Default 2)
	    - Use_Pre_Seed_File: Boolean that decides whether or not to use a pre-seeded request file (Default false)
	    - Pre_Seed_File_Path: String that stores the path to a desired pre-seeded request file (Default preSeed.txt)
	    - Top_Floor: Integer indicating the highest floor allowed (Default 102 - Reflects actual floors of Empire State Building)
	    - Bottom_Floor: Integer indicationg the lowest floor allowed (Default -2 - Reflects actual floors of Empire State Building)

Design:
    - DisplayElements: Handles the display of larger UI elements (menus and splash screen)
    - Elevator:
        - Runs on it's own thread to simulate an elevator
        - Attached to individual GUI to display progress
        - Paired with a Scheduler for receiving instructions
    - RequestsRouter:
        - Entry point for user's elevator requests
        - Holds a collection of available Schedulers to send user requests
        - Routes based on number of requests a Scheduler currently has
    - Scheduler:
        - Take in and manage user requests for a single elevator
        - Direct elevator based on the elevator's current direction and outstanding requests
        - Primary data handler
        - Mutex locked major operations to avoid race conditions
    - Settings: Hanldes the application's configuration
    - CommandLineInput: Utility to streamline colletion of user input

Assumptions:
    - Elevators have a default floor
    - End floor is known before passenger is picked up

Things I'd do if I had infinite time (in no particular order):
- Better error Handling
- Logging
- Request validator class
- UI for elevator stats
- Even hotter scheduling (allowing hot scheduling if start floor is between elevator's current floor and it's next stop)
- Special cases: fire alarm, door shut button held
- User input cleaning
- Better way to close application
- More sophisticated routing algorithm
- Make waits smaller increments
- Make pre-seeded file system more feature rich for testing
- Testing
- Delayed end floor scheduling (have to wait till you pick someone up before you know where they are going)