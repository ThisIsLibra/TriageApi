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
public class Dropper {

    private String family;
    private String language;
    private String source;
    private String deobfuscated;
    private DropperURL[] urls;
    private boolean empty;

    public Dropper() {
        empty = true;
        family = "";
        language = "";
        source = "";
        deobfuscated = "";
        urls = new DropperURL[0];
    }

    public Dropper(String family, String language, String source, String deobfuscated, DropperURL[] urls) {
        empty = false;
        this.family = family;
        this.language = language;
        this.source = source;
        this.deobfuscated = deobfuscated;
        this.urls = urls;
    }

    public boolean isEmpty() {
        return empty;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDeobfuscated() {
        return deobfuscated;
    }

    public void setDeobfuscated(String deobfuscated) {
        this.deobfuscated = deobfuscated;
    }

    public DropperURL[] getUrls() {
        return urls;
    }

    public void setUrls(DropperURL[] urls) {
        this.urls = urls;
    }
}
