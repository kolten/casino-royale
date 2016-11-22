
import DDS.*; // opensplice stuff
import CR.*; // idl stuff
import java.util.*; // components used: Arraylist

public class DealerMain implements MAX_PLAYERS
{
	public static final int value = (int)(6); // MAX_Players
	public static int seqno = 1; // Increments by 1 after sending a pub
	
	// Declare local game objects (server-side)
	public static ArrayList<Player> playerList = new ArrayList<Player>();
	public static Dealer dealer;
	
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
		
		dealer = new Dealer(); // TODO add constructor params
		
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
		
		// Prepare the publisher
		writer = mgrPub.getWriter();
		typedWriter = bjDealerDataWriterHelper.narrow(writer);
		
		// Prepare an object for sending
		pubInstance = new bjDealer(); // TODO: Replace this with a bjDealer instance created by the Dealer class
		pubInstance.uuid = 1;
		
		
		System.out.println("Running DealerMain");
		
		
		boolean runFlag = true; // loop end condition
		int gameCount = 1; // run a game this many times
		int replyCount = 0; // this many players have replied to our pub
		int repliesExpected = 1; // assume only one player for now
		// repliesExpected = playerList.size(); // number of players ingame
		while(runFlag)
		{
			//test
			//publishInstance(typedWriter,pubInstance); // note: 100 ms wait before sending
			
			//shuffling (handle join)
			Timer.wait(1000);
			pubInstance.action = CR.bjd_action.shuffling;
			publishInstance(typedWriter,pubInstance); // publish
			System.out.println("\n[Dealer] Shuffling action published; accepting joins\n");
			
			//(handlings joins)
			Timer.start(); // allow 5 seconds for joins
			while(Timer.getTimeMs()<5000)
			{
				bjPlayer obj = readSequence(typedReader, sequence, sampleInfoSequence);
				handleSequence(obj);
				handleSequenceDebug(obj);
				typedReader.return_loan(sequence, sampleInfoSequence); // free memory
			}
			
			//waiting (handle wager receive, echo if received)
			Timer.wait(1000);
			pubInstance.action = CR.bjd_action.waiting;
			publishInstance(typedWriter,pubInstance); // publish
			System.out.println("\n[Dealer] waiting action published; accepting wagers\n");
			
			//(echo wagers)
			while(replyCount<repliesExpected)
			{
				bjPlayer obj = readSequence(typedReader, sequence, sampleInfoSequence);
				handleSequence(obj);
				handleSequenceDebug(obj);
				typedReader.return_loan(sequence, sampleInfoSequence); // free memory
				
				publishInstance(typedWriter,pubInstance); // publish
				System.out.println("\n[Dealer] player X has wagered Y credits\n");
				
				replyCount++;
			}
			
			//dealing (initial deal)
			Timer.wait(1000);
			pubInstance.action = CR.bjd_action.dealing;
			publishInstance(typedWriter,pubInstance); // publish
			System.out.println("\n[Dealer] dealing action published; cards sent\n");
			
			
			//(ask for hit/stand, then deal. Repeat.)
			while(replyCount<repliesExpected)
			{
				Timer.wait(1000);
				publishInstance(typedWriter,pubInstance); // publish
				System.out.println("\n[Dealer] asking player X for hit/stand\n");
				
				bjPlayer obj = readSequence(typedReader, sequence, sampleInfoSequence);
				handleSequence(obj);
				handleSequenceDebug(obj);
				typedReader.return_loan(sequence, sampleInfoSequence); // free memory
				
				if(obj.action.value() == bjp_action._none)
				{
					replyCount++;
					System.out.println("\n[Dealer] Player X has stood\n");
				}
				else
				{
					System.out.println("\n[Dealer] Player X has hit\n");
					publishInstance(typedWriter,pubInstance); // publish
				}
			}
			
			replyCount=0;
			//collecting (look at who won or lost. Take money from losers)
			Timer.wait(1000);
			pubInstance.action = CR.bjd_action.collecting;
			publishInstance(typedWriter,pubInstance); // publish
			System.out.println("\n[Dealer] collecting losses\n");
			
			
			//paying (give money to winners)
			Timer.wait(1000);
			pubInstance.action = CR.bjd_action.paying;
			publishInstance(typedWriter,pubInstance); // publish
			System.out.println("\n[Dealer] paying out winning bets\n");
			
			
			
			
			//end game
			
			
			if(--gameCount <= 0) // decrement first
				runFlag = false;
		}
		Timer.wait(1000);
		
		// clean up
		System.out.println();
		cleanUpPublisher(mgrPub, typedWriter);
		cleanUpSubscriber(mgrSub,typedReader);
	}
// end of main
	
	// take the data writer and the bjDealer, send the message to the publisher
	public static void publishInstance(bjDealerDataWriter typedWriter, bjDealer pubInstance)
	{
		pubInstance.seqno = DealerMain.seqno;
		typedWriter.register_instance(pubInstance); // pass assignments into publisher
		Timer.wait(100);
		int status = typedWriter.write(pubInstance, HANDLE_NIL.value); // send the message instance
		ErrorHandler.checkStatus(status, "bjDealerDataWriter.write"); // make sure it got sent?
		//System.out.println("  status value: " + status);
		DealerMain.seqno++;
	}
	
	// magic happens here
	
	public static bjPlayer readSequence(bjPlayerDataReader typedReader, bjPlayerSeqHolder sequence, SampleInfoSeqHolder sampleInfoSequence)
	{
		bjPlayer obj;
		Timer.start(); // note: some operating systems use 10 ms instead of 1 ms for their java timer
		while (Timer.getTimeMs() < 10000 ) // run until 10 seconds have passed or reply received
		{
			typedReader.take(sequence, sampleInfoSequence, LENGTH_UNLIMITED.value, ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
			for (int i = 0; i < sequence.value.length; i++)
			{
				obj = sequence.value[i];
				return obj; // pass along bjPlayer object
			}
		}
		obj = new bjPlayer();
		obj.seqno = -1;
		return obj; // return an empty message
	}
	
	
	public static void handleSequenceDebug(bjPlayer obj)
	{
		System.out.println("\n=== [Dealer] Message received from player :\n");
		
		// seqno will be 0 for a player closing their connection
		System.out.println("          uuid : " + obj.uuid);
		System.out.println("         seqno : " + obj.seqno);
		System.out.println("       credits : " + obj.credits);
		System.out.println("        action : " + obj.action.value()); 
		System.out.println("         wager : " + obj.wager); 
		System.out.println("     dealer_id : " + obj.dealer_id); 
	}
	
	
	// passes along the correct handling of information based on sequence number
	public static void handleSequence(bjPlayer obj)
	{
		if(obj.seqno==0)
		{
			// TODO: Closed connection
			System.out.println("Player " + obj.uuid + " has closed their connection");
		}
		
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
	
	/*
	//print out message information from a bjPlayer object sent by a player
	public static void handleSequenceDebug(bjPlayer obj)
	{
		System.out.println("\n=== [Subscriber] message received :\n");
		
		// seqno will be 0 for a player closing their connection
		System.out.println("         seqno : " + obj.seqno); 
		System.out.println("          uuid : " + obj.uuid);  
	}
	*/
}



