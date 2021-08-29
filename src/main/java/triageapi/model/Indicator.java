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
public class Indicator {

    private String ioc;
    private String description;
    private int at;
    private int sourcePid;
    private int sourceProcId;
    private int targetPid;
    private int targetProcId;
    private int flow;
    private String dumpFile;
    private String resource;
    private String yaraRule;
    private boolean empty;

    public Indicator() {
        empty = true;
        ioc = "";
        description = "";
        at = -1;
        sourcePid = -1;
        sourceProcId = -1;
        targetPid = -1;
        targetProcId = -1;
        flow = -1;
        dumpFile = "";
        resource = "";
        yaraRule = "";
    }

    public Indicator(String ioc, String description, int at, int sourcePid, int sourceProcId, int targetPid, int targetProcId, int flow, String dumpFile, String resource, String yaraRule) {
        empty = false;
        this.ioc = ioc;
        this.description = description;
        this.at = at;
        this.sourcePid = sourcePid;
        this.sourceProcId = sourceProcId;
        this.targetPid = targetPid;
        this.targetProcId = targetProcId;
        this.flow = flow;
        this.dumpFile = dumpFile;
        this.resource = resource;
        this.yaraRule = yaraRule;
    }

    public boolean isEmpty() {
        return empty;
    }

    public String getIoc() {
        return ioc;
    }

    public void setIoc(String ioc) {
        this.ioc = ioc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAt() {
        return at;
    }

    public void setAt(int at) {
        this.at = at;
    }

    public int getSourcePid() {
        return sourcePid;
    }

    public void setSourcePid(int sourcePid) {
        this.sourcePid = sourcePid;
    }

    public int getSourceProcId() {
        return sourceProcId;
    }

    public void setSourceProcId(int sourceProcId) {
        this.sourceProcId = sourceProcId;
    }

    public int getTargetPid() {
        return targetPid;
    }

    public void setTargetPid(int targetPid) {
        this.targetPid = targetPid;
    }

    public int getTargetProcId() {
        return targetProcId;
    }

    public void setTargetProcId(int targetProcId) {
        this.targetProcId = targetProcId;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public String getDumpFile() {
        return dumpFile;
    }

    public void setDumpFile(String dumpFile) {
        this.dumpFile = dumpFile;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getYaraRule() {
        return yaraRule;
    }

    public void setYaraRule(String yaraRule) {
        this.yaraRule = yaraRule;
    }
}
