
import DDS.*;
import CR.*;

public class PlayerMain
{
	
	
	public static void main(String[] args) {
		// Declare Subscriber variables (bjDealer data subscribe)
		DDSEntityManager mgrSub;
		bjDealerTypeSupport typeSupportSub;
		DataReader reader;
		bjDealerDataReader typedReader;
		bjDealerSeqHolder sequence;
		SampleInfoSeqHolder sampleInfoSequence; // no idea what this does, leaving it in.
		
		// Declare Publisher variables (bjPlayer data publish)
		DDSEntityManager mgrPub;
		bjPlayerTypeSupport typeSupportPub;
		DataWriter writer;
		bjPlayerDataWriter typedWriter;
		bjPlayer pubInstance;
		
		// End PubSub declarations
		
		mgrSub = new DDSEntityManager(); // bjD type receive 
		mgrPub = new DDSEntityManager(); // bjP type send
		String partitionName = "CasinoRoyale example";

		// create Domain Participant, Type, and register Type
		mgrSub.createParticipant(partitionName);
		typeSupportSub = new bjDealerTypeSupport();
		mgrSub.registerType(typeSupportSub); // receive bjD
		mgrPub.createParticipant(partitionName);
		typeSupportPub = new bjPlayerTypeSupport();
		mgrPub.registerType(typeSupportPub); // send bjP

		// create Topic, subscriber, DataReader
		mgrSub.createTopic("Casino_RoyaleDealerPub"); // no spaces, should be same as other group's
		mgrSub.createSubscriber();
		mgrSub.createReader();
		mgrPub.createTopic("Casino_RoyalePlayerPub");
		mgrPub.createPublisher();
		mgrPub.createWriter();
		
		// set up subscriber to read the p2p connection (on localhost) and sequences to read
		reader = mgrSub.getReader();
		typedReader = bjDealerDataReaderHelper.narrow(reader);
		sequence = new bjDealerSeqHolder();
		sampleInfoSequence = new SampleInfoSeqHolder();

		// actual handling code here
		
		// Prepare a bjPlayer object for sending
		writer = mgrPub.getWriter();
		typedWriter = bjPlayerDataWriterHelper.narrow(writer);
		pubInstance = new bjPlayer(); // TODO: Replace this with a bjPlayer instance created by the Player class
		
		// Assign values
		pubInstance.seqno = 05;
		pubInstance.wager = 5;
		pubInstance.uuid = 101;
		pubInstance.action = CR.bjp_action.wagering;
		
		typedWriter.register_instance(pubInstance); // finish assigning values, now save it to the instance for sending (or something like that)
		
		
		
		
		System.out.println ("=== [Subscriber] Waiting for bjDealer data");
		
		// int terminate = 0;
		// int count = 0;
		
		boolean replyReceived = false;
		Timer.start(); // note: some operating systems use 10 ms instead of 1 ms for their java timer
		while (Timer.getTimeMs() < 5000 && !replyReceived) // run until 5000 milliseconds have passed or reply received
		{
			// put the backlog of messages into sequence
			typedReader.take(sequence, sampleInfoSequence, LENGTH_UNLIMITED.value, ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
			for (int i = 0; i < sequence.value.length; i++)
			{
				
				handleSequenceDebug(sequence.value[i]); // pass along bjDealer object
				handleSequence(sequence.value[i]); // pass along bjDealer object
				
				replyReceived = true;
				
				// System.out.println("terminate value : " + terminate);
				// terminate++;
			}
			
			// waiting causes information to be missed, if they're close enough in timing together.
			// Timer.wait(50);
			
			// ++count;
		}
		// reply that we got the message
		typedReader.return_loan(sequence, sampleInfoSequence);
		
		// clean up
		System.out.println();
		cleanUpPublisher(mgrPub, typedWriter);
		cleanUpSubscriber(mgrSub,typedReader);
	}
// End of main
	
	public static void handleSequenceDebug(bjDealer obj)
	{
		System.out.println("\n=== [Subscriber] message received :\n");
		
		// seqno will be 0 for a player closing their connection
		System.out.println("         seqno : " + obj.seqno); 
		System.out.println("          uuid : " + obj.uuid); 
		System.out.println("active_players : " + obj.active_players); 
	}
	
	// passes along the correct handling of information based on sequence number
	public static void handleSequence(bjDealer obj)
	{
		switch (obj.action.value())
		{
			case bjd_action._shuffling: // player action when dealer is shuffling
				System.out.println("Dealer " + obj.uuid + " is shuffling");
				
				
				break;
			case bjd_action._waiting: // player action when dealer is waiting
				System.out.println("Dealer " + obj.uuid + " is waiting");
				
				
				break;
			case bjd_action._dealing: // player action when dealer is dealing
				System.out.println("Dealer " + obj.uuid + " is dealing");
				
				
				break;
			case bjd_action._collecting: // player action when dealer is collecting losing wagers
				System.out.println("Dealer " + obj.uuid + " is collecting wagers");
				
				
				break;
			case bjd_action._paying: // player action when dealer is paying out winning bets
				System.out.println("Dealer " + obj.uuid + " is paying out bets");
				
				
				break;
			default:
				break;
		}
	}
	

	public static void cleanUpPublisher(DDSEntityManager mgrPub, bjPlayerDataWriter typedWriter)
	{
		mgrPub.getPublisher().delete_datawriter(typedWriter);
		mgrPub.deletePublisher();
		mgrPub.deleteTopic();
		mgrPub.deleteParticipant();
		System.out.println ("Publisher connection closed.");
	}
	public static void cleanUpSubscriber(DDSEntityManager mgrSub, bjDealerDataReader typedReader)
	{
		mgrSub.getSubscriber().delete_datareader(typedReader);
		mgrSub.deleteSubscriber();
		mgrSub.deleteTopic();
		mgrSub.deleteParticipant();
		System.out.println ("Subscriber connection closed.");
	}

}


