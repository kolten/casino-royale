
import DDS.*;
import CR.*;
import java.util.*; // components used: Arraylist

public class DealerMain implements MAX_PLAYERS
{
	public static final int value = (int)(6);
	public static int seqno = 0; // Increment by 1 before sending a pub
	
	public static void main(String[] args) {
		// Declare Publisher variables (bjDealer data publish)
		DDSEntityManager mgrPub;
		bjDealerTypeSupport typeSupportPub;
		DataWriter writer;
		bjDealerDataWriter typedWriter;
		bjDealer pubInstance;
		
		// Declare Subscriber variables (bjPlayer data subscribe)
		DDSEntityManager mgrSub;
		bjPlayerTypeSupport typeSupportSub;
		DataReader reader;
		bjPlayerDataReader typedReader;
		bjPlayerSeqHolder sequence;
		SampleInfoSeqHolder sampleInfoSequence; // no idea what this does, leaving it in.
		
		// End PubSub declarations
		
		// Declare local game objects (server-side)
		ArrayList<Player> playerList = new ArrayList<Player>();
		Dealer dealer = new Dealer(); // TODO add constructor params
		
		// Instantiate  helper objects to maintain player information
		mgrPub = new DDSEntityManager(); // bjD type send
		mgrSub = new DDSEntityManager(); // bjP type receive
		String partitionName = "CasinoRoyale example";

		// create Domain Participant, Type
		mgrPub.createParticipant(partitionName);
		typeSupportPub = new bjDealerTypeSupport();
		mgrPub.registerType(typeSupportPub); // send bjD
		mgrSub.createParticipant(partitionName);
		typeSupportSub = new bjPlayerTypeSupport();
		mgrSub.registerType(typeSupportSub); // receive bjP

		// create Publisher, DataWriter, Topic
		mgrPub.createTopic("Casino_RoyaleDealerPub"); // no spaces, should be same as other group's
		mgrPub.createPublisher();
		mgrPub.createWriter();
		mgrSub.createTopic("Casino_RoyalePlayerPub");
		mgrSub.createSubscriber();
		mgrSub.createReader();
		
		// set up subscriber to read the p2p connection (on localhost) and sequences to receive a bjP
		reader = mgrSub.getReader();
		typedReader = bjPlayerDataReaderHelper.narrow(reader);
		sequence = new bjPlayerSeqHolder();
		sampleInfoSequence = new SampleInfoSeqHolder();
		
		// Prepare a bjDealer object for sending
		writer = mgrPub.getWriter();
		typedWriter = bjDealerDataWriterHelper.narrow(writer);
		pubInstance = new bjDealer(); // TODO: Replace this with a bjDealer instance created by the Dealer class
		
		// Assign values
		pubInstance.seqno = 05;
		pubInstance.active_players = 6;
		pubInstance.uuid = 42;
		pubInstance.action = CR.bjd_action.shuffling;
		
		typedWriter.register_instance(pubInstance); // finish assigning values, now save it to the instance for sending (or something like that)
		
		
		
		
		System.out.println ("=== [Publisher] Sending bjDealer data");
		
		/*
		bjD action
		shuffling (none)
		waiting
		dealing
		collecting
		paying
		*/
		
		
		
		
		
		// Do the actual publishing
		// run 10 times for testing
		// for(int i = 0; i<10; i++)
		
		// run once
		{
			Timer.wait(50);
			int status = typedWriter.write(pubInstance, HANDLE_NIL.value); // send the message instance
			ErrorHandler.checkStatus(status, "bjDealerDataWriter.write"); // make sure it got sent
			System.out.println("  status value: " + status);
			pubInstance.seqno++;
		}
		Timer.wait(1000);
		
		// clean up
		System.out.println();
		cleanUpPublisher(mgrPub, typedWriter);
		cleanUpSubscriber(mgrSub,typedReader);
	}
// end of main
	
	
	
	
	
	
	
	
	
	
	
	// shuffle the dealer's shoe and return a bjDealer object with the action and its information
	public static bjDealer dealerShuffle(Dealer dealer)
	{
		return null;
	}
	
	//print out message information from a bjPlayer object sent by a player
	public static void handleSequenceDebug(bjPlayer obj)
	{
		System.out.println("\n=== [Subscriber] message received :\n");
		
		// seqno will be 0 for a player closing their connection
		System.out.println("         seqno : " + obj.seqno); 
		System.out.println("          uuid : " + obj.uuid);  
	}
	
	// passes along the correct handling of information based on sequence number
	public static void handleSequence(bjPlayer obj)
	{
		switch (obj.action.value())
		{
			case bjp_action._none: // dealer action when player is doing nothing
				System.out.println("Player " + obj.uuid + " says none");
				
				
				break;
			case bjp_action._joining: // dealer action when player is joining
				System.out.println("Player " + obj.uuid + " is joining");
				
				
				break;
			case bjp_action._exiting: // dealer action when player is exiting
				System.out.println("Player " + obj.uuid + " is exiting");
				
				
				break;
			case bjp_action._wagering: // dealer action when player is wagering
				System.out.println("Player " + obj.uuid + " is wagering");
				
				
				break;
			case bjp_action._requesting_a_card: // dealer action when player is requesting a card
				System.out.println("Player " + obj.uuid + " is requesting a card");
				
				
				break;
			default:
				break;
		}
	}
	
	
	public static void cleanUpPublisher(DDSEntityManager mgrPub, bjDealerDataWriter typedWriter)
	{
		mgrPub.getPublisher().delete_datawriter(typedWriter);
		mgrPub.deletePublisher();
		mgrPub.deleteTopic();
		mgrPub.deleteParticipant();
		System.out.println ("Publisher connection closed.");
	}
	public static void cleanUpSubscriber(DDSEntityManager mgrSub, bjPlayerDataReader typedReader)
	{
		mgrSub.getSubscriber().delete_datareader(typedReader);
		mgrSub.deleteSubscriber();
		mgrSub.deleteTopic();
		mgrSub.deleteParticipant();
		System.out.println ("Subscriber connection closed.");
	}
	
	
}



