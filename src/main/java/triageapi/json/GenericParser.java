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
package triageapi.json;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Converts a given JSONArray object into a list of strings
     *
     * @param input the JSONArray to convert
     * @return the same values in a list
     */
    public List<String> optListString(JSONArray input) {
        if (input == null) {
            return new ArrayList<>();
        }
        List<String> output = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            output.add(input.optString(i));
        }
        return output;
    }

    protected LocalDateTime epochSecondsToLocalDateTime(int epoch) {
        Instant instant;
        if (epoch >= 0) {
            instant = Instant.ofEpochSecond(epoch);
        } else {
            instant = Instant.MIN;
        }
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
