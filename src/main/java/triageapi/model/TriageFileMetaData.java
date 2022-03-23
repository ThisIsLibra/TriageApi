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
 * This seems to only work for PE files in Triage's back-end. An update will be
 * pushed to this library once this is elaborated on their end.
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 */
public class TriageFileMetaData {

    private String timestamp;
    private TriageFileImport[] imports;
    private TriageFileSection[] sections;
    private TriageFileHeader header;
    private boolean hasAuthenticode;
    private String impHash;
    private boolean empty;

    public TriageFileMetaData() {
        timestamp = "";
        imports = new TriageFileImport[0];
        sections = new TriageFileSection[0];
        header = new TriageFileHeader();
        hasAuthenticode = false;
        impHash = "";
        empty = true;
    }

    public TriageFileMetaData(String timestamp, TriageFileImport[] imports, TriageFileSection[] sections, TriageFileHeader header, boolean hasAuthenticode, String impHash) {
        this.timestamp = timestamp;
        this.imports = imports;
        this.sections = sections;
        this.header = header;
        this.hasAuthenticode = hasAuthenticode;
        this.impHash = impHash;
        this.empty = false;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public TriageFileImport[] getImports() {
        return imports;
    }

    public void setImports(TriageFileImport[] imports) {
        this.imports = imports;
    }

    public TriageFileSection[] getSections() {
        return sections;
    }

    public void setSections(TriageFileSection[] sections) {
        this.sections = sections;
    }

    public TriageFileHeader getHeader() {
        return header;
    }

    public void setHeader(TriageFileHeader header) {
        this.header = header;
    }

    public boolean isHasAuthenticode() {
        return hasAuthenticode;
    }

    public void setHasAuthenticode(boolean hasAuthenticode) {
        this.hasAuthenticode = hasAuthenticode;
    }

    public String getImpHash() {
        return impHash;
    }

    public void setImpHash(String impHash) {
        this.impHash = impHash;
    }

    public boolean isEmpty() {
        return empty;
    }
}
