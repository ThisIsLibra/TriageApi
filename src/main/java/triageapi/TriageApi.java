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
package triageapi;

import java.io.File;
import java.io.IOException;
import java.util.List;
import json.JsonParser;
import model.FileUploadResult;
import model.Sample;
import model.SearchResult;
import model.StaticReport;
import model.TriageReport;
import network.Connector;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class serves as the single point entry for the Triage API. Using the
 * functions within this class, one can access various API endpoints. These
 * functions will return objects that are within this class, unless a native
 * approach is better fit. An example of a embedded object that is returned, is
 * a TriageReport. This object contains all information regarding a dynamic
 * analysis result. Alternatively, downloading a malware sample from a
 * submission is returned as a byte array, which can then be used in whatever
 * way the user sees fit.
 *
 * Note that each object has a <code>isEmpty</em> boolean, via which one can
 * check if the object is empty or not. This is done to ensure that all fields
 * within the object can be accessed safely, meaning there is no field that can
 * be null.
 *
 * To build a JAR with all dependencies in it:
 *
 * <code>mvn clean compile assembly:single</code>
 *
 * One can also use <code>mvn package</code> to generate JARs with JavaDoc, as
 * well as source code. This will also generate source code in the target
 * folder.
 *
 * @author Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
 */
public class TriageApi {

    /**
     * The base URL, which is either the public or private cloud of Triage
     */
    private final String apiBase;

    /**
     * The connector module, which handles the HTTP requests with Triage's
     * servers
     */
    private final Connector connector;

    /**
     * The JSON parser, which returns objects to the user, instead of plain JSON
     */
    private final JsonParser parser;

    /**
     * Create an instance of the TriageApi class that uses a given API key to
     * connect to Triage's endpoints. One can use a private cloud account or a
     * public account, as is specified by the boolean.
     *
     * @param key the API key to use when connecting with Triage's service
     * @param privateCloud true if the account is used for Triage's private
     * cloud, false if it is using the public cloud
     */
    public TriageApi(String key, boolean privateCloud) {
        //Sets the base of URL, which differs between the public and private cloud
        if (privateCloud) {
            this.apiBase = "https://private.tria.ge/api/v0/";
        } else {
            this.apiBase = "https://api.tria.ge/v0/";
        }

        //The connector needs the API key, as it is needed in a header in each request
        connector = new Connector(key);
        //The parser only has to be initialised once, which is why it is done in the constructor
        parser = new JsonParser();
    }

    /**
     * A non-exposed function that adds an appendix to the set base URL (either
     * Triage's public or private cloud). Note that the API base URL already
     * ends with a forward slash.
     *
     * @param appendix the additional part of the URL to add, without a leading
     * slash
     * @return the full URL to use
     */
    private String getUrl(String appendix) {
        return apiBase + appendix;
    }

    /**
     * Get the Triage report of a specific sample. This is the report of the
     * dynamic execution, if it is finished
     *
     * @param sampleId the sample ID that Triage gave it when the file was
     * uploaded
     * @param taskId the task ID within the sample, this is often
     * <em>behavioral1</em> and <em>behavioral2</em> in the default profile
     * @return the TriageReport object that contains all data
     * @throws IOException if the HTTP request fails
     */
    public TriageReport getTriageReport(String sampleId, String taskId) throws IOException {
        String json = new String(connector.get(getUrl("samples/" + sampleId + "/" + taskId + "/report_triage.json")));
        return parser.parseTriageReport(json);
    }

    /**
     * Gets the static analysis report of the specified sample based on the
     * given sample ID.
     *
     * @param sampleId the sample ID that Triage gave it when the file was
     * uploaded
     * @return the static report of the uploaded sample
     * @throws IOException if the HTTP request fails
     */
    public StaticReport getStaticReport(String sampleId) throws IOException {
        String url = getUrl("samples/" + sampleId + "/reports/static");
        String json = new String(connector.get(url));
        return parser.parseStaticReport(json);
    }

    /**
     * Gets a sample object (not the raw malware sample) based on the given
     * sample ID
     *
     * @param sampleId the sample ID that Triage gave it when the file was
     * uploaded
     * @return the Triage sample object (not the raw malware sample)
     * @throws IOException if the HTTP request fails
     */
    public Sample getSample(String sampleId) throws IOException {
        String url = getUrl("samples/" + sampleId);
        String json = new String(connector.get(url));
        return parser.parseSample(json);
    }

    /**
     * Downloads the raw malware sample from Triage, based on the given sample
     * ID
     *
     * @param sampleId the sample ID that Triage gave it when the file was
     * uploaded
     * @return a native byte array that contains the raw sample
     * @throws IOException if the HTTP request fails
     */
    public byte[] downloadSample(String sampleId) throws IOException {
        String url = getUrl("samples/" + sampleId + "/sample");
        return connector.get(url);
    }

    /**
     * Gets sample objects (meaning not raw samples) from Triage. The given
     * boolean specifies of these samples are only those uploaded from this
     * account, or if they are to be taken from the latest public sample set.
     *
     * @param ownUploadsOnly if the given samples should only be taken from the
     * currently selected account
     * @return a list (an ArrayList as instance) that contains all selected
     * Triage sample objects
     * @throws IOException if the HTTP request fails
     */
    public List<Sample> getSamples(boolean ownUploadsOnly) throws IOException {
        String temp = "samples?subset=";
        if (ownUploadsOnly) {
            temp += "owned";
        } else {
            temp += "public";
        }
        String url = getUrl(temp);
        String json = new String(connector.get(url));
        return parser.parseSamples(json);
    }

    /**
     * Uploads the file at the given path, based on the given Java native file
     * object
     *
     * @param file the file to upload and analyse on Triage
     * @return the file upload result within a single object
     * @throws IOException if the HTTP request fails
     */
    public FileUploadResult uploadSample(File file) throws IOException {
        String url = getUrl("samples");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", file);
        builder.addTextBody("_json", "{\"kind\":\"file\",\"interactive\":false}");

        String json = new String(connector.post(url, builder));
        return parser.parseFileUpload(json);
    }

    /**
     * Gets the status of the sample based on the given sample ID
     *
     * @param sampleId the sample ID that Triage gave it when the file was
     * uploaded
     * @return gets the sample's status. Upon any failure that does not throw an
     * exception, an empty string is returned
     * @throws IOException if the HTTP request fails
     */
    public String getSampleStatus(String sampleId) throws IOException {
        String url = getUrl("samples/" + sampleId + "/status");
        String json = new String(connector.get(url));
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject.optString("status");
    }

    /**
     * The kernel monitor output as a string (which is the raw format) based on
     * the given sample ID and task ID. This function determines the platform
     * based on the tags that are included in the static analysis report, and
     * will subsequently get the matching kernel monitoring output in a string.
     *
     * @param sampleId the sample ID that Triage gave it when the file was
     * uploaded
     * @param taskId the task ID within the sample, this is often
     * <em>behavioral1</em> and <em>behavioral2</em> in the default profile
     * @return the raw kernel monitor output of Triage's dynamic analysis, given
     * as string
     * @throws IOException if the HTTP request fails
     */
    public String getKernelMonitorOutput(String sampleId, String taskId) throws IOException {
        StaticReport staticReport = getStaticReport(sampleId);
        String platform = "";
        for (String tag : staticReport.getFiles()[0].getTags()) {
            platform += tag.toLowerCase() + ";";
        }

        if (platform.contains("windows")) {
            return getOneMonLog(sampleId, taskId);
        } else if (platform.contains("linux")) {
            return getStahpLog(sampleId, taskId);
        }
        throw new IOException("Unable to find a supported platform for this sample!");
    }

    /**
     * The Windows kernel monitor output as a string (which is the raw format)
     * based on the given sample ID and task ID.
     *
     * @param sampleId the sample ID that Triage gave it when the file was
     * uploaded
     * @param taskId the task ID within the sample, this is often
     * <em>behavioral1</em> and <em>behavioral2</em> in the default profile
     * @return the raw Windows kernel monitor output of Triage's dynamic
     * analysis, given as string
     * @throws IOException if the HTTP request fails
     */
    public String getOneMonLog(String sampleId, String taskId) throws IOException {
        String url = getUrl("samples/" + sampleId + "/" + taskId + "/logs/onemon.json");
        String json = new String(connector.get(url));
        return json;
    }

    /**
     * The Linux kernel monitor output as a string (which is the raw format)
     * based on the given sample ID and task ID.
     *
     * @param sampleId the sample ID that Triage gave it when the file was
     * uploaded
     * @param taskId the task ID within the sample, this is often
     * <em>behavioral1</em> and <em>behavioral2</em> in the default profile
     * @return the raw Linux kernel monitor output of Triage's dynamic analysis,
     * given as string
     * @throws IOException if the HTTP request fails
     */
    public String getStahpLog(String sampleId, String taskId) throws IOException {
        String url = getUrl("samples/" + sampleId + "/" + taskId + "/logs/stahp.json");
        String json = new String(connector.get(url));
        return json;
    }

    /**
     * Gets the raw Pcap file (as a byte array) based on the given sample ID and
     * task ID
     *
     * @param sampleId the sample ID that Triage gave it when the file was
     * uploaded
     * @param taskId the task ID within the sample, this is often
     * <em>behavioral1</em> and <em>behavioral2</em> in the default profile
     * @return the raw Pcap as a native byte array
     * @throws IOException if the HTTP request fails
     */
    public byte[] getPcap(String sampleId, String taskId) throws IOException {
        String url = getUrl("samples/" + sampleId + "/" + taskId + "/dump.pcap");
        return connector.get(url);
    }

    /**
     * Gets the raw PcapNg file (as a byte array) based on the given sample ID
     * and task ID
     *
     * @param sampleId the sample ID that Triage gave it when the file was
     * uploaded
     * @param taskId the task ID within the sample, this is often
     * <em>behavioral1</em> and <em>behavioral2</em> in the default profile
     * @return the raw PcapNg as a native byte array
     * @throws IOException if the HTTP request fails
     */
    public byte[] getPcapNg(String sampleId, String taskId) throws IOException {
        String url = getUrl("samples/" + sampleId + "/" + taskId + "/dump.pcapng");
        return connector.get(url);
    }

    /**
     * Provides the given URL to the Triage sandbox.
     *
     * @param url the malicious URL to submit for analysis to Triage
     * @return a FileUploadResult object that contains the relevant fields for
     * this upload
     * @throws IOException if the HTTP request fails
     */
    public FileUploadResult uploadUrl(String url) throws IOException {
        String fullUrl = getUrl("samples");
        String input = "{\"kind\":\"url\", \"url\":\"" + url + "\",\"interactive\":false}";

        String json = new String(connector.post(fullUrl, input));
        return parser.parseFileUpload(json);
    }

    /**
     * Fetches a file from the given URL, which is then executed within the
     * sandbox environment.
     *
     * @param url the malicious URL to submit for analysis to Triage
     * @return a FileUploadResult object that contains the relevant fields for
     * this upload
     * @throws IOException if the HTTP request fails
     */
    public FileUploadResult uploadSampleViaUrl(String url) throws IOException {
        String fullUrl = getUrl("samples");
        String input = "{\"kind\":\"fetch\", \"url\":\"" + url + "\",\"interactive\":false}";

        String json = new String(connector.post(fullUrl, input));
        return parser.parseFileUpload(json);
    }

    /**
     * Gets all the detected malware families as a list of strings
     *
     * @return a list of strings with the supported families
     * @throws IOException if the HTTP request fails
     */
    public List<String> getSupportedFamilies() throws IOException {
        String url = "https://hatching.dev/family.json";
        JSONObject json = new JSONObject(new String(connector.get(url)));
        JSONArray jsonArray = json.optJSONArray("all");
        return parser.parseList(jsonArray);
    }

    /**
     * Gets all the supported extractors for malware families as a list of
     * strings
     *
     * @return a list of strings with the supported families
     * @throws IOException if the HTTP request fails
     */
    public List<String> getSupportedFamillyExtractors() throws IOException {
        String url = "https://hatching.dev/family.json";
        JSONObject json = new JSONObject(new String(connector.get(url)));
        JSONArray jsonArray = json.optJSONArray("extractor");
        return parser.parseList(jsonArray);
    }

    /**
     * Gets all the supported ransomware extractors for malware families as a
     * list of strings
     *
     * @return a list of strings with the supported families
     * @throws IOException if the HTTP request fails
     */
    public List<String> getSupportedRansomwareFamilies() throws IOException {
        String url = "https://hatching.dev/family.json";
        JSONObject json = new JSONObject(new String(connector.get(url)));
        JSONArray jsonArray = json.optJSONArray("ransomware");
        return parser.parseList(jsonArray);
    }

    /**
     * Searches for the given query in the given cloud (either public or
     * private) and returns a SearchResult object. This object contains a list
     * of all results and the search offset field. By default, the Triage API
     * will return a maximum of 50 results. Several of the function overloads
     * accept a maximum value as an argument, where the limit may not exceed
     * 200. The offset field can be used to obtain more search results for the
     * same query, beyond the given limit. The offset can be used in several of
     * the function overloads.
     *
     * More information about queries on Triage can be found here:
     * https://hatching.io/blog/tt-2020-10-23/ and https://tria.ge/s/
     *
     * @param query the query to search for
     * @return the search result object, as described in the documentation above
     * @throws IOException if the HTTP request fails
     */
    public SearchResult search(String query) throws IOException {
        String url = getUrl("search?query=" + query);
        String json = new String(connector.get(url));
        return parser.parseSearchResult(json);
    }

    /**
     * Searches for the given query in the given cloud (either public or
     * private) and returns a SearchResult object. This object contains a list
     * of all results and the search offset field. The maximum amount of results
     * is given as the second argument in this function. The maximum amount for
     * the limit is 200. Any value higher than that will result in the usage of
     * 200 as the limit. The offset field can be used to obtain more search
     * results for the same query, beyond the given limit. The offset can be
     * used in several of the function overloads.
     *
     * More information about queries on Triage can be found here:
     * https://hatching.io/blog/tt-2020-10-23/ and https://tria.ge/s/
     *
     * @param query the query to search for
     * @param limit the maximum amount of search results, with a minimum of 1
     * and a maximum of 200
     * @return the search result object, as described in the documentation above
     * @throws IOException if the HTTP request fails
     */
    public SearchResult search(String query, int limit) throws IOException {
        if (limit < 1) {
            limit = 1;
        } else if (limit > 200) {
            limit = 200;
        }
        String url = getUrl("search?query=" + query + "&limit=" + limit);
        String json = new String(connector.get(url));
        return parser.parseSearchResult(json);
    }

    /**
     * Searches for the given query in the given cloud (either public or
     * private) and returns a SearchResult object. This object contains a list
     * of all results and the search offset field. The offset field can be used
     * to obtain more search results for the same query, beyond the given limit.
     * The offset value is defined in the second argument. By default, the
     * Triage API will return a maximum of 50 results. Several of the function
     * overloads accept a maximum value as an argument.
     *
     * More information about queries on Triage can be found here:
     * https://hatching.io/blog/tt-2020-10-23/ and https://tria.ge/s/
     *
     * @param query the query to search for
     * @param offset the offset of a previous search result object
     * @return the search result object, as described in the documentation above
     * @throws IOException if the HTTP request fails
     */
    public SearchResult search(String query, String offset) throws IOException {
        String url = getUrl("search?query=" + query + "&offset=" + offset);
        String json = new String(connector.get(url));
        return parser.parseSearchResult(json);
    }

    /**
     * Searches for the given query in the given cloud (either public or
     * private) and returns a SearchResult object. This object contains a list
     * of all results and the search offset field. The offset field can be used
     * to obtain more search results for the same query, beyond the given limit.
     * The offset value is defined in the second argument. The maximum amount of
     * results is given as the third argument in this function. The maximum
     * amount for the limit is 200. Any value higher than that will result in
     * the usage of 200 as the limit.
     *
     * More information about queries on Triage can be found here:
     * https://hatching.io/blog/tt-2020-10-23/ and https://tria.ge/s/
     *
     * @param query the query to search for
     * @param offset the offset of a previous search result object
     * @param limit the maximum amount of search results, with a minimum of 1
     * and a maximum of 200
     * @return the search result object, as described in the documentation above
     * @throws IOException if the HTTP request fails
     */
    public SearchResult search(String query, String offset, int limit) throws IOException {
        if (limit < 1) {
            limit = 1;
        } else if (limit > 200) {
            limit = 200;
        }
        String url = getUrl("search?query=" + query + "&offset=" + offset + "&limit=" + limit);
        String json = new String(connector.get(url));
        return parser.parseSearchResult(json);
    }
}
