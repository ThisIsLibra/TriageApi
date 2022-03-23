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
public class OverviewExtracted extends Extract {

    private String[] tasks;
    private boolean empty;

    public OverviewExtracted() {
        super();
        tasks = new String[0];
        empty = true;
    }

    public OverviewExtracted(Extract extract, String[] tasks) {
        super(extract.getDumpedFile(), extract.getResource(), extract.getConfig(), extract.getPath(), extract.getRansomNote(), extract.getDropper(), extract.getCredentials());
        this.tasks = tasks;
        empty = false;
    }

    public String[] getTasks() {
        return tasks;
    }

    public void setTasks(String[] tasks) {
        this.tasks = tasks;
    }

    public boolean isEmpty() {
        return empty;
    }
}
