package engine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Ticker manages periodic updates for the game engine.
 * It provides a central timing mechanism for game state updates.
 */
public class Ticker {
    private final ScheduledExecutorService executor;
    private final List<TickListener> listeners;
    private long lastTickTime;
    private boolean running;
    
    public interface TickListener {
        void onTick(double deltaTime);
    }
    
    public Ticker() {
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.listeners = new ArrayList<>();
        this.lastTickTime = System.nanoTime();
        this.running = false;
    }
    
    public void addListener(TickListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }
    
    public void removeListener(TickListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
    
    public void start(int ticksPerSecond) {
        if (running) return;
        
        running = true;
        lastTickTime = System.nanoTime();
        
        long period = 1000000000L / ticksPerSecond; // nanoseconds
        
        executor.scheduleAtFixedRate(() -> {
            long currentTime = System.nanoTime();
            double deltaTime = (currentTime - lastTickTime) / 1_000_000_000.0;
            lastTickTime = currentTime;
            
            synchronized (listeners) {
                for (TickListener listener : listeners) {
                    listener.onTick(deltaTime);
                }
            }
        }, 0, period, TimeUnit.NANOSECONDS);
    }
    
    public void stop() {
        running = false;
        executor.shutdown();
    }
    
    public boolean isRunning() {
        return running;
    }
} 