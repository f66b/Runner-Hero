package game;

import java.awt.Graphics2D;

import engine.Ticker;
import engine.controller.Controller;
import engine.model.Config;
import engine.model.Model;
import engine.model.Player;
import engine.view.View;
import oop.graphics.Canvas;

public class Game {
  private Canvas m_canvas;
  private Model m_model;
  private View m_view;
  private Controller m_controller;
  private Ticker m_ticker;

  Game(Canvas canvas, int nrows, int ncols) {
    this.m_canvas = canvas;
    
    Config conf = new Config();
    conf.tore = true;
    
    m_model = new Model(nrows, ncols);
    m_model.config(conf); // configure before adding entities
    
    m_view = new View0(canvas, m_model);
    new Player(m_model, 5, 5, 0);
    
    
    m_controller = new Controller0(canvas, m_model, m_view);
    
    // Create and start the ticker for game updates
    m_ticker = new Ticker();
    m_ticker.addListener(m_controller);
    m_ticker.start(60); // 60 FPS
  }

  public void paint(Canvas canvas, Graphics2D g) {
    m_view.paint(canvas, g);
  }

}
