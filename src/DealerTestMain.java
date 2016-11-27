import CR.bjDealer;
import CR.bjPlayer;

public class DealerTestMain
{
	public static void main(String[] args) {
		DealerPub Pub = new DealerPub("CR example", "CR_bjDealer");
		DealerSub Sub = new DealerSub("CR example", "CR_bjPlayer");
		
		
		Timer PubTimer = new Timer();
		
		System.out.println("Writing Now");
		PubTimer.start();
		int i, k = 1;
		while(PubTimer.getTimeMs() < 5000)
		{
			for(int j = 0; j < 2; j++)
			{
				bjDealer msg = new bjDealer();
				msg.uuid = k;
				msg.seqno = 2*k;
				msg.active_players = 3*k;
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

		i = 1;
		System.out.println("Reading Now");
		PubTimer.start();
		while(PubTimer.getTimeMs() < 5000)
		{
			bjPlayer test = Sub.read();
			if(test != null)
			{
				Sub.printMsg(test);
				i++;
			}
		}
		System.out.printf("Read %d times\n", i);

		Pub.close();
		Sub.close();
	}
}


