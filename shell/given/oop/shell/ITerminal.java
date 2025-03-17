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

/*
 * A terminal is a character-oriented display, 
 * with a fixed number of columns and rows, 
 * for a given font size.
 * It has the concept of a cursor where it displays 
 * the next printed character. 
 * The row numbers start at zero and are growing downward
 * and the column numbers start at zero and are growing from 
 * left to right.
 */
public interface ITerminal {

  /*
   * Returns the number of columns
   */
  public int ncols();

  /*
   * Returns the number of rows
   */
  public int nrows();

  /*
   * Sets the cursor position.
   */
  public void setCursor(int row, int col);

  /*
   * Returns the current column where the 
   * cursor is.
   */
  public int column();

  /*
   * Returns the current row where the 
   * cursor is.
   */
  public int row();

  /*
   * Moves the cursor left one columns, on the same line.
   * The cursor will stay visible.
   */
  public void left();

  /*
   * Moves the cursor right one columns, on the same line.
   * The cursor will stay visible.
   */
  public void right();

  /*
   * Moves the cursor up one line, on the same column.
   * The cursor will stay visible.
   */
  public void up();

  /*
   * Moves the cursor down one line, on the same column.
   * The cursor will stay visible.
   */
  public void down();

  /*
   * Delete the character under the cursor, shifting left
   * by one column the rest of the line, right of where the
   * cursor is at.
   * Returns the deleted character.
   */
  public void delete();

  /*
   * Delete the character left the cursor, moving the cursor
   * left one column, and shifting left by one column the rest 
   * of the line, right of where the cursor is now at.
   * Returns the deleted character.
   */
  public void backspace();

  /*
   * Clears the entire display and set the cursor position
   * at (0,0), that is, the top-left corner.
   */
  public void clear();

  /*
   * Clears only the given line and set the cursor position
   * at the first column on that line, that is, the column 0. 
   */
  public void clear(int row);

  /*
   * This is the "enter" key. It moves the cursor down a row
   * and left to the first column, potentially scrolling the 
   * display up one row if the cursor is on the last row.
   */
  public void enter();

  /*
   * Insert the given character where the cursor
   * is positioned at on the display, shifting right 
   * the rest of the line and also moving the cursor
   * right.
   */
  public void insert(char c);
  
  /*
   * This is an optional listener to be able to spy 
   * on the terminal activity. 
   */
  public interface Listener {
    // called when this listener is operational 
    // after it has been set on a terminal.
    void available();
    // called when this listener has been revoked,
    // it will never be called again.
    void revoked();

    // the display has been cleared.
    void cleared();

    // the given row has been cleared.
    void cleared(int row);

    // the display has been scrolled up by 
    // the given number of rows.
    void scrolled(int nrows);

    // gives the new cursor position
    void cursorAt(int row, int col);

    // gives the character inserted at the given position
    void inserted(int row, int col, char c);

    // gives the character deleted at the given position
    void deleted(int row, int col, char c);
  }

  /*
   * Sets the unique listener for this terminal.
   * If there is currently a listener, the current 
   * listener is first revoked and the new listener is
   * set up.  
   * Nota bene: the listener must be called back on
   *            the task that was used to set the listener.
   */
  public void set(Listener l);

}
