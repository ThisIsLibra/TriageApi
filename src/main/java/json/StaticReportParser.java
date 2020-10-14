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

import exception.EmptyArgumentException;
import model.File;
import model.SampleWrapper;
import model.StaticReport;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This parser is used to parse JSON, in the form of a string, into a a static
 * report object
 *
 * @author Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
 */
public class StaticReportParser extends GenericParser {

    /**
     * Converts the given JSON value in string form into an object. Missing
     * values are set to empty values (or false for booleans) but never null. As
     * such, every field in the returned field can be accessed safely. Each
     * object contains a boolean that is called <code>isEmpty</em>, which is set
     * to true if an object is completely empty.
     *
     * @param rawJson the JSON value to parse
     * @return the object based on the given JSON value
     * @throws EmptyArgumentException if the JSON value is null
     */
    public StaticReport parse(String rawJson) throws EmptyArgumentException {
        if (rawJson == null) {
            throw new EmptyArgumentException("The given JSON blob is null!");
        }
        JSONObject json = new JSONObject(rawJson);

        String version = json.optString("version");
        SampleWrapper sampleWrapper = getSampleWrapper(json.optJSONObject("sample"));
        String target = sampleWrapper.getTarget();
        File[] files = optFileArray(json.getJSONArray("files"));
        int unpackCount = json.optInt("unpack_count");
        int errorCount = json.optInt("error_count");
        return new StaticReport(version, sampleWrapper, target, files, unpackCount, errorCount);
    }

    private SampleWrapper getSampleWrapper(JSONObject json) {
        if (json == null) {
            return new SampleWrapper();
        }

        String sample = json.optString("sample");
        String kind = json.optString("kind");
        int size = json.optInt("size");
        String target = json.optString("target");
        return new SampleWrapper(sample, kind, size, target);
    }

    private File[] optFileArray(JSONArray input) {
        if (input == null) {
            return new File[0];
        }
        File[] output = new File[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getFile(input.getJSONObject(i));
        }
        return output;
    }

    private File getFile(JSONObject json) {
        if (json == null) {
            return new File();
        }
        String fileName = json.optString("filename");
        int fileSize = json.optInt("filesize");
        String md5 = json.optString("md5");
        String sha1 = json.optString("sha1");
        String sha256 = json.optString("sha256");
        String sha512 = json.optString("sha512");
        String[] extensions = optStringArray(json.optJSONArray("exts"));
        String[] tags = optStringArray(json.optJSONArray("tags"));
        int depth = json.optInt("depth");
        String kind = json.optString("kind");
        boolean selected = json.optBoolean("selected");
        String runas = json.optString("runas");
        return new File(fileName, fileSize, md5, sha1, sha256, sha512, extensions, tags, depth, kind, selected, runas);
    }

}
