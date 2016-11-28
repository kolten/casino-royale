import DDS.ANY_INSTANCE_STATE;	//DDS imports for Reading
import DDS.ANY_SAMPLE_STATE;
import DDS.ANY_VIEW_STATE;
import DDS.DataReader;
import DDS.LENGTH_UNLIMITED;
import DDS.SampleInfoSeqHolder;

import DDS.DataWriter;			//DDS imports for Writing
import DDS.HANDLE_NIL;

import CR.bjPlayer;
import CR.bjp_action;

import CR.bjPlayerDataReader;		//CR imports for Reading Topic
import CR.bjPlayerDataReaderHelper;
import CR.bjPlayerSeqHolder;
import CR.bjPlayerTypeSupport;

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

	//Psuedo-Content Filtered Read
	public ArrayList<bjPlayer> read(int uuid, int size, int playerList[])
	{
		ArrayList<bjPlayer> msg = new ArrayList<bjPlayer>();
		bjpReader.read(bjpSeq, infoSeq, LENGTH_UNLIMITED.value, ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
		if(bjpSeq.value.length != 0 && bjpSeq.value[0] != null && !bjpSeq.value[0].equals(null))
		{
			for(int j = 0; j < bjpSeq.value.length; j++)
			{
				bjPlayer temp = bjpSeq.value[j];
				if(temp.dealer_id == uuid)
				{
					if(temp.action.value() == 1 && size < 6)
					{
						for(int id: playerList)
						{
							if(temp.uuid != id)
							{
								msg.add(copy(temp));
							}
							else System.out.println("Why do you do these things?");
						}
							
					}
					else 
					{
						for(int id: playerList)
						{
							if(temp.uuid == id)
							{
								msg.add(copy(temp));
							}
							else System.out.println("We have a phantom player.");
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

	//Psuedo-Content Filtered Read only for joining
	public ArrayList<bjPlayer> read(int uuid)
	{
		ArrayList<bjPlayer> msg = new ArrayList<bjPlayer>();
		bjpReader.read(bjpSeq, infoSeq, LENGTH_UNLIMITED.value, ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
		if(bjpSeq.value.length != 0 && bjpSeq.value[0] != null && !bjpSeq.value[0].equals(null))
		{
			for(int j = 0; j < bjpSeq.value.length; j++)
			{
				bjPlayer temp = bjpSeq.value[j];
				if(temp.dealer_id == uuid && temp.action.value() == 1)
				{
					msg.add(copy(temp));
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
			System.out.println("          uuid : " + obj.uuid);
			System.out.println("         seqno : " + obj.seqno);
			System.out.println("       credits : " + obj.credits);
			System.out.println("         wager : " + obj.wager);
			System.out.println("      dealerID : " + obj.dealer_id);
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



