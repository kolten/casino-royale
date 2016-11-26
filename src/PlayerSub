import DDS.ANY_INSTANCE_STATE;	//DDS imports for Reading
import DDS.ANY_SAMPLE_STATE;
import DDS.ANY_VIEW_STATE;
import DDS.DataReader;
import DDS.LENGTH_UNLIMITED;
import DDS.SampleInfoSeqHolder;

import DDS.DataWriter;			//DDS imports for Writing
import DDS.HANDLE_NIL;

import CR.bjDealer;
import CR.bjd_action;
import CR.card;
import CR.player_status;

import CR.bjDealerDataReader;		//CR imports for Reading Topic
import CR.bjDealerDataReaderHelper;
import CR.bjDealerSeqHolder;
import CR.bjDealerTypeSupport;

public class PlayerSub
{
	private DDSEntityManager Sub;
	private bjDealerTypeSupport bjdTS;
	private DataReader dreader;
	private bjDealerDataReader bjdReader;
	private bjDealerSeqHolder bjdSeq;
	private SampleInfoSeqHolder infoSeq;
	
	public PlayerSub(String partitionName, String TopicName)
	{
		Sub = new DDSEntityManager();

		// create Domain Participant
		Sub.createParticipant(partitionName);

		// create Type
		bjdTS = new bjDealerTypeSupport();
		Sub.registerType(bjdTS);

		// create Topic
		Sub.createTopic(TopicName);

		// create Subscriber
		Sub.createSubscriber();

		// create DataReader
		Sub.createReader();

		// Read Events
		dreader = Sub.getReader();
		bjdReader = bjDealerDataReaderHelper.narrow(dreader);

		bjdSeq = new bjDealerSeqHolder();
		infoSeq = new SampleInfoSeqHolder();

        System.out.println ("=== [Subscriber] Ready ...");
	}

	public bjDealer read()
	{
		bjDealer msg;
		bjdReader.read(bjdSeq, infoSeq, LENGTH_UNLIMITED.value, ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
		if(bjdSeq.value.length != 0 && bjdSeq.value[0] != null && !bjdSeq.value[0].equals(null) && bjdSeq.value[0].uuid != 0 && bjdSeq.value[0].seqno != 0)
		{
			msg = copy(bjdSeq.value[0]);
		}
		else
		{
			msg = null;
		}
		bjdReader.return_loan(bjdSeq, infoSeq);
		return msg;
	}

	public static bjDealer copy(bjDealer obj)
	{
		if(obj != null)
		{
			int i, j;
			bjDealer temp = new bjDealer();
			temp.uuid = obj.uuid;
			temp.seqno = obj.seqno;
			temp.active_players = obj.active_players;
			temp.players = new player_status[6];
			for(i = 0; i < 6; i++)
			{
				if(obj.players[i] != null)
				{
					temp.players[i] = new player_status();
					temp.players[i].uuid = obj.players[i].uuid;
					temp.players[i].wager = obj.players[i].wager;
					temp.players[i].payout = obj.players[i].payout;
					for(j = 0; j < 21; j++)
					{
						if(obj.players[i].cards[j] != null)
							temp.players[i].cards[j] = obj.players[i].cards[j];
					}
				}
			}
			temp.action = obj.action.from_int(obj.action.value());
			temp.cards = new card[21];
			for(i = 0; i < 21; i++)
			{
				if(obj.cards[i] != null)
					temp.cards[i] = obj.cards[i];
			}
			temp.target_uuid = obj.target_uuid;
			return temp;
		}
		System.out.println("Bug Report");
		return null;
	}

	public static void printMsg(bjDealer obj)
	{
		if(obj != null)
		{
			int i, j;
			Hand cardLogic = new Hand();
			System.out.println("\n== [Dealer] Message sent to player :\n");
			System.out.println("          uuid : " + obj.uuid);
			System.out.println("         seqno : " + obj.seqno);
			System.out.println("players @ table: " + obj.active_players);
			for(i = 0; i < 6; i++)
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
						for(j = 0; j < 21; j++)
						{
							if(cardLogic.isValidCard(obj.players[i].cards[j]))
								cardLogic.printCard(obj.players[i].cards[j]);
						}
					}
				}
			}
			System.out.println("===================");
			for(j = 0; j < 21; j++)
			{
				if(cardLogic.isValidCard(obj.cards[j]))
					cardLogic.printCard(obj.cards[j]);
			}
			System.out.println("     dealer_id : " + obj.target_uuid); 
		}
	}


	public void close()
	{
		Sub.getSubscriber().delete_datareader(bjdReader);
		Sub.deleteSubscriber();
		Sub.deleteTopic();
		Sub.deleteParticipant();
	}
}


