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

public class DealerSub
{
	private DDSEntityManager Sub;
	private bjPlayerTypeSupport bjpTS;
	private DataReader dreader;
	private bjPlayerDataReader bjpReader;
	private bjPlayerSeqHolder bjpSeq;
	private SampleInfoSeqHolder infoSeq;
	
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

	public bjPlayer read()
	{
		bjPlayer msg;
		bjpReader.read(bjpSeq, infoSeq, LENGTH_UNLIMITED.value, ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
		if(bjpSeq.value.length != 0 && bjpSeq.value[0] != null && !bjpSeq.value[0].equals(null) && bjpSeq.value[0].uuid != 0 && bjpSeq.value[0].seqno != 0)
		{
			int throwaway = bjpSeq.value[0].uuid;
			msg = copy(bjpSeq.value[0]);
		}
		else
		{
			msg = null; //No values
		}
		//bjpReader.return_loan(bjpSeq, infoSeq);
		return msg;
	}

	public static bjPlayer copy(bjPlayer obj)
	{
		if(obj != null)
		{
			bjPlayer temp = new bjPlayer();
			temp.uuid = obj.uuid;
			temp.seqno = obj.seqno;
			temp.credits = obj.credits;
			temp.wager = obj.wager;
			temp.dealer_id = obj.dealer_id;
			temp.action = obj.action.from_int(obj.action.value());
			return temp;
		}
		System.out.println("How in the world did this happen");
		return null;
	}

	public static void printMsg(bjPlayer obj)
	{
		if(obj != null)
		{
			System.out.println("\n          uuid : " + obj.uuid);
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


