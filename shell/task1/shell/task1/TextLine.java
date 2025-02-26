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
package shell.task1;

import oop.graphics.Canvas;
import oop.graphics.Graphics;

public class TextLine {

  /*
   * Listener to the evolution of this line of text.
   * The methods must be invoked on the task 
   * that set the listener. 
   */
  public interface Listener {
    /*
     * Invoked when the given character is inserted
     * at the given position in the current line 
     * of text. The valid position are within the 
     * range [0:length] with length being the current
     * length of the line of text.
     */
    void inserted(int pos, char c);
    /*
     * Invoked when the given character is inserted
     * at the given position in the current line 
     * of text. The valid position are within the 
     * range [0:length[ with length being the current
     * length of the line of text.
     */
    void deleted(int pos, char c);
    
    /*
     * Invoked when the line of text is validated,
     * which happens when the character '\n'
     * has been typed. The line must not include
     * that character '\n'.
     */
    void validated(String line);
  }

  public void set(Listener l) {
    throw new RuntimeException("NYI");
  }

  public TextLine(Canvas canvas) {
    canvas.set(new PaintListener());
    canvas.set(new MouseListener());
    canvas.set(new KeyListener());
    canvas.repaint();
    throw new RuntimeException("NYI");
  }

  class KeyListener implements Canvas.KeyListener {

    @Override
    public void pressed(Canvas canvas, int keyCode, char keyChar) {
      throw new RuntimeException("NYI");
    }

    @Override
    public void released(Canvas canvas, int keyCode, char keyChar) {
      throw new RuntimeException("NYI");
    }

    @Override
    public void typed(Canvas canvas, char keyChar) {
      throw new RuntimeException("NYI");
    }

  }

  class MouseListener implements Canvas.MouseListener {

    @Override
    public void moved(Canvas canvas, int x, int y) {
      throw new RuntimeException("NYI");
    }

    @Override
    public void pressed(Canvas canvas, int bno, int x, int y) {
      throw new RuntimeException("NYI");
    }

    @Override
    public void released(Canvas canvas, int bno, int x, int y) {
      throw new RuntimeException("NYI");
    }
  }

  class PaintListener implements Canvas.PaintListener {

    @Override
    public void paint(Canvas canvas, Graphics g) {
      throw new RuntimeException("NYI");
    }

    @Override
    public void visible(Canvas canvas) {
      throw new RuntimeException("NYI");
    }

    @Override
    public void revoked(Canvas canvas) {
      throw new RuntimeException("NYI");
    }
  }
}
