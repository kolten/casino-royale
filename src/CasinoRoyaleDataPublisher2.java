
import DDS.*;
import CR.*;

public class CasinoRoyaleDataPublisher2
{
	// warning: poor programming standards within
	// (class variables with void functions that modify class variables)
	// Publisher class variables
	public static DDSEntityManager mgrPub;
	public static bjDealerTypeSupport typeSupportPub1;
	public static DataWriter writer;
	public static bjDealerDataWriter typedWriter1;
	public static bjDealer pubInstance1;
		
	
	public static void main(String[] args) {
		mgrPub = new DDSEntityManager();
		String partitionName = "CasinoRoyale example";

		// create Domain Participant, Type
		mgrPub.createParticipant(partitionName);
		typeSupportPub1 = new bjDealerTypeSupport();
		mgrPub.registerType(typeSupportPub1);

		// create Publisher, DataWriter, Topic
		mgrPub.createTopic("Casino_Royale"); // no spaces, should be same as other group's
		mgrPub.createPublisher();
		mgrPub.createWriter();

		// "Publish Events"
		writer = mgrPub.getWriter();
		typedWriter1 = bjDealerDataWriterHelper.narrow(writer);
		pubInstance1 = new bjDealer();
		
		// Assign values
		pubInstance1.seqno = 1;
		pubInstance1.active_players = 6;
		pubInstance1.uuid = 1000123456;
		
		typedWriter1.register_instance(pubInstance1); // finish assigning values, now save it to the instance for sending (or something like that)
		
		// Do the actual publishing
		// run 10 times for testing
		for(int i = 0; i<10; i++)
		{
			wait100ms();
			int status = typedWriter1.write(pubInstance1, HANDLE_NIL.value); // send the message instance
			ErrorHandler.checkStatus(status, "bjDealerDataWriter.write"); // make sure it got sent
			System.out.println("  status value: " + status);
			pubInstance1.seqno++;
		}
		wait1000ms();
		
		// clean up
		cleanUpPublisher();
	}
	
	
	public static void cleanUpPublisher()
	{
		mgrPub.getPublisher().delete_datawriter(typedWriter1);
		mgrPub.deletePublisher();
		mgrPub.deleteTopic();
		mgrPub.deleteParticipant();
	}
	
	
	
	
	
	
	public static void wait1000ms()
	{
		try
		{
			Thread.sleep(1000);
		}
		catch(InterruptedException ie)
		{
			// nothing to do
		}
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
