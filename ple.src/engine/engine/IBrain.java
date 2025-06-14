package engine;
import engine.model.*;
public interface IBrain {
    public interface IBot {
        Entity entity();
        void think(int elapsed);
    }
    
    void tick(int elapsed);
    void register(IBot bot);
    void unregister(IBot bot);
} 