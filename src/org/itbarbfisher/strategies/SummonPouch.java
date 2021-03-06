package org.itbarbfisher.strategies;

import org.itbarbfisher.user.Utilities;
import org.itbarbfisher.user.Variables;
import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Condition;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;

public class SummonPouch extends Strategy implements Task {

    @Override
    public boolean validate() {
        return Variables.guiInitialized && Settings.get(448) == -1 || Widgets.get(662, 1).getModelId() == -1 && Variables.pouch != -1
                && Inventory.getCount(Variables.pouch) > 0;
    }

    @Override
    public void run() {
        if (Skills.getLevel(Skills.SUMMONING) < 10) {
            if (canDrink()) {
                drink();
            }
        } else {
            if (Inventory.getItem(Variables.pouch).getWidgetChild().interact("Summon")) {
                Utilities.waitFor(2000, new Condition() {
                    @Override
                    public boolean validate() {
                        return Settings.get(448) != -1 || Widgets.get(662, 1).getModelId() != -1;
                    }
                });
            }
            Time.sleep(Random.nextInt(300, 400));
        }
    }

    public boolean canDrink() {
        int potCount = 0;
        int[] pots = {12140, 12142, 12144, 12146, 3024, 3026, 3028, 3030};
        for (int pot : pots) {
            if (Inventory.getCount(pot) > 0) {
                potCount++;
            }
        }
        return potCount > 0;
    }

    private void drink() {
        int[] pots = {12140, 12142, 12144, 12146, 3024, 3026, 3028, 3030};
        for (int pot : pots) {
            if (Inventory.getItem(pot) != null) {
                if (Inventory.getItem(pot).getWidgetChild().interact("Drink")) {
                    Time.sleep(800);
                    Utilities.waitFor(2000, new Condition() {
                        @Override
                        public boolean validate() {
                            return Skills.getLevel(Skills.SUMMONING) > 10;
                        }
                    });
                }
                Time.sleep(Random.nextInt(300, 400));
            }
        }
    }
}