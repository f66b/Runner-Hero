/*
 *  Copyright (C) Pr. Olivier Gruber <olivier dot gruber at acm dot org>
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package oop.term;

import oop.graphics.Canvas;
import oop.graphics.Graphics;
import oop.shell.ITerminal;

public class Terminal implements ITerminal {

  public Terminal(Canvas canvas, String fontName, int fontSize) {
    throw new RuntimeException("Not Implemented Yet");
  }
  
  /*
   * Sets the cursor at the given coordinates,
   * coordinates given in pixels on the canvas. 
   * To translate (x,y) in (row,column), one 
   * needs to use the font used to display 
   * the characters, because each characters has 
   * its own width, for a given font.
   */
  public void clicked(int x, int y) {
    throw new RuntimeException("Not Implemented Yet");
  }
  
  /*
   * Request this terminal to repaint itself
   * on the given canvas with the given graphics.
   */
  public void paint(Canvas canvas, Graphics g) {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public int ncols() {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public int nrows() {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public void setCursor(int row, int col) {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public int column() {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public int row() {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public void left() {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public void right() {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public void up() {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public void down() {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public void delete() {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public void backspace() {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public void clear() {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public void clear(int row) {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public void enter() {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public void insert(char c) {
    throw new RuntimeException("Not Implemented Yet");
  }

  @Override
  public void set(Listener l) {
    throw new RuntimeException("Not Implemented Yet");
  }

}
