import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.lang.*;
class InReader {
    static BufferedReader reader;
    static StringTokenizer tokenizer;
    
    /** call this method to initialize reader for InputStream */
    static void init(InputStream input) {
        reader = new BufferedReader(
                     new InputStreamReader(input) );
        tokenizer = new StringTokenizer("");
    }

    /** get next word */
    static String next() throws IOException {
        while ( ! tokenizer.hasMoreTokens() ) {
            //TODO add check for eof if necessary
            tokenizer = new StringTokenizer(
                   reader.readLine() );
        }
        return tokenizer.nextToken();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt( next() );
    }
    
    static double nextDouble() throws IOException {
        return Double.parseDouble( next() );
    }
    
    static float nextFloat() throws IOException {
    	return Float.parseFloat( next() );
    }
}
class Passenger
{
	int age;
	String name, gender, pID;
	ArrayList<Flight> myflights;
	public Passenger()
	{
		age=0;
		name=gender=pID="";
		myflights=new ArrayList<Flight>();
	}
	
}
class PassengerDB
{
	ArrayList<Passenger> all_passengers;
	ReentrantLock lock;
	public PassengerDB()
	{
		all_passengers=new ArrayList<Passenger>();
		lock=new ReentrantLock();
	}
}
class FlightDB
{
	ArrayList<Flight> all_flights;
	ReentrantLock lock;
	public FlightDB()
	{
		all_flights=new ArrayList<Flight>();
		lock=new ReentrantLock();
	}
}
class Flight
{
	int total_seats, av_seats, reserved;
	String ID, name, source, destination, departure, arrival;
	ArrayList<String> passenger_list;
	public Flight()
	{
		ID=name=source=destination=departure=arrival="";
		total_seats=av_seats=reserved=0;
		passenger_list=new ArrayList<String>();
	}
	public void reserve(String i)
	{
		passenger_list.add(i);
		av_seats-=1;
		reserved+=1;
	}
	public void cancel(String i)
	{
		passenger_list.remove(i);
		av_seats+=1;
		reserved-=1;
	}
}
class Transaction implements Runnable
{
	PassengerDB pDB;
	FlightDB fDB;
	int tid;
	static int id=0;
	int R=0, C=0, M=0, T=0, Tr=0;
	public Transaction(PassengerDB pDB1, FlightDB fDB1)
	{
		pDB=pDB1;
		fDB=fDB1;
		tid=++id;
	}
	public void Reserve(Flight F, String i)
	{
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Transaction "+tid+" Begins");
		if (F.av_seats==0)
			System.out.println("Sorry!!! Flight "+F.ID+" "+F.name+" is full.");
		else
		{
			Passenger p=new Passenger();
			for (int j=0; j<pDB.all_passengers.size(); j++)
			{
				if (pDB.all_passengers.get(j).pID.equals(i))
				{
					p=pDB.all_passengers.get(j);
					break;
				}
			}
			F.reserve(i);
			p.myflights.add(F);
			System.out.println("Reservation successful on Flight "+F.ID+" "+F.name+" for Passenger "+p.pID+" "+p.name);
		}	
		System.out.println("Transaction "+tid+" Committed");
	}
	public void Cancel(Flight F, String i)
	{
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Transaction "+tid+" Begins");
		if (F.passenger_list.contains(i)==false)
			System.out.println("No Passenger With ID "+i+" in Flight "+F.ID);
		else
		{
			Passenger p=new Passenger();
			for (int j=0; j<pDB.all_passengers.size(); j++)
			{
				if (pDB.all_passengers.get(j).pID.equals(i))
				{
					p=pDB.all_passengers.get(j);
					break;
				}
			}
			F.cancel(i);
			p.myflights.remove(F);
			System.out.println("Cancellation successful for Flight "+F.ID+" "+F.name+" for Passenger "+p.pID+" "+p.name);
		}
		System.out.println("Transaction "+tid+" Committed");
		
	}
	public void My_Flights(String id)
	{
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Transaction "+tid+" Begins");
		Passenger p=new Passenger();
		for (int i=0; i<pDB.all_passengers.size(); i++)
		{
			if (pDB.all_passengers.get(i).pID.equals(id))
			{
				p=pDB.all_passengers.get(i);
				break;
			}
		}
		if (p==null)
			System.out.println("No Passenger With "+id);
		else if (p.myflights.size()==0)
			System.out.println("No Flights In The List for Passenger "+p.pID+" "+p.name);
		else
		{
			System.out.println("Passenger "+p.pID+" "+p.name+" Flight List: ");
			System.out.println("Flight ID "+" Flight Name "+" Source "+" Destination "+" Departure Time "+" Arrival Time "+" Total Seats");
			for (int i=0; i<p.myflights.size(); i++)
				System.out.println(p.myflights.get(i).ID+" "+p.myflights.get(i).name+" "+p.myflights.get(i).source+" "+p.myflights.get(i).destination+" "+p.myflights.get(i).departure+" "+p.myflights.get(i).arrival+" "+p.myflights.get(i).total_seats);
		}	
		System.out.println("Transaction "+tid+" Committed");
	}
	public void Total_Reservations()
	{
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Transaction "+tid+" Begins");
		int sum=0;
		for (int i=0; i<fDB.all_flights.size(); i++)
			sum+=fDB.all_flights.get(i).reserved;
		System.out.println("Sum Total Of All Reservations On All Flights : "+sum);
		System.out.println("Transaction "+tid+" Committed");
	}
	public void Transfer(Flight F1, Flight F2, String i)
	{
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Transaction "+tid+" Begins");
		if (F1.passenger_list.contains(i)==false)
			System.out.println("No Passenger With ID "+i+" in Flight "+F1.ID);
		else if (F2.av_seats==0)
			System.out.println("Sorry!!! Flight "+F2.ID+" is full.");
		else
		{
			Passenger p=new Passenger();
			for (int j=0; j<pDB.all_passengers.size(); j++)
			{
				if (pDB.all_passengers.get(j).pID.equals(i))
				{
					p=pDB.all_passengers.get(j);
					break;
				}
			}
			F1.cancel(i);
			
			F2.reserve(i);
			
			p.myflights.remove(F1);
			p.myflights.add(F2);
			
			System.out.println("Transfer Successful "+F1.passenger_list.contains(p)+" "+F2.passenger_list.contains(p));
		}
		System.out.println("Transaction "+tid+" Committed");
	}
	public void run()
	{
		boolean done=false;
		while (!done)
		{
			if (fDB.lock.tryLock() && pDB.lock.tryLock())
			{
				try
				{
					//System.out.println("Transaction "+tid+" Begins");*/
					Random rn=new Random();
					int number=rn.nextInt(5)+1;
					/*switch(number)
					{
					case 1: R++;
							break;
					case 2: C++;
							break;
					case 3: M++;
					break;
					case 4: T++;
					break;
					case 5: Tr++;
					break;
					}*/
					//System.out.println("random="+number+" "+tid);
					//System.out.println(R+" "+C+" "+M+" "+T+" "+Tr);
					Random rn1=new Random();
					int flight_no=rn1.nextInt(5);
					int passenger_no=rn1.nextInt(pDB.all_passengers.size()-1);
					//System.out.println("Flight "+fDB.all_flights.get(flight_no).ID+" Passenger "+pDB.all_passengers.get(passenger_no).pID);
					switch(number)
					{
					case 1: Reserve(fDB.all_flights.get(flight_no), pDB.all_passengers.get(passenger_no).pID);
							break;
					case 2: Cancel(fDB.all_flights.get(flight_no), pDB.all_passengers.get(passenger_no).pID);
							break;
					case 3: My_Flights(pDB.all_passengers.get(passenger_no).pID);
							break;
					case 4: Total_Reservations();
							break;
					case 5: Random rn2=new Random();
							int flight_no2=rn2.nextInt(5);
							while (flight_no==flight_no2)
								flight_no2=rn1.nextInt(5);
							Transfer(fDB.all_flights.get(flight_no), fDB.all_flights.get(flight_no2), pDB.all_passengers.get(passenger_no).pID);
							break;
					}
					//Total_Reservations();
					//System.out.println(tid+" stopped");
				}
				finally
				{
					fDB.lock.unlock();
					pDB.lock.unlock();
					//System.out.println("Transaction "+tid+" Committed");
				}
				done=true;
			}
		}
	}		
}
public class Airline {
	public static void main(String []args) throws IOException
	{
		InReader.init(System.in);
		Flight[] flight=new Flight[5];
		FlightDB fDB=new FlightDB();
		for (int i=0; i<5; i++)
			flight[i]=new Flight();
		
		flight[0].ID="6E-155";
		flight[0].name="IndiGO";
		flight[0].total_seats=200;
		flight[0].av_seats=flight[0].total_seats;
		flight[0].source="New Delhi";
		flight[0].destination="Mumbai";
		flight[0].departure="22:30";
		flight[0].arrival="00:45";
		fDB.all_flights.add(flight[0]);
		
		flight[1].ID="BA-142";
		flight[1].name="British Airways";
		flight[1].total_seats=169;
		flight[1].av_seats=flight[1].total_seats;
		flight[1].source="New Delhi";
		flight[1].destination="San Francisco";
		flight[1].departure="03:20";
		flight[1].arrival="10:45";
		fDB.all_flights.add(flight[1]);
		
		flight[2].ID="AI-765";
		flight[2].name="Air India";
		flight[2].total_seats=300;
		flight[2].av_seats=flight[2].total_seats;
		flight[2].source="Kolkata";
		flight[2].destination="Chennai";
		flight[2].departure="14:35";
		flight[2].arrival="17:05";
		fDB.all_flights.add(flight[2]);
		
		flight[3].ID="SG-517";
		flight[3].name="SpiceJet";
		flight[3].total_seats=249;
		flight[3].av_seats=flight[3].total_seats;
		flight[3].source="Pune";
		flight[3].destination="Bangalore";
		flight[3].departure="15:55";
		flight[3].arrival="17:15";
		fDB.all_flights.add(flight[3]);
		
		flight[4].ID="AF-1181";
		flight[4].name="AirFrance";
		flight[4].total_seats=265;
		flight[4].av_seats=flight[4].total_seats;
		flight[4].source="London";
		flight[4].destination="Paris";
		flight[4].departure="19:50";
		flight[4].arrival="22:00S";
		fDB.all_flights.add(flight[4]);
		
		System.out.print("Enter the no. of threads to run: ");
		int size=InReader.nextInt();
		System.out.println();
		System.out.println("Please Enter Passenger Credentials");
		System.out.println();
		String run="y";
		int count=0;
		PassengerDB newDB=new PassengerDB();
		while(run.equalsIgnoreCase("y"))
		{
			count+=1;
			System.out.println("Passenger "+count+": ");
			Passenger p=new Passenger();
			System.out.print("Enter Name: ");
			p.name=InReader.next();
			System.out.print("Enter ID (Must be of 5 digits): ");
			p.pID=InReader.next();
			System.out.print("Enter Gender(Male/Female): ");
			p.gender=InReader.next();
			System.out.print("Enter Age: ");
			p.age=InReader.nextInt();
			System.out.println();
			System.out.print("Want to continue(Y/N) ? ");
			run=InReader.next();
			System.out.println();
			newDB.all_passengers.add(p);
		}

		ExecutorService pool = Executors.newFixedThreadPool(size);
		long starttime=System.currentTimeMillis();
	    Runnable t1[] = new Runnable[size];
	    for (int i=0; i<size; i++)
	    	t1[i]=new Transaction(newDB, fDB);
	    for (int i=0; i<size; i++)
	    	pool.execute(t1[i]);
	    pool.shutdown();
	    long endtime=System.currentTimeMillis();
	    System.out.println("Time: "+(endtime-starttime));
	    
	}
}
