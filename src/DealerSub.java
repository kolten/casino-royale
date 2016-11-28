import DDS.ANY_INSTANCE_STATE;	//DDS imports for Reading
import DDS.NOT_READ_SAMPLE_STATE;
import DDS.ANY_VIEW_STATE;
import DDS.DataReader;
import DDS.LENGTH_UNLIMITED;
import DDS.SampleInfoSeqHolder;

import CR.bjPlayer;
import CR.bjp_action;

import CR.bjPlayerDataReader;		//CR imports for Reading Topic
import CR.bjPlayerDataReaderHelper;
import CR.bjPlayerSeqHolder;
import CR.bjPlayerTypeSupport;

import CR.MAX_PLAYERS;

import java.util.ArrayList;

public class DealerSub
{
	public DDSEntityManager Sub;
	public bjPlayerTypeSupport bjpTS;
	public DataReader dreader;
	public bjPlayerDataReader bjpReader;
	public bjPlayerSeqHolder bjpSeq;
	public SampleInfoSeqHolder infoSeq;
	
	public DealerSub(String partitionName, String TopicName)
	{
		Sub = new DDSEntityManager();

		// create Domain Participant
		Sub.createParticipant(partitionName);

		// create Type
		bjpTS = new bjPlayerTypeSupport();
		Sub.registerType(bjpTS);

		// create Topic
		Sub.createTopic(TopicName);

		// create Subscriber
		Sub.createSubscriber();

		// create DataReader
		Sub.createReader();

		// Read Events
		dreader = Sub.getReader();
		bjpReader = bjPlayerDataReaderHelper.narrow(dreader);

		bjpSeq = new bjPlayerSeqHolder();
		infoSeq = new SampleInfoSeqHolder();

        System.out.println ("=== [Subscriber] Ready ...");
	}

	/* Psuedo Content filter for read. The parameters will check if conditions match with the exception of joining messages
	@param uuid
		ID of dealer
	@param size
		Number of players at the table
	@param playerList[]
		Array of players' UUID
	@return ArrayList of bjPlayer messages that are either joining messages or  messages from players at the table.
	*/
	public ArrayList<bjPlayer> read(int uuid, int size, int playerList[])
	{
		ArrayList<bjPlayer> msg = new ArrayList<bjPlayer>();
		bjpReader.read(bjpSeq, infoSeq, LENGTH_UNLIMITED.value, NOT_READ_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
		if(bjpSeq.value.length != 0 && bjpSeq.value[0] != null && !bjpSeq.value[0].equals(null))
		{
			for(int j = 0; j < bjpSeq.value.length && bjpSeq.value[j] != null && !bjpSeq.value[j].equals(null) && bjpSeq.value[j].seqno != 0; j++)
			{
				bjPlayer temp = bjpSeq.value[j];
				if(temp.dealer_id == uuid)
				{
					boolean invalidID = true;
					if(temp.action.value() == 1 && size < MAX_PLAYERS.value)
					{
						for(int i = 0; i < size && invalidID; i++)	//Checks if player is already at table
						{
							if(temp.uuid == playerList[i])
							{
								invalidID = false;
							}
						}
						if(!invalidID)
						{
							msg.add(copy(temp));	//Player isn't at the table, and has met the conditions.
						}
						else System.out.println("Why do you do these things?");
					}
					else 
					{
						for(int i = 0; i < size && invalidID; i++)	//Checks if player is at the table
						{
							if(temp.uuid == playerList[i])
							{
								msg.add(copy(temp));
								invalidID = false;
							}
						}
						if(invalidID)
						{
							System.out.println("We have a phantom player.");
						}
					}
				}
			}
		}
		else
		{
			msg = null; //No values
		}
		bjpReader.return_loan(bjpSeq, infoSeq);
		return msg;
	}

	/*Psuedo-Content Filtered Read only for joining, and used only when there are no players at the table
	@param uuid
		Dealer ID number
	@return all joining messages intended for specific dealer*/
	public ArrayList<bjPlayer> read(int uuid)
	{
		ArrayList<bjPlayer> msg = new ArrayList<bjPlayer>();
		int j, i = 1;
		bjpReader.read(bjpSeq, infoSeq, LENGTH_UNLIMITED.value, NOT_READ_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
		if(bjpSeq.value.length != 0)
		{
			boolean isValidID = true;
			for(j = 0; j < bjpSeq.value.length && i <= MAX_PLAYERS.value && bjpSeq.value[j] != null && !bjpSeq.value[j].equals(null) && bjpSeq.value[j].seqno != 0; j++)
			{
				bjPlayer temp = bjpSeq.value[j];
				if(temp.dealer_id == uuid && temp.action.value() == 1)
				{
					msg.add(copy(temp));
					i++;
				}
				else if(temp.dealer_id == uuid)
				{
					System.out.println("We have a phantom player.");
				}
			}
		}
		else
		{
			msg = null; //No values
		}
		bjpReader.return_loan(bjpSeq, infoSeq);
		return msg;
	}

	public static bjPlayer copy(bjPlayer obj)
	{
		if(obj != null)
		{
			bjPlayer temp = new bjPlayer(obj.uuid, obj.seqno, obj.credits, obj.wager, obj.dealer_id, obj.action.from_int(obj.action.value()));
			return temp;
		}
		System.out.println("How in the world did this happen");
		return null;
	}

	public static void printMsg(bjPlayer obj)
	{
		if(obj != null)
		{
			System.out.println("[Dealer] received message from [Player] :");
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

	public void close()
	{
		Sub.getSubscriber().delete_datareader(bjpReader);
		Sub.deleteSubscriber();
		Sub.deleteTopic();
		Sub.deleteParticipant();
	}
}



