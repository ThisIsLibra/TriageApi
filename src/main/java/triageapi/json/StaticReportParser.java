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

import triageapi.model.TriageFileSection;
import triageapi.model.TriageFileHeader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import triageapi.model.TriageFile;
import triageapi.model.SampleWrapper;
import triageapi.model.StaticReport;
import org.json.JSONArray;
import org.json.JSONObject;
import triageapi.model.StaticAnalysis;
import triageapi.model.StaticSignature;
import triageapi.model.TriageFileImport;
import triageapi.model.TriageFileMetaData;

/**
 * This parser is used to parse JSON, in the form of a string, into a a static
 * report object
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
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
     */
    public StaticReport parse(String rawJson) {
        if (rawJson == null) {
            return new StaticReport();
        }
        JSONObject json = new JSONObject(rawJson);

        String version = json.optString("version");
        SampleWrapper sampleWrapper = getSampleWrapper(json.optJSONObject("sample"));
        String target = sampleWrapper.getTarget();
        TriageFile[] files = optFileArray(json.optJSONArray("files"));
        StaticAnalysis staticAnalysis = getStaticAnalysis(json.optJSONObject("analysis"));
        List<StaticSignature> staticSignatures = getStaticSignatures(json.optJSONArray("signatures"));
        int unpackCount = json.optInt("unpack_count");
        int errorCount = json.optInt("error_count");
        return new StaticReport(version, sampleWrapper, target, files, staticAnalysis, staticSignatures, unpackCount, errorCount);
    }

    private List<StaticSignature> getStaticSignatures(JSONArray json) {
        List<StaticSignature> staticSignatures = new ArrayList<>();
        if (json == null) {
            return staticSignatures;
        }
        for (int i = 0; i < json.length(); i++) {
            JSONObject currentObject = json.optJSONObject(i);
            if (currentObject == null) {
                continue;
            }
            String name = currentObject.optString("name");
            int score = currentObject.optInt("score", -1);

            Set<String> tags = new HashSet<>();

            JSONArray currentArray = currentObject.optJSONArray("tags");
            if (currentArray != null) {
                for (int j = 0; j < currentArray.length(); j++) {
                    String tag = currentArray.optString(i);
                    tags.add(tag);
                }
            }

            currentArray = currentObject.optJSONArray("indicators");
            if (currentArray != null) {
                for (int j = 0; j < currentArray.length(); j++) {
                    JSONObject embeddedObject = currentArray.optJSONObject(i);
                    if (embeddedObject != null) {
                        String tag = embeddedObject.optString("yara_rule");
                        tags.add(tag);
                    }
                }
            }
            staticSignatures.add(new StaticSignature(name, score, tags));
        }
        return staticSignatures;
    }

    private StaticAnalysis getStaticAnalysis(JSONObject json) {
        if (json == null) {
            return new StaticAnalysis();
        }
        String reported = json.optString("reported");
        int score = json.optInt("score", -1);

        Set<String> tags = new HashSet<>();
        JSONArray array = json.optJSONArray("tags");
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                String tag = array.optString(i);
                if (tag.isEmpty() == false) {
                    tags.add(tag);
                }
            }
        }
        return new StaticAnalysis(reported, score, tags);
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

    private TriageFile[] optFileArray(JSONArray input) {
        if (input == null) {
            return new TriageFile[0];
        }
        TriageFile[] output = new TriageFile[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getFile(input.getJSONObject(i));
        }
        return output;
    }

    private TriageFile getFile(JSONObject json) {
        if (json == null) {
            return new TriageFile();
        }
        String fileName = json.optString("filename");
        int fileSize = json.optInt("filesize");
        String md5 = json.optString("md5");
        String sha1 = json.optString("sha1");
        String sha256 = json.optString("sha256");
        String sha512 = json.optString("sha512");
        String ssdeep = json.optString("ssdeep");
        String[] extensions = optStringArray(json.optJSONArray("exts"));
        String[] tags = optStringArray(json.optJSONArray("tags"));
        int depth = json.optInt("depth");
        String kind = json.optString("kind");
        boolean selected = json.optBoolean("selected");
        String runas = json.optString("runas");
        String password = json.optString("password");
        TriageFileMetaData metaData = getTriageFileMetaDeta(json);

        return new TriageFile(fileName, fileSize, md5, sha1, sha256, sha512, ssdeep, extensions, tags, depth, kind, selected, runas, metaData, password);
    }

    protected TriageFileMetaData getTriageFileMetaDeta(JSONObject json) {
        try {
            if (json == null) {
                throw new Exception("Given JSON input is null");
            }

            JSONObject metadata = json.optJSONObject("metadata");
            if (metadata == null) {
                throw new Exception("No metadata present");
            }

            JSONObject pe = metadata.optJSONObject("pe");
            if (pe == null) {
                throw new Exception("No pe field present within the metadata object");
            }

            String timestamp = pe.optString("timestamp");
            TriageFileImport[] imports = optTriageFileImportArray(pe.optJSONArray("imports"));
            TriageFileSection[] sections = optTriageFileSectionArray(pe.optJSONArray("sections"));
            TriageFileHeader header = getTriageFileHeader(json.optJSONObject("header"));

            boolean hasAuthenticode = false;

            JSONObject codeSign = json.optJSONObject("code_sign");
            if (codeSign != null) {
                hasAuthenticode = codeSign.optBoolean("has_authenticode");
            }

            String imphash = pe.optString("imphash");

            return new TriageFileMetaData(timestamp, imports, sections, header, hasAuthenticode, imphash);
        } catch (Exception e) {
            /**
             * Ignore the catch, as it will fall through to the default return
             * below. The return of a valid object is done within the try-catch
             * structure.
             */
        }
        return new TriageFileMetaData();
    }

    private TriageFileHeader getTriageFileHeader(JSONObject json) {
        if (json == null) {
            return new TriageFileHeader();
        }

        int entryPoint = json.optInt("entry_point");
        int magicNumber = json.optInt("magic_number");
        String[] dllCharacteristics = optStringArray(json.optJSONArray("dll_characteristics"));
        String[] fileCharacteristics = optStringArray(json.optJSONArray("file_characteristics"));

        return new TriageFileHeader(entryPoint, magicNumber, dllCharacteristics, fileCharacteristics);
    }

    private TriageFileImport[] optTriageFileImportArray(JSONArray input) {
        if (input == null) {
            return new TriageFileImport[0];
        }
        TriageFileImport[] output = new TriageFileImport[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getTriageFileImport(input.getJSONObject(i));
        }
        return output;
    }

    private TriageFileImport getTriageFileImport(JSONObject json) {
        if (json == null) {
            return new TriageFileImport();
        }

        String dllName = json.optString("dll_name");
        String[] imports = optStringArray(json.optJSONArray("imports"));

        return new TriageFileImport(dllName, imports);
    }

    private TriageFileSection getTriageFileSection(JSONObject json) {
        if (json == null) {
            return new TriageFileSection();
        }

        String name = json.optString("name");
        int offset = json.optInt("offset");
        int virtualSize = json.optInt("virtual_size");
        int size = json.optInt("size");
        String[] characteristics = optStringArray(json.optJSONArray("characteristics"));

        return new TriageFileSection(name, offset, virtualSize, size, characteristics);
    }

    private TriageFileSection[] optTriageFileSectionArray(JSONArray input) {
        if (input == null) {
            return new TriageFileSection[0];
        }
        TriageFileSection[] output = new TriageFileSection[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getTriageFileSection(input.getJSONObject(i));
        }
        return output;
    }

}
