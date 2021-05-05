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

import java.util.ArrayList;
import java.util.List;
import triageapi.model.SearchResult;
import triageapi.model.SearchResultEntry;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
 */
public class SearchResultParser extends GenericParser {

    public SearchResult parse(String json) {
        JSONObject jsonObject = new JSONObject(json);
        String nextOffset = jsonObject.optString("next");
        List<SearchResultEntry> searchResults = parseEntries(jsonObject.optJSONArray("data"));
        return new SearchResult(searchResults, nextOffset);
    }

    private List<SearchResultEntry> parseEntries(JSONArray jsonArray) {
        List<SearchResultEntry> results = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.optJSONObject(i);
            String id = object.optString("id");
            String kind = object.optString("kind");
            String filename = object.optString("filename");
            boolean isPrivate = object.optBoolean("private");
            String submitted = object.optString("submitted");
            String completed = object.optString("completed");
            JSONArray taskArray = object.optJSONArray("tasks");
            List<String> tasks = new ArrayList<>();
            for (int taskCount = 0; taskCount < taskArray.length(); taskCount++) {
                JSONObject taskObject = taskArray.optJSONObject(taskCount);
                String task = taskObject.optString("id");
                if (tasks.contains(task) == false) {
                    tasks.add(task);
                }
            }
            results.add(new SearchResultEntry(id, tasks, kind, filename, isPrivate, submitted, completed));
        }
        return results;
    }
}
