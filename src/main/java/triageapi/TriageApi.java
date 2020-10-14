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

import exception.EmptyArgumentException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import json.JsonParser;
import model.FileUploadResult;
import model.Sample;
import model.StaticReport;
import model.TriageReport;
import network.Connector;
import org.apache.http.entity.mime.MultipartEntityBuilder;
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
        //Public cloud API
        //https://api.tria.ge/v0/
        //Private cloud API
        //https://private.tria.ge/api/v0/
        //this.apiBase = apiBase;

        if (privateCloud) {
            this.apiBase = "https://private.tria.ge/api/v0/";
        } else {
            this.apiBase = "https://api.tria.ge/v0/";
        }

        connector = new Connector(key);
        parser = new JsonParser();
    }

    /**
     * A non-exposed function that adds an appendix to the set base URL (either
     * Triage's public or private cloud)
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
     * @throws IOException if the HTTP request fails for some reason
     * @throws EmptyArgumentException if the returned JSON value from the server
     * is null
     */
    public TriageReport getTriageReport(String sampleId, String taskId) throws IOException, EmptyArgumentException {
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
     * @throws IOException if the HTTP request fails for some reason
     * @throws EmptyArgumentException if the returned JSON value from the server
     * is null
     */
    public StaticReport getStaticReport(String sampleId) throws EmptyArgumentException, IOException {
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
     * @throws IOException if the HTTP request fails for some reason
     * @throws EmptyArgumentException if the returned JSON value from the server
     * is null
     */
    public Sample getSample(String sampleId) throws IOException, EmptyArgumentException {
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
     * @throws IOException if the HTTP request fails for some reason
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
     * @throws IOException if the HTTP request fails for some reason
     * @throws EmptyArgumentException if the returned JSON value from the server
     * is null
     */
    public List<Sample> getSamples(boolean ownUploadsOnly) throws IOException, EmptyArgumentException {
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
     * @throws IOException if the HTTP request fails for some reason
     * @throws EmptyArgumentException if the returned JSON value from the server
     * is null
     */
    public FileUploadResult uploadSample(File file) throws IOException, EmptyArgumentException {
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
     * @throws IOException if the HTTP request fails for some reason
     */
    public String getSampleStatus(String sampleId) throws IOException {
        String url = getUrl("samples/" + sampleId + "/status");
        String json = new String(connector.get(url));
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject.optString("status");
    }

    /**
     * The kernel monitor output as a string (which is the raw format) based on
     * the given sample ID and task ID
     *
     * @param sampleId the sample ID that Triage gave it when the file was
     * uploaded
     * @param taskId the task ID within the sample, this is often
     * <em>behavioral1</em> and <em>behavioral2</em> in the default profile
     * @return the raw kernel monitor output of Triage's dynamic analysis, given
     * as string
     * @throws IOException if the HTTP request fails for some reason
     */
    public String getKernelMonitorOutput(String sampleId, String taskId) throws IOException {
        String url = getUrl("samples/" + sampleId + "/" + taskId + "/logs/onemon.json");
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
     * @throws IOException if the HTTP request fails for some reason
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
     * @throws IOException if the HTTP request fails for some reason
     */
    public byte[] getPcapNg(String sampleId, String taskId) throws IOException {
        String url = getUrl("samples/" + sampleId + "/" + taskId + "/dump.pcapng");
        return connector.get(url);
    }
}
