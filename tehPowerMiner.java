package com.skyxer.infibot.scripts;

import com.infibot.api.env.local.Inventory;
import com.infibot.api.env.local.Player;
import com.infibot.bot.script.Script;

public class tehPowerMiner extends Script{
  	private Random rand;
	private static final int MIN_RANDOM = 500
	private static final int MAX_RANDOM = 3000;
	private static final int IRON_ROCK_ID = 1; //NEEDS TO BE SET
	private static final int MAX_ROCK_DISTANCE = 10;
	private static final int[] PICKAXE_IDS = {100, 101, 102, 103} //NEEDS TO BE SET (ADD A CASH STACK ID)

	private boolean onStartup()
	{
		println("tehPowerMiner has started!!1! LAWL BOTTER");
		return true;
	}

	public GameObject findNearestRock(int ids, int distance){
		GameObject[] objects = Objects.getNearest(distance, ids);

		for(GameObject object : objects){
			if(object != null){
				return object;
			}
		}
	}

	@Override
	public void run(){
		if(onStartup()){
			while(true){
				sleep(loop());
			}
		}
		println("Bye bye <3");
	}

	private int loop(){
		if(Inventory.isFull()){
			Inventory.dropAllExcept(this.PICKAXE_IDS);
		}
		else{
			if(Player.getAnimation == -1){
				GameObject mineableRock = findNearestRock(IRON_ROCK_ID, MAX_ROCK_DISTANCE);

				if (mineableRock != null){
					mineableRock.Interact("Mine");
					while(Player.getAnimation != -1){
						rand = new Random();
						int randomNum = rand.nextInt(MAX_RANDOM - MIN_RANDOM + 1) + MIN_RANDOM;
						sleep(randomNum);
					}
				}
			}
		}
		return 30;
	}
}
