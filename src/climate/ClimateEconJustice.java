package climate;

import java.util.ArrayList;



public class ClimateEconJustice {

    private StateNode firstState;
    
    /*
    * Constructor
    * 
    * **** DO NOT EDIT *****
    */
    public ClimateEconJustice() {
        firstState = null;
    }

    /*
    * Get method to retrieve instance variable firstState
    * 
    * @return firstState
    * 
    * **** DO NOT EDIT *****
    */ 
    public StateNode getFirstState () {
        // DO NOT EDIT THIS CODE
        return firstState;
    }

    /**
     * Creates 3-layered linked structure consisting of state, county, 
     * and community objects by reading in CSV file provided.
     * 
     * @param inputFile, the file read from the Driver to be used for
     * @return void
     * 
     * **** DO NOT EDIT *****
     */
    public void createLinkedStructure ( String inputFile ) {
        
        // DO NOT EDIT THIS CODE
        StdIn.setFile(inputFile);
        
        StdIn.readLine();
    
        
        // Reads the file one line at a time
        while ( StdIn.hasNextLine() ) {
            // Reads a single line from input file
            String line = StdIn.readLine();
            
            
          
            
          
            addToStateLevel(line);
            addToCountyLevel(line);
            addToCommunityLevel(line);
          
        }
    }

    /*
    * Adds a state to the first level of the linked structure.
    * Do nothing if the state is already present in the structure.
    * 
    * @param inputLine a line from the input file
    */

    
    public void addToStateLevel(String inputLine) {
        String[] a = inputLine.split(",");
        String stateName = a[2].trim();

        StateNode currentState = firstState;
        boolean isStateExists = false;

        if (firstState == null) {
            StateNode newState = new StateNode();
            newState.setName(stateName);
            firstState = newState;
            return; 
        }

        while (currentState != null) {
            if (currentState.getName().equals(stateName)) {
            	isStateExists= true;
                break;
            }
            if (currentState.getNext() == null) {
                break; 
            }
            currentState = currentState.getNext();
        }


        if (!isStateExists) {
            StateNode newState = new StateNode();
            newState.setName(stateName);


            currentState.setNext(newState);
        }
    }





    /*
    * Adds a county to a state's list of counties.
    * 
    * Access the state's list of counties' using the down pointer from the State class.
    * Do nothing if the county is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCountyLevel(String inputLine) {
        String[] a = inputLine.split(",");
        String stateName = a[2].trim();
        String countyName = a[1].trim();

        // Create a new county node
        CountyNode countyNode = new CountyNode();
        countyNode.setName(countyName);

        // Find the state node
        StateNode currentState = firstState;
        while (currentState != null) {
            if (currentState.getName().equals(stateName)) {
                // Check if the county already exists in the county list
                CountyNode currentCounty = currentState.getDown();
                CountyNode previousCounty = null;
                boolean countyExists = false;
                while (currentCounty != null) {
                    if (currentCounty.getName().equals(countyName)) {
                        countyExists = true;
                        break;
                    }
                    previousCounty = currentCounty;
                    currentCounty = currentCounty.getNext();
                }

                // Add the county if it does not already exist
                if (!countyExists) {
                    if (previousCounty == null) {
                        currentState.setDown(countyNode);
                    } else {
                        previousCounty.setNext(countyNode);
                    }
                }

                break; // Exit the loop once the state is found
            }
            currentState = currentState.getNext();
        }
    }

    public void addToCommunityLevel(String inputLine) {
        String[] a = inputLine.split(",");
        String communityName = a[0];
        String stateName = a[2].trim();
        String countyName = a[1].trim();

        // Create a new community node
        CommunityNode newCommunityNode = new CommunityNode();
        newCommunityNode.setName(communityName);

        // Create a new data object
        Data d = new Data(
                Double.parseDouble(a[3]),
                Double.parseDouble(a[4]),
                Double.parseDouble(a[5]),
                Double.parseDouble(a[8]),
                Double.parseDouble(a[9]),
                a[19],
                Double.parseDouble(a[49]),
                Double.parseDouble(a[37]),
                Double.parseDouble(a[121])
        );
        newCommunityNode.setInfo(d);

        StateNode currentState = firstState;
        while (currentState != null) {
            if (currentState.getName().equals(stateName)) {
                CountyNode currentCounty = currentState.getDown();
                while (currentCounty != null) {
                    if (currentCounty.getName().equals(countyName)) {
                        if (currentCounty.getDown() == null) {
                            currentCounty.setDown(newCommunityNode);
                        } else {
                            CommunityNode lastCommunity = currentCounty.getDown();
                            while (lastCommunity.getNext() != null) {
                                lastCommunity = lastCommunity.getNext();
                            }
                            lastCommunity.setNext(newCommunityNode);
                        }
                        break; // Exit the loop once the county is found
                    }
                    currentCounty = currentCounty.getNext();
                }
                break; // Exit the loop once the state is found
            }
            currentState = currentState.getNext();
        }
    }










    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int disadvantagedCommunities(double userPercentage, String race) {
        int count = 0;
        double item = 0.0;
        StateNode currentState = firstState;
        while (currentState != null) {
            CountyNode currentCounty = currentState.getDown();
            while (currentCounty != null) {
                CommunityNode currentCommunity = currentCounty.getDown();
                while (currentCommunity != null) {
                    double totalPercentage =Math.abs( currentCommunity.getInfo().getPrcntAfricanAmerican() +
                            currentCommunity.getInfo().getPrcntNative() +
                            currentCommunity.getInfo().getPrcntAsian() +
                            currentCommunity.getInfo().getPrcntWhite() +
                            currentCommunity.getInfo().getPrcntHispanic());

                    
                    if (race.equals("African American")) {
                        item =Math.round(Math.abs( (currentCommunity.getInfo().getPrcntAfricanAmerican() / totalPercentage)*100));
//                    
                    } else if (race.equals("Native American")) {
                        item = Math.round(Math.abs((currentCommunity.getInfo().getPrcntNative() / totalPercentage)*100));
                    } else if (race.equals("Asian American")) {
                        item = Math.round(Math.abs((currentCommunity.getInfo().getPrcntAsian() / totalPercentage)*100));
                    } else if (race.equals("White American")) {
                        item = Math.round(Math.abs( (currentCommunity.getInfo().getPrcntWhite() / totalPercentage)*100));
                    } else if (race.equals("Hispanic American")) {
                        item = Math.round( Math.abs((currentCommunity.getInfo().getPrcntHispanic() / totalPercentage)*100));
                    }

                    if (currentCommunity.getInfo().getAdvantageStatus().toUpperCase().equals("TRUE") && (item>=userPercentage)) {
                        count++;
                    }
                    currentCommunity = currentCommunity.getNext();
                }
                currentCounty = currentCounty.getNext();
            }
            currentState = currentState.getNext();
        }

        return count;
    }




    /*
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as non disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int nonDisadvantagedCommunities(double userPercentage, String race) {
        

    	
    	
    	int count = 0;
        double item = 0.0;
        StateNode currentState = firstState;
        while (currentState != null) {
            CountyNode currentCounty = currentState.getDown();
            while (currentCounty != null) {
                CommunityNode currentCommunity = currentCounty.getDown();
                while (currentCommunity != null) {
                    double totalPercentage =Math.abs( currentCommunity.getInfo().getPrcntAfricanAmerican() +
                            currentCommunity.getInfo().getPrcntNative() +
                            currentCommunity.getInfo().getPrcntAsian() +
                            currentCommunity.getInfo().getPrcntWhite() +
                            currentCommunity.getInfo().getPrcntHispanic());

                    
                    if (race.equals("African American")) {
                        item =Math.round(Math.abs( (currentCommunity.getInfo().getPrcntAfricanAmerican() / totalPercentage)*100));
//                    
                    } else if (race.equals("Native American")) {
                        item = Math.round(Math.abs((currentCommunity.getInfo().getPrcntNative() / totalPercentage)*100));
                    } else if (race.equals("Asian American")) {
                        item = Math.round(Math.abs((currentCommunity.getInfo().getPrcntAsian() / totalPercentage)*100));
                    } else if (race.equals("White American")) {
                        item = Math.round( ((currentCommunity.getInfo().getPrcntWhite() / totalPercentage)*100));
                    } else if (race.equals("Hispanic American")) {
                        item = Math.round( Math.abs((currentCommunity.getInfo().getPrcntHispanic() / totalPercentage)*100));
                    }

                    if (currentCommunity.getInfo().getAdvantageStatus().toUpperCase().equals("FALSE") && (item>=userPercentage)) {
                        count++;
                    }
                    currentCommunity = currentCommunity.getNext();
                }
                currentCounty = currentCounty.getNext();
            }
            currentState = currentState.getNext();
        }

        return count;
    }

    
    /** 
     * Returns a list of states that have a PM (particulate matter) level
     * equal to or higher than value inputted by user.
     * 
     * @param PMlevel the level of particulate matter
     * @return the States which have or exceed that level
     */ 
    

    public ArrayList<StateNode> statesPMLevels(double PMlevel) {
        ArrayList<StateNode> statesWithHighPM = new ArrayList<>();

        
        StateNode currentState = firstState;
        while (currentState != null) {
            CountyNode currentCounty = currentState.getDown();
            while (currentCounty != null) {
                CommunityNode currentCommunity = currentCounty.getDown();
                while (currentCommunity != null) {
                   
                    if (currentCommunity.getInfo().getPMlevel() >= PMlevel) {
                       
                        if (!statesWithHighPM.contains(currentState)) {
                            statesWithHighPM.add(currentState);
                        }
                     
                        break;
                    }
                    currentCommunity = currentCommunity.getNext();
                }
                currentCounty = currentCounty.getNext();
            }
            currentState = currentState.getNext();
        }

        return statesWithHighPM;
    }


    /**
     * Given a percentage inputted by user, returns the number of communities 
     * that have a chance equal to or higher than said percentage of
     * experiencing a flood in the next 30 years.
     * 
     * @param userPercntage the percentage of interest/comparison
     * @return the amount of communities at risk of flooding
     */
    public int chanceOfFlood(double userPercentage) {
        
        int count = 0;

       
        StateNode currentState = firstState;
        while (currentState != null) {
            CountyNode currentCounty = currentState.getDown();
            while (currentCounty != null) {
                CommunityNode currentCommunity = currentCounty.getDown();
                while (currentCommunity != null) {
                   
                    if (currentCommunity.getInfo().getChanceOfFlood() >= userPercentage) {
                        
                        count++;
                    }
                    currentCommunity = currentCommunity.getNext();
                }
                currentCounty = currentCounty.getNext();
            }
            currentState = currentState.getNext();
        }

        return count;
    }


    /** 
     * Given a state inputted by user, returns the communities with 
     * the 10 lowest incomes within said state.
     * 
     *  @param stateName the State to be analyzed
     *  @return the top 10 lowest income communities in the State, with no particular order
    */
    public ArrayList<CommunityNode> lowestIncomeCommunities(String stateName) {
        ArrayList<CommunityNode> lowestIncomeCommunities = new ArrayList<>();

        
        StateNode currentState = firstState;
        while (currentState != null && !currentState.getName().equals(stateName)) {
            currentState = currentState.getNext();
        }

        if (currentState == null) {
          
            return null;
        }

        
        CountyNode currentCounty = currentState.getDown();
        while (currentCounty != null) {
            CommunityNode currentCommunity = currentCounty.getDown();
            while (currentCommunity != null) {
                
                if (lowestIncomeCommunities.size() < 10) {
                    lowestIncomeCommunities.add(currentCommunity);
                } else {
                   
                    double lowestPovertyLine = Double.MAX_VALUE;
                    int lowestIndex = -1;
                    for (int i = 0; i < lowestIncomeCommunities.size(); i++) {
                        double povertyLine = lowestIncomeCommunities.get(i).getInfo().getPercentPovertyLine();
                        if (povertyLine < lowestPovertyLine) {
                            lowestPovertyLine = povertyLine;
                            lowestIndex = i;
                        }
                    }

                  
                    if (currentCommunity.getInfo().getPercentPovertyLine() > lowestPovertyLine) {
                        lowestIncomeCommunities.set(lowestIndex, currentCommunity);
                    }
                }
                currentCommunity = currentCommunity.getNext();
            }
            currentCounty = currentCounty.getNext();
        }

        return lowestIncomeCommunities;
    }

}
    
