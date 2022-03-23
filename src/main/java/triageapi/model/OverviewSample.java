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
public class OverviewSample extends TargetDesc {

    private String created;
    private String completed;
    private OverviewIOCs overviewIOCs;
    private boolean empty;

    public OverviewSample() {
        super();
        empty = true;
        created = "";
        completed = "";
        overviewIOCs = new OverviewIOCs();
    }

    public OverviewSample(TargetDesc targetDesc, String created, String completed, OverviewIOCs overviewIOCs) {
        super(targetDesc.getId(), targetDesc.getScore(), targetDesc.getSubmitted(), targetDesc.getCompatCompleted(), targetDesc.getTarget(), targetDesc.getPick(), targetDesc.getType(), targetDesc.getSize(), targetDesc.getMd5(), targetDesc.getSha1(), targetDesc.getSha256(), targetDesc.getSha512(), targetDesc.getFileType(), targetDesc.getStaticTags(), targetDesc.getCompatFamily());
        empty = false;
        this.created = created;
        this.completed = completed;
        this.overviewIOCs = overviewIOCs;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
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
