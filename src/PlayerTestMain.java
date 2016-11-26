import CR.bjPlayer;
import CR.bjDealer;

public class PlayerTestMain
{
	public static void main(String[] args) {
		PlayerPub Pub = new PlayerPub("CR example", "CR_bjPlayer");
		PlayerSub Sub = new PlayerSub("CR example", "CR_bjDealer");
		
		Timer PubTimer = new Timer();
		

		
		int k, i = 1;
		System.out.println("Reading Now");
		PubTimer.start();
		while(PubTimer.getTimeMs() < 5000)
		{
			bjDealer test = Sub.read();
			if(test != null)
			{
				Sub.printMsg(test);
				i++;
			}
		}
		System.out.printf("Read %d times\n", i);

		System.out.println("Writing Now");
		PubTimer.start();
		k = 1;
		while(PubTimer.getTimeMs() < 5000)
		{
			for(int j = 0; j < 10; j++)
			{
				bjPlayer msg = new bjPlayer();
				msg.uuid = k;
				msg.seqno = 2*k;
				msg.wager = 3*k;
				msg.dealer_id = 4*k;
				i = Pub.write(msg);
				if(i == 1)
				{
					Pub.printMsg(msg);
					k++;
				}
			}
			try
			{
				Thread.sleep(1000);
			}
			catch(InterruptedException ie){}
		}
		System.out.printf("Wrote %d times\n", k);
		
		Pub.close();
		Sub.close();
	}
}
