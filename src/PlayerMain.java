
import DDS.*;
import CR.*;

public class PlayerMain
{
	// warning: poor programming standards within
	// (class variables with void functions that modify class variables)
	// Subscriber class variables (bjDealer data subscribe)
	public static DDSEntityManager mgrSub;
	public static bjDealerTypeSupport typeSupportSub;
	public static DataReader reader;
	public static bjDealerDataReader typedReader;
	public static bjDealerSeqHolder sequence;
	public static SampleInfoSeqHolder sampleInfoSequence; // no idea what this does, leaving it in.
	
	// Publisher class variables (bjPlayer data publish)
	
	public static void main(String[] args) {
		mgrSub = new DDSEntityManager();
		String partitionName = "CasinoRoyale example";

		// create Domain Participant, Type, and register Type
		mgrSub.createParticipant(partitionName);
		typeSupportSub = new bjDealerTypeSupport();
		mgrSub.registerType(typeSupportSub);

		// create Topic, subscriber, DataReader
		mgrSub.createTopic("Casino_Royale"); // no spaces, should be same as other group's
		mgrSub.createSubscriber();
		mgrSub.createReader();

		// set up subscriber to read the p2p connection (on localhost) and sequences to read
		reader = mgrSub.getReader();
		typedReader = bjDealerDataReaderHelper.narrow(reader);
		sequence = new bjDealerSeqHolder();
		sampleInfoSequence = new SampleInfoSeqHolder();

		System.out.println ("=== [Subscriber] Waiting for bjDealer data");
		
		int terminate = 0;
		int count = 0;
		
		while (terminate < 20) // run until 20 messages received
		{
			// put the backlog of messages into sequence
			typedReader.take(sequence, sampleInfoSequence, LENGTH_UNLIMITED.value, ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
			for (int i = 0; i < sequence.value.length; i++)
			{
				handleSequence(sequence.value[i]);
				// System.out.println("=== [Subscriber] message received :\n");
				// System.out.println("        seqno : " + sequence.value[i].seqno); // this will be 0 for a player closing their connection
				System.out.println("terminate value : " + terminate);
				// System.out.println();
				terminate++;
			}
			
			// waiting causes information to be missed, if they're close enough in timing together.
			// Timer.wait(50);
			
			++count;
		}
		// reply that we got the message
		typedReader.return_loan(sequence, sampleInfoSequence);
		
		// clean up
		cleanUpSubscriber();
	}

	// passes along the correct handling of information based on sequence number
	public static void handleSequence(bjDealer obj)
	{
		switch (obj.seqno)
		{
			case 1:		
						break;
			case 2:		
						break;
			case 3:		
						break;
			case 4:		
						break;
			case 5:		
						break;
			default:	
						break;
		}
	}

	public static void cleanUpSubscriber()
	{
		mgrSub.getSubscriber().delete_datareader(typedReader);
		mgrSub.deleteSubscriber();
		mgrSub.deleteTopic();
		mgrSub.deleteParticipant();
	}

}


