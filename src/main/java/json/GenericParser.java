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
package json;

import org.json.JSONArray;

/**
 * This abstract class contains functions that are used by other parsers
 *
 * @author Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
 */
public abstract class GenericParser {

    /**
     * Converts a given JSONArray object into a String array with all values
     *
     * @param input the JSONArray to convert
     * @return the string array with all values
     */
    protected String[] optStringArray(JSONArray input) {
        if (input == null) {
            return new String[0];
        }
        String[] output = new String[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = input.optString(i);
        }
        return output;
    }
}
