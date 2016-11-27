
import DDS.*; // opensplice stuff
import CR.*; // idl stuff
import java.util.ArrayList;

public class DealerMain

{
	Dealer dealer;
	DealerSub sub;
	DealerPub pub;
	Timer timer;

	public static void main(String[] args) {
		DealerMain main = new DealerMain();
		main.run(args[0], args[1], args[2]);
	}
	
	public void run(String partition, String pubTopic, String subtopic)
	{
		dealer = new Dealer();
		sub = new PlayerSub(partition, subtopic); // Sub needs to have the same topic name as the dealer pub
		pub = new PlayerPub(partition, pubTopic); // Vice versa
		timer = new Timer();

		boolean exiting = false;

		while( !exiting ){
			dealer.shuffle();
			pub.write(dealer.getMsg());

			while(dealer.getActivePlayers() == 0){
				ArrayList <bjPlayer> players = sub.read(dealer.getUuid());
				int i;
				for(bjPlayer temp : players){
					dealer.join(temp);
				}
			}
		}
	
	}
	
}



