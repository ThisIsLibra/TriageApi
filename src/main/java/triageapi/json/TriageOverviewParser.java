/*
 * Copyright (C) 2022 Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
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

import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import triageapi.model.Credentials;
import triageapi.model.Extract;
import triageapi.model.OverviewAnalysis;
import triageapi.model.OverviewExtracted;
import triageapi.model.OverviewIOCs;
import triageapi.model.OverviewSample;
import triageapi.model.OverviewTarget;
import triageapi.model.ReportTaskFailure;
import triageapi.model.Signature;
import triageapi.model.TargetDesc;
import triageapi.model.TaskSummary;
import triageapi.model.TriageOverview;

/**
 * This parser is used to parse JSON, in the form of a string, into a Triage
 * Overview object
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 */
public class TriageOverviewParser extends GenericParser {

    /**
     * Converts the given JSON value in string form into an object. Missing
     * values are set to empty values (or false for booleans) but never null. As
     * such, every field in the returned field can be accessed safely. Each
     * object contains a boolean that is called <code>empty</code>, which is set
     * to true if an object is completely empty.
     *
     * @param rawJson the JSON value to parse
     * @return the object based on the given JSON value
     */
    public TriageOverview parse(String rawJson) {
        if (rawJson == null) {
            return new TriageOverview();
        }
        JSONObject json = new JSONObject(rawJson);

        String version = json.optString("version");
        OverviewSample sample = getOverviewSample(json.optJSONObject("sample"));
        TaskSummary[] tasks = optTaskSummaryArray(json.optJSONObject("tasks"));
        OverviewAnalysis analysis = getOverviewAnalysis(json.optJSONObject("analysis"));
        OverviewTarget[] targets = optOverviewTargetArray(json.optJSONArray("targets"));
        ReportTaskFailure[] errors = optReportTaskFailureArray(json.optJSONArray("errors"));
        Signature[] signatures = optSignatureArray(json.optJSONArray("signatures"));
        OverviewExtracted[] extracted = optOverviewExtractedArray(json.optJSONArray("extracted"));

        return new TriageOverview(version, sample, tasks, analysis, targets, errors, signatures, extracted);
    }

    protected OverviewExtracted[] optOverviewExtractedArray(JSONArray input) {
        if (input == null) {
            return new OverviewExtracted[0];
        }
        OverviewExtracted[] output = new OverviewExtracted[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getOverviewExtracted(input.optJSONObject(i));
        }
        return output;
    }

    protected OverviewExtracted getOverviewExtracted(JSONObject json) {
        if (json == null) {
            return new OverviewExtracted();
        }

        Extract extract = getExtract(json);
        String[] tasks = optStringArray(json.optJSONArray("tasks"));

        return new OverviewExtracted(extract, tasks);
    }

    protected OverviewTarget[] optOverviewTargetArray(JSONArray input) {
        if (input == null) {
            return new OverviewTarget[0];
        }
        OverviewTarget[] output = new OverviewTarget[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getOverviewTarget(input.optJSONObject(i));
        }
        return output;
    }

    protected OverviewTarget getOverviewTarget(JSONObject json) {
        if (json == null) {
            return new OverviewTarget();
        }

        TargetDesc targetDesc = getTargetDesc(json);
        String[] tasks = optStringArray(json.optJSONArray("tasks"));
        String[] tags = optStringArray(json.optJSONArray("tags"));
        String[] families = optStringArray(json.optJSONArray("family"));
        Signature[] signatures = optSignatureArray(json.optJSONArray("signatures"));
        OverviewIOCs overviewIOCs = getOverviewIOCs(json.optJSONObject("iocs"));

        return new OverviewTarget(targetDesc, tasks, tags, families, signatures, overviewIOCs);
    }

    protected OverviewAnalysis getOverviewAnalysis(JSONObject json) {
        if (json == null) {
            return new OverviewAnalysis();
        }

        int score = json.optInt("score");
        String[] families = optStringArray(json.optJSONArray("family"));
        String[] tags = optStringArray(json.optJSONArray("tags"));

        return new OverviewAnalysis(score, families, tags);
    }

    protected TaskSummary[] optTaskSummaryArray(JSONObject input) {
        if (input == null) {
            return new TaskSummary[0];
        }
        Set<String> keySet = input.keySet();
        TaskSummary[] output = new TaskSummary[keySet.size()];

        int i = 0;
        for (String key : keySet) {
            JSONObject x = input.optJSONObject(key);
            output[i] = getTaskSummary(x);
            i++;
        }

        return output;
    }

    protected TaskSummary getTaskSummary(JSONObject json) {
        if (json == null) {
            return new TaskSummary();
        }

        String sample = json.optString("sample");
        String kind = json.optString("kind");
        String name = json.optString("name");
        String status = json.optString("status");
        String[] ttps = optStringArray(json.optJSONArray("ttp"));
        String[] tags = optStringArray(json.optJSONArray("tags"));
        int score = json.optInt("score");
        String target = json.optString("target");
        String backend = json.optString("backend");
        String resource = json.optString("resource");
        String platform = json.optString("platform");
        String taskName = json.optString("task_name");
        String failure = json.optString("failure");
        int queueId = json.optInt("queue_id");
        String pick = json.optString("pick");

        return new TaskSummary(sample, kind, name, status, ttps, tags, score, target, backend, resource, platform, taskName, failure, queueId, pick);
    }

    protected OverviewSample getOverviewSample(JSONObject json) {
        if (json == null) {
            return new OverviewSample();
        }

        TargetDesc targetDesc = getTargetDesc(json);
        String created = json.optString("created");
        String completed = json.optString("completed");
        OverviewIOCs overviewIOCs = getOverviewIOCs(json.optJSONObject("iocs"));

        return new OverviewSample(targetDesc, created, completed, overviewIOCs);
    }

    protected OverviewIOCs getOverviewIOCs(JSONObject json) {
        if (json == null) {
            return new OverviewIOCs();
        }

        String[] urls = optStringArray(json.optJSONArray("urls"));
        String[] domains = optStringArray(json.optJSONArray("domains"));
        String[] ips = optStringArray(json.optJSONArray("ips"));

        return new OverviewIOCs(urls, domains, ips);
    }

    //Leftover
    protected Credentials[] optCredentialsArray(JSONArray input) {
        if (input == null) {
            return new Credentials[0];
        }
        Credentials[] output = new Credentials[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getCredentials(input.optJSONObject(i));
        }
        return output;
    }

}
