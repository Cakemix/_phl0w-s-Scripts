package org.beta.itchopper.strategies;

import org.beta.itchopper.functions.Functions;
import org.beta.itchopper.functions.Mode;
import org.beta.itchopper.functions.State;
import org.beta.itchopper.functions.Tree;
import org.beta.itchopper.user.Variables;
import org.powerbot.beta.core.script.BetaScript;
import org.powerbot.concurrent.strategy.Condition;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Entity;
import org.powerbot.game.api.wrappers.Locatable;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;

public class Chopper extends BetaScript {

    //private final Tree NORMAL = new Tree(1511, 38782, 38783, 38784, 38785, 38786, 38787, 38788);
    private final Tree NORMAL = new Tree(1519, 38616, 38627);

    @Override
    public int loop() {
        State state = getState();
        switch (state) {
            case WALKING:
                return Random.nextInt(400, 500);
            case BUSY:
                return Random.nextInt(400, 500);
            case FULL:
                Mode m = Variables.MODE;
                System.out.println("Full");
                switch (m) {
                    case DROPPING:
                        for (Item i : NORMAL.getItems()) {
                            if (SceneEntities.getAt(Players.getLocal()) != null) {
                                Walking.walk(Players.getLocal().getLocation().derive(-1, 1));
                            } else {
                                if (i.getWidgetChild().interact("Light")) {
                                    Functions.waitFor(1000, new Condition() {
                                        @Override
                                        public boolean validate() {
                                            return Players.getLocal().getAnimation() != -1;
                                        }
                                    });
                                    Functions.waitFor(1000, new Condition() {
                                        @Override
                                        public boolean validate() {
                                            return Players.getLocal().getAnimation() == -1;
                                        }
                                    });
                                }
                            }
                        }
                        break;
                    case BANKING:
                        System.out.println("Banking");
                        Entity bank = Bank.getNearest();
                        if (bank != null) {
                            System.out.println("nearest not null");
                            if (bank.isOnScreen()) {
                                if (Bank.open()) {
                                    if (Bank.isOpen()) {
                                        Bank.deposit(NORMAL.log, 0);
                                        if (Inventory.getCount(NORMAL.log) == 0) {
                                            Bank.close();
                                        }
                                    }
                                }
                            } else {
                                Camera.turnTo((Locatable) bank);
                                if (!bank.isOnScreen()) {
                                    Walking.walk((Locatable) bank);
                                }
                            }
                        } else {
                            System.out.println("Nearest is null");
                        }
                        break;
                }
                break;
            case INACTIVE:
                SceneObject tree = NORMAL.get();
                if (tree != null) {
                    if (!tree.isOnScreen()) {
                        Camera.turnTo(tree);
                        if (!tree.isOnScreen()) {
                            Walking.walk(tree);
                        }
                    } else {
                        if (tree.interact("Chop down")) {
                            Functions.waitFor(3000, new Condition() {
                                @Override
                                public boolean validate() {
                                    return getState() == State.BUSY;
                                }
                            });
                        }
                    }
                }
                break;
        }
        System.out.println("loop, curstate=" + state.toString());
        return Random.nextInt(800, 1000);
    }

    private State getState() {
        if (Players.getLocal().isMoving()) {
            return State.WALKING;
        } else if (Players.getLocal().getAnimation() != -1) {
            return State.BUSY;
        } else if (Inventory.isFull()) {
            return State.FULL;
        }
        return State.INACTIVE;
    }
}
