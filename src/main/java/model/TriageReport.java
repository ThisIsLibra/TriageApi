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
public class TriageReport {

    private String version;
    private String taskId;
    private TargetDesc sample;
    private TargetDesc task;
    private ReportTaskFailure[] errors;
    private ReportAnalysisInfo analysis;
    private Process[] processes;
    private Signature[] signatures;
    private NetworkReport networkReport;
    private Dump[] dumped;
    private Extract[] extracted;
    private boolean isEmpty;

    public TriageReport() {
        this.version = "";
        this.taskId = "";
        this.sample = new TargetDesc();
        this.task = new TargetDesc();
        this.errors = new ReportTaskFailure[0];
        this.analysis = new ReportAnalysisInfo();
        this.processes = new Process[0];
        this.signatures = new Signature[0];
        this.networkReport = new NetworkReport();
        this.dumped = new Dump[0];
        this.extracted = new Extract[0];
        this.isEmpty = true;
    }

    public TriageReport(String version, String taskId, TargetDesc sample, TargetDesc task, ReportTaskFailure[] errors, ReportAnalysisInfo analysis, Process[] processes, Signature[] signatures, NetworkReport networkReport, Dump[] dumped, Extract[] extracted) {
        this.version = version;
        this.taskId = taskId;
        this.sample = sample;
        this.task = task;
        this.errors = errors;
        this.analysis = analysis;
        this.processes = processes;
        this.signatures = signatures;
        this.networkReport = networkReport;
        this.dumped = dumped;
        this.extracted = extracted;
        this.isEmpty = false;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskid) {
        this.taskId = taskId;
    }

    public TargetDesc getSample() {
        return sample;
    }

    public void setSample(TargetDesc sample) {
        this.sample = sample;
    }

    public TargetDesc getTask() {
        return task;
    }

    public void setTask(TargetDesc task) {
        this.task = task;
    }

    public ReportTaskFailure[] getErrors() {
        return errors;
    }

    public void setErrors(ReportTaskFailure[] errors) {
        this.errors = errors;
    }

    public ReportAnalysisInfo getAnalysis() {
        return analysis;
    }

    public void setAnalysis(ReportAnalysisInfo analysis) {
        this.analysis = analysis;
    }

    public Process[] getProcesses() {
        return processes;
    }

    public void setProcesses(Process[] processes) {
        this.processes = processes;
    }

    public Signature[] getSignatures() {
        return signatures;
    }

    public void setSignatures(Signature[] signatures) {
        this.signatures = signatures;
    }

    public NetworkReport getNetworkReport() {
        return networkReport;
    }

    public void setNetworkReport(NetworkReport networkReport) {
        this.networkReport = networkReport;
    }

    public Dump[] getDumped() {
        return dumped;
    }

    public void setDumped(Dump[] dumped) {
        this.dumped = dumped;
    }

    public Extract[] getExtracted() {
        return extracted;
    }

    public void setExtracted(Extract[] extracted) {
        this.extracted = extracted;
    }

}
