import DDS.ANY_INSTANCE_STATE;	//DDS imports for Reading
import DDS.NOT_READ_SAMPLE_STATE;
import DDS.ANY_VIEW_STATE;
import DDS.DataReader;
import DDS.LENGTH_UNLIMITED;
import DDS.SampleInfoSeqHolder;

import CR.bjDealer;
import CR.bjd_action;
import CR.card;
import CR.player_status;

import CR.bjDealerDataReader;		//CR imports for Reading Topic
import CR.bjDealerDataReaderHelper;
import CR.bjDealerSeqHolder;
import CR.bjDealerTypeSupport;

import CR.MAX_PLAYERS;

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

	/* @return first valid bjDealer message*/
	public bjDealer read()
	{
		bjDealer msg = null;
		bjdReader.read(bjdSeq, infoSeq, LENGTH_UNLIMITED.value, NOT_READ_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
		if(bjdSeq.value.length != 0)
		{
			for(int i = 0; i < bjdSeq.value.length; i++)
			{
				if(bjdSeq.value[i].uuid != 0 && bjdSeq.value[i].seqno != 0)
				{
					msg = copy(bjdSeq.value[i]);
					printMsg(msg);
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

	/* Psuedo-Content filter read that will only return the messages from any dealer that  matches the parameters
	@param uuid
		Integer value of the intended dealer uuid.
	@return bjDealer message that matches the uuid from param*/
	public bjDealer read(int uuid)
	{
		bjDealer msg = null;
		bjdReader.read(bjdSeq, infoSeq, LENGTH_UNLIMITED.value, NOT_READ_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
		if(bjdSeq.value.length != 0)
		{
			for(int i = 0; i < bjdSeq.value.length; i++)
			{
				if(bjdSeq.value[i].uuid == uuid )
				{
					msg = copy(bjdSeq.value[i]);
					printMsg(msg);
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
	
	public void close()
	{
		Sub.getSubscriber().delete_datareader(bjdReader);
		Sub.deleteSubscriber();
		Sub.deleteTopic();
		Sub.deleteParticipant();
		System.out.println ("Subscriber connection closed.");
	}

	public static bjDealer copy(bjDealer obj)
	{
		if(obj != null)
		{
			int i, j;
			player_status team[] = new player_status[MAX_PLAYERS.value];
			card cardigon[] = new card[21];	//The mighty Cardigon will copy your hand!
			for(i = 0; i < MAX_PLAYERS.value; i++)	//Copies the full table
			{
				if(obj.players[i] != null && obj.players[i].uuid != 0)
				{
					for(j = 0; j < 21; j++)	//Copies hand of player @ seat (i+1)
					{
						if(obj.players[i].cards[j] != null)
						{
							card temp = obj.players[i].cards[j];
							cardigon[j] = new card(temp.suite, temp.base_value, temp.visible);
						}
					}
					team[i] = new player_status(obj.players[i].uuid, obj.players[i].wager, obj.players[i].payout, cardigon);
					cardigon = new card[21];
				}
			}
			cardigon = null;
			cardigon = new card[21];
			for(i = 0; i < 21; i++)	//Copies dealer's hand
			{
				if(obj.cards[i] != null)
				{
					cardigon[i] = new card(obj.cards[i].suite, obj.cards[i].base_value, obj.cards[i].visible);
				}
			}
			bjDealer temp = new bjDealer(obj.uuid, obj.seqno, obj.active_players, team, bjd_action.from_int( obj.action.value() ), cardigon, obj.target_uuid);
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
			System.out.println("\n== [Player] received message from [Dealer] :\n");
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
							if(Hand.isValidCard(obj.players[i].cards[j]))
								Hand.printCard(obj.players[i].cards[j]);
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
				if(Hand.isValidCard(obj.cards[j]))
					Hand.printCard(obj.cards[j]);
			}
			System.out.println("     target id : " + obj.target_uuid); 
		}
		else
		{
			System.out.println("Printing bad message for bad read.");
		}
	}
}