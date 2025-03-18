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
import oop.graphics.Color;
import oop.graphics.Font;

public class TextLine {
	private StringBuilder text;
    private int cursorPosition;
    private int mouseX, mouseY;
    private boolean cursorVisible;
    private long lastCursorBlink;
    private Listener listener;
    private Canvas canvas;
    private static final int BLINK_INTERVAL = 500;
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
    this.listener=l; }

  public TextLine(Canvas canvas) {
	  this.canvas=canvas;
	  this.text=new StringBuilder();
	  this.cursorPosition=0;
	  this.cursorVisible=true;
	  this.lastCursorBlink=System.currentTimeMillis();
	  this.mouseX=10;
	  this.mouseY=20;
	  
    canvas.set(new PaintListener());
    canvas.set(new MouseListener());
    canvas.set(new KeyListener());
    canvas.repaint();
    
  }
  
  private void updateCursorBlink() {
      long currentTime = System.currentTimeMillis();
      if (currentTime - lastCursorBlink >= BLINK_INTERVAL) {
          cursorVisible = !cursorVisible;
          lastCursorBlink = currentTime;
          canvas.repaint();
      }
  }

  class KeyListener implements Canvas.KeyListener {

    @Override
    public void pressed(Canvas canvas, int keyCode, char keyChar) {
      switch(keyCode) {
      case 37:
    	  if(cursorPosition>0) {
    		  cursorPosition--;
    		  canvas.repaint();
    	  }
    	  break;
      case 39:
    	  if(cursorPosition<text.length()) {
    		  cursorPosition++;
    		  canvas.repaint();
    	  }  
    	  break;
      case 127:
    	  if (cursorPosition < text.length()) {
              char deleted = text.charAt(cursorPosition);
              text.deleteCharAt(cursorPosition);
              if (listener != null) {
                  listener.deleted(cursorPosition, deleted);
              }
              canvas.repaint();
    	  }
              break;
    	  case 8: // VK_BACK_SPACE
              if (cursorPosition > 0) {
                  cursorPosition--;
                  char deleted = text.charAt(cursorPosition);
                  text.deleteCharAt(cursorPosition);
                  if (listener != null) {
                      listener.deleted(cursorPosition, deleted);
                  }
                  canvas.repaint();
              }
              break;
    	  case 10:
    		  if (listener != null) {
    			  listener.validated(text.toString());
    		  }
    		  text.setLength(0);
    		  cursorPosition =0;
    		  canvas.repaint();
    		  break;
      }
      
          
      
    }

    @Override
    public void released(Canvas canvas, int keyCode, char keyChar) {
      
    }

    @Override
    public void typed(Canvas canvas, char keyChar) {
    	{
            if (keyChar >= 32 && keyChar < 127) { // Printable ASCII characters
                text.insert(cursorPosition, keyChar);
                if (listener != null) {
                    listener.inserted(cursorPosition, keyChar);
                }
                cursorPosition++;
                canvas.repaint();
            }
        }
    }

  }

  class MouseListener implements Canvas.MouseListener {

    @Override
    public void moved(Canvas canvas, int x, int y) {
    	mouseX = x;
        mouseY = y;
        canvas.repaint();
    }

    @Override
    public void pressed(Canvas canvas, int bno, int x, int y) {
      
    }

    @Override
    public void released(Canvas canvas, int bno, int x, int y) {
      
    }
  }

  class PaintListener implements Canvas.PaintListener {

    @Override
    public void paint(Canvas canvas, Graphics g) {
    	updateCursorBlink();
        
        // Set background to black
        g.setColor(g.getColor(255, 0, 0, 0)); // Black
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Draw text in white
        g.setColor(g.getColor(255, 255, 255, 255)); // White
        String displayText = text.toString();
        g.drawString(displayText, mouseX, mouseY);
        
        // Draw cursor
        if (cursorVisible) {
            String beforeCursor = displayText.substring(0, cursorPosition);
            int cursorX = mouseX + g.getFont().getWidth(beforeCursor);
            Font font = g.getFont();
            g.drawLine(cursorX, 
                      mouseY - font.getAscent(),
                      cursorX, 
                      mouseY + font.getDescent());
        }
    }

    @Override
    public void visible(Canvas canvas) {
      canvas.repaint();
    }

    @Override
    public void revoked(Canvas canvas) {
      
    }
  }
}
