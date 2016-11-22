
import DDS.*; // opensplice stuff
import CR.*; // idl stuff


public class PlayerMain
{
	public static int seqno = 1; // Increments by 1 after sending a pub
	
	public static int runtimeFlag = 0; // For helping the publisher know what to publish
	public static int sequenceFlagWaiting = 0; // For identifying multiple parts of the waiting sequence
	public static int sequenceFlagDealing = 0; // For identifying multiple parts of the dealing sequence
	public static boolean askForCard = false;
	
	public static void main(String[] args) {
		// Declare Subscriber variables (bjDealer data subscribe)
		DDSEntityManager mgrSub;
		bjDealerTypeSupport typeSupportSub;
		DataReader reader;
		bjDealerDataReader typedReader;
		bjDealerSeqHolder sequence;
		SampleInfoSeqHolder sampleInfoSequence; // no idea what this does, leaving it in.
		
		// Declare Publisher variables (bjPlayer data publish)
		DDSEntityManager mgrPub;
		bjPlayerTypeSupport typeSupportPub;
		DataWriter writer;
		bjPlayerDataWriter typedWriter;
		bjPlayer pubInstance;
		
		// End PubSub declarations
		
		
		// Declare a local player object
		Player player = new Player();
		
		
		mgrSub = new DDSEntityManager(); // bjD type receive 
		mgrPub = new DDSEntityManager(); // bjP type send
		String partitionName = "CasinoRoyale example";

		// create Domain Participant, Type, and register Type
		mgrSub.createParticipant(partitionName);
		typeSupportSub = new bjDealerTypeSupport();
		mgrSub.registerType(typeSupportSub); // receive bjD
		mgrPub.createParticipant(partitionName);
		typeSupportPub = new bjPlayerTypeSupport();
		mgrPub.registerType(typeSupportPub); // send bjP

		// create Topic, subscriber, DataReader
		mgrSub.createTopic("Casino_RoyaleDealerPub"); // no spaces, should be same as other group's
		mgrSub.createSubscriber();
		mgrSub.createReader();
		mgrPub.createTopic("Casino_RoyalePlayerPub");
		mgrPub.createPublisher();
		mgrPub.createWriter();
		
		// set up subscriber to read the p2p connection (on localhost) and sequences to read
		reader = mgrSub.getReader();
		typedReader = bjDealerDataReaderHelper.narrow(reader);
		sequence = new bjDealerSeqHolder();
		sampleInfoSequence = new SampleInfoSeqHolder();

		// actual handling code here
		
		// Prepare a bjPlayer object for sending
		writer = mgrPub.getWriter();
		typedWriter = bjPlayerDataWriterHelper.narrow(writer);
		pubInstance = new bjPlayer(); // TODO: Replace this with a bjPlayer instance created by the Player class
		
		// Assign values
		pubInstance.seqno = 05;
		pubInstance.wager = 5;
		pubInstance.uuid = 101;
		pubInstance.action = CR.bjp_action.wagering;
		
		typedWriter.register_instance(pubInstance); // finish assigning values, now save it to the instance for sending (or something like that)
		
		
		System.out.println ("=== [Player] Waiting for Dealer\n");
		
		boolean runFlag = true;
		while(runFlag)
		{
			bjDealer obj = readSequence(typedReader, sequence, sampleInfoSequence);
			handleSequence(obj);
			handleSequenceDebug(obj);
			typedReader.return_loan(sequence, sampleInfoSequence); // free memory (obj is now null)
			
			pubInstance.action=bjp_action.none;
			
			Timer.wait(500);
			publishInstance(typedWriter,pubInstance); // just reply every time
			if(askForCard) // stop asking for a card flag after asking for a card
				askForCard=false;
			
			if(obj.seqno==0) // uuid needs to be from Dealer, not the empty message which sets it to 0
				runFlag=false;
		}
		// clean up
		System.out.println();
		cleanUpPublisher(mgrPub, typedWriter);
		cleanUpSubscriber(mgrSub,typedReader);
	}
// End of main


	public static bjDealer readSequence(bjDealerDataReader typedReader, bjDealerSeqHolder sequence, SampleInfoSeqHolder sampleInfoSequence)
	{
		bjDealer obj;
		Timer.start(); // note: some operating systems use 10 ms instead of 1 ms for their java timer
		while (Timer.getTimeMs() < 10000 ) // run until 10 seconds have passed or reply received
		{
			typedReader.take(sequence, sampleInfoSequence, LENGTH_UNLIMITED.value, ANY_SAMPLE_STATE.value, ANY_VIEW_STATE.value, ANY_INSTANCE_STATE.value);
			for (int i = 0; i < sequence.value.length; i++)
			{
				obj = sequence.value[i];
				return obj; // pass along bjDealer object
			}
		}
		obj = new bjDealer();
		obj.seqno = -1;
		return obj; // return an empty message
	}
	
	
	// take the data writer and the bjDealer, send the message to the publisher
	public static void publishInstance(bjPlayerDataWriter typedWriter, bjPlayer pubInstance)
	{
		pubInstance.seqno = PlayerMain.seqno;
		typedWriter.register_instance(pubInstance); // pass assignments into publisher
		Timer.wait(100);
		int status = typedWriter.write(pubInstance, HANDLE_NIL.value); // send the message instance
		ErrorHandler.checkStatus(status, "bjPlayerDataWriter.write"); // make sure it got sent?
		//System.out.println("  status value: " + status);
		PlayerMain.seqno++;
	}
	
	public static void handleSequenceDebug(bjDealer obj)
	{
		System.out.println("\n=== [Player] Message received from dealer :\n");
		
		// seqno will be 0 for a player closing their connection
		System.out.println("          uuid : " + obj.uuid);
		System.out.println("         seqno : " + obj.seqno);
		System.out.println("active_players : " + obj.active_players);
		System.out.println("  player0 uuid : " + obj.players[0].uuid);
		System.out.println(" player0 wager : " + obj.players[0].uuid);
		System.out.println("player0 payout : " + obj.players[0].uuid);
		System.out.println("        action : " + obj.action.value()); 
		System.out.println("   target_uuid : " + obj.target_uuid); 
	}
	
	// passes along the correct handling of information based on sequence number
	public static void handleSequence(bjDealer obj)
	{
		switch (obj.action.value())
		{
			case bjd_action._shuffling: // player action when dealer is shuffling
				if(seqno==0) break; // action and seqno both show as 0 if it's a closed connection
				System.out.println("Dealer " + obj.uuid + " is shuffling");
				
				// need to reply so that dealer knows we joined
				
				break;
			
			
			case bjd_action._waiting: // player action when dealer is waiting
				
				// TODO: Lose money after wagering? Or should that be handled in dealer collecting wagers step?
				
				if(sequenceFlagWaiting == 0)
				{
					System.out.println("Dealer " + obj.uuid + " is waiting for wagers");
					sequenceFlagWaiting = 1;
				}
				else
				{
					System.out.println("Dealer has accepted wager of Y credits from player X");
				}
				
				
				break;
			
			
			case bjd_action._dealing: // player action when dealer is dealing
				System.out.println("Dealer " + obj.uuid + " is dealing");
				sequenceFlagWaiting=0; // reset wagering flag
				
				
				if(sequenceFlagDealing == 0) // initial deal, 2 card info
				{
					System.out.println("Dealer " + obj.uuid + " is doing initial deal");
					sequenceFlagDealing = 1;
					
					//TODO: receive 2 cards
				}
				else if (sequenceFlagDealing == 1) // first round of asking. no card info
				{
					System.out.println("Dealer is asking player X if they want a card (first time)");
					// TODO: if my targetID, set a flag for a reply in main
					if(obj.target_uuid == -1)
					{
						// TODO: if total points of my cards under 17, do it
						if ( 0 < 17 )
							askForCard = true;
					}
				}
				else if (sequenceFlagDealing == 2) // second round of asking, with card info from last thing
				{
					System.out.println("Dealer is giving a card to player X");
					System.out.println("Dealer is asking player X if they want a card (repeat)");
					// TODO: if my targetID, set a flag for a reply in main
					if(obj.target_uuid == -1)
					{
						// TODO: receive a card
						// TODO: if total points of my cards under 17, do it
						if ( 0 < 17 )
							askForCard = true;
					}
					
				}
				
				break;
			
			
			case bjd_action._collecting: // player action when dealer is collecting losing wagers
				System.out.println("Dealer " + obj.uuid + " is collecting wagers");
				sequenceFlagDealing = 0; // reset dealing flag
				// TODO: Decrease my wallet? or should that be done in wagering step?
				
				// no replies to dealer
				
				break;
			
			
			case bjd_action._paying: // player action when dealer is paying out winning bets
				System.out.println("Dealer " + obj.uuid + " is paying out bets");
				
				// float payout = obj.players[0].payout // TODO: 0 should be my array ID in player_status[6] in bjDealer
				
				// TODO: Increase my wallet
				
				// no replies to dealer
				
				break;
			
			
			default:
				break;
		}
		
		if(obj.seqno==0)
		{
			// TODO: Closed connection
			System.out.println("Dealer " + obj.uuid + " has closed their connection");
		}
		
	}
	
	
	public static void cleanUpPublisher(DDSEntityManager mgrPub, bjPlayerDataWriter typedWriter)
	{
		mgrPub.getPublisher().delete_datawriter(typedWriter);
		mgrPub.deletePublisher();
		mgrPub.deleteTopic();
		mgrPub.deleteParticipant();
		System.out.println ("Publisher connection closed.");
	}
	public static void cleanUpSubscriber(DDSEntityManager mgrSub, bjDealerDataReader typedReader)
	{
		mgrSub.getSubscriber().delete_datareader(typedReader);
		mgrSub.deleteSubscriber();
		mgrSub.deleteTopic();
		mgrSub.deleteParticipant();
		System.out.println ("Subscriber connection closed.");
	}

}


