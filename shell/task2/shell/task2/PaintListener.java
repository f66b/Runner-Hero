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
import oop.graphics.Graphics;
import oop.runtime.EventPump;
import oop.term.Terminal;

public class PaintListener implements Canvas.PaintListener {
  Canvas m_canvas;
  Terminal m_term;
  PaintListener(Canvas canvas, Terminal term) {
    m_canvas = canvas;
    m_term = term;
    canvas.set(this);
  }
  
  @Override
  public void paint(Canvas canvas, Graphics g) {
    m_term.paint(canvas,g);
  }

  @Override
  public void visible(Canvas canvas) {
    canvas.repaint();
  }

  @Override
  public void revoked(Canvas canvas) {
    EventPump.self().shutdown();
  }


}
