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
package triageapi.model;

/**
 *
 * @author Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
 */
public class Process {

    private int procId;
    private int parentProcId;
    private int pid;
    private int ppid;
    private String cmd;
    private String image;
    private boolean orig;
    private boolean system;
    private int started;
    private int terminated;
    private boolean empty;

    public Process() {
        empty = true;
        procId = -1;
        parentProcId = -1;
        pid = -1;
        ppid = -1;
        cmd = "";
        image = "";
        orig = false;
        system = false;
        started = -1;
        terminated = -1;
    }

    public Process(int procId, int parentProcId, int pid, int ppid, String cmd, String image, boolean orig, boolean system, int started, int terminated) {
        empty = false;
        this.procId = procId;
        this.parentProcId = parentProcId;
        this.pid = pid;
        this.ppid = ppid;
        this.cmd = cmd;
        this.image = image;
        this.orig = orig;
        this.system = system;
        this.started = started;
        this.terminated = terminated;
    }

    public boolean isEmpty() {
        return empty;
    }

    public int getProcId() {
        return procId;
    }

    public void setProcId(int procId) {
        this.procId = procId;
    }

    public int getParentProcId() {
        return parentProcId;
    }

    public void setParentProcId(int parentProcId) {
        this.parentProcId = parentProcId;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPpid() {
        return ppid;
    }

    public void setPpid(int ppid) {
        this.ppid = ppid;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isOrig() {
        return orig;
    }

    public void setOrig(boolean orig) {
        this.orig = orig;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public int getStarted() {
        return started;
    }

    public void setStarted(int started) {
        this.started = started;
    }

    public int getTerminated() {
        return terminated;
    }

    public void setTerminated(int terminated) {
        this.terminated = terminated;
    }

}
