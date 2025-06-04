package engine.model;

import java.util.ArrayList;
import java.util.List;
import engine.IBrain;

public class Brain implements IBrain {
    private List<IBot> m_allBots;
    private Model m_model;
    
    public Brain(Model model) {
        m_allBots = new ArrayList<>();
        m_model = model;
    }
    
    @Override
    public void tick(int elapsed) {
        // Call think on all registered bots
        for (IBot bot : m_allBots) {
            bot.think(elapsed);
        }
    }
    
    @Override
    public void register(IBot bot) {
        if (!m_allBots.contains(bot)) {
            m_allBots.add(bot);
        }
    }
    
    @Override
    public void unregister(IBot bot) {
        m_allBots.remove(bot);
    }
    
    public Model getModel() {
        return m_model;
    }
} 