package net.minecraft.src;

import net.minecraft.client.Minecraft;
import java.io.*;
import java.util.Calendar;

public  class ThreadWatchDog extends Thread
{
    protected final Minecraft mc;
    private  long timeStarted = System.currentTimeMillis();
    private  long timePlayed;
    private  long timeAllotted;
    private  long timePlayedToday;
    private  long milliAllowed;
    public ThreadWatchDog(Minecraft minecraft, int minutesAllowed) 
    {
	super("ThreadWatchDog");
	System.out.println("I'm in the code");
	mc = minecraft;
	setDaemon(true);

	milliAllowed = new Long(60000*minutesAllowed);
	
	Long[] numbers = getNumbers();
	//set what the last day played was

	Calendar lastDayPlayed = Calendar.getInstance();
	lastDayPlayed.setTimeInMillis(numbers[0]);

	System.out.println(lastDayPlayed.getTime().toString());

	timePlayed = numbers[1];


	//Set how much time is left
	if (isToday(lastDayPlayed))
	    {
		System.out.println("Today is today!");
		timeAllotted = milliAllowed - timePlayed;
		System.out.println("You've already played " + timePlayed + " today.");
	    }
	else
	    {
		System.out.println("Today is not today!");
		timeAllotted = milliAllowed;
	    }
	System.out.println("This session has " + timeAllotted + " milliseconds to play.");
	System.out.println("(Or " + (timeAllotted / 60000) + " minutes)");
	start();
    }

    //Will be the main loop
    public void run() 
    {
	long timePlayedThisSession;
	while (true)
	    {
		//I think this was what was messing up the times
		//It would only have the time update every 5000 milli,
		//so the time played would always be the last multiple of 5000
		//		try{Thread.sleep(5000);}
		//		catch (InterruptedException e) {}

		timePlayedThisSession = System.currentTimeMillis() - timeStarted;
		timePlayedToday = timePlayed + timePlayedThisSession;
		if (timePlayedThisSession > timeAllotted){
		    System.out.println("Time is up!");
		    mc.displayGuiScreen(new GuiTimerOut());
		    this.stop();
		}
		//		else {System.out.println("You've played " + timePlayedToday + " today so far.");}
	    }
	
    }

    public Long[] getNumbers()
    {
	Long[] numbers = {mc.gameSettings.milliOfDayLastPlayed, mc.gameSettings.milliPlayedToday};
	System.out.println("Got " + numbers[0] + " and " + numbers[1] + " from the GameSettings.");
	return numbers;

    }
    
    //Get the numbers from the file 
    //First one is the day of the last time played
    //Second one is the amount of time they played that day
    // public Long[] getNumbers() 
    // {
    // 	try {
    // 	    File file = new File("watch.tim");
    // 	    FileReader reader = new FileReader(file);
    // 	    BufferedReader buff = new BufferedReader(reader);
    // 	    if (file.createNewFile())
    // 		{
    // 		    System.out.println("Making a new Watch File");
    // 		    Long[] numbers = {new Long(System.currentTimeMillis()), 15L};
    // 		    System.out.println("Got " + numbers[0] + " and "+ numbers[1]);
    // 		    return numbers; 
   // 		}
    // 	    else
    // 		{
    // 		    Long[] numbers = {new Long(buff.readLine()), new Long(buff.readLine())};
    // 		    System.out.println("Got " + numbers[0] + " and "+ numbers[1]);		    
    // 		    return numbers;
    // 		}
    // 	}
	
    // 	catch(IOException ex)
    // 	    {
    // 		Long[] numbers = {0L,20L};
    // 		return numbers;
    // 	    }
    // 	catch(NumberFormatException ex)
    // 	    {
    // 		Long[] numbers = {0L,20L};
    // 		return numbers;		
    // 	    }

    // }
    
    
     public void setNumbers()
     {
     	mc.gameSettings.milliPlayedToday = timePlayedToday;
	System.out.println("Set timePlayedToday to:" + mc.gameSettings.milliPlayedToday);
	mc.gameSettings.milliOfDayLastPlayed = System.currentTimeMillis();
	System.out.println("Set milliOfDayLastPlayed to:" + mc.gameSettings.milliOfDayLastPlayed);
     	mc.gameSettings.saveOptions();
     }
    
    //Rewrote this class using the gamesettings class to store the file
    //Save the time played and the day played on
    //Called by the mc.shutdown method, saves the watch.tim file
    // public static void setNumbers ()
    // {
    // 	try{
    // 	    FileWriter fstream = new FileWriter("watch.tim");
    // 	    BufferedWriter out = new BufferedWriter(fstream);
    //  	    out.write(System.currentTimeMillis() + "\n" + timePlayedToday);
    // 	    out.close();
    // 	    System.out.println("Printing\n" + System.currentTimeMillis() + "\n" + timePlayedToday + "\ninto file.");
    // 	}
    // 	catch(IOException ex)
    // 	    {
    // 		System.out.println("Could not Write to file");
    // 	    }

    // }

    //Check if Calendar day is today
    public  boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());

    }

    public String getTimeLeft()
    {
	
	int milli = (int) (milliAllowed - timePlayedToday);
	int seconds = milli / 1000;
	int minutes = seconds / 60;
	seconds = seconds % 60;
	//Add if necessary
	//	int hours = minutes / 60;
	//	minutes = minutes % 60;
	return (new StringBuilder()).append(minutes).append(":").append(seconds).toString();
    }
	
    //Check if two Calendar days are the same day
    public  boolean isSameDay(Calendar cal1, Calendar cal2) {
	//Shouldn't be necessary, but I'll keep it Justin Case
	//        if (cal1 == null || cal2 == null) {       
	//            throw new IllegalArgumentException("The dates must not be null");
	//        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
}
