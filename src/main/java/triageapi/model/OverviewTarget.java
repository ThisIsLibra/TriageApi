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
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 */
public class OverviewTarget extends TargetDesc {

    private String[] tasks;
    private String[] tags;
    private String[] families;
    private Signature[] signatures;
    private OverviewIOCs overviewIOCs;
    private boolean empty;

    public OverviewTarget() {
        super();
        tasks = new String[0];
        tags = new String[0];
        families = new String[0];
        signatures = new Signature[0];
        overviewIOCs = new OverviewIOCs();
        empty = true;
    }

    public OverviewTarget(TargetDesc targetDesc, String[] tasks, String[] tags, String[] families, Signature[] signatures, OverviewIOCs overviewIOCs) {
        super(targetDesc.getId(), targetDesc.getScore(), targetDesc.getSubmitted(), targetDesc.getCompatCompleted(), targetDesc.getTarget(), targetDesc.getPick(), targetDesc.getType(), targetDesc.getSize(), targetDesc.getMd5(), targetDesc.getSha1(), targetDesc.getSha256(), targetDesc.getSsdeep(), targetDesc.getSha512(), targetDesc.getFileType(), targetDesc.getStaticTags(), targetDesc.getCompatFamily());
        this.tasks = tasks;
        this.tags = tags;
        this.families = families;
        this.signatures = signatures;
        this.overviewIOCs = overviewIOCs;
        empty = false;
    }

    public String[] getTasks() {
        return tasks;
    }

    public void setTasks(String[] tasks) {
        this.tasks = tasks;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String[] getFamilies() {
        return families;
    }

    public void setFamilies(String[] families) {
        this.families = families;
    }

    public Signature[] getSignatures() {
        return signatures;
    }

    public void setSignatures(Signature[] signatures) {
        this.signatures = signatures;
    }

    public OverviewIOCs getOverviewIOCs() {
        return overviewIOCs;
    }

    public void setOverviewIOCs(OverviewIOCs overviewIOCs) {
        this.overviewIOCs = overviewIOCs;
    }

    public boolean isEmpty() {
        return empty;
    }
}
