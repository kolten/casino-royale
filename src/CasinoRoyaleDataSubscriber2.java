
import DDS.*;
import CR.*;

public class CasinoRoyaleDataSubscriber2
{
	// warning: poor programming standards within
	// (class variables with void functions that modify class variables)
	// Subscriber class variables
	public static DDSEntityManager mgrSub;
	public static bjDealerTypeSupport typeSupportSub1;
	public static DataReader reader;
	public static bjDealerDataReader typedReader1;
	public static bjDealerSeqHolder sequence;
	public static SampleInfoSeqHolder sampleInfoSequence; // no idea what this does, leaving it in.
	
	
	public static void main(String[] args) {
		mgrSub = new DDSEntityManager();
		String partitionName = "CasinoRoyale example";

		// create Domain Participant, Type, and register Type
		mgrSub.createParticipant(partitionName);
		typeSupportSub1 = new bjDealerTypeSupport();
		mgrSub.registerType(typeSupportSub1);

		// create Topic, subscriber, DataReader
		mgrSub.createTopic("Casino_Royale"); // no spaces, should be same as other group's
		mgrSub.createSubscriber();
		mgrSub.createReader();

		// set up subscriber to read the p2p connection (on localhost) and sequences to read
		reader = mgrSub.getReader();
		typedReader1 = bjDealerDataReaderHelper.narrow(reader);
		sequence = new bjDealerSeqHolder();
		sampleInfoSequence = new SampleInfoSeqHolder();

		System.out.println ("=== [Subscriber] Waiting for bjDealer data");
		
		int terminate = 0;
		int count = 0;
		
		while (terminate < 20) // run until 20 messages received
		{
			// put the backlog of messages into sequence
			typedReader1.take(sequence, sampleInfoSequence, LENGTH_UNLIMITED.value, ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
			for (int i = 0; i < sequence.value.length; i++)
			{
				System.out.println("=== [Subscriber] message received :\n");
				System.out.println("        seqno : " + sequence.value[i].seqno);
				System.out.println("terminate val : " + terminate);
				System.out.println();
				terminate++;
			}
			
			//wait50ms(); // waiting causes information to be missed, if they're close enough in timing together.
			
			++count;
		}
		// reply that we got the message
		typedReader1.return_loan(sequence, sampleInfoSequence);
		
		// clean up
		cleanUpSubscriber();
	}


	public static void cleanUpSubscriber()
	{
		mgrSub.getSubscriber().delete_datareader(typedReader1);
		mgrSub.deleteSubscriber();
		mgrSub.deleteTopic();
		mgrSub.deleteParticipant();
	}

	public static void wait50ms()
	{
		try
		{
			Thread.sleep(50);
		}
		catch(InterruptedException ie)
		{
			// nothing to do
		}
	}
}


