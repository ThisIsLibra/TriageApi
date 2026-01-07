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

import java.util.List;
import triageapi.model.FileUploadResult;
import triageapi.model.Sample;
import triageapi.model.SearchResult;
import triageapi.model.StaticReport;
import triageapi.model.TriageReport;
import org.json.JSONArray;
import triageapi.model.SampleEvents;
import triageapi.model.TriageOverview;

/**
 * This class serves as a wrapper class for the more specific parsers. As such,
 * one can simply create an instance of this object and parse JSON values into
 * objects that can then be used.
 *
 * By keeping the specifics of the parsers in different classes, the code
 * remains clean. Especially when using the GenericParser for functions that are
 * used in multiple parsers.
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 */
public class JsonParser {

    /**
     * The Triage report parser
     */
    private TriageReportParser triageReportParser;

    /**
     * The sample parser
     */
    private SampleParser sampleParser;

    /**
     * The static report parser
     */
    private StaticReportParser staticReportParser;

    /**
     * The file upload result parser
     */
    private FileUploadResultParser fileUploadResultParser;

    /**
     * The parser to convert JSON arrays into lists
     */
    private ListParser listParser;

    /**
     * The parser to convert JSON data into search result objects
     */
    private SearchResultParser searchResultParser;

    /**
     * The parser to convert JSON data into sample event objects
     */
    private SampleEventParser sampleEventParser;

    /**
     * The parser to convert JSON data into a triage overview objects
     */
    private TriageOverviewParser triageOverviewParser;

    /**
     * Creates an instance of this parser, which instantiates all required
     * embedded parsers.
     */
    public JsonParser() {
        triageReportParser = new TriageReportParser();
        sampleParser = new SampleParser();
        staticReportParser = new StaticReportParser();
        fileUploadResultParser = new FileUploadResultParser();
        listParser = new ListParser();
        searchResultParser = new SearchResultParser();
        sampleEventParser = new SampleEventParser();
        triageOverviewParser = new TriageOverviewParser();
    }

    /**
     * Converts the given JSON value in string form into an object. Missing
     * values are set to empty values (or false for booleans) but never null. As
     * such, every field in the returned field can be accessed safely. Each
     * object contains a boolean that is called <code>isEmpty</code>, which is set
     * to true if an object is completely empty.
     *
     * @param json the sample events data in JSON
     * @return the Java object to work with the sample events
     */
    public SampleEvents parseSampleEvents(String json) {
        return sampleEventParser.parse(json);
    }

    /**
     * Converts the given JSON value in string form into an object. Missing
     * values are set to empty values (or false for booleans) but never null. As
     * such, every field in the returned field can be accessed safely. Each
     * object contains a boolean that is called <code>isEmpty</code>, which is set
     * to true if an object is completely empty.
     *
     * @param json the JSON value to parse
     * @return the object based on the given JSON value
     */
    public StaticReport parseStaticReport(String json) {
        return staticReportParser.parse(json);
    }

    /**
     * Converts the given JSON value in string form into an object. Missing
     * values are set to empty values (or false for booleans) but never null. As
     * such, every field in the returned field can be accessed safely. Each
     * object contains a boolean that is called <code>isEmpty</code>, which is set
     * to true if an object is completely empty.
     *
     * @param json the JSON value to parse
     * @param taskId the id of the task
     * @return the object based on the given JSON value
     */
    public TriageReport parseTriageReport(String json, String taskId) {
        return triageReportParser.parse(json, taskId);
    }

    /**
     * Converts the given JSON value in string form into an object. Missing
     * values are set to empty values (or false for booleans) but never null. As
     * such, every field in the returned field can be accessed safely. Each
     * object contains a boolean that is called <code>isEmpty</code>, which is set
     * to true if an object is completely empty.
     *
     * @param json the JSON value to parse
     * @return the object based on the given JSON value
     */
    public List<Sample> parseSamples(String json) {
        return sampleParser.parseBulk(json);
    }

    /**
     * Converts the given JSON value in string form into an object. Missing
     * values are set to empty values (or false for booleans) but never null. As
     * such, every field in the returned field can be accessed safely. Each
     * object contains a boolean that is called <code>isEmpty</code>, which is set
     * to true if an object is completely empty.
     *
     * @param json the JSON value to parse
     * @return the object based on the given JSON value
     */
    public Sample parseSample(String json) {
        return sampleParser.parse(json);
    }

    /**
     * Converts the given JSON value in string form into an object. Missing
     * values are set to empty values (or false for booleans) but never null. As
     * such, every field in the returned field can be accessed safely. Each
     * object contains a boolean that is called <code>isEmpty</code>, which is set
     * to true if an object is completely empty.
     *
     * @param json the JSON value to parse
     * @return the object based on the given JSON value
     */
    public FileUploadResult parseFileUpload(String json) {
        return fileUploadResultParser.parseFileUploadResult(json);
    }

    /**
     * Parses a JSON array into a list of strings
     *
     * @param jsonArray the array to parse
     * @return the list of strings
     */
    public List<String> parseList(JSONArray jsonArray) {
        return listParser.parse(jsonArray);
    }

    /**
     * Parses a JSON string into a SearchResult object
     *
     * @param json the JSON string
     * @return the SearchResult object
     */
    public SearchResult parseSearchResult(String json) {
        return searchResultParser.parse(json);
    }

    /**
     * Parses a JSON string into a TriageOverview object
     *
     * @param json the JSON string
     * @return the TriageOverview object
     */
    public TriageOverview parseTriageOverview(String json) {
        return triageOverviewParser.parse(json);
    }
}
