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

public interface OutputStream extends Stream {

  /*
   * This method writes the given byte, 
   * if this stream is available, that is, 
   * if the method available() returns true. 
   * Otherwise, invoking this method throws
   * an illegal-state exception.
   */
  void write(byte bits);

  /*
   * This is a request to write the bytes
   * in the range [offset,offset+length[.
   * This method returns the number of bytes
   * actually written, which may be zero.
   * 
   * Invoking this method when this stream 
   * is unavailable will result in 
   * an illegal-state exception being thrown.
   */
  int write(byte bytes[], int offset, int length);
  
}
