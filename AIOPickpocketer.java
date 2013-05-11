package com.skyxer.infibot.scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.infibot.api.Random;
import com.infibot.api.Sleep;
import com.infibot.api.env.Logger;
import com.infibot.api.env.Mouse;
import com.infibot.api.env.local.Camera;
import com.infibot.api.env.world.Npcs;
import com.infibot.api.env.world.Players;
import com.infibot.api.model.Npc;
import com.infibot.bot.script.Manifest;
import com.infibot.bot.script.Script;

@Manifest(name = "Skyxer's AIO Pickpocketer", description = "Pickpockets a variety of NPCs", authors = { "Skyxer" }, version = 0.1)
public class AIOPickpocketer extends Script {

  // Script vars
	private static int[] NPC_ID = { 1, 4, 7, 15, 2234, 1805, 660, 365, 21, 2362 };
	private static String[] NPC_NAMES = { "Man", "Women", "Farmer", "Warrior",
			"Master farmer", "Guard", "Knight", "Paladin", "Hero", "Elf" };
	private static double[] NPC_XP = { 8, 8, 14.5, 26, 43, 46.5, 84.3, 151.75,
			273.3, 353.3 };
	private String npcChoice;
	// Paint vars
	private final Color color1 = new Color(255, 255, 255);
	private final Font font1 = new Font("Arial", 0, 8);
	private final Image img1 = getImage("http://i.imgur.com/UYnNCHC.png");
	private long startTime;
	private double xpGained;

	@Override
	public void render(Graphics2D g) {
		long timeRun = System.currentTimeMillis() - startTime;
		long timeRunSeconds = timeRun / 1000 % 60;
		long timeMinutes = timeRun / 1000 / 60 % 60;
		long timeHours = timeRun / 1000 / 60 / 60 % 24;
		g.drawImage(img1, 0, 321, null);
		g.setFont(font1);
		g.setColor(color1);
		g.drawString(timeHours + ":" + timeMinutes + ":" + timeRunSeconds, 369,
				477);
	}

	@Override
	public int loop() {
		// TODO Auto-generated method stub
		if (!Players.getMyPlayer().isMoving()
				&& !Players.getMyPlayer().isAnimating()) {
			pickpocketNpc(getNpcId(npcChoice));
		}
		return Random.next(300, 600);
	}

	@Override
	public boolean onStart() {
		npcChoice = (String) JOptionPane.showInputDialog(null,
				"Choose how do you want to fish...", "Fishing Method Picker",
				JOptionPane.QUESTION_MESSAGE, null, NPC_NAMES, NPC_NAMES[0]);
		startTime = System.currentTimeMillis();
		xpGained = 0;
		return true;
	}

	/**
	 * Actual pickpocketing action
	 * 
	 * @param npcId
	 * @return
	 */
	public boolean pickpocketNpc(int npcId) {
		Npc pickpocketTarget = Npcs.getNearest(npcId);
		if (pickpocketTarget != null && Camera.isVisible(pickpocketTarget)) {
			pickpocketTarget.interact("Pickpocket", true);
			xpGained += getNpcXp(npcId);
			antiBan(pickpocketTarget);
		} else {
			Camera.pitch(0);
			while (!Camera.isVisible(pickpocketTarget)) {
				Camera.rotateRandomly();
				Sleep.millis(300, 1700);
			}
		}
		return true;
	}

	/**
	 * Anti ban
	 * 
	 * @param npc
	 * @return true
	 */
	public boolean antiBan(Npc npc) {
		Logger.print("Anti-ban");
		int val = Random.next(1, 7);
		switch (val) {
		case 1:
			Camera.rotateRandomly();
			break;
		case 2:
			Camera.face(npc);
			break;
		case 3:
			Mouse.moveRandomly(0, 1000);
			break;
		}
		Sleep.millis(100, 400);
		return true;
	}

	/**
	 * Method used to get the NPC based on its name
	 * 
	 * @param npc
	 * @return
	 */
	public int getNpcId(String npc) {
		for (int i = 0; i == NPC_NAMES.length; i++) {
			if (npc.equals(NPC_NAMES[i])) {
				return NPC_ID[i];
			}
		}
		return 0;
	}

	public double getNpcXp(int id) {
		for (int i = 0; i == NPC_ID.length; i++) {
			if (id == NPC_ID[i]) {
				return NPC_XP[i];
			}
		}
		return 0;
	}

	/**
	 * Method used to grab an Image form an URL
	 * 
	 * @param url
	 * @return
	 */
	private Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void onExit() {
	}
}
