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

import triageapi.model.Dump;
import triageapi.model.Extract;
import triageapi.model.NetworkReport;
import triageapi.model.ReportAnalysisInfo;
import triageapi.model.ReportTaskFailure;
import triageapi.model.Signature;
import triageapi.model.TargetDesc;
import triageapi.model.TriageReport;
import org.json.JSONObject;

/**
 * This parser is used to parse JSON, in the form of a string, into a Triage
 * Report object
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 */
public class TriageReportParser extends GenericParser {

    /**
     * Converts the given JSON value in string form into an object. Missing
     * values are set to empty values (or false for booleans) but never null. As
     * such, every field in the returned field can be accessed safely. Each
     * object contains a boolean that is called <code>isEmpty</em>, which is set
     * to true if an object is completely empty.
     *
     * @param rawJson the JSON value to parse
     * @param taskId the ID of the task
     * @return the object based on the given JSON value
     */
    public TriageReport parse(String rawJson, String taskId) {
        if (rawJson == null) {
            return new TriageReport();
        }
        JSONObject json = new JSONObject(rawJson);

        String version = json.optString("version");
        TargetDesc sample = getTargetDesc(json.optJSONObject("sample"));
        TargetDesc task = getTargetDesc(json.optJSONObject("task"));
        ReportTaskFailure[] errors = optReportTaskFailureArray(json.optJSONArray("errors"));
        ReportAnalysisInfo analysis = getReportAnalysisInfo(json.optJSONObject("analysis"));
        triageapi.model.Process[] processes = optProcessArray(json.optJSONArray("processes"));
        Signature[] signatures = optSignatureArray(json.optJSONArray("signatures"));
        NetworkReport networkReport = getNetworkReport(json.optJSONObject("network"));
        //debug is skipped
        Dump[] dumped = optDumpArray(json.optJSONArray("dumped"));
        Extract[] extracted = optExtractArray(json.optJSONArray("extracted"));
        return new TriageReport(version, taskId, sample, task, errors, analysis, processes, signatures, networkReport, dumped, extracted);
    }
}
