import DDS.DataWriter;			//DDS imports for Writing
import DDS.HANDLE_NIL;

import CR.bjDealer;					//CR imports for Writing Topic
import CR.bjDealerDataWriter;
import CR.bjDealerDataWriterHelper;
import CR.bjDealerTypeSupport;

import CR.MAX_PLAYERS;

public class DealerPub
{
	public DDSEntityManager Pub;
	public bjDealerTypeSupport bjdTS;
	public DataWriter dwriter;
	public bjDealerDataWriter bjdWriter;
	public String TopicName;
	
	public DealerPub(String partitionName, String topicName)
	{
		Pub = new DDSEntityManager();

		// create Domain Participant
		Pub.createParticipant(partitionName);

		// create Type
		bjdTS = new bjDealerTypeSupport();
		Pub.registerType(bjdTS);

		// create Topic
		Pub.createTopic(topicName);

		// create Publisher
		Pub.createPublisher();

		// create DataWriter
		Pub.createWriter();

		dwriter = Pub.getWriter();
		bjdWriter = bjDealerDataWriterHelper.narrow(dwriter);
		TopicName = topicName;
		
	        System.out.println ("=== [Publisher] Ready ...");
	}

	public void registerUUID(bjDealer msg)
	{
		bjdWriter.register_instance(msg);
	}

	public static void printMsg(bjDealer obj)
	{
		if(obj != null)
		{
			int i, j;
			Hand cardLogic = new Hand();
			System.out.println("\n== [Dealer] message sent :\n");
			System.out.println("          uuid : " + obj.uuid);
			System.out.println("         seqno : " + obj.seqno);
			System.out.println("players @ table: " + obj.active_players);
			for(i = 0; i < MAX_PLAYERS.value; i++)
			{
				if(obj.players[i] != null)
				{
					if(obj.players[i].uuid != 0)
					{
						System.out.println("===================");
						System.out.printf("Players in seat %d:\n", i + 1);
						System.out.println("          uuid : " + obj.players[i].uuid);
						System.out.println("         wager : " + obj.players[i].wager);
						System.out.println("        payout : " + obj.players[i].payout);
						System.out.println("        in hand:");
						for(j = 0; j < 21; j++)
						{
							if(cardLogic.isValidCard(obj.players[i].cards[j]))
								cardLogic.printCard(obj.players[i].cards[j]);
						}
					}
				}
			}
			System.out.println("===================");
			System.out.print("        action : ");
			switch(obj.action.value())
			{
				case 0: System.out.println("shuffling"); break;
				case 1: System.out.println("waiting"); break;
				case 2: System.out.println("dealing"); break;
				case 3: System.out.println("paying"); break;
				case 4: System.out.println("collecting"); break;
				default: System.out.println("ERROR"); break;
			}
			System.out.println("        in hand:");
			for(j = 0; j < 21; j++)
			{
				if(cardLogic.isValidCard(obj.cards[j]))
					cardLogic.printCard(obj.cards[j]);
			}
			System.out.println("     target id : " + obj.target_uuid); 
		}
	}

	public int write(bjDealer msg)
	{
		if(msg != null)
		{
			int status = bjdWriter.write(msg, HANDLE_NIL.value);
			ErrorHandler.checkStatus(status, "bjDealerDataWriter.write");
			return 1;
		}
		else System.out.println("Everything is terrible");
		return 0;
	}
	
	public void close()
	{
		Pub.getPublisher().delete_datawriter(bjdWriter);
		Pub.deletePublisher();
		Pub.deleteTopic();
		Pub.deleteParticipant();
		System.out.println ("Publisher connection closed.");
	}
}



