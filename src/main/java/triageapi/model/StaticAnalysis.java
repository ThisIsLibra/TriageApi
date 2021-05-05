/*
 * Copyright (C) 2021 Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
 */
public class StaticAnalysis {

    private String reported;
    private int score;
    private Set<String> tags;
    private boolean empty;

    public StaticAnalysis() {
        this.reported = ""; //LocalDateTime.ofInstant(Instant.ofEpochSecond(-900000000), ZoneId.systemDefault());
        this.score = -1;
        this.tags = new HashSet<>();
        this.empty = true;
    }

    public StaticAnalysis(String reported, int score, Set<String> tags) {
        this.reported = reported;
        this.score = score;
        this.tags = tags;
        this.empty = false;
    }

    public String getReported() {
        return reported;
    }

    public int getScore() {
        return score;
    }

    public Set<String> getTags() {
        return tags;
    }

    public boolean isEmpty() {
        return empty;
    }

}
