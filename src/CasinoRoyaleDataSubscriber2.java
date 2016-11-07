
import DDS.ANY_INSTANCE_STATE;
import DDS.ANY_SAMPLE_STATE;
import DDS.ANY_VIEW_STATE;
import DDS.DataReader;
import DDS.LENGTH_UNLIMITED;
import DDS.SampleInfoSeqHolder;
import CasinoRoyaleData.MsgDataReader;
import CasinoRoyaleData.MsgDataReaderHelper;
import CasinoRoyaleData.MsgSeqHolder;
import CasinoRoyaleData.MsgTypeSupport;

public class CasinoRoyaleDataSubscriber2
{
	// warning: poor programming standards within
	// (class variables with void functions that modify class variables)
	public static DDSEntityManager mgr;
	public static MsgTypeSupport msgTS;
	public static DataReader reader;
	public static MsgDataReader typedReader;
	public static MsgSeqHolder sequence;
	public static SampleInfoSeqHolder sampleInfoSequence; // no idea what this does, leaving it in.
	
	
	public static void main(String[] args) {
		mgr = new DDSEntityManager();
		String partitionName = "CasinoRoyale example";

		// create Domain Participant, Type, and register Type
		mgr.createParticipant(partitionName);
		msgTS = new MsgTypeSupport();
		mgr.registerType(msgTS);

		// create Topic, subscriber, DataReader
		mgr.createTopic("CasinoRoyaleData_Msg");
		mgr.createSubscriber();
		mgr.createReader();

		// set up subscriber to read the p2p connection (on localhost) and sequences to read
		reader = mgr.getReader();
		typedReader = MsgDataReaderHelper.narrow(reader);
		sequence = new MsgSeqHolder();
		sampleInfoSequence = new SampleInfoSeqHolder();

		System.out.println ("=== [Subscriber] Ready ...");
		
		boolean terminate = false;
		int count = 0;
		
		while (!terminate && count < 1500) { // run for 2.5 minutes or until message receive
			typedReader.take(sequence, sampleInfoSequence, LENGTH_UNLIMITED.value, ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
			for (int i = 0; i < sequence.value.length; i++) {
				System.out.println("=== [Subscriber] message received :");
				System.out.println("    Message : \"" + sequence.value[i].message + "\"");
				terminate = true;
			}
			
			wait100ms();
			
			++count;
		}
		// reply that we got the message
		typedReader.return_loan(sequence, sampleInfoSequence);
		
		// clean up
		cleanUpSubscriber();
	}


	public static void cleanUpSubscriber()
	{
		mgr.getSubscriber().delete_datareader(typedReader);
		mgr.deleteSubscriber();
		mgr.deleteTopic();
		mgr.deleteParticipant();
	}

	public static void wait100ms()
	{
		try
		{
			Thread.sleep(100);
		}
		catch(InterruptedException ie)
		{
			// nothing to do
		}
	}
}


