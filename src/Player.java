import CR.*;
import DDS.*;

public class Player {

	float credits;
	int dealer_id;
	int seqno;
	int uuid;
	int wager;

	String typeOfPlayer;

	bjp_action action;
	bjPlayer msg;

	Hand hand;
	Bank bank;

	public Player(){
		hand = new Hand();
		credits = 100.0;
		typeOfPlayer = "Mr. Conservative";
		action = new bjp_action(1);
	}

	// Sets player ID and the dealer ID it joined it, not sure if this is correct.
	public Player(int uuid, int dealer_id){
		this.uuid = uuid;
		this.dealer_id = dealer_id;
		hand = new Hand();
		credits = 100.0;
		typeOfPlayer = "Mr. Conservative";
		action = new bjp_action(1);
	}

	public float getCredits(){
		return credits;
	}

	public float setCredits(float credits){
		this.credits = credits;
		return credits;
	}

	public int getSeqno(){
		return seqno;
	}

	public int setSeqno(int seqno){
		this.seqno = seqno;
		return seqno;
	}

	public String getTypeOfPlayer(){
		return typeOfPlayer;
	}

	public String setTypeOfPlayer(String player){
		this.typeOfPlayer = player;
		return typeOfPlayer;
	}

	public int getUuid(){
		return uuid;
	}

	public int setUuid(int uuid){
		this.uuid = uuid;
		return uuid;
	}

	public int getWager(){
		return wager;
	}

	public int setWager(int wager){
		this.wager = wager;
		return wager;
	}

	public int getDealerID(){
		return dealer_id;
	}

	/* Core functions */

	public void joinGame(bjDealer dealer){

	}

	public void exitGame(bjDealer dealer){
		
	}

	public void requestCard(bjDealer dealer){
		// Hit
	}

	public void stay(bjDealer dealer){
		// stay

	}

	public void loss(bjDealer dealer){

	}

	public void win(bjDealer dealer){

	}

	public void placeWager(bjDealer dealer){

	}

}
