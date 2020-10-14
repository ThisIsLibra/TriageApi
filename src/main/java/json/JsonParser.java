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
import java.util.List;
import model.FileUploadResult;
import model.Sample;
import model.StaticReport;
import model.TriageReport;

/**
 * This class serves as a wrapper class for the more specific parsers. As such,
 * one can simply create an instance of this object and parse JSON values into
 * objects that can then be used.
 *
 * By keeping the specifics of the parsers in different classes, the code
 * remains clean. Especially when using the GenericParser for functions that are
 * used in multiple parsers.
 *
 * @author Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
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
     * Creates an instance of this parser, which instantiates all required
     * embedded parsers.
     */
    public JsonParser() {
        triageReportParser = new TriageReportParser();
        sampleParser = new SampleParser();
        staticReportParser = new StaticReportParser();
        fileUploadResultParser = new FileUploadResultParser();
    }

    /**
     * Converts the given JSON value in string form into an object. Missing
     * values are set to empty values (or false for booleans) but never null. As
     * such, every field in the returned field can be accessed safely. Each
     * object contains a boolean that is called <code>isEmpty</em>, which is set
     * to true if an object is completely empty.
     *
     * @param json the JSON value to parse
     * @return the object based on the given JSON value
     * @throws EmptyArgumentException if the JSON value is null
     */
    public StaticReport parseStaticReport(String json) throws EmptyArgumentException {
        return staticReportParser.parse(json);
    }

    /**
     * Converts the given JSON value in string form into an object. Missing
     * values are set to empty values (or false for booleans) but never null. As
     * such, every field in the returned field can be accessed safely. Each
     * object contains a boolean that is called <code>isEmpty</em>, which is set
     * to true if an object is completely empty.
     *
     * @param json the JSON value to parse
     * @return the object based on the given JSON value
     * @throws EmptyArgumentException if the JSON value is null
     */
    public TriageReport parseTriageReport(String json) throws EmptyArgumentException {
        return triageReportParser.parse(json);
    }

    /**
     * Converts the given JSON value in string form into an object. Missing
     * values are set to empty values (or false for booleans) but never null. As
     * such, every field in the returned field can be accessed safely. Each
     * object contains a boolean that is called <code>isEmpty</em>, which is set
     * to true if an object is completely empty.
     *
     * @param json the JSON value to parse
     * @return the object based on the given JSON value
     * @throws EmptyArgumentException if the JSON value is null
     */
    public List<Sample> parseSamples(String json) throws EmptyArgumentException {
        return sampleParser.parseBulk(json);
    }

    /**
     * Converts the given JSON value in string form into an object. Missing
     * values are set to empty values (or false for booleans) but never null. As
     * such, every field in the returned field can be accessed safely. Each
     * object contains a boolean that is called <code>isEmpty</em>, which is set
     * to true if an object is completely empty.
     *
     * @param json the JSON value to parse
     * @return the object based on the given JSON value
     * @throws EmptyArgumentException if the JSON value is null
     */
    public Sample parseSample(String json) throws EmptyArgumentException {
        return sampleParser.parse(json);
    }

    /**
     * Converts the given JSON value in string form into an object. Missing
     * values are set to empty values (or false for booleans) but never null. As
     * such, every field in the returned field can be accessed safely. Each
     * object contains a boolean that is called <code>isEmpty</em>, which is set
     * to true if an object is completely empty.
     *
     * @param json the JSON value to parse
     * @return the object based on the given JSON value
     * @throws EmptyArgumentException if the JSON value is null
     */
    public FileUploadResult parseFileUpload(String json) throws EmptyArgumentException {
        return fileUploadResultParser.parseFileUploadResult(json);
    }
}
