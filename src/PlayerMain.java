
import DDS.*; // opensplice stuff
import CR.*; // idl stuff


public class PlayerMain{


	Player player;
	PlayerSub sub;
	PlayerPub pub;
	int seatCount;
	Timer timer;

	public static void main(String[] args) {
		PlayerMain main = new PlayerMain();
		main.run(args[0], args[1], args[2]);
	}
	
	public void run(String partition, String pubTopic, String subtopic)
	{
		player = new Player();
		sub = new PlayerSub(partition, subtopic); // Sub needs to have the same topic name as the dealer pub
		pub = new PlayerPub(partition, pubTopic); // Vice versa
		timer = new Timer();

		boolean exiting = false;
		boolean notSeated = true;

		bjDealer temp;
		// Are you ready ???? 🎺🎺🎺
		while ( !exiting ) {

			while( notSeated ){
				temp = sub.read();
				if(temp != null){
					seatCount = seatCount + 1;

					if(temp.active_players < 6){
						player.joinGame(temp);
						pub.write(player.getMsg());
						//notSeated = false;
						//timer.start();   ??? maybe ???
						timer.wait(5000); // Wait 5 seconds
						temp = sub.read();
						if(temp != null){
							int i = 0;
							for(i = 0; i < temp.active_players; i++){
								if(player.getUuid() == temp.players[i].uuid){
										notSeated = false;
								} 
							}
						}
					}

				} else {
					// No dealer found
				}
			}
		}
	}
	
	

}


