package engine;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Ticker manages periodic updates for the game engine.
 * It provides a central timing mechanism for game state updates.
 */
public class Ticker {
    private ScheduledExecutorService executor;
    private final List<TickListener> listeners;
    private long lastTickTime;
    private boolean running;
    private int tickCount = 0;
    
    public interface TickListener {
        void onTick(double deltaTime);
    }
    
    public Ticker() {
        this.listeners = new CopyOnWriteArrayList<>(); // Thread-safe list
        this.lastTickTime = System.nanoTime();
        this.running = false;
        System.out.println("Ticker created");
    }
    
    public void addListener(TickListener listener) {
        listeners.add(listener);
        System.out.println("Added tick listener: " + listener.getClass().getSimpleName() + " (total listeners: " + listeners.size() + ")");
    }
    
    public void removeListener(TickListener listener) {
        listeners.remove(listener);
        System.out.println("Removed tick listener: " + listener.getClass().getSimpleName() + " (total listeners: " + listeners.size() + ")");
    }
    
    public void start(int ticksPerSecond) {
        if (running) {
            System.out.println("Ticker already running, ignoring start request");
            return;
        }
        
        System.out.println("Starting Ticker at " + ticksPerSecond + " ticks per second");
        
        // Create new executor
        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "GameTicker");
            t.setDaemon(true); // Don't prevent JVM shutdown
            return t;
        });
        
        running = true;
        lastTickTime = System.nanoTime();
        tickCount = 0;
        
        long periodMs = 1000L / ticksPerSecond; // milliseconds
        System.out.println("Ticker period: " + periodMs + " ms");
        
        executor.scheduleAtFixedRate(() -> {
            try {
                long currentTime = System.nanoTime();
                double deltaTime = (currentTime - lastTickTime) / 1_000_000_000.0;
                lastTickTime = currentTime;
                tickCount++;
                
                // Debug output every 60 ticks (1 second at 60 FPS)
                if (tickCount % 60 == 0) {
                    System.out.println("Ticker: Tick #" + tickCount + ", calling " + listeners.size() + " listeners with deltaTime=" + String.format("%.4f", deltaTime));
                }
                
                // Call all listeners
                for (TickListener listener : listeners) {
                    try {
                        listener.onTick(deltaTime);
                    } catch (Exception e) {
                        System.err.println("Error in tick listener " + listener.getClass().getSimpleName() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                System.err.println("Error in ticker main loop: " + e.getMessage());
                e.printStackTrace();
            }
        }, 0, periodMs, TimeUnit.MILLISECONDS);
        
        System.out.println("Ticker started successfully");
    }
    
    public void stop() {
        System.out.println("Stopping Ticker");
        running = false;
        if (executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Ticker stopped");
    }
    
    public boolean isRunning() {
        return running && executor != null && !executor.isShutdown();
    }
} 