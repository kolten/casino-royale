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
 * LOGICAL_NAME:    CasinoRoyalePublisher.java
 * FUNCTION:        Publisher's main for the CasinoRoyale OpenSplice programming example.
 * MODULE:          OpenSplice CasinoRoyale example for the java programming language.
 * DATE             September 2010.
 ************************************************************************/

import DDS.DataWriter;
import DDS.HANDLE_NIL;
import CasinoRoyaleData.Msg;
import CasinoRoyaleData.MsgDataWriter;
import CasinoRoyaleData.MsgDataWriterHelper;
import CasinoRoyaleData.MsgTypeSupport;

public class CasinoRoyaleDataPublisher {

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

		// create Publisher
		mgr.createPublisher();

		// create DataWriter
		mgr.createWriter();

		// Publish Events

		DataWriter dwriter = mgr.getWriter();
		MsgDataWriter CasinoRoyaleWriter = MsgDataWriterHelper.narrow(dwriter);
		Msg msgInstance = new Msg();
		msgInstance.userID = 1;
		msgInstance.message = "Hello World";
		msgInstance.student_ID_number = 1000766987; // my edit here
		msgInstance.name = "Michael Pham";
		System.out.println("=== [Publisher] writing a message containing :");
		System.out.println("    userID  : " + msgInstance.userID);
		System.out.println("    Message : \"" + msgInstance.message + "\"");
		System.out.println("    student_ID_number  : " + msgInstance.student_ID_number); // my edit here
		System.out.println("    name : \"" + msgInstance.name + "\"");
		CasinoRoyaleWriter.register_instance(msgInstance);
		int status = CasinoRoyaleWriter.write(msgInstance, HANDLE_NIL.value);
		ErrorHandler.checkStatus(status, "MsgDataWriter.write");
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// clean up
		mgr.getPublisher().delete_datawriter(CasinoRoyaleWriter);
		mgr.deletePublisher();
		mgr.deleteTopic();
		mgr.deleteParticipant();

	}
}
