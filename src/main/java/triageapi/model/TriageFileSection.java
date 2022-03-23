/*
 * Copyright (C) 2022 Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package triageapi.model;

/**
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 */
public class TriageFileSection {
    private String name;
    private int offset;
    private int virtualSize;
    private int size;
    private String[] characteristics;
    private boolean empty;

    public TriageFileSection() {
        name = "";
        offset = -1;
        virtualSize = -1;
        size = -1;
        characteristics = new String[0];
        empty = true;
    }

    public TriageFileSection(String name, int offset, int virtualSize, int size, String[] characteristics) {
        this.name = name;
        this.offset = offset;
        this.virtualSize = virtualSize;
        this.size = size;
        this.characteristics = characteristics;
        this.empty = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getVirtualSize() {
        return virtualSize;
    }

    public void setVirtualSize(int virtualSize) {
        this.virtualSize = virtualSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String[] getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String[] characteristics) {
        this.characteristics = characteristics;
    }

    public boolean isEmpty() {
        return empty;
    }
}
