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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;

/**
 * This parser is used to parse JSON, in the form of an org.JSON.JSONArray, into
 * a list of strings
 *
 * @author Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
 */
public class ListParser extends GenericParser {

    /**
     * This function parses a given JSON array into a list of strings
     *
     * @param jsonArray the array to parse
     * @return a list with the same content as the JSON array
     */
    public List<String> parse(JSONArray jsonArray) {
        List<String> output = new ArrayList<>();
        if (jsonArray == null) {
            return output;
        }
        output.addAll(Arrays.asList(optStringArray(jsonArray)));
        return output;
    }
}
