
import DDS.ANY_INSTANCE_STATE;
import DDS.ANY_SAMPLE_STATE;
import DDS.ANY_VIEW_STATE;
import DDS.DataReader;
import DDS.LENGTH_UNLIMITED;
import DDS.SampleInfoSeqHolder;
import CR.*;

public class TesterSub {

	public static void main(String[] args) {
		
		System.out.println("Placeholder print to screen - TesterSub");
		
		DDSEntityManager mgr = new DDSEntityManager();
		String partitionName = "TesterSub test by Michael";

		// create Domain Participant
		mgr.createParticipant(partitionName);

		// create Type
		MsgTypeSupport msgTS = new MsgTypeSupport();
		mgr.registerType(msgTS);

		// create Topic
		mgr.createTopic("TesterSub_card");

		// create Subscriber
		mgr.createSubscriber();

		// create DataReader
		mgr.createReader();

		// Read Events

		DataReader dreader = mgr.getReader();
		bjDealerDataReader bjDealerReader = bjDealerDataReaderHelper.narrow(dreader);

		bjDealerSeqHolder dealerSeq = new bjDealerSeqHolder();
		SampleInfoSeqHolder infoSeq = new SampleInfoSeqHolder();

                System.out.println ("=== [Subscriber] Ready ...");
		boolean terminate = false;
		int count = 0;
		while (!terminate && count < 5500) { // We dont want the example to run indefinitely
			CasinoRoyaleReader.take(dealerSeq, infoSeq, LENGTH_UNLIMITED.value,
					ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value,
					ANY_INSTANCE_STATE.value);
			for (int i = 0; i < dealerSeq.value.length; i++) {
				if (dealerSeq.value[i].message.equals("Hello World")) {
					System.out.println("=== [Subscriber] message received :");
					System.out.println("    userID  : "
							+ dealerSeq.value[i].userID);
					System.out.println("    Message : \""
							+ dealerSeq.value[i].message + "\"");
					System.out.println("    student_ID_number  : "
							+ dealerSeq.value[i].student_ID_number); // my edit here
					System.out.println("    name : \""
							+ dealerSeq.value[i].name + "\"");
					terminate = true;
				}
			}
			try
			{
				Thread.sleep(100);
			}
			catch(InterruptedException ie)
			{
				// nothing to do
			}
			++count;
			
		}
                CasinoRoyaleReader.return_loan(dealerSeq, infoSeq);
		
		// clean up
		mgr.getSubscriber().delete_datareader(CasinoRoyaleReader);
		mgr.deleteSubscriber();
		mgr.deleteTopic();
		mgr.deleteParticipant();
	}
}
