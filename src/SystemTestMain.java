import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class SystemTestMain {

	static final int pauseBuffer = 600;
	static final int shortBuffer = 20;	//Hard-coded read buffer.
	static final int longBuffer = 500;	//Hard-coded publish time buffer.

    @Test
    public void systemTest(){

        Timer timer = new Timer();

        Thread pThread = new Thread () {
            public void run () {
                PlayerMain pMain = new PlayerMain();
                pMain.player.setUuid(101);
                pMain.run("Casino Royale", "bjPlayer", "bjDealer",pauseBuffer);
            }
        };
        Thread pThread2 = new Thread () {
            public void run () {
                PlayerMain pMain = new PlayerMain();
                pMain.player.setUuid(202);
                pMain.run("Casino Royale", "bjPlayer", "bjDealer",pauseBuffer);
            }
        };
        Thread pThread3 = new Thread () {
            public void run () {
                PlayerMain pMain = new PlayerMain();
                pMain.player.setUuid(303);
                pMain.run("Casino Royale", "bjPlayer", "bjDealer",pauseBuffer);
            }
        };
        Thread dThread = new Thread () {
            public void run () {
                DealerMain dMain = new DealerMain();
                dMain.getDealer().setUuid(13);
                dMain.getDealer().getDeck().stackDeck(); // stack the deck with more aces
                dMain.run("Casino Royale", "bjDealer", "bjPlayer",shortBuffer,longBuffer);
            }
        };
        // add 2 players
        pThread.start();
        pThread2.start();

        // wait some time then start a dealer
        timer.wait(5000);
        dThread.start();

        // after some time, add a 3rd player
        timer.wait(pauseBuffer*3);
        pThread3.start();

        // wait for the last player and dealer to terminate
        while(pThread.isAlive() || pThread2.isAlive() || pThread3.isAlive() || dThread.isAlive())
        {}
        assertTrue("Executables successfully ran",true);
    }

    public static void main(String[] args) {
        // test the SystemTestMain class
        Result systemTest = JUnitCore.runClasses(SystemTestMain.class);
            
        for (Failure failure : systemTest.getFailures()) {
            System.out.println(failure.toString());
        }
            
        System.out.println(systemTest.wasSuccessful());
		
    }
}