/*
 * Copyright (C) 2021 Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
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
public class SampleEvents {

    private String id;
    private String status;
    private String kind;
    private String fileName;
    private boolean isPrivate;
    private Event[] sampleEvents;
    private String submitted;
    private String completed;
    private boolean empty;

    public SampleEvents() {
        this.id = "";
        this.status = "";
        this.kind = "";
        this.fileName = "";
        this.isPrivate = false;
        this.sampleEvents = new Event[0];
        this.submitted = "";
        this.completed = "";
        empty = true;
    }

    public SampleEvents(String id, String status, String kind, String fileName, boolean isPrivate, Event[] sampleEvents, String submitted, String completed) {
        this.id = id;
        this.status = status;
        this.kind = kind;
        this.fileName = fileName;
        this.isPrivate = isPrivate;
        this.sampleEvents = sampleEvents;
        this.submitted = submitted;
        this.completed = completed;
        empty = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Event[] getSampleEvents() {
        return sampleEvents;
    }

    public void setSampleEvents(Event[] sampleEvents) {
        this.sampleEvents = sampleEvents;
    }

    public String getSubmitted() {
        return submitted;
    }

    public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public boolean isEmpty() {
        return empty;
    }
}
