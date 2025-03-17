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
package oop.shell;

public interface IShell {

  /*
   * Two methods used to drive the input of this
   * shell. Invoking "pressed" for a key must be matched
   * with a corresponding invocation of "released".
   */
  public void pressed(int keyCode,char keyChar);
  public void released(int keyCode,char keyChar);
  
  /*
   * Invoking "typed" provides the last unicode character 
   * typed on the keyboard, but understand that it may not
   * be the last key pressed. Indeed, many characters 
   * require a composition of several key strokes, 
   * like upper-case letters that require a composition
   * of SHIFT and a regular lower-case key stroke.
   */
  public void typed(char keyChar);

  /*
   * Sets the current prompt and returns
   * the previous prompt. 
   */
  public String prompt(String prompt);

  /*
   * Returns the current prompt.
   * The default prompt is "$ ".
   */
  public String prompt();

  /*
   * Returns the history, with the most recent entered
   * command lines in the lowest indices.
   */
  public String[] history();

  /* 
   * Returns the string corresponding to the current command line,
   * as it was typed and edited.
   */
  public String line();
  
  /*
   * Lookups the variable with the given name.
   * Return null if the variable does not exist.
   */
  public String valueOf(String name);
  
  /*
   * Sets the registry of program factories for this shell.
   */
  public void set(IRegistry reg);
  
  /*
   * This is a simple listener to listen to entered command lines.
   * The callback will happen on the task that was used to set the listener.
   */
  public interface Listener {
    void entered(String cmd, String args[]);
  }
  public void set(Listener l);

}
