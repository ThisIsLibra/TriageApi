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
public class Sample {

    private String id;
    private String status;
    private String kind;
    private String fileNameOrUrl;
    private boolean isPrivate;
    private Task[] tasks;
    private String submitted;
    private String completed;
    private boolean empty;

    public Sample() {
        empty = true;
        id = "";
        status = "";
        kind = "";
        fileNameOrUrl = "";
        isPrivate = false;
        tasks = new Task[0];
        submitted = "";
        completed = "";
    }

    public Sample(String id, String status, String kind, String fileNameOrUrl, boolean isPrivate, Task[] tasks, String submitted, String completed) {
        empty = false;
        this.id = id;
        this.status = status;
        this.kind = kind;
        this.fileNameOrUrl = fileNameOrUrl;
        this.isPrivate = isPrivate;
        this.tasks = tasks;
        this.submitted = submitted;
        this.completed = completed;
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

    public String getFileNameOrUrl() {
        return fileNameOrUrl;
    }

    public void setFileNameOrUrl(String fileNameOrUrl) {
        this.fileNameOrUrl = fileNameOrUrl;
    }

    public boolean isIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Task[] getTasks() {
        return tasks;
    }

    public void setTasks(Task[] tasks) {
        this.tasks = tasks;
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

}
