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

import java.util.List;

/**
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 */
public class StaticReport {

    private String version;
    private SampleWrapper sampleWrapper;
    private String target;
    private TriageFile[] files;
    private StaticAnalysis staticAnalysis;
    List<StaticSignature> staticSignatures;
    private int unpackCount;
    private int errorCount;
    private boolean empty;

    public StaticReport() {
        empty = true;
        version = "";
        sampleWrapper = new SampleWrapper();
        target = "";
        files = new TriageFile[0];
        staticAnalysis = new StaticAnalysis();
        unpackCount = -1;
        errorCount = -1;
    }

    public StaticReport(String version, SampleWrapper sampleWrapper, String target, TriageFile[] files, StaticAnalysis staticAnalysis, List<StaticSignature> staticSignatures, int unpackCount, int errorCount) {
        empty = false;
        this.version = version;
        this.sampleWrapper = sampleWrapper;
        this.target = target;
        this.files = files;
        this.staticAnalysis = staticAnalysis;
        this.staticSignatures = staticSignatures;
        this.unpackCount = unpackCount;
        this.errorCount = errorCount;
    }

    public String getVersion() {
        return version;
    }

    public SampleWrapper getSampleWrapper() {
        return sampleWrapper;
    }

    public String getTarget() {
        return target;
    }

    public TriageFile[] getFiles() {
        return files;
    }

    public StaticAnalysis getStaticAnalysis() {
        return staticAnalysis;
    }

    public List<StaticSignature> getStaticSignatures() {
        return staticSignatures;
    }

    public int getUnpackCount() {
        return unpackCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public boolean isEmpty() {
        return empty;
    }

}
