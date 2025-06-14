package engine.model;

import java.util.ArrayList;
import java.util.List;
import engine.IBrain;

public class Brain implements IBrain{
    private List<IBot> allBots;
    
    public Brain() {
        allBots = new ArrayList<>();
    }
    @Override
    public void register(IBot bot) {
        if (!allBots.contains(bot)) {
            allBots.add(bot);
        }
    }
    @Override
    public void unregister(IBot bot) {
        allBots.remove(bot);
    }
    @Override
    public void tick(int elapsed) {
        for (IBot bot : allBots) {
            if (bot instanceof Bot) {
                ((Bot) bot)._think(elapsed);
            } else {
                bot.think(elapsed);
            }
        }
    }
    
    public List<IBot> getBots() {
        return new ArrayList<>(allBots);
    }
} 
