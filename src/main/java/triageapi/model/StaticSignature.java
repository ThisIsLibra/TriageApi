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

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
 */
public class StaticSignature {

    private String name;
    private int score;
    private Set<String> tags;
    private boolean empty;

    public StaticSignature() {
        name = "";
        score = -1;
        tags = new HashSet<>();
        empty = true;
    }

    public StaticSignature(String name, int score, Set<String> tags) {
        this.name = name;
        this.score = score;
        this.tags = tags;
        empty = false;
    }

    public String getName() {
        return name;
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
