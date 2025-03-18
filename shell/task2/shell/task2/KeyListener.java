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
import oop.shell.ITerminal;
import oop.streams.VirtualKeyCodes;

public class KeyListener implements Canvas.KeyListener {

  Canvas m_canvas;
  ITerminal m_term;
  KeyListener(Canvas canvas, ITerminal term) {
    m_canvas = canvas;
    m_term = term;
    canvas.set(this);
  }

  @Override
  public void pressed(Canvas canvas, int keyCode, char keyChar) {
    //System.out.printf("TextLine: pressed=[%d]\n", keyCode);
    if (keyCode == VirtualKeyCodes.VK_LEFT ||
        keyCode == VirtualKeyCodes.VK_KP_LEFT) {
      m_term.left();
    } else if (keyCode == VirtualKeyCodes.VK_RIGHT ||
        keyCode == VirtualKeyCodes.VK_KP_RIGHT) {
      m_term.right();
    } else if (keyCode == VirtualKeyCodes.VK_UP ||
        keyCode == VirtualKeyCodes.VK_KP_UP) {
      m_term.up();
    } else if (keyCode == VirtualKeyCodes.VK_DOWN ||
        keyCode == VirtualKeyCodes.VK_KP_DOWN) {
      m_term.down();
    } else if (keyCode == VirtualKeyCodes.VK_DELETE) {
      m_term.delete();
    } else if (keyCode == VirtualKeyCodes.VK_BACK_SPACE) {
      m_term.backspace();
    } else if (keyCode == VirtualKeyCodes.VK_ENTER) {
      m_term.enter();
    }
  }

  @Override
  public void released(Canvas canvas, int keyCode, char keyChar) {
    //System.out.printf("TextLine: released=[%d]\n", keyCode);
  }

  @Override
  public void typed(Canvas canvas, char keyChar) {
    m_term.insert(keyChar);
  }


}
