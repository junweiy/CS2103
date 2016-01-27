/*
 * ==============NOTE TO STUDENTS======================================
 * This class is not written in pure Object-Oriented fashion. That is 
 * because we haven't covered OO theory yet. Yes, it is possible to 
 * write non-OO code using an OO language.
 * ====================================================================
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 * This class is used to store and retrieve the distance between various locations 
 * A route is assumed to be bidirectional. i.e., a route from CityA to CityB is 
 * same as a route from CityB to CityA. Furthermore, there can be no more than 
 * one route between two locations. Deleting a route is not supported at this point.
 * The storage limit for this version is 10 routes.
 * In the case more than multiple routes between the same two locations were entered,
 * we store only the latest one. The command format is given by the example interaction below:

 Welcome to SimpleRouteStore!
 Enter command:addroute Clementi BuonaVista 12
 Route from Clementi to BuonaVista with distance 12km added
 Enter command:getdistance Clementi BuonaVista
 Distance from Clementi to BuonaVista is 12
 Enter command:getdistance clementi buonavista
 Distance from clementi to buonavista is 12
 Enter command:getdistance Clementi JurongWest
 No route exists from Clementi to JurongWest!
 Enter command:addroute Clementi JurongWest 24
 Route from Clementi to JurongWest with distance 24km added
 Enter command:getdistance Clementi JurongWest
 Distance from Clementi to JurongWest is 24
 Enter command:exit

 * @author Dave Jun
 */

//I WOULD USE A HASHMAP INSTEAD OF A 2D ARRAY

public class CityConnect {

	/*
	 * ==============NOTE TO STUDENTS======================================
	 * These messages shown to the user are defined in one place for convenient
	 * editing and proof reading. Such messages are considered part of the UI
	 * and may be subjected to review by UI experts or technical writers. Note
	 * that Some of the strings below include '%1$s' etc to mark the locations
	 * at which java String.format(...) method can insert values.
	 * ====================================================================
	 */
	private static final String MESSAGE_DISTANCE = "Distance from %1$s to %2$s is %3$s";
	private static final String MESSAGE_NO_ROUTE = "No route exists from %1$s to %2$s!";
	private static final String MESSAGE_ADDED = "Route from %1$s to %2$s with distance %3$skm added";
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format :%1$s";
	private static final String WELCOME_MESSAGE = "Welcome to SimpleRouteStore!";

	// These are the possible command types
	enum COMMAND_TYPE {
		ADD_ROUTE, GET_DISTANCE, INVALID, EXIT
	};
	
	// This is used to indicate the route was not found in the database
	private static final int NOT_FOUND = -2;

	// These are the correct number of parameters for each command
	private static final int PARAM_SIZE_FOR_ADD_ROUTE = 3;
	private static final int PARAM_SIZE_FOR_GET_DISTANCE = 2;

	// These are the locations at which various parameters will appear in a command
	private static final int PARAM_POSITION_START_LOCATION = 0;
	private static final int PARAM_POSITION_END_LOCATION = 1;
	private static final int PARAM_POSITION_DISTANCE = 2;

	private static Map<String, Integer> routes = new HashMap<String, Integer>();
	// This array will be used to store the routes

	/*
	 * These are the locations at which various components of the route will be
	 * stored in the routes[][] array.
	 */
	/*
	 * This variable is declared for the whole class (instead of declaring it
	 * inside the readUserCommand() method to facilitate automated testing using
	 * the I/O redirection technique. If not, only the first line of the input
	 * text file will be processed.
	 */
	private static Scanner scanner = new Scanner(System.in);

	/*
	 * ==============NOTE TO STUDENTS======================================
	 * Notice how this method solves the whole problem at a very high level. We
	 * can understand the high-level logic of the program by reading this method
	 * alone.
	 * ====================================================================
	 */
	public static void main(String[] args) {
		System.out.println(WELCOME_MESSAGE);
        
		while (true) {
			System.out.print("Enter command: ");
			String command = scanner.nextLine();
			String userCommand = command;
			String feedback = executeCommand(userCommand);
			System.out.println(feedback);
		}
	}

	/*
	 * ==============NOTE TO STUDENTS==========================================
	 * If the reader wants a deeper understanding of the solution, he/she can 
	 * go to the next level of abstraction by reading the methods (given below)
	 * that is referenced by the method above.
	 * ====================================================================
	 */

	public static String executeCommand(String userCommand) {
		if (userCommand.trim().equals(""))
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);

		String commandTypeString = getFirstWord(userCommand);
		COMMAND_TYPE commandType = determineCommandType(commandTypeString);

		switch (commandType) {
        case ADD_ROUTE:
			return addRoute(userCommand);
		case GET_DISTANCE:
			return getDistance(userCommand);
		case INVALID:
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		case EXIT:
			System.exit(0);
		default:
			//throw an error if the command is not recognized
			throw new Error("Unrecognized command type");
		}
		/*
		 * ==============NOTE TO STUDENTS======================================
		 * If the rest of the program is correct, this error will never be thrown.
		 * That is why we use an Error instead of an Exception.
		 * ====================================================================
		 */
	}

	/*
	 * ==============NOTE TO STUDENTS======================================
	 * After reading the above code, the reader should have a reasonable
	 * understanding of how the program works. If the reader wants to go EVEN
	 * more deep into the solution, he/she can read the methods given below that
	 * solves various sub-problems at lower levels of abstraction.
	 * ====================================================================
	 */

	/**
	 * This operation determines which of the supported command types the user
	 * wants to perform
	 * 
	 * @param commandTypeString
	 *            is the first word of the user command
	 */
	private static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");

		if (commandTypeString.equalsIgnoreCase("addroute")) {
			return COMMAND_TYPE.ADD_ROUTE;
		} else if (commandTypeString.equalsIgnoreCase("getdistance")) {
			return COMMAND_TYPE.GET_DISTANCE;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
		 	return COMMAND_TYPE.EXIT;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}

	/**
	 * This operation is used to find the distance between two locations
	 * 
	 * @param userCommand
	 *            is the full string user has entered as the command
	 * @return the distance
	 */
	private static String getDistance(String userCommand) {

		String[] parameters = splitParameters(removeFirstWord(userCommand));

        //Unclear what param size for get distance is trying to accomplish -- need comment for this
		if (parameters.length < PARAM_SIZE_FOR_GET_DISTANCE) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}

		String newStartLocation = parameters[PARAM_POSITION_START_LOCATION];
		String newEndLocation = parameters[PARAM_POSITION_END_LOCATION];

		int position = getPositionOfExistingRoute(newStartLocation, newEndLocation);

		if (position == NOT_FOUND) {
			return String.format(MESSAGE_NO_ROUTE, newStartLocation,
					newEndLocation);
		} 
		else 
		{
			return String.format(MESSAGE_DISTANCE, newStartLocation, newEndLocation,
					routes.get(newStartLocation.toLowerCase() + " " + newEndLocation.toLowerCase()));
		}

	}

	/**
	 * @return Returns the position of the route represented by 
	 *    newStartLocation and newEndLocation. Returns NOT_FOUND if not found.
	 */
    
    //Use a hash map instead
	private static int  getPositionOfExistingRoute(String newStartLocation,
			String newEndLocation) {
		if (routes.get(newStartLocation.toLowerCase() + " " + newEndLocation.toLowerCase()) == null) {
			return NOT_FOUND;
		} else {
			return routes.get(newStartLocation.toLowerCase() + " " + newEndLocation.toLowerCase());
		}
	}

	/**
	 * This operation adds a route to the storage. If the route already exists,
	 * it will be overwritten.
	 * 
	 * @param userCommand
	 *            (although we receive the full user command, we assume without
	 *            checking the first word to be 'addroute')
	 * @return status of the operation
	 */
	private static String addRoute(String userCommand) {
		
		String[] parameters = splitParameters(removeFirstWord(userCommand));
		
		if (parameters.length < PARAM_SIZE_FOR_ADD_ROUTE){
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}

		String newStartLocation = parameters[PARAM_POSITION_START_LOCATION];
		String newEndLocation = parameters[PARAM_POSITION_END_LOCATION];
		String distance = parameters[PARAM_POSITION_DISTANCE];

		if (!isPositiveNonZeroInt(distance)){
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}

		routes.put(newStartLocation.toLowerCase() + " " + newEndLocation.toLowerCase(), Integer.valueOf(distance));
		routes.put(newEndLocation.toLowerCase() + " " + newStartLocation.toLowerCase(), Integer.valueOf(distance));

		return String.format(MESSAGE_ADDED, newStartLocation, newEndLocation,
				distance);
	}

	private static boolean isPositiveNonZeroInt(String s) {
		try {
			int i = Integer.parseInt(s);
			//return true if i is greater than 0
			return (i > 0 ? true : false);
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private static String removeFirstWord(String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}

	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	private static String[] splitParameters(String commandParametersString) {
		String[] parameters = commandParametersString.trim().split("\\s+");
		return parameters;
	}
}