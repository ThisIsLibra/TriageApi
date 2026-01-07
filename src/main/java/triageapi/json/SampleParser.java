/*
 * Copyright (C) 2020 Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
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

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import triageapi.model.Sample;
import triageapi.model.Task;

/**
 * This parser is used to parse JSON, in the form of a string, into a sample
 * object
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 */
public class SampleParser {

    /**
     * Converts the given JSON value in string form into a list of objects.
     * Missing values within each object are set to empty values (or false for
     * booleans) but never null. As such, every field in the returned field can
     * be accessed safely. Each object contains a boolean that is called
     * <code>isEmpty</code>, which is set to true if an object is completely
     * empty.
     *
     * @param rawJson the JSON value to parse
     * @return the object based on the given JSON value
     */
    public List<Sample> parseBulk(String rawJson) {
        List<Sample> samples = new ArrayList<>();
        if (rawJson == null) {
            return samples;
        }

        JSONArray array = new JSONObject(rawJson).optJSONArray("data");
        for (int i = 0; i < array.length(); i++) {
            Sample sample = getSample(array.optJSONObject(i));
            samples.add(sample);
        }
        return samples;
    }

    /**
     * Converts the given JSON value in string form into an object. Missing
     * values are set to empty values (or false for booleans) but never null. As
     * such, every field in the returned field can be accessed safely. Each
     * object contains a boolean that is called <code>isEmpty</code>, which is set
     * to true if an object is completely empty.
     *
     * @param rawJson the JSON value to parse
     * @return the object based on the given JSON value
     */
    public Sample parse(String rawJson) {
        if (rawJson == null) {
            return new Sample();
        }
        JSONObject json = new JSONObject(rawJson);
        return getSample(json);
    }

    private Sample getSample(JSONObject json) {
        if (json == null) {
            return new Sample();
        }
        String id = json.optString("id");
        String status = json.optString("status");
        String kind = json.optString("kind");
        String fileNameOrUrl;
        if (kind.equalsIgnoreCase("file")) {
            fileNameOrUrl = json.optString("filename");
        } else {
            fileNameOrUrl = json.optString("url");
        }
        boolean isPrivate = json.optBoolean("private");
        Task[] tasks = optDumpArray(json.optJSONArray("tasks"));
        String submitted = json.optString("submitted");
        String completed = json.optString("completed");
        return new Sample(id, status, kind, fileNameOrUrl, isPrivate, tasks, submitted, completed);
    }

    private Task[] optDumpArray(JSONArray input) {
        if (input == null) {
            return new Task[0];
        }
        Task[] output = new Task[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getTask(input.getJSONObject(i));
        }
        return output;
    }

    private Task getTask(JSONObject json) {
        if (json == null) {
            return new Task();
        }
        String id = json.optString("id");
        String status = json.optString("status");
        String target = json.optString("target");
        return new Task(id, status, target);
    }
}
