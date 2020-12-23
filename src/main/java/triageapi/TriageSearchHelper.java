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
package triageapi;

/**
 * This class is meant to contain helper functions when searching the platform
 * via the TriageApi class. This class is not to be used alone. As such, the
 * class contains no public functions, only protected and private functions.
 * This is a work in progress.
 *
 * @author Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
 */
public class TriageSearchHelper {

    /**
     * The base of the query URL
     */
    private String base;

    /**
     * This helper class is used within the TriageAPI class to further structure
     * search queries.
     */
    protected TriageSearchHelper() {
        base = "search?query=";
    }

    protected String custom(String input) {
        return base + input;
    }

    protected String family(String family) {
        return base + "family:" + family;
    }
}
