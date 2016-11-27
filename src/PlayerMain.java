
import DDS.*; // opensplice stuff
import CR.*; // idl stuff


public class PlayerMain
{
	public static void main(String[] args) {
		PlayerMain main = new PlayerMain();
		main.run();
	}
	
	public void run()
	{
		// Do not remove psuedo code or write active code in the middle of it.
		// psuedo code begins.
		/*
		Declare and initialize the following:
		Player player;
		PlayerSub
		PlayerPub
		
		statuses should be changed right before PlayerPub sends messages, as noted in
		the statement "PlayerPub a message (status)"
		boolean exiting = false;
		
		player = new Player();
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
				if( foundMessageWaiting && target==me ) // we respond to waiting messages only
				{
					call Player's wagering stuff, which will be saved into Player.getMsg
					PlayerPub a message (wagering)
					wagering = false
				}
			}
			
			//=============================================================
			while( playingInitial ) // the receive 2 cards step, or initial game state
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
				if( foundMessageDealing && target==me ) // we respond to dealing messages only
				{
					if( !firstTimeAsking ) // if this is not the first time
					{
						store new card information into player
					}
					
					// figure out if we want a card or not
					if(player.getCurrentHandValue() >= 17){
						player.stay();
					} else if(player.getCurrentHandValue() <= 16){
						player.requestCard();
					}
					
					PlayerPub a message (requestin_a_card or none)
				}
			}
			
			//=============================================================
			
			while( losing ) // the losing step
			{
				read PlayerSub's messages
				if( foundMessageCollecting && target==me ) // we respond to collecting messages only
				{
					subtract money from Player Bank
					losing = false
				}
			}
			
			//=============================================================
			while( winning ) // the winning step
			{
				read PlayerSub's messages
				if( foundMessagePaying && target==me ) // we respond to paying messages only
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


