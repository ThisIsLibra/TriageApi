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
package triageapi;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import triageapi.json.JsonParser;
import triageapi.model.Dump;
import triageapi.model.FileUploadResult;
import triageapi.model.Sample;
import triageapi.model.SearchResult;
import triageapi.model.SearchResultEntry;
import triageapi.model.Signature;
import triageapi.model.StaticReport;
import triageapi.model.TargetDesc;
import triageapi.model.TriageReport;
import triageapi.network.TriageConnector;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import triageapi.model.SampleEvents;
import triageapi.model.StaticSignature;
import triageapi.model.TriageOverview;

/**
 * This class serves as the single point entry for the Triage API. Using the
 * functions within this class, one can access various API endpoints. These
 * functions will return objects that are within this project, unless a native
 * approach is better fit.<br>
 * <br>
 * An example of a embedded object that is returned, is a
 * <code>TriageReport</code>. This object contains all information regarding a
 * dynamic analysis result. Alternatively, the download of a sample returns a
 * <code>byte[]</code>, which can then be used in whatever way the user sees
 * fit.<br>
 * <br>
 * Note that each embedded object has an <code>isEmpty</code> boolean, via which
 * one can check if the object is empty or not. This is done to ensure that all
 * fields within the object can be accessed safely, meaning there is no field
 * that can be null.<br>
 * <br>
 * To build a JAR with all dependencies in it:<br>
 * <br>
 * <code>mvn clean compile assembly:single</code><br>
 * <br>
 * One can also use <code>mvn package</code> to generate JARs with JavaDoc, as
 * well as source code. This will also generate source code in the target
 * folder.<br>
 * <br>
 * One can also install this library in a local Maven repository, as is
 * explained here:
 * https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
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
    private final TriageConnector connector;

    /**
     * The JSON parser, which returns objects to the user, instead of plain JSON
     */
    private final JsonParser parser;

    /**
     * A string that is used to cache all families, avoiding reloading the full
     * list of names at every request
     */
    private String allFamilies;

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
        this.connector = new TriageConnector(key);
        //The parser only has to be initialised once, which is why it is done in the constructor
        this.parser = new JsonParser();
    }

    /**
     * A private function that adds an appendix to the set base URL (either
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
     * A private function that converts a given LocalDateTime object into an
     * object that represents the exact same moment in time, but in the UTC time
     * zone. Note that this function assumes that the input's time zone is equal
     * to the system's time zone
     *
     * @param input the object to be converted
     * @return the converted object
     */
    private LocalDateTime setToUtc(LocalDateTime input) {
        ZonedDateTime zonedEarliest = input.atZone(ZoneId.systemDefault());
        return zonedEarliest.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
    }

    /**
     * A private function to URL encode a given string
     *
     * @param toEncode the string to encode
     * @return the encoded string, or an empty string if the UTF-8 charset
     * cannot be found
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
     * A private function that ets the yyyy-mm-dd hh:mm format string from the
     * given input
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
     * A private function that returns a boxed byte array, based on the given
     * native byte array. A boxed array is required because native types cannot
     * be used in lists, mappings, nor sets.
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
     * dynamic execution. Note that it is only accessible once the sandbox
     * execution has finished.
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
     * Gets a sample object (<b>not</b> the raw malware sample) based on the
     * given sample ID
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
     * @throws IOException if the HTTP request fails, or if an internal server
     * error occurs
     */
    public byte[] downloadSample(String sampleId) throws IOException {
        String url = getUrl("samples/" + sampleId + "/sample");
        byte[] sample = connector.get(url);
        return sample;
    }

    /**
     * Downloads the raw malware sample from Triage, based on the given sample
     * ID
     *
     * @param report the TriageReport of the sample that should be downloaded
     * @return a native byte array that contains the raw sample
     * @throws IOException if the HTTP request fails, or if an internal server
     * error occurs
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
     * @throws IOException if the HTTP request fails, or if an internal server
     * error occurs
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
     * @throws IOException if the HTTP request fails, or if an internal server
     * error occurs
     */
    public byte[] downloadSample(Sample sample) throws IOException {
        return downloadSample(sample.getId());
    }

    /**
     * Downloads the raw malware sample from Triage, based on the given sample
     * ID
     *
     * @param overview the TriageOverview of the sample that should be
     * downloaded
     * @return a native byte array that contains the raw sample
     * @throws IOException if the HTTP request fails, or if an internal server
     * error occurs
     */
    public byte[] downloadSample(TriageOverview overview) throws IOException {
        return downloadSample(overview.getSample().getId());
    }

    /**
     * Download samples in bulk, based on the given sample IDs. One or more
     * samples might be missing from the returned mapping if exceptions are
     * suppressed. All samples that were downloaded correctly will be
     * returned.<br>
     * <br>
     * If exceptions are not suppressed, all correctly downloaded samples are
     * discarded once an exception is thrown.
     *
     * @param sampleIds the sample IDs of the samples to be downloaded
     * @param suppressExceptions true to ignore exceptions, false to throw any
     * encountered exception to the caller of this function
     * @return a mapping with sample IDs as a key, and the sample as a boxed
     * byte array
     * @throws IOException if the HTTP request fails, if the given list of IDs
     * is null or empty, or if an internal server error occurs
     */
    public Map<String, Byte[]> downloadSamples(List<String> sampleIds, boolean suppressExceptions) throws IOException {
        if (sampleIds == null || sampleIds.isEmpty()) {
            throw new IOException("The given list of IDs is null or empty!");
        }
        Map<String, Byte[]> results = new HashMap<>();
        for (String sampleId : sampleIds) {
            try {
                //Gets the native byte array from the individual download function
                byte[] bytes = downloadSample(sampleId);

                //Boxes the given byte array
                Byte[] sample = box(bytes);

                //Add the entry to the mapping
                results.put(sampleId, sample);
            } catch (IOException ex) {
                //If the function should suppress exceptions, the error is ignored
                if (suppressExceptions == false) {
                    throw ex;
                }
            }
        }
        return results;
    }

    /**
     * Download samples in bulk, based on the given reports. One or more samples
     * might be missing from the returned mapping if the exceptions are
     * suppressed. All samples that were downloaded correctly will be
     * returned.<br>
     * <br>
     * If exceptions are not suppressed, all correctly downloaded samples are
     * discarded once an exception is thrown.
     *
     * @param reports the reports of the samples to download
     * @param suppressExceptions true to ignore exceptions, false to throw any
     * encountered exception to the caller of this function
     * @return a mapping with sample IDs as a key, and the sample as a boxed
     * byte array
     * @throws IOException if the HTTP request fails, if the given array of
     * reports is null or empty, or if an internal server error occurs
     */
    public Map<String, Byte[]> downloadSamples(TriageReport[] reports, boolean suppressExceptions) throws IOException {
        if (reports == null || reports.length < 1) {
            throw new IOException("The given array of reports is null or empty!");
        }
        List<String> sampleIds = new ArrayList<>();
        for (TriageReport report : reports) {
            sampleIds.add(report.getSample().getId());
        }
        return downloadSamples(sampleIds, suppressExceptions);

    }

    /**
     * Download samples in bulk, based on the given sample object array. One or
     * more samples might be missing from the returned mapping if the faulty
     * samples are suppressed. All samples that were downloaded correctly will
     * be returned.<br>
     * <br>
     * If exceptions are not suppressed, all correctly downloaded samples are
     * discarded once an exception is thrown.
     *
     * @param samples the sample objects of the samples to be downloaded
     * @param suppressExceptions true to ignore exceptions, false to throw any
     * encountered exception to the caller of this function
     * @return a mapping with sample IDs as a key, and the sample as a boxed
     * byte array
     * @throws IOException if the HTTP request fails, if the given array of
     * reports is null or empty, or if an internal server error occurs
     */
    public Map<String, Byte[]> downloadSamples(Sample[] samples, boolean suppressExceptions) throws IOException {
        if (samples == null || samples.length < 1) {
            throw new IOException("The given array of sample objects is null or empty!");
        }
        List<String> sampleIds = new ArrayList<>();
        for (Sample sample : samples) {
            sampleIds.add(sample.getId());
        }
        return downloadSamples(sampleIds, suppressExceptions);
    }

    /**
     * Download samples in bulk, based on the given TargetDesc object array. One
     * or more samples might be missing from the returned mapping if the faulty
     * samples are suppressed. All samples that were downloaded correctly will
     * be returned.<br>
     * <br>
     * If the faulty samples are not suppressed, all correctly downloaded
     * samples are discarded once an exception is thrown.
     *
     * @param targetDescs the TargetDesc objects of the samples to be downloaded
     * @param suppressExceptions true to ignore exceptions, false to throw any
     * encountered exception to the caller of this function
     * @return a mapping with sample IDs as a key, and the sample as a boxed
     * byte array
     * @throws IOException if the HTTP request fails, if the given array of
     * reports is null or empty, or if an internal server error occurs
     */
    public Map<String, Byte[]> downloadSamples(TargetDesc[] targetDescs, boolean suppressExceptions) throws IOException {
        if (targetDescs == null || targetDescs.length < 1) {
            throw new IOException("The given array of TargetDesc objects is null or empty!");
        }
        List<String> sampleIds = new ArrayList<>();
        for (TargetDesc targetDesc : targetDescs) {
            sampleIds.add(targetDesc.getId());
        }
        return downloadSamples(sampleIds, suppressExceptions);
    }

    /**
     * Download samples in bulk, based on the given overviews. One or more
     * samples might be missing from the returned mapping if the exceptions are
     * suppressed. All samples that were downloaded correctly will be
     * returned.<br>
     * <br>
     * If exceptions are not suppressed, all correctly downloaded samples are
     * discarded once an exception is thrown.
     *
     * @param overviews the overviews of the samples to download
     * @param suppressExceptions true to ignore exceptions, false to throw any
     * encountered exception to the caller of this function
     * @return a mapping with sample IDs as a key, and the sample as a boxed
     * byte array
     * @throws IOException if the HTTP request fails, if the given array of
     * reports is null or empty, or if an internal server error occurs
     */
    public Map<String, Byte[]> downloadSamples(TriageOverview[] overviews, boolean suppressExceptions) throws IOException {
        if (overviews == null || overviews.length < 1) {
            throw new IOException("The given array of reports is null or empty!");
        }
        List<String> sampleIds = new ArrayList<>();
        for (TriageOverview overview : overviews) {
            sampleIds.add(overview.getSample().getId());
        }
        return downloadSamples(sampleIds, suppressExceptions);

    }

    /**
     * Gets sample objects (meaning not raw samples) from Triage. The given
     * boolean specifies if these samples are only those uploaded from this
     * account, or if they are to be taken from the group's latest
     * submissions.<br>
     * <br>
     * The <em>group</em> refers to all samples on the public environment for
     * the public cloud, or all samples on the institution's private cloud when
     * using a private cloud environment.
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
     * Uploads the file at the given path, based on the given Java file object
     *
     * @param file the file to upload and analyse on Triage
     * @return the file upload result object
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
            throw new IOException("The given java.io.File object refers to a file, whereas it should refer to a folder!");
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
     * given folder. This depends on the given Java File object's path.
     *
     * @param object the file object that points to data that should be uploaded
     * to the sandbox
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
     * Gets the status of the sample at the moment of the request, based on the
     * given sample ID. This function is a wrapper for
     * <code>getSample(sampleId).getStatus()</code>. This function does
     * <b>not</b> wait until the analysis of the sample is finished. If you do
     * want to wait for the analysis to finish, please refer to
     * <code>getSampleEvents</code> and
     * <code>awaitSampleAnalysisCompletion</code>.<br>
     * <br>
     * Prior to version 1.5-stable of this API client, this function pointed to
     * a different endpoint, which has since been deprecated by Triage. As such,
     * this function failed. The current wrapper functionality is offered to
     * provide backwards compatibility, and to improve the ease-of-use of this
     * library.
     *
     * @param sampleId the sample ID that Triage gave it when the file was
     * uploaded
     * @return gets the sample's status. Upon any failure that does not throw an
     * exception, an empty string is returned
     * @throws IOException if the HTTP request fails
     */
    public String getSampleStatus(String sampleId) throws IOException {
        return getSample(sampleId).getStatus();
    }

    /**
     * Gets the statuses for all given sample IDs.This function obtains the
     * <b>current</b> status of each given sample, meaning it will <b>not</b>
     * wait until the sample is complete. Please refer to
     * <code>getSampleEvents</code> and
     * <code>awaitSampleAnalysisCompletion</code> for functions that wait until
     * the sample analysis has finished.
     *
     * @param sampleIds the sample IDs to check
     * @param suppressExceptions true to ignore exceptions, false to throw any
     * encountered exception to the caller of this function
     * @return a mapping where the keys are the given sample IDs, and the value
     * of each key is that status of that sample on the moment it was requested
     * @throws IOException if the HTTP request fails
     */
    public Map<String, String> getSampleStatuses(List<String> sampleIds, boolean suppressExceptions) throws IOException {
        Map<String, String> mapping = new HashMap<>();

        for (String sampleId : sampleIds) {
            try {
                String status = getSampleStatus(sampleId);
                mapping.put(sampleId, status);
            } catch (IOException ex) {
                if (suppressExceptions == false) {
                    throw ex;
                }
            }
        }

        return mapping;
    }

    /**
     * Serving as a wrapper around <code>getSampleEvents</code>, returning the
     * sample events for all given sample IDs.<br>
     * <br>
     * Note that this function only returns once <b>all</b> samples in the list
     * have been analysed by Triage! If a large list is used, it might take a
     * while before this function returns.If you want to know the current status
     * of each sample, please refer to <code>getSampleStatus</code> and
     * <code>getSampleStatuses</code>.
     *
     * @param sampleIds the sample IDs to check for completion
     * @param suppressExceptions true to ignore exceptions, false to throw any
     * encountered exception to the caller of this function
     * @return a mapping where the keys are the given sample IDs, and the value
     * for each key is the returned sample event object
     * @throws IOException if the HTTP request fails
     */
    public Map<String, SampleEvents> awaitSampleAnalysisCompletion(List<String> sampleIds, boolean suppressExceptions) throws IOException {
        Map<String, SampleEvents> mapping = new HashMap<>();

        for (String sampleId : sampleIds) {
            try {
                SampleEvents sampleEvents = getSampleEvents(sampleId);
                mapping.put(sampleId, sampleEvents);
            } catch (IOException ex) {
                if (suppressExceptions == false) {
                    throw ex;
                }
            }
        }

        return mapping;
    }

    /**
     * Gets the sample events for the given sample ID. This function only
     * returns once Triage's analysis of the sample has been completed.
     * Generally this is a few minutes once the upload is complete, but it can
     * take more time when the service is heavily used.<br>
     * <br>
     * For functions that obtain the current status, please refer to
     * <code>getSampleStatus</code> or <code>getSampleStatuses</code>.
     *
     * @param sampleId the ID to fetch the sample events for
     * @return the sample events object for the given sample ID
     * @throws IOException if the HTTP request fails
     */
    public SampleEvents getSampleEvents(String sampleId) throws IOException {
        String json = new String(connector.get(getUrl("samples/" + sampleId + "/events")));
        return parser.parseSampleEvents(json);
    }

    /**
     * The kernel monitor output as a string (which is the raw format) based on
     * the given sample ID and task ID. This function determines the platform
     * based on the tags that are included in the static analysis report, and
     * will subsequently fetch the matching kernel monitoring output.
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
     * @return a FileUploadResult object with the relevant fields for this
     * upload
     * @throws IOException if the HTTP request fails
     */
    public FileUploadResult uploadUrl(String url) throws IOException {
        String fullUrl = getUrl("samples");
        String input = "{\"kind\":\"url\", \"url\":\"" + url + "\",\"interactive\":false}";

        String json = new String(connector.post(fullUrl, input));
        return parser.parseFileUpload(json);
    }

    /**
     * Provides the given URL to the Triage sandbox.
     *
     * @param urls the malicious URLs to submit for analysis to Triage
     * @return a mapping where the keys are the given URLs, and the value for
     * each key is the corresponding <code>FileUploadResult</code> object
     * @throws IOException if the HTTP request fails
     */
    public Map<String, FileUploadResult> uploadUrls(List<String> urls) throws IOException {
        Map<String, FileUploadResult> mapping = new HashMap<>();

        for (String url : urls) {
            FileUploadResult fileUploadResult = uploadUrl(url);
            mapping.put(url, fileUploadResult);
        }

        return mapping;
    }

    /**
     * The file at the URL is fetched and executed within the sandbox
     * environment.
     *
     * @param url the malicious URLs to submit for analysis to Triage
     * @return the corresponding <code>FileUploadResult</code> object
     * @throws IOException if the HTTP request fails
     */
    public FileUploadResult uploadSampleViaUrl(String url) throws IOException {
        String fullUrl = getUrl("samples");
        String input = "{\"kind\":\"fetch\", \"url\":\"" + url + "\",\"interactive\":false}";

        String json = new String(connector.post(fullUrl, input));
        return parser.parseFileUpload(json);
    }

    /**
     * For each given URL, the file at the URL is fetched and executed within
     * the sandbox environment.
     *
     * @param urls the malicious URLs to submit for analysis to Triage
     * @return a mapping where the keys are the given URLs, and the value for
     * each key is the corresponding <code>FileUploadResult</code> object
     * @throws IOException if the HTTP request fails
     */
    public Map<String, FileUploadResult> uploadSamplesViaUrls(List<String> urls) throws IOException {
        Map<String, FileUploadResult> mapping = new HashMap<>();

        for (String url : urls) {
            FileUploadResult result = uploadSampleViaUrl(url);
            mapping.put(url, result);
        }

        return mapping;
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
     * of all results and the search offset field.<br>
     * <br>
     * By default, the Triage API will return a maximum of 50 results. Several
     * of the function overloads accept a maximum value as an argument, where
     * the limit may not exceed 200. The offset field can be used to obtain more
     * search results for the same query, beyond the given limit. The offset can
     * be used in several of the function overloads.<br>
     * <br>
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
     * of all results and the search offset field.<br>
     * <br>
     * The maximum amount of results is given as the second argument in this
     * function. The maximum amount for the limit is 200. Any value higher than
     * that will result in the usage of 200 as the limit. The offset field can
     * be used to obtain more search results for the same query, beyond the
     * given limit. The offset can be used in several of the function
     * overloads.<br>
     * <br>
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
     * of all results and the search offset field.<br>
     * <br>
     * The offset field can be used to obtain more search results for the same
     * query, beyond the given limit. The offset value is defined in the second
     * argument. By default, the Triage API will return a maximum of 50 results.
     * Several of the function overloads accept a maximum value as an
     * argument.<br>
     * <br>
     * Note that the offset should be UTC based, as Triage works based on the
     * UTC time zone.<br>
     * <br>
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
     * of all results and the search offset field.<br>
     * <br>
     * The offset field can be used to obtain more search results for the same
     * query, beyond the given limit. The offset value is defined in the second
     * argument. The maximum amount of results is given as the third argument in
     * this function. The maximum amount for the limit is 200. Any value higher
     * than that will result in the usage of 200 as the limit.<br>
     * <br>
     * Note that the offset should be UTC based, as Triage works based on the
     * UTC time zone.<br>
     * <br>
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
     * Triage within the given date ranges.<br>
     * <br>
     * The upper limit of 200 samples per search query is still maintained
     * within this function, as multiple function calls are used to iterate
     * through the sample set of Triage between the two given moments in
     * time.<br>
     * <br>
     * Note that both the earliest and latest variables should be based upon
     * this system's time zone, as they are converted into UTC within this API.
     * The conversion to UTC is mandatory, as Triage solemnly works with the UTC
     * time zone.<br>
     * <br>
     * Note that the bigger the timespan between the two moments is, the longer
     * this function takes to execute. As such, one should use this with
     * caution!<br>
     * <br>
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
        earliest = setToUtc(earliest);
        latest = setToUtc(latest);

        List<SearchResultEntry> searchResults = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'");

        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        now = setToUtc(now);
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
                sampleDate = setToUtc(sampleDate);
                if (sampleDate.isAfter(earliest) && sampleDate.isBefore(latest)) {
                    searchResults.add(searchResult);
                }
            }

            LocalDateTime firstSampleCompletion = LocalDateTime.parse(formatDateTimeString(result.getSearchResults().get(0).getCompleted()), formatter);
            firstSampleCompletion = setToUtc(firstSampleCompletion);
            LocalDateTime lastSampleCompletion = LocalDateTime.parse(formatDateTimeString(result.getSearchResults().get(result.getSearchResults().size() - 1).getCompleted()), formatter);
            lastSampleCompletion = setToUtc(lastSampleCompletion);

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
     * called.<br>
     * <br>
     * The upper limit of 200 samples per search query is still maintained
     * within this function, as multiple function calls are used to iterate
     * through the sample set of Triage between the two given moments in
     * time.<br>
     * <br>
     * Note that both the earliest and latest variables should be based upon
     * this system's time zone, as they are converted into UTC within this API.
     * The conversion to UTC is mandatory, as Triage solemnly works with the UTC
     * time zone.<br>
     * <br>
     * Note that the bigger the timespan between the two moments is, the longer
     * this function takes to execute. As such, one should use this with
     * caution!<br>
     * <br>
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
        return search(query, earliest, LocalDateTime.now(ZoneId.systemDefault()));
    }

    /**
     * Gets all associated families that were detected in the sandbox based on
     * the given signatures.<br>
     * <br>
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
        List<String> toMatch = new ArrayList<>();

        for (int i = 0; i < signatures.length; i++) {
            String name = signatures[i].getName();
            toMatch.add(name);
        }
        return getFamilies(toMatch, allowCaching);
    }

    /**
     * Gets all associated families that were detected in the sandbox based on
     * the given signatures.<br>
     * <br>
     * The allowCaching boolean defines if the list of all families, which is
     * kept as a global object in this class, can remain cached, or if it needs
     * to be recreated when this function is called.The first time any function
     * is called that uses this list, it is instantiated, regardless of the
     * boolean's value.
     *
     * @param toMatch the list of strings to match, which can be manually
     * created or taken from Triage reports
     * @param allowCaching if the global family list may be cached, or not
     * @return a list of families that were detected for the given signatures.
     * This list can be empty if no matches have been found.
     * @throws IOException if the HTTP request fails
     */
    public Set<String> getFamilies(List<String> toMatch, boolean allowCaching) throws IOException {
        Set<String> families = new HashSet<>();

        if (allFamilies == null || (allFamilies != null && allowCaching == false)) {
            allFamilies = "";
            List<String> fetchedFamilies = getSupportedFamilies();

            for (int i = 0; i < fetchedFamilies.size(); i++) {
                String familyName = fetchedFamilies.get(i).toLowerCase();
                allFamilies += familyName + ";";

                if (familyName.contains("_")) {
                    familyName = familyName.replace("_", " ");
                }
                allFamilies += familyName + ";";
            }
        }

        for (int i = 0; i < toMatch.size(); i++) {
            String name = toMatch.get(i).toLowerCase();
            if (allFamilies.contains(name)) {
                families.add(name);
            }
        }
        return families;
    }

    /**
     * Gets all associated families that were detected in the sandbox based on
     * the given report.<br>
     * <br>
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
        List<String> toMatch = new ArrayList<>();

        for (Signature signature : report.getSignatures()) {
            toMatch.add(signature.getName());
        }

        StaticReport staticReport = getStaticReport(report.getSample().getId());

        Set<String> tags = new HashSet<>();
        tags.addAll(staticReport.getStaticAnalysis().getTags());

        for (StaticSignature staticSignature : staticReport.getStaticSignatures()) {
            tags.addAll(staticSignature.getTags());
        }

        for (String tag : tags) {
            tag = tag.replace("family:", "");
            toMatch.add(tag);
        }

        return getFamilies(toMatch, allowCaching);
    }

    /**
     * Gets all associated families that were detected in the sandbox based on
     * the given reports.<br>
     * <br>
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
    public Map<TriageReport, Set<String>> getFamilies(TriageReport[] reports, boolean allowCaching) throws IOException {
        Map<TriageReport, Set<String>> mapping = new HashMap<>();

        for (TriageReport report : reports) {
            Set<String> families = getFamilies(report, allowCaching);
            mapping.put(report, families);
        }

        return mapping;
    }

    /**
     * Gets all associated families that were detected in the sandbox based on
     * the given sample and task ID.<br>
     * <br>
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
     * Gets all associated families that were detected in the sandbox based on
     * the given array of overviews.<br>
     * <br>
     * Note that unlike the other <code>getFamilies</code> overloads, this
     * function does not need to make additional calls to the Triage backend, as
     * the families per overview are already embedded within the object. This
     * method is purely added for convenience, as other common objects in the
     * client can be used in the same manner.
     *
     * @param overviews the array of overview objects to fetch all families from
     * @return a list of families that were detected for the given sample. This
     * list can be empty if no matches have been found.
     */
    public Set<String> getFamilies(TriageOverview[] overviews) {
        Set<String> families = new HashSet<>();

        for (TriageOverview overview : overviews) {
            families.addAll(Arrays.asList(overview.getAnalysis().getFamilies()));
        }

        return families;
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
     * in <code>Dump.getName()</code>), and the value is the raw section in a
     * boxed byte array
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
     * @throws IOException if any of the HTTP requests fail
     */
    public Map<TriageReport, Map<String, Byte[]>> getDumpedSections(List<TriageReport> reports) throws IOException {
        Map<TriageReport, Map<String, Byte[]>> mapping = new HashMap<>();

        for (TriageReport report : reports) {
            Map<String, Byte[]> dumpedSections = getDumpedSections(report);
            mapping.put(report, dumpedSections);
        }

        return mapping;
    }

    /**
     * Fetches all details from the given submission (regardless of the used
     * profile during the execution) in a single object
     *
     * @param sampleId the sample ID of the submission
     * @return a TriageOverview object, which contains all details of the
     * submitted sample, regardless of the used profile during the execution
     * @throws IOException if the HTTP request fails
     */
    public TriageOverview getTriageOverview(String sampleId) throws IOException {
        String json = new String(connector.get(getUrl("samples/" + sampleId + "/overview.json")));
        TriageOverview triageOverview = parser.parseTriageOverview(json);
        return triageOverview;
    }
}
