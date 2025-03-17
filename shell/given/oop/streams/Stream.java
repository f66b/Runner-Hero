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
package oop.streams;

/*
 * Each stream supports one listener, allowing
 * to be notified when the stream is closed
 * or when it is available to be operated on.
 * 
 * The method "available():void" on the listener
 * is invoked only if the stream is available
 * to be operated on. For an input stream, this 
 * means there are available bytes for reading.
 * For an output stream, this means there is room
 * to accept written bytes.
 * 
 * Once the method "available():void" has been called
 * on a listener, it will be called again only once 
 * the stream has expressed that it is no longer available,
 * that is, the method "available()" returned false.
 * 
 * Nota Bene:
 *   The task setting the listener is captured
 *   and any listener invocation happens on 
 *   that task.
 */
public interface Stream {
  
  public interface Listener {
    /*
     * This callback indicates that the given stream 
     * can be operated on: reading for an input stream,
     * writing for an output stream.
     */
    void available(Stream s);
    
    /*
     * This callback indicates that the given stream
     * has been closed. A closed stream can no longer
     * be operated on.
     */
    void closed(Stream s);
  }

  /*
   * Sets the listener for this stream. This listener
   * is set once for all.  
   * Nota Bene:
   *   The task setting the listener is captured
   *   and any listener invocation happens on 
   *   that task.
   */
  void set(Listener l);
  
  
  /*
   * This closes the stream. A closed stream can no longer
   * be operated on.
   */
  void close();
  
  /*
   * Returns true if this stream is already closed.
   */
  boolean closed();
  
  /*
   * Returns true if this stream can be operated on:
   * reading for an input stream, writing for 
   * an output stream.
   * Nota Bene: always check the actual available of 
   * a stream before operating on it, even after an
   * available notification has been received. 
   */
  boolean available();
}
