
import DDS.*; // opensplice stuff
import CR.*; // idl stuff


public class PlayerMain
{
	public static void main() {
		PlayerMain main = new PlayerMain();
		main.run();
	}
	
	public void run()
	{
		// psuedo code begins. This goes into another function and may be split up for modularity
		/*
		Declare and initialize the following:
		Player
		PlayerSub
		PlayerPub
		
		statuses should be changed right before PlayerPub sends messages, as noted in
		the statement "PlayerPub a message (status)"
		boolean exiting = false;
		while( !exiting )
			
			//=============================================================
			while( notSeated ) // the joining step
			{
				read PlayerSub's messages
				if( foundMessageAny ) // we respond to any message
				{
					send PlayerPub message (joining)
					if( nowSeated ) // make sure we got a join confirmation in case we are a 7th player
						notSeated = false
				}
			}
			
			//=============================================================
			while( wagering ) // the wagering step
			{
				read PlayerSub's messages
				if( foundMessageWaiting ) // we respond to waiting messages only
				{
					call Player's wagering stuff, which will be saved into Player.getMsg
					PlayerPub a message (wagering)
					wagering = false
				}
			}
			
			//=============================================================
			while( playingInitial ) // the receive2cards step, or initial game state
			{
				read PlayerSub's messages
				if( foundMessageDealing ) // we respond to dealing messages only
				{
					store card information into Player
					playingInitial = false
				}
			}
			//=============================================================
			
			while( playing ) // the request card (hit/stay) step
			{
				
				read PlayerSub's messages
				if( foundMessageDealing ) // we respond to dealing messages only
				{
					if( !firstTimeAsking ) // if this is not the first time
					{
						store new card information into player
					}
					have Player figure out if we want a new card or not
					if(wantCard)
						PlayerPub a message (request_a_card)
					else
					{
						PlayerPub a message (none)
						playing = false
					}
				}
			}
			
			//=============================================================
			
			while( losing ) // the losing step
			{
				read PlayerSub's messages
				if( foundMessageCollecting ) // we respond to collecting messages only
				{
					subtract money from Player Bank
					losing = false
				}
			}
			
			//=============================================================
			while( winning ) // the winning step
			{
				read PlayerSub's messages
				if( foundMessagePaying ) // we respond to paying messages only
				{
					add money to Player Bank
					playingInitial = false
				}
			}
			//=============================================================
			
			// need to add exiting code somewhere
		}
		
		*/
		// psuedo code ends
	}
	
	
// End of main

}


