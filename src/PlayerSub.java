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

import java.util.ArrayList;

public class PlayerSub
{
	public DDSEntityManager Sub;
	public bjDealerTypeSupport bjdTS;
	public DataReader dreader;
	public bjDealerDataReader bjdReader;
	public bjDealerSeqHolder bjdSeq;
	public SampleInfoSeqHolder infoSeq;
	
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

	/* @param uuid
	Integer value of the intended dealer uuid.
	@return bjDealer message that matches the uuid from param*/
	public bjDealer read(int uuid)
	{
		bjDealer msg = null;
		bjdReader.take(bjdSeq, infoSeq, LENGTH_UNLIMITED.value, ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
		if(bjdSeq.value.length != 0)
		{
			for(int i = 0; i < bjdSeq.value.length; i++)
			{
				if(bjdSeq.value[i].uuid == uuid )
				{
					msg = copy(bjdSeq.value[i]);
					i = bjdSeq.value.length;
				}
			}
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
			Hand cardLogic = new Hand();
			int i, j;
			bjDealer temp = new bjDealer();
			temp.uuid = obj.uuid;
			temp.seqno = obj.seqno;
			temp.active_players = obj.active_players;
			temp.players = new player_status[6];
			for(i = 0; i < 6; i++)
			{
				if(obj.players[i] != null && obj.players[i].uuid != 0)
				{
					card cardigon[] = new card[21];	//The mighty Cardigon will copy your data!
					for(j = 0; j < 21; j++)
					{
						if(cardLogic.isValidCard(obj.players[i].cards[j]))
						{
							cardigon[j] = new card(obj.players[i].cards[j].suite, obj.players[i].cards[j].base_value, obj.players[i].cards[j].visible);
						}
						else j = 21;	//Cardigon has ended
					}
					temp.players[i] = new player_status(obj.players[i].uuid, obj.players[i].wager, obj.players[i].payout, cardigon);
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



