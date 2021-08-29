/*
 * Copyright (C) 2021 Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
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

import org.json.JSONArray;
import org.json.JSONObject;
import triageapi.model.Event;
import triageapi.model.SampleEvents;

/**
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 */
public class SampleEventParser {

    public SampleEvents parse(String rawJson) {
        if (rawJson == null) {
            return new SampleEvents();
        }
        JSONObject json = new JSONObject(rawJson);
        String id = json.optString("id");
        String status = json.optString("status");
        String kind = json.optString("kind");
        String filename = json.optString("filename");
        boolean isPrivate = json.optBoolean("private");
        Event[] events = optEventArray(json.optJSONArray("tasks"));
        String submitted = json.optString("submitted");
        String completed = json.optString("completed");
        return new SampleEvents(id, status, kind, filename, isPrivate, events, submitted, completed);
    }

    private Event[] optEventArray(JSONArray input) {
        if (input == null) {
            return new Event[0];
        }
        Event[] output = new Event[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getEvent(input.getJSONObject(i));
        }
        return output;
    }

    private Event getEvent(JSONObject json) {
        if (json == null) {
            return new Event();
        }

        String id = json.optString("id");
        String status = json.optString("status");
        String target = json.optString("target");
        String pick = json.optString("pick");

        return new Event(id, status, target, pick);
    }
}
