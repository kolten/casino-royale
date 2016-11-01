/*
 *                         OpenSplice DDS
 *
 *   This software and documentation are Copyright 2006 to 2013 PrismTech
 *   Limited and its licensees. All rights reserved. See file:
 *
 *                     $OSPL_HOME/LICENSE 
 *
 *   for full copyright notice and license terms. 
 *
 */

/************************************************************************
 * LOGICAL_NAME:    CasinoRoyaleSubscriber.java
 * FUNCTION:        Publisher's main for the CasinoRoyale OpenSplice programming example.
 * MODULE:          OpenSplice CasinoRoyale example for the java programming language.
 * DATE             September 2010.
 ************************************************************************/

import DDS.ANY_INSTANCE_STATE;
import DDS.ANY_SAMPLE_STATE;
import DDS.ANY_VIEW_STATE;
import DDS.DataReader;
import DDS.LENGTH_UNLIMITED;
import DDS.SampleInfoSeqHolder;
import CasinoRoyaleData.MsgDataReader;
import CasinoRoyaleData.MsgDataReaderHelper;
import CasinoRoyaleData.MsgSeqHolder;
import CasinoRoyaleData.MsgTypeSupport;

public class CasinoRoyaleDataSubscriber {

	public static void main(String[] args) {
		DDSEntityManager mgr = new DDSEntityManager();
		String partitionName = "CasinoRoyale example";

		// create Domain Participant
		mgr.createParticipant(partitionName);

		// create Type
		MsgTypeSupport msgTS = new MsgTypeSupport();
		mgr.registerType(msgTS);

		// create Topic
		mgr.createTopic("CasinoRoyaleData_Msg");

		// create Subscriber
		mgr.createSubscriber();

		// create DataReader
		mgr.createReader();

		// Read Events

		DataReader dreader = mgr.getReader();
		MsgDataReader CasinoRoyaleReader = MsgDataReaderHelper.narrow(dreader);

		MsgSeqHolder msgSeq = new MsgSeqHolder();
		SampleInfoSeqHolder infoSeq = new SampleInfoSeqHolder();

                System.out.println ("=== [Subscriber] Ready ...");
		boolean terminate = false;
		int count = 0;
		while (!terminate && count < 1500) { // We dont want the example to run indefinitely
			CasinoRoyaleReader.take(msgSeq, infoSeq, LENGTH_UNLIMITED.value,
					ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value,
					ANY_INSTANCE_STATE.value);
			for (int i = 0; i < msgSeq.value.length; i++) {
				if (msgSeq.value[i].message.equals("Hello World")) {
					System.out.println("=== [Subscriber] message received :");
					System.out.println("    userID  : "
							+ msgSeq.value[i].userID);
					System.out.println("    Message : \""
							+ msgSeq.value[i].message + "\"");
					System.out.println("    student_ID_number  : "
							+ msgSeq.value[i].student_ID_number); // my edit here
					System.out.println("    name : \""
							+ msgSeq.value[i].name + "\"");
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
                CasinoRoyaleReader.return_loan(msgSeq, infoSeq);
		
		// clean up
		mgr.getSubscriber().delete_datareader(CasinoRoyaleReader);
		mgr.deleteSubscriber();
		mgr.deleteTopic();
		mgr.deleteParticipant();

	}
}
