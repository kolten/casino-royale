
import DDS.*; // opensplice stuff
import CR.*; // idl stuff


public class DealerMain
{
	public static void main(String[] args) {
		DealerMain main = new DealerMain();
		main.run();
	}
	
	public void run()
	{
		// Do not remove psuedo code or write active code in the middle of it.
		// psuedo code begins.
		/*
		Declare and initialize the following:
		Dealer dealer; // do not shuffle while initializing
		ArrayList<Player> localPlayers;
		DealerSub
		DealerPub
		
		breakout conditions 
		statuses should be changed right before DealerPub sends messages, as noted in
		the statement "DealerPub a message (status)"
		
		ideally, boolean conditions inside while loops should be pulled from the dealer object and not handled inside dealerMain
		
		boolean exiting = false;
		needToShuffle = true;
		
		
		localPlayers = new ArrayList<Player>();
		while( !exiting )
		{
			//=============================================================
			if(needToShuffle) // the shuffling step
			{
				dealer.shuffle
				needToShuffle=false
				DealerPub a message (shuffling)
				Timer.wait()?
			}
			
			//=============================================================
			fill up a stack/queue with a list of active players called activeNotReplied
			while( !(allActiveReplied || timeoutLarge) ) // the waiting step
			{
				set target_id = activeNotReplied.uuid; // keep going through players who haven't replied. Make it a queue or stack
				DealerPub a message (waiting) for target_id
				while(timeoutSmall) // about 5 seconds
				{
					read PlayerSub's messages
					if( foundMessageWagering && target==me ) // we respond to wagering messages only
					{
						save wager information
						respond to player's wager to show confirmation? Not handled in PlayerMain yet
						remove player from the NotReplied queue/stack
						numReplies++
						
						if(numReplies == numActive)
							allActiveReplied = true;
						
						break;? it will speed up the process
					}
				}
			}
			
			if(timeoutLarge) // we can add this later during testing
				if we timed out, we need to have dealer remove inactive players from the active players list
			
			Timer.wait() // there should be a little extra time for players to process info, just in case
			
			//=============================================================
			// the dealing step (initial 2 cards)
			Dealer.startGame, or something like that
			DealerPub a message (dealing)
			Timer.wait() // give players time for processing
			
			//=============================================================
			fill up a stack/queue with a list of active players called activeNotStayed
			while( !(allActiveStayed || timeoutLarge) ) // the dealing step (asking)
			{
				set target_id = activeNotStayed.uuid; // keep going through players who haven't replied. Make it a queue or stack
				DealerPub a message (dealing)
				while(timeoutSmall) // about 5 seconds
				{
					read PlayerSub's messages
					if( foundMessage && target==me ) // we respond to wagering messages only
					{
						if( requesting_a_card )
						{
							dealer.putCardInPlayer'sHand
						}
						else if ( none )
						{
							numStayed++;
							remove the guy from activeNotStayed stack/queue
						}
						if(activeNotStayed is empty)
							allActiveStayed = true;
					}
				}
				
			}
			
			if(timeoutLarge) // we can add this later during testing
				if we timed out, we need to have dealer remove inactive players who have not replied from the active players list
			
			Timer.wait() // there should be a little extra time for players to process info, just in case
			
				//=============================================================
			
			
			// need to add exiting code somewhere
		}
		
		*/
		// psuedo code ends
		
		
	}
	
}



