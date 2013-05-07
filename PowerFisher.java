package com.skyxer.infibot.scripts;

import java.awt.Graphics2D;

import javax.swing.JOptionPane;

import com.infibot.api.Random;
import com.infibot.api.Sleep;
import com.infibot.api.env.Logger;
import com.infibot.api.env.local.Camera;
import com.infibot.api.env.local.Game;
import com.infibot.api.env.local.Inventory;
import com.infibot.api.env.world.Objects;
import com.infibot.api.env.world.Players;
import com.infibot.api.model.GameObject;
import com.infibot.api.model.Player;
import com.infibot.api.model.Tile;
import com.infibot.bot.script.Manifest;
import com.infibot.bot.script.Script;

@Manifest(name = "Skyxer's Powerfisher", description = "Powerfishes whatever you want.", authors = { "Skyxer" }, version = 0.1)
public class PowerFisher extends Script {
  private String FISHING_TYPE_CHOSEN;
	private static final int SMALLNET_ID = 303, BAIT_ID = 317, ROD_ID = 307,
			FLYROD_ID = 309, BIGNET_ID = 305, LOBSTERPOT_ID = 301,
			HARPOON_ID = 311, FEATHER_ID = 314, FISHINGSPOT_ID = 0;
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
				if (Inventory.contains(303)) {
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
		GameObject fishingSpot = Objects.getNearest(FISHINGSPOT_ID);
		if (fishingSpot != null) {
			if (!Camera.isVisible(fishingSpot)) {
				Camera.face(fishingSpot, Random.next(30, 40));
				if (Players.getMyPlayer().getAnimation() == -1) {
					fishingSpot.interact(""); // SETTS
				}
			}
		}
	}

	public void dropFishies() {
		Inventory.dropAllExcept(getUndropables(FISHING_TYPE_CHOSEN));
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
				System.out
						.print("Setting fishing type: " + FISHING_TYPE_CHOSEN);
			}
			return true;
		}
		return false;
	}

	@Override
	public int loop() {
		if (Game.isLoggedIn()) {
			if (hasAllItems()) {
				while (!Inventory.isFull()) {
					catchFishies();
				}
				dropFishies();
			}
		}
		return 100;
	}

	@Override
	public void onExit() {
		Logger.print("Thank you for using Skyxer's Powerfisher");
	}

	@Override
	public void render(Graphics2D arg0) {

	}
}
