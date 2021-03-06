package org.itpotionmixer.strategies;

import org.itpotionmixer.user.Functions;
import org.itpotionmixer.user.Variables;
import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Condition;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.node.Item;

public class ItemOnItem extends Strategy implements Task {

    @Override
    public boolean validate() {
        return Variables.guiInitialized && !Widgets.get(905, 14).validate() && !Functions.waitFor(1200, new Condition() {
            @Override
            public boolean validate() {
                return Players.getLocal().getAnimation() != -1;
            }
        });
    }

    @Override
    public void run() {
        Variables.status = "ioi";
        Item prim = Inventory.getItem(Variables.primary);
        Item sec = Inventory.getItem(Variables.secondary);
        prim.getWidgetChild().interact("Use");
        sec.getWidgetChild().interact("Use");
        Functions.waitFor(3000, new Condition() {
            @Override
            public boolean validate() {
                return Widgets.get(905, 14).validate();
            }
        });
    }
}
