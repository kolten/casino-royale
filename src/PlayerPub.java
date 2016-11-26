import DDS.DataWriter;			//DDS imports for Writing
import DDS.HANDLE_NIL;

import CR.bjPlayer;					//CR imports for Writing Topic
import CR.bjPlayerDataWriter;
import CR.bjPlayerDataWriterHelper;
import CR.bjPlayerTypeSupport;

public class PlayerPub
{
	private DDSEntityManager Pub;
	private bjPlayerTypeSupport bjpTS;
	private DataWriter dwriter;
	private bjPlayerDataWriter bjpWriter;
	private String TopicName;
	
	public PlayerPub(String partitionName, String topicName)
	{
		Pub = new DDSEntityManager();

		// create Domain Participant
		Pub.createParticipant(partitionName);

		// create Type
		bjpTS = new bjPlayerTypeSupport();
		Pub.registerType(bjpTS);

		// create Topic
		Pub.createTopic(topicName);

		// create Publisher
		Pub.createPublisher();

		// create DataWriter
		Pub.createWriter();

		dwriter = Pub.getWriter();
		bjpWriter = bjPlayerDataWriterHelper.narrow(dwriter);
		TopicName = topicName;

	        System.out.println ("=== [Publisher] Ready ...");
	}

	public void registerUUID(bjPlayer msg)
	{
		bjpWriter.register_instance(msg);
	}

	public int write(bjPlayer msg)
	{
		if(msg != null)
		{
			int status = bjpWriter.write(msg, HANDLE_NIL.value);
			ErrorHandler.checkStatus(status, TopicName);
			return 1;
		}
		else System.out.println("For the love of all that is holy don't set off");
		return 0;
	}

	public static void printMsg(bjPlayer obj)
	{
		if(obj != null)
		{
			System.out.println("          uuid : " + obj.uuid);
			System.out.println("         seqno : " + obj.seqno);
			System.out.println("       credits : " + obj.credits);
			System.out.println("         wager : " + obj.wager);
			System.out.println("      dealerID : " + obj.dealer_id);
		}
	}
	public void close()
	{
		Pub.getPublisher().delete_datawriter(bjpWriter);
		Pub.deletePublisher();
		Pub.deleteTopic();
		Pub.deleteParticipant();
		System.out.println ("Publisher connection closed.");
	}
}


