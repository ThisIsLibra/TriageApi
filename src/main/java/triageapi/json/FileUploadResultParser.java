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

import triageapi.model.FileUploadResult;
import org.json.JSONObject;

/**
 * This parser is used to parse JSON, in the form of a string, into a
 * FileUploadResult object.
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 */
public class FileUploadResultParser {

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
    public FileUploadResult parseFileUploadResult(String rawJson) {
        if (rawJson == null) {
            return new FileUploadResult();
        }
        JSONObject json = new JSONObject(rawJson);
        String id = json.optString("id");
        String status = json.optString("status");
        String kind = json.optString("kind");
        String fileName = json.optString("filename");
        boolean isPrivate = json.optBoolean("private");
        String submitted = json.optString("submitted");
        return new FileUploadResult(id, status, kind, fileName, isPrivate, submitted);
    }
}
