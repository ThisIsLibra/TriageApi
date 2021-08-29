/*
 * Copyright (C) 2020 Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
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
public class Extract {

    private String dumpedFile;
    private String resource;
    private Config config;
    private String path;
    private Ransom ransomNote;
    private Dropper dropper;
    private boolean empty;

    public Extract() {
        empty = true;
        dumpedFile = "";
        resource = "";
        config = new Config();
        path = "";
        ransomNote = new Ransom();
        dropper = new Dropper();
    }

    public Extract(String dumpedFile, String resource, Config config, String path, Ransom ransomNote, Dropper dropper) {
        empty = false;
        this.dumpedFile = dumpedFile;
        this.resource = resource;
        this.config = config;
        this.path = path;
        this.ransomNote = ransomNote;
        this.dropper = dropper;
    }

    public boolean isEmpty() {
        return empty;
    }

    public String getDumpedFile() {
        return dumpedFile;
    }

    public void setDumpedFile(String dumpedFile) {
        this.dumpedFile = dumpedFile;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Ransom getRansomNote() {
        return ransomNote;
    }

    public void setRansomNote(Ransom ransomNote) {
        this.ransomNote = ransomNote;
    }

    public Dropper getDropper() {
        return dropper;
    }

    public void setDropper(Dropper dropper) {
        this.dropper = dropper;
    }

}
