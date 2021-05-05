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

import java.util.List;

/**
 *
 * @author Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
 */
public class SearchResultEntry {
    private String id;
    private List<String> tasks;
    private String kind;
    private String filename;
    private boolean isPrivate;
    private String submitted;
    private String completed;
    private boolean isEmpty;

    public SearchResultEntry(String id, List<String> tasks, String kind, String filename, boolean isPrivate, String submitted, String completed) {
        this.id = id;
        this.tasks = tasks;
        this.kind = kind;
        this.filename = filename;
        this.isPrivate = isPrivate;
        this.submitted = submitted;
        this.completed = completed;
        isEmpty = false;
    }

    public String getId() {
        return id;
    }
    public List<String> getTasks() {
        return tasks;
    }

    public String getKind() {
        return kind;
    }

    public String getFilename() {
        return filename;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getSubmitted() {
        return submitted;
    }

    public String getCompleted() {
        return completed;
    }
    
    public boolean isEmpty() {
        return isEmpty;
    }
}
