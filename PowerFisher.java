package com.skyxer.infibot.scripts;

import java.awt.Graphics2D;

import javax.swing.JOptionPane;

import com.infibot.api.Random;
import com.infibot.api.Sleep;
import com.infibot.api.env.Logger;
import com.infibot.api.env.Mouse;
import com.infibot.api.env.local.Camera;
import com.infibot.api.env.local.Game;
import com.infibot.api.env.local.Inventory;
import com.infibot.api.env.world.Npcs;
import com.infibot.api.env.world.Objects;
import com.infibot.api.env.world.Players;
import com.infibot.api.model.GameObject;
import com.infibot.api.model.Npc;
import com.infibot.api.model.Player;
import com.infibot.api.model.Tile;
import com.infibot.bot.script.Manifest;
import com.infibot.bot.script.Script;

@Manifest(name = "Skyxer's Powerfisher", description = "Powerfishes whatever you want.", authors = { "Skyxer" }, version = 0.1)
public class PowerFisher extends Script {
	private String FISHING_TYPE_CHOSEN;
	private static final int SMALLNET_ID = 304, BAIT_ID = 317, ROD_ID = 307,
			FLYROD_ID = 309, BIGNET_ID = 305, LOBSTERPOT_ID = 301,
			HARPOON_ID = 311, FEATHER_ID = 314, FISHINGSPOT_ID = 4908;
	private static final String[] FISHING_TYPE_LIST = { "Net", "Bait",
			"Big Net", "Fly", "Lobster pot", "Harpoon" };

	public void getFishingType() {
		// Lets popup a nice interface asking what type of fishing what the user
		// wants to do.
		FISHING_TYPE_CHOSEN = (String) JOptionPane.showInputDialog(null,
				"Choose how do you want to fish...", "Fishing Method Picker",
				JOptionPane.QUESTION_MESSAGE, null, FISHING_TYPE_LIST,
				FISHING_TYPE_LIST[0]);
	}

	public int[] getUndropables(String type) {
		int[] dropNone = { 0, 0 };
		if (type == FISHING_TYPE_LIST[0]) {
			int[] dropables = { SMALLNET_ID, 0 };
			return dropables;
		} else if (type == FISHING_TYPE_LIST[1]) {
			int[] dropables = { ROD_ID, BAIT_ID };
			return dropables;
		} else if (type == FISHING_TYPE_LIST[2]) {
			int[] dropables = { BIGNET_ID, 0 };
			return dropables;
		} else if (type == FISHING_TYPE_LIST[3]) {
			int[] dropables = { FLYROD_ID, FEATHER_ID };
			return dropables;
		} else if (type == FISHING_TYPE_LIST[4]) {
			int[] dropables = { LOBSTERPOT_ID, 0 };
			return dropables;
		} else if (type == FISHING_TYPE_LIST[5]) {
			int[] dropables = { HARPOON_ID, 0 };
			return dropables;
		} else {
			return dropNone;
		}
	}

	public String getInteractionType(String type) {
		if (type == FISHING_TYPE_LIST[0]) {
			return "Net";
		} else if (type == FISHING_TYPE_LIST[1]) {
			return "Bait";
		} else if (type == FISHING_TYPE_LIST[2]) {
			return "Net";
		} else if (type == FISHING_TYPE_LIST[3]) {
			return "Lure";
		} else if (type == FISHING_TYPE_LIST[4]) {
			return "Cage";
		} else if (type == FISHING_TYPE_LIST[5]) {
			return "Harpoon";
		} else {
			return null;
		}
	}

	public Boolean hasAllItems() {
		// Check for bait+rod or feathers and fly rod
		if (FISHING_TYPE_CHOSEN == FISHING_TYPE_LIST[1]
				|| FISHING_TYPE_CHOSEN == FISHING_TYPE_LIST[3]) {
			// Checking for both rod and bait
			if (FISHING_TYPE_CHOSEN == FISHING_TYPE_LIST[1]) {
				// Check if it contains both bait and rod
				if (Inventory.contains(BAIT_ID) && Inventory.contains(ROD_ID)) {
					return true;
				} else {
					return false;
				}
			}
			// Checking for fly rod and feathers
			else {
				// Check for feathers and fly rod
				if (Inventory.contains(FEATHER_ID)
						&& Inventory.contains(FLYROD_ID)) {
					return true;
				} else {
					return false;
				}
			}

		}
		// Only needs one item, so we check for that instead
		else {
			// Check if contains needed item to fish with net
			if (FISHING_TYPE_CHOSEN == FISHING_TYPE_LIST[0]) {
				if (Inventory.contains(SMALLNET_ID)) {
					return true;
				}
			}
			// Check if contains needed item to fish with big net
			if (FISHING_TYPE_CHOSEN == FISHING_TYPE_LIST[2]) {
				if (Inventory.contains(305)) {
					return true;
				}
			}
			// Check if contains needed item to fish with lobster pot
			if (FISHING_TYPE_CHOSEN == FISHING_TYPE_LIST[4]) {
				if (Inventory.contains(301)) {
					return true;
				}
			}
			// Check if contains needed item to fish with harpoon
			if (FISHING_TYPE_CHOSEN == FISHING_TYPE_LIST[5]) {
				if (Inventory.contains(311)) {
					return true;
				}
			}
			return false;
		}
	}

	public void catchFishies() {
		Npc fishingSpot = Npcs.getNearest("Fishing spot");
		if (!Players.getMyPlayer().isMoving()) {
			while (Players.getMyPlayer().isAnimating()) {
				Sleep.millis(250, 500);
				antiBan(fishingSpot);
			}
			fishingSpot.interact("Net");
			Sleep.millis(500, 1500);
		}
	}

	public boolean antiBan(Npc npc) {
		Logger.print("Anti-ban");
		int val = Random.next(1, 5);
		switch (val) {
		case 1:
			break;
		case 2:
			Camera.rotateRandomly();
			break;
		case 3:
			Camera.face(npc);
			break;
		case 4:
			Mouse.moveRandomly(0, 500);
			break;
		case 5:
			break;
		}
		Sleep.millis(1000, 12000);
		return true;
	}

	public void dropFishies() {
		/**
		 * Still needs work
		 */
	}

	@Override
	public boolean onStart() {
		if (!Game.isLoggedIn()) {
			Logger.print("Login in to start the script.");
			return false;
		}
		if (Game.isLoggedIn()) {
			System.out.print("Skyxer's PowerFisher has started :)");
			if (FISHING_TYPE_CHOSEN == null) {
				getFishingType();
				Logger.print(FISHING_TYPE_CHOSEN.toString());
				return true;
			}
		}
		return false;
	}

	@Override
	public int loop() {
		if (hasAllItems()) {
			if (!Inventory.isFull()) {
				catchFishies();
			} else {
				dropFishies();
			}
		}
		else if (!hasAllItems()) {
			Logger.print("We're missing something");
		}
		return Random.next(100, 400);
	}

	@Override
	public void onExit() {
	}

	@Override
	public void render(Graphics2D arg0) {
		arg0.drawString("Test", 10, 10);

	}
}
