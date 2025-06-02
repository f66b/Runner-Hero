package engine;

import java.awt.Graphics2D;

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

  Game(Canvas canvas, int nrows, int ncols) {
    this.m_canvas = canvas;
    
    Config conf = new Config();
    conf.tore = true;
    
    m_model = new Model(nrows, ncols);
    m_model.config(conf); // configure before adding entities
    
    new Player(m_model, 5, 5, 0);
    
    m_view = new View(canvas, m_model);
    m_controller = new Controller(canvas, m_model, m_view);
    
  }

  public void paint(Canvas canvas, Graphics2D g) {
    m_view.paint(canvas, g);
  }

}
