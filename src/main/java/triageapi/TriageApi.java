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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import json.JsonParser;
import model.Dump;
import model.FileUploadResult;
import model.Sample;
import model.SearchResult;
import model.SearchResultEntry;
import model.Signature;
import model.StaticReport;
import model.TargetDesc;
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
 * One can also install this library in the local Maven repository, as is
 * explained here:
 * https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html
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
     * A list that can be used to cache all families, avoiding reloading this
     * full list every time
     */
    private List<String> allFamilies;

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
        this.connector = new Connector(key);
        //The parser only has to be initialised once, which is why it is done in the constructor
        this.parser = new JsonParser();
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
     * Function to URL encode a given string
     *
     * @param toEncode the string to encode
     * @return the encoded string
     */
    private String encode(String toEncode) {
        try {
            return URLEncoder.encode(toEncode, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            System.out.println("ERROR: unable to get the UTF-8 character set!");
            return "";
        }
    }

    /**
     * Gets the yyyy-mm-dd hh:mm format string from the given input
     *
     * @param input the input to scan
     * @return the found date time format if found, or an empty string if no
     * match is found
     */
    private String formatDateTimeString(String input) {
        Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}");
        Matcher matcher = pattern.matcher(input);

        String match = null;

        while (matcher.find()) {
            match = matcher.group(0);
            break;
        }

        if (match != null) {
            return match + "Z";
        } else {
            return "";
        }

    }

    /**
     * Returns a boxed byte array based on the given native byte array. The
     * reason that a boxed array is required, is that native types cannot be
     * used in lists, mappings, nor sets.
     *
     * @param input the byte array to convert into a boxed byte array
     * @return a boxed bye array with the same values as the input array
     */
    private Byte[] box(byte[] input) {
        //Creates a boxed byte array, which is required in the mapping
        Byte[] output = new Byte[input.length];

        //Box all values
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i];
        }

        return output;
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
        return parser.parseTriageReport(json, taskId);
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
     * Downloads the raw malware sample from Triage, based on the given sample
     * ID
     *
     * @param report the TriageReport of the sample that should be downloaded
     * @return a native byte array that contains the raw sample
     * @throws IOException if the HTTP request fails
     */
    public byte[] downloadSample(TriageReport report) throws IOException {
        return downloadSample(report.getSample().getId());
    }

    /**
     * Downloads the raw malware sample from Triage, based on the given
     * TargetDesc. A TargetDesc is either a task or sample within the Triage
     * Report.
     *
     * @param targetDesc the TargetDesc that contains the ID of the sample that
     * should be downloaded
     * @return a native byte array that contains the raw sample
     * @throws IOException if the HTTP request fails
     */
    public byte[] downloadSample(TargetDesc targetDesc) throws IOException {
        return downloadSample(targetDesc.getId());
    }

    /**
     * Downloads the raw malware sample from Triage, based on the given sample
     * object
     *
     * @param sample the sample object that contains the ID of the sample that
     * should be downloaded
     * @return a native byte array that contains the raw sample
     * @throws IOException if the HTTP request fails
     */
    public byte[] downloadSample(Sample sample) throws IOException {
        return downloadSample(sample.getId());
    }

    /**
     * Download samples in bulk, based on the given sample IDs
     *
     * @param sampleIds the sample IDs of the samples to be downloaded
     * @return a mapping with sample IDs as a key, and the sample as a boxed
     * byte array
     * @throws IOException if the HTTP request fails, or if the given list of
     * IDs is null or empty
     */
    public Map<String, Byte[]> downloadSamples(List<String> sampleIds) throws IOException {
        if (sampleIds == null || sampleIds.isEmpty()) {
            throw new IOException("The given list of IDs is null or empty!");
        }
        Map<String, Byte[]> results = new HashMap<>();
        for (String sampleId : sampleIds) {
            //Gets the native byte array from the individual download function
            byte[] bytes = downloadSample(sampleId);

            //Boxes the given byte array
            Byte[] sample = box(bytes);

            //Add the entry to the mapping
            results.put(sampleId, sample);
        }
        return results;
    }

    /**
     * Download samples in bulk, based on the given reports
     *
     * @param reports the reports of the samples to download
     * @return a mapping with sample IDs as a key, and the sample as a boxed
     * byte array
     * @throws IOException if the HTTP request fails, or if the given array of
     * reports is null or empty
     */
    public Map<String, Byte[]> downloadSamples(TriageReport[] reports) throws IOException {
        if (reports == null || reports.length < 1) {
            throw new IOException("The given array of reports is null or empty!");
        }
        List<String> sampleIds = new ArrayList<>();
        for (TriageReport report : reports) {
            sampleIds.add(report.getSample().getId());
        }
        return downloadSamples(sampleIds);

    }

    /**
     * Download samples in bulk, based on the given sample object array
     *
     * @param samples the sample objects of the samples to be downloaded
     * @return a mapping with sample IDs as a key, and the sample as a boxed
     * byte array
     * @throws IOException if the HTTP request fails, or if the given array of
     * reports is null or empty
     */
    public Map<String, Byte[]> downloadSamples(Sample[] samples) throws IOException {
        if (samples == null || samples.length < 1) {
            throw new IOException("The given array of sample objects is null or empty!");
        }
        List<String> sampleIds = new ArrayList<>();
        for (Sample sample : samples) {
            sampleIds.add(sample.getId());
        }
        return downloadSamples(sampleIds);
    }

    /**
     * Download samples in bulk, based on the given TargetDesc object array
     *
     * @param targetDescs the TargetDesc objects of the samples to be downloaded
     * @return a mapping with sample IDs as a key, and the sample as a boxed
     * byte array
     * @throws IOException if the HTTP request fails, or if the given array of
     * reports is null or empty
     */
    public Map<String, Byte[]> downloadSamples(TargetDesc[] targetDescs) throws IOException {
        if (targetDescs == null || targetDescs.length < 1) {
            throw new IOException("The given array of TargetDesc objects is null or empty!");
        }
        List<String> sampleIds = new ArrayList<>();
        for (TargetDesc targetDesc : targetDescs) {
            sampleIds.add(targetDesc.getId());
        }
        return downloadSamples(sampleIds);
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
     * @throws IOException if the HTTP request fails, or if a folder is selected
     * instead of a file
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
     * Uploads all files in the given folder, excluding subdirectories
     *
     * @param folder the folder to upload all files from, excluding
     * subdirectories
     * @return the file upload results for all uploaded files, in a list. This
     * list can be empty if the given folder does not contain any files
     * @throws IOException if the HTTP request fails
     */
    public List<FileUploadResult> uploadFolder(File folder) throws IOException {
        if (folder.isFile()) {
            throw new IOException("The given java.io.File object refers to a file, whilst it should refer to a folder!");
        }

        List<FileUploadResult> results = new ArrayList<>();

        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                FileUploadResult result = uploadSample(file);
                results.add(result);
            }
        }
        return results;
    }

    /**
     * Uploads data to the sandbox: either a single file, or all files in the
     * given folder. This depends on the given java.io.File object's path.
     *
     * @param object the java.io.File object that points to data that should be
     * uploaded to the sandbox
     * @return the file upload results for all uploaded files, in a list. This
     * list can be empty if the given folder does not contain any files. If the
     * given object refers to a file, a list with one item is returned.
     * @throws IOException if the HTTP request fails
     */
    public List<FileUploadResult> upload(File object) throws IOException {
        if (object.isFile()) {
            List<FileUploadResult> results = new ArrayList<>();
            FileUploadResult result = uploadSample(object);
            results.add(result);
            return results;
        } else {
            return uploadFolder(object);
        }
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
        String url = getUrl("search?query=" + encode(query));
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
        String url = getUrl("search?query=" + encode(query) + "&limit=" + limit);
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
        String url = getUrl("search?query=" + encode(query) + "&offset=" + offset);
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
        String url = getUrl("search?query=" + encode(query) + "&offset=" + offset + "&limit=" + limit);
        String json = new String(connector.get(url));
        return parser.parseSearchResult(json);
    }

    /**
     * Searches for the given query in the given cloud (either public or
     * private) and returns a list of SearchResultEntry objects. This list
     * contains all samples that match the given query, and were completed on
     * Triage within the given date ranges.
     *
     * The upper limit of 200 samples per search query is still maintained
     * within this function, as multiple function calls are used to iterate
     * through the sample set of Triage between the two given moments in time.
     *
     * Note that the bigger the timespan between the two moments is, the longer
     * this function takes to execute. As such, one should use this with
     * caution!
     *
     * More information about queries on Triage can be found here:
     * https://hatching.io/blog/tt-2020-10-23/ and https://tria.ge/s/
     *
     * @param query the query to search for
     * @param earliest the earliest moment in time from when samples should be
     * included, if they match the given query
     * @param latest the latest moment in time from when samples should be
     * included, if they match the given query
     * @return a list of search results, which contains all search results. If
     * no results were found, an empty list is returned!
     * @throws IOException if the HTTP request fails, or if the earliest date is
     * later than the system's current date
     */
    public List<SearchResultEntry> search(String query, LocalDateTime earliest, LocalDateTime latest) throws IOException {
        List<SearchResultEntry> searchResults = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'");

        LocalDateTime now = LocalDateTime.now();
        if (earliest.isAfter(now)) {
            throw new IOException("The earliest date is later than the current system time");
        }

        String nextOffset = latest.format(formatter);;
        SearchResult result = null;

        while (true) {
            result = search(query, nextOffset, 200);
            nextOffset = result.getNextOffset();

            if (result.isEmpty()) {
                break;
            }

            for (SearchResultEntry searchResult : result.getSearchResults()) {
                LocalDateTime sampleDate = LocalDateTime.parse(formatDateTimeString(searchResult.getCompleted()), formatter);
                if (sampleDate.isAfter(earliest) && sampleDate.isBefore(latest)) {
                    searchResults.add(searchResult);
                }
            }

            LocalDateTime firstSampleCompletion = LocalDateTime.parse(formatDateTimeString(result.getSearchResults().get(0).getCompleted()), formatter);
            LocalDateTime lastSampleCompletion = LocalDateTime.parse(formatDateTimeString(result.getSearchResults().get(result.getSearchResults().size() - 1).getCompleted()), formatter);

            if (firstSampleCompletion.isBefore(earliest) || firstSampleCompletion.isAfter(latest) || lastSampleCompletion.isBefore(earliest) || lastSampleCompletion.isAfter(latest)) {
                break;
            }

        }

        return searchResults;
    }

    /**
     * Searches for the given query in the given cloud (either public or
     * private) and returns a list of SearchResultEntry objects. This list
     * contains all samples that match the given query, and were completed on
     * Triage between the earliest given moment and the moment this function was
     * called.
     *
     * The upper limit of 200 samples per search query is still maintained
     * within this function, as multiple function calls are used to iterate
     * through the sample set of Triage between the two given moments in time.
     *
     * Note that the bigger the timespan between the two moments is, the longer
     * this function takes to execute. As such, one should use this with
     * caution!
     *
     * More information about queries on Triage can be found here:
     * https://hatching.io/blog/tt-2020-10-23/ and https://tria.ge/s/
     *
     * @param query the query to search for
     * @param earliest the earliest moment in time from when samples should be
     * included, if they match the given query
     * @return a list of search results, which contains all search results. If
     * no results were found, an empty list is returned!
     * @throws IOException if the HTTP request fails, or if the earliest date is
     * later than the system's current date
     */
    public List<SearchResultEntry> search(String query, LocalDateTime earliest) throws IOException {
        return search(query, earliest, LocalDateTime.now());
    }

    /**
     * Gets all associated families that were detected in the sandbox based on
     * the given signatures.
     *
     * The allowCaching boolean defines if the list of all families, which is
     * kept as a global object in this class, can remain cached, or if it needs
     * to be recreated when this function is called. The first time any function
     * is called that uses this list, it is instantiated, regardless of the
     * boolean's value.
     *
     * @param signatures the list of signatures to match
     * @param allowCaching if the global family list may be cached, or not
     * @return a list of families that were detected for the given signatures.
     * This list can be empty if no matches have been found.
     * @throws IOException if the HTTP request fails
     */
    public Set<String> getFamilies(Signature[] signatures, boolean allowCaching) throws IOException {
        Set<String> families = new HashSet<>();

        if (allFamilies == null || (allFamilies != null && allowCaching == false)) {
            allFamilies = getSupportedFamilies();

            for (int i = 0; i < allFamilies.size(); i++) {
                String familyName = allFamilies.get(i);
                familyName = familyName.toLowerCase();
                allFamilies.set(i, familyName);

                if (familyName.contains("_")) {
                    String newName = familyName.replace("_", " ");
                    allFamilies.add(newName);
                }
            }
        }

        for (int i = 0; i < signatures.length; i++) {
            String name = signatures[i].getName().toLowerCase();
            if (allFamilies.contains(name)) {
                families.add(name);
            }
        }
        return families;
    }

    /**
     * Gets all associated families that were detected in the sandbox based on
     * the given report.
     *
     * The allowCaching boolean defines if the list of all families, which is
     * kept as a global object in this class, can remain cached, or if it needs
     * to be recreated when this function is called. The first time any function
     * is called that uses this list, it is instantiated, regardless of the
     * boolean's value.
     *
     * @param report the report to get the families from
     * @param allowCaching if the global family list may be cached, or not
     * @return a list of families that were detected for the given signatures.
     * This list can be empty if no matches have been found.
     * @throws IOException if the HTTP request fails
     */
    public Set<String> getFamilies(TriageReport report, boolean allowCaching) throws IOException {
        Signature[] signatures = report.getSignatures();
        return getFamilies(signatures, allowCaching);
    }

    /**
     * Gets all associated families that were detected in the sandbox based on
     * the given reports.
     *
     * The allowCaching boolean defines if the list of all families, which is
     * kept as a global object in this class, can remain cached, or if it needs
     * to be recreated when this function is called. The first time any function
     * is called that uses this list, it is instantiated, regardless of the
     * boolean's value.
     *
     * @param reports a list of all reports that need to be matched
     * @param allowCaching if the global family list may be cached, or not
     * @return a mapping where the key is each of the given reports, and the
     * value is the resulting set of family matches. The list can be empty if no
     * family matches were found.
     * @throws IOException if the HTTP request fails
     */
    public Map<TriageReport, Set<String>> getFamilies(List<TriageReport> reports, boolean allowCaching) throws IOException {
        Map<TriageReport, Set<String>> mapping = new HashMap<>();

        for (TriageReport report : reports) {
            Set<String> families = getFamilies(report, allowCaching);
            mapping.put(report, families);
        }

        return mapping;
    }

    /**
     * Gets all associated families that were detected in the sandbox based on
     * the given sample and task ID.
     *
     * The allowCaching boolean defines if the list of all families, which is
     * kept as a global object in this class, can remain cached, or if it needs
     * to be recreated when this function is called. The first time any function
     * is called that uses this list, it is instantiated, regardless of the
     * boolean's value.
     *
     * @param sampleId the id of the sample on Triage
     * @param taskId the task to get the report from
     * @param allowCaching if the global family list may be cached, or not
     * @return a list of families that were detected for the given sample. This
     * list can be empty if no matches have been found.
     * @throws IOException if the HTTP request fails
     */
    public Set<String> getFamilies(String sampleId, String taskId, boolean allowCaching) throws IOException {
        TriageReport report = getTriageReport(sampleId, taskId);
        return getFamilies(report, allowCaching);
    }

    /**
     * Gets a dumped section, based on a given sample ID, task ID, and the name
     * of the dumped file
     *
     * @param sampleId the sample's ID
     * @param taskId the task's ID
     * @param dumpName the name of the dumped file, as present in Dump.getName()
     * @return the raw section as a byte array
     * @throws IOException if the HTTP request fails
     */
    public byte[] getDumpedSection(String sampleId, String taskId, String dumpName) throws IOException {
        String url = getUrl("samples/" + sampleId + "/" + taskId + "/" + dumpName);
        return connector.get(url);
    }

    /**
     * Gets all dumped sections for the given report
     *
     * @param report the report to download all dumped sections from
     * @return a mapping where the key is the dumped section's name (as present
     * in Dump.getName()), and the value is the raw section in a boxed byte
     * array
     * @throws IOException if the HTTP request fails
     */
    public Map<String, Byte[]> getDumpedSections(TriageReport report) throws IOException {
        Map<String, Byte[]> mapping = new HashMap<>();

        for (Dump dump : report.getDumped()) {
            byte[] rawDump = getDumpedSection(report.getSample().getId(), report.getTaskId(), dump.getName());

            //Creates a boxed byte array, which is required in the mapping
            Byte[] dumpedSection = box(rawDump);

            mapping.put(dump.getName(), dumpedSection);
        }

        return mapping;
    }

    /**
     * Gets all dumped sections per report, for all given reports
     *
     * @param reports the reports to download all dumped sections from
     * @return a mapping that contains all downloaded sections in a key-value
     * mapping, per report, in a key-value mapping. In other words: a mapping
     * that has reports as keys, with a mapping of all section names and the
     * corresponding sections as a boxed byte array.
     * @throws IOException if the HTTP request fails
     */
    public Map<TriageReport, Map<String, Byte[]>> getDumpedSections(List<TriageReport> reports) throws IOException {
        Map<TriageReport, Map<String, Byte[]>> mapping = new HashMap<>();

        for (TriageReport report : reports) {
            Map<String, Byte[]> dumpedSections = getDumpedSections(report);
            mapping.put(report, dumpedSections);
        }

        return mapping;
    }
}
