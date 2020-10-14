/*
 * Copyright (C) 2020 Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
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
package model;

/**
 *
 * @author Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
 */
public class StaticReport {

    private String version;
    private SampleWrapper sampleWrapper;
    private String target;
    private File[] files;
    private int unpackCount;
    private int errorCount;
    private boolean empty;

    public StaticReport() {
        empty = true;
        version = "";
        sampleWrapper = new SampleWrapper();
        target = "";
        files = new File[0];
        unpackCount = -1;
        errorCount = -1;
    }

    public StaticReport(String version, SampleWrapper sampleWrapper, String target, File[] files, int unpackCount, int errorCount) {
        empty = false;
        this.version = version;
        this.sampleWrapper = sampleWrapper;
        this.target = target;
        this.files = files;
        this.unpackCount = unpackCount;
        this.errorCount = errorCount;
    }

    public boolean isEmpty() {
        return empty;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public SampleWrapper getSampleWrapper() {
        return sampleWrapper;
    }

    public void setSampleWrapper(SampleWrapper sampleWrapper) {
        this.sampleWrapper = sampleWrapper;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public File[] getFiles() {
        return files;
    }

    public void setFiles(File[] files) {
        this.files = files;
    }

    public int getUnpackCount() {
        return unpackCount;
    }

    public void setUnpackCount(int unpackCount) {
        this.unpackCount = unpackCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

}
