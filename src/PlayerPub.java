import DDS.DataWriter;			//DDS imports for Writing
import DDS.HANDLE_NIL;

import CR.bjPlayer;					//CR imports for Writing Topic
import CR.bjPlayerDataWriter;
import CR.bjPlayerDataWriterHelper;
import CR.bjPlayerTypeSupport;

public class PlayerPub
{
	public DDSEntityManager Pub;
	public bjPlayerTypeSupport bjpTS;
	public DataWriter dwriter;
	public bjPlayerDataWriter bjpWriter;
	
	/** Constructor for OpenSplice DDS entities and Publisher for usage.
	 * @param partitionName to use for the DDS Partition.
	 * @param TopicName to use to publish information under. */
	public PlayerPub(String partitionName, String TopicName)
	{
		Pub = new DDSEntityManager();

		// create Domain Participant
		Pub.createParticipant(partitionName);

		// create Type
		bjpTS = new bjPlayerTypeSupport();
		Pub.registerType(bjpTS);

		// create Topic
		Pub.createTopic(TopicName);

		// create Publisher
		Pub.createPublisher();

		// create DataWriter
		Pub.createWriter();

		dwriter = Pub.getWriter();
		bjpWriter = bjPlayerDataWriterHelper.narrow(dwriter);
		
		System.out.println ("=== [Publisher] Ready ...");
	}

	/** Register's the UUID value into OpenSplice.
	 * @param msg bjDealer object with specific UUID to register. */
	public void registerUUID(bjPlayer msg)
	{
		bjpWriter.register_instance(msg);
	}

	/** Writes bjDealer object from the parameter's.
	 * @param msg bjDealer object to write.
	 * @return 1 for valid object, 0 for null object.
	 */
	public int write(bjPlayer msg)
	{
		if(msg != null)
		{
			printMsg(msg);
			int status = bjpWriter.write(msg, HANDLE_NIL.value);
			ErrorHandler.checkStatus(status, "bjPlayerDataWriter.write");
			return 1;
		}
		else System.out.println("For the love of all that is holy don't set off");
		return 0;
	}
	
	/** Deletes and closes DDS Writer, Publisher, Topic, and Partition.
	 *
	 */
	public void close()
	{
		Pub.getPublisher().delete_datawriter(bjpWriter);
		Pub.deletePublisher();
		Pub.deleteTopic();
		Pub.deleteParticipant();
		System.out.println ("Publisher connection closed.");
	}

	/** Prints all values of the bjDealer object, primarily for debugging purposes.
	 * @param obj bjDealer object to print out. 
	 */
	public static void printMsg(bjPlayer obj)
	{
		if(obj != null)
		{
			System.out.println("[Player] Message sent to dealer :");
			System.out.println("          uuid : " + obj.uuid);
			System.out.println("         seqno : " + obj.seqno);
			System.out.println("       credits : " + obj.credits);
			System.out.println("         wager : " + obj.wager);
			System.out.println("      dealerID : " + obj.dealer_id);
			System.out.print("        action : ");
			switch(obj.action.value())
			{
				case 0: System.out.println("none"); break;
				case 1: System.out.println("joining"); break;
				case 2: System.out.println("exiting"); break;
				case 3: System.out.println("wagering"); break;
				case 4: System.out.println("requesting a card"); break;
			}
		}
	}
}



