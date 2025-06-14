package game;

import java.awt.Graphics2D;

import engine.Ticker;
import engine.controller.Controller;
import engine.model.Config;
import engine.model.Entity;
import engine.model.Model;
import engine.model.Player;
import engine.view.View;
import oop.graphics.Canvas;
import engine.model.Brain;
import engine.model.Bot;
import game.model.WalkerBot;
import game.model.TrackerBot;

public class Game implements Ticker.Listener {
  private Entity e;
  private Canvas m_canvas;
  private Model m_model;
  private View m_view;
  private Controller m_controller;
  private Ticker m_ticker;
  private final Brain brain;
  private final Bot bot1;
  private final Bot bot2;

  Game(Canvas canvas, int nrows, int ncols) {
    this.m_canvas = canvas;
    
    Config conf = new Config();
    conf.tore = true;
    
    m_model = new Model(nrows, ncols);
    m_model.config(conf); // configure before adding entities
    
    m_view = new View0(canvas, m_model);
    
    brain = new Brain();  // Initialize brain in constructor
    bot1 = new WalkerBot(brain, e);
    bot2 = new TrackerBot(brain, e);
    new Player(m_model, 5, 5, 0);
    
    m_controller = new Controller0(canvas, m_model, m_view);
    
    // Create and start the ticker for game updates
    m_ticker = new Ticker();
    m_ticker.addListener(this); // Listen for ticks directly in Game class
    m_ticker.start(60); // 60 FPS
  }

  public void paint(Canvas canvas, Graphics2D g) {
    m_view.paint(canvas, g);
  }

  @Override
  public void onTick(double deltaTime) {
    // Convert deltaTime from seconds to milliseconds
    int elapsedMs = (int)(deltaTime * 1000);
    
    // Phase 1: Update all entities' stunts
    for (Entity entity : m_model.entities()) {
      if (entity.stunt != null) {
        entity.stunt.tick(elapsedMs);
      }
    }
    
    // Phase 2: Update model physics
    m_model.update(deltaTime);
    
    // Phase 3: Repaint the view
    m_canvas.repaint();
  }
} 