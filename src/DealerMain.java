
import DDS.*;
import CR.*;

public class DealerMain implements MAX_PLAYERS
{
    public static final int value = (int)(6);
	
	// warning: poor programming standards within
	// (class variables with void functions that modify class variables)
	// Publisher class variables (bjDealer data publish)
	public static DDSEntityManager mgrPub;
	public static bjDealerTypeSupport typeSupportPub;
	public static DataWriter writer;
	public static bjDealerDataWriter typedWriter;
	public static bjDealer pubInstance;
	
	// Subscriber class variables (bjPlayer data subscribe)
	public static DDSEntityManager mgrSub;
	public static bjPlayerTypeSupport typeSupportSub;
	public static DataReader reader;
	public static bjPlayerDataReader typedReader;
	public static bjPlayerSeqHolder sequence;
	public static SampleInfoSeqHolder sampleInfoSequence; // no idea what this does, leaving it in.
	
	
	
	public static void main(String[] args) {
		mgrPub = new DDSEntityManager();
		String partitionName = "CasinoRoyale example";

		// create Domain Participant, Type
		mgrPub.createParticipant(partitionName);
		typeSupportPub = new bjDealerTypeSupport();
		mgrPub.registerType(typeSupportPub);

		// create Publisher, DataWriter, Topic
		mgrPub.createTopic("Casino_Royale"); // no spaces, should be same as other group's
		mgrPub.createPublisher();
		mgrPub.createWriter();

		// "Publish Events"
		writer = mgrPub.getWriter();
		typedWriter = bjDealerDataWriterHelper.narrow(writer);
		pubInstance = new bjDealer();
		
		// Assign values
		pubInstance.seqno = 1;
		pubInstance.active_players = 6;
		pubInstance.uuid = 1000123456;
		
		typedWriter.register_instance(pubInstance); // finish assigning values, now save it to the instance for sending (or something like that)
		
		// Do the actual publishing
		// run 10 times for testing
		for(int i = 0; i<10; i++)
		{
			Timer.wait(50);
			int status = typedWriter.write(pubInstance, HANDLE_NIL.value); // send the message instance
			ErrorHandler.checkStatus(status, "bjDealerDataWriter.write"); // make sure it got sent
			System.out.println("  status value: " + status);
			pubInstance.seqno++;
		}
		Timer.wait(1000);
		
		// clean up
		cleanUpPublisher();
	}
	
	
	public static void cleanUpPublisher()
	{
		mgrPub.getPublisher().delete_datawriter(typedWriter);
		mgrPub.deletePublisher();
		mgrPub.deleteTopic();
		mgrPub.deleteParticipant();
	}
	
	
}
