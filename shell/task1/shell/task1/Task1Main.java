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

import java.awt.Dimension;

import oop.graphics.Canvas;
import oop.runtime.EventPump;
import oop.tasks.Task;

public class Task1Main {

  public static void main(String args[]) {
    EventPump ep = new EventPump();
    Dimension d = new Dimension(640,480);
    Runnable r = new Runnable() {
      public void run() {
        Task task = Task.task();
        Canvas canvas = (Canvas)task.find("canvas");
        new TextLine(canvas);
      }
    };
    ep.boot(d, r);
  }
  
}
