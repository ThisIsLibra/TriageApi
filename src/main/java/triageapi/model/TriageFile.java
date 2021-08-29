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
public class TriageFile {

    private String fileName;
    private int fileSize;
    private String md5;
    private String sha1;
    private String sha256;
    private String sha512;
    private String[] extensions;
    private String[] tags;
    private int depth;
    private String kind;
    private boolean selected;
    private String runAs;
private boolean empty;

    public TriageFile() {
        empty = true;
        fileName = "";
        fileSize = -1;
        md5 = "";
        sha1 = "";
        sha256 = "";
        sha512 = "";
        extensions = new String[0];
        tags = new String[0];
        depth = 0;
        kind = "";
        selected = false;
        runAs = "";
    }

    public TriageFile(String fileName, int fileSize, String md5, String sha1, String sha256, String sha512, String[] extensions, String[] tags, int depth, String kind, boolean selected, String runAs) {
        empty = false;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.md5 = md5;
        this.sha1 = sha1;
        this.sha256 = sha256;
        this.sha512 = sha512;
        this.extensions = extensions;
        this.tags = tags;
        this.depth = depth;
        this.kind = kind;
        this.selected = selected;
        this.runAs = runAs;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getSha512() {
        return sha512;
    }

    public void setSha512(String sha512) {
        this.sha512 = sha512;
    }

    public String[] getExtensions() {
        return extensions;
    }

    public void setExtensions(String[] extensions) {
        this.extensions = extensions;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getRunAs() {
        return runAs;
    }

    public void setRunAs(String runAs) {
        this.runAs = runAs;
    }

}
