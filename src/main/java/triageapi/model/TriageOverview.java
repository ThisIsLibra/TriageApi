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
 * An overview of the given submission, including information from all reports
 * (both dynamic and static)
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 */
public class TriageOverview {

    private String version;
    private OverviewSample sample;
    private TaskSummary[] tasks;
    private OverviewAnalysis analysis;
    private OverviewTarget[] targets;
    private ReportTaskFailure[] errors;
    private Signature[] signatures;
    private OverviewExtracted[] extracted;
    private boolean empty;

    public TriageOverview() {
        empty = true;
        this.version = "";
        this.sample = new OverviewSample();
        this.tasks = new TaskSummary[0];
        this.analysis = new OverviewAnalysis();
        this.targets = new OverviewTarget[0];
        this.errors = new ReportTaskFailure[0];
        this.signatures = new Signature[0];
        this.extracted = new OverviewExtracted[0];
    }

    public TriageOverview(String version, OverviewSample sample, TaskSummary[] tasks, OverviewAnalysis analysis, OverviewTarget[] targets, ReportTaskFailure[] errors, Signature[] signatures, OverviewExtracted[] extracted) {
        empty = false;
        this.version = version;
        this.sample = sample;
        this.tasks = tasks;
        this.analysis = analysis;
        this.targets = targets;
        this.errors = errors;
        this.signatures = signatures;
        this.extracted = extracted;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public OverviewSample getSample() {
        return sample;
    }

    public void setSample(OverviewSample sample) {
        this.sample = sample;
    }

    public TaskSummary[] getTasks() {
        return tasks;
    }

    public void setTasks(TaskSummary[] tasks) {
        this.tasks = tasks;
    }

    public OverviewAnalysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(OverviewAnalysis analysis) {
        this.analysis = analysis;
    }

    public OverviewTarget[] getTargets() {
        return targets;
    }

    public void setTargets(OverviewTarget[] targets) {
        this.targets = targets;
    }

    public ReportTaskFailure[] getErrors() {
        return errors;
    }

    public void setErrors(ReportTaskFailure[] errors) {
        this.errors = errors;
    }

    public Signature[] getSignatures() {
        return signatures;
    }

    public void setSignatures(Signature[] signatures) {
        this.signatures = signatures;
    }

    public OverviewExtracted[] getExtracted() {
        return extracted;
    }

    public void setExtracted(OverviewExtracted[] extracted) {
        this.extracted = extracted;
    }

    public boolean isEmpty() {
        return empty;
    }
}
