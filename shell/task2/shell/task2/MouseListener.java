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
package shell.task2;

import oop.graphics.Canvas;
import oop.term.Terminal;

public class MouseListener implements Canvas.MouseListener {
  boolean m_pressed;
  Canvas m_canvas;
  Terminal m_term;
  MouseListener(Canvas canvas, Terminal term) {
    m_canvas = canvas;
    m_term = term;
    canvas.set(this);
  }

  private void clicked(int bno, int x, int y) {
    m_term.clicked(x, y);
  }

  @Override
  public void moved(Canvas canvas, int x, int y) {
  }

  @Override
  public void pressed(Canvas canvas, int bno, int x, int y) {
    m_pressed = true;
  }

  @Override
  public void released(Canvas canvas, int bno, int x, int y) {
    if (m_pressed) {
      clicked(bno, x, y);
      m_pressed = false;
    }
  }


}
