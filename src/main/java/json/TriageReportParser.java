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

import model.Config;
import model.Dropper;
import model.DropperURL;
import model.Dump;
import model.Extract;
import model.Indicator;
import model.Key;
import model.NetworkDomainRequest;
import model.NetworkDomainResponse;
import model.NetworkFlow;
import model.NetworkReport;
import model.NetworkRequest;
import model.NetworkWebRequest;
import model.NetworkWebResponse;
import model.Ransom;
import model.ReportAnalysisInfo;
import model.ReportTaskFailure;
import model.Signature;
import model.TargetDesc;
import model.TriageReport;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This parser is used to parse JSON, in the form of a string, into a Triage
 * Report object
 *
 * @author Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
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
        model.Process[] processes = optProcessArray(json.optJSONArray("processes"));
        Signature[] signatures = optSignatureArray(json.optJSONArray("signatures"));
        NetworkReport networkReport = getNetworkReport(json.optJSONObject("network"));
        //debug is skipped
        Dump[] dumped = optDumpArray(json.optJSONArray("dumped"));
        Extract[] extracted = optExtractArray(json.optJSONArray("extracted"));
        return new TriageReport(version, taskId, sample, task, errors, analysis, processes, signatures, networkReport, dumped, extracted);

    }

    private Extract[] optExtractArray(JSONArray input) {
        if (input == null) {
            return new Extract[0];
        }
        Extract[] output = new Extract[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getExtract(input.getJSONObject(i));
        }
        return output;
    }

    private Dump[] optDumpArray(JSONArray input) {
        if (input == null) {
            return new Dump[0];
        }
        Dump[] output = new Dump[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getDump(input.getJSONObject(i));
        }
        return output;
    }

    private Signature[] optSignatureArray(JSONArray input) {
        if (input == null) {
            return new Signature[0];
        }
        Signature[] output = new Signature[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getSignature(input.getJSONObject(i));
        }
        return output;
    }

    private model.Process[] optProcessArray(JSONArray input) {
        if (input == null) {
            return new model.Process[0];
        }
        model.Process[] output = new model.Process[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getProcess(input.getJSONObject(i));
        }
        return output;
    }

    private ReportTaskFailure[] optReportTaskFailureArray(JSONArray input) {
        if (input == null) {
            return new ReportTaskFailure[0];
        }
        ReportTaskFailure[] output = new ReportTaskFailure[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getReportTaskFailure(input.getJSONObject(i));
        }
        return output;
    }

    private DropperURL[] optDropperUrlArray(JSONArray input) {
        if (input == null) {
            return new DropperURL[0];
        }
        DropperURL[] output = new DropperURL[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getDropperURL(input.optJSONObject(i));
        }
        return output;
    }

    private Key[] optKeyArray(JSONArray input) {
        if (input == null) {
            return new Key[0];
        }
        Key[] output = new Key[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getKey(input.optJSONObject(i));
        }
        return output;
    }

    private byte[][] optByteArrayArray(JSONArray input) {
        if (input == null) {
            return new byte[0][0];
        }
        byte[][] output = new byte[0][input.length()];
        for (int i = 0; i < output.length; i++) {
            //output[i] = getKey(input.optJSONObject(i));
        }
        return output;
    }

    private NetworkFlow[] optNetworkFlowArray(JSONArray input) {
        if (input == null) {
            return new NetworkFlow[0];
        }
        NetworkFlow[] output = new NetworkFlow[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getNetworkFlow(input.optJSONObject(i));
        }
        return output;
    }

    private NetworkRequest[] optNetworkRequestArray(JSONArray input) {
        if (input == null) {
            return new NetworkRequest[0];
        }
        NetworkRequest[] output = new NetworkRequest[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getNetworkRequest(input.optJSONObject(i));
        }
        return output;
    }

    private Indicator[] optIndicatorArray(JSONArray input) {
        if (input == null) {
            return new Indicator[0];
        }
        Indicator[] output = new Indicator[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getIndicator(input.optJSONObject(i));
        }
        return output;
    }

    private TargetDesc getTargetDesc(JSONObject json) {
        if (json == null) {
            return new TargetDesc();
        }
        String id = json.optString("id");
        int score = json.optInt("score");
        String submitted = json.optString("submitted");
        String compatCompleted = json.optString("completed");
        String target = json.optString("target");
        String pick = json.optString("pick");
        String type = json.optString("type");
        int size = json.optInt("size");
        String md5 = json.optString("md5");
        String sha1 = json.optString("sha1");
        String sha256 = json.optString("sha256");
        String sha512 = json.optString("sha512");
        String fileType = json.optString("filetype");
        //static_tags
        String[] staticTags = optStringArray(json.optJSONArray("static_tags"));
        String compatFamily = json.optString("family");

        return new TargetDesc(id, score, submitted, compatCompleted, target, pick, type, size, md5, sha1, sha256, sha512, fileType, staticTags, compatFamily);
    }

    private ReportTaskFailure getReportTaskFailure(JSONObject json) {
        if (json == null) {
            return new ReportTaskFailure();
        }

        String task = json.optString("task");
        String backend = json.optString("backend");
        String reason = json.optString("reason");
        return new ReportTaskFailure(task, backend, reason);
    }

    private ReportAnalysisInfo getReportAnalysisInfo(JSONObject json) {
        if (json == null) {
            return new ReportAnalysisInfo();
        }
        int score = json.optInt("score");
        String family = json.optString("family");
        String[] tags = optStringArray(json.getJSONArray("tags"));
        String[] ttp = optStringArray(json.optJSONArray("ttp"));
        String[] features = optStringArray(json.optJSONArray("features"));
        String submitted = json.optString("submitted");
        String reported = json.optString("reported");
        int maxTimeNetwork = json.optInt("max_time_network");
        int maxTimeKernel = json.optInt("max_time_kernel");
        String backend = json.optString("backend");
        String resource = json.optString("resource");
        String[] resourceTags = optStringArray(json.optJSONArray("resource_tags"));
        String platform = json.optString("platform");
        return new ReportAnalysisInfo(score, family, tags, ttp, features, submitted, reported, maxTimeNetwork, maxTimeKernel, backend, resource, resourceTags, platform);
    }

    private model.Process getProcess(JSONObject json) {
        if (json == null) {
            return new model.Process();
        }
        int procId = json.optInt("procid");
        int parentProcId = json.optInt("procid_parent");
        int pid = json.optInt("pid");
        int ppid = json.optInt("ppid");
        //TODO string on windows, string[] on linux: fix
        String cmd = json.optString("cmd");
        String image = json.optString("image");
        boolean orig = json.optBoolean("orig");
        boolean system = json.optBoolean("-");
        int started = json.optInt("started");
        int terminated = json.optInt("terminated");
        return new model.Process(procId, parentProcId, pid, ppid, cmd, image, orig, system, started, terminated);
    }

    /*
    Process struct {
        ProcID       int32       `json:"procid,omitempty"`
        ParentProcID int32       `json:"procid_parent,omitempty"`
        PID          uint64      `json:"pid"`
        PPID         uint64      `json:"ppid"`
        Cmd          interface{} `json:"cmd"`
        Image        string      `json:"image,omitempty"`
        Orig         bool        `json:"orig"`
        System       bool        `json:"-"`
        Started      uint32      `json:"started"`
        Terminated   uint32      `json:"terminated,omitempty"`
    }
     */
    private Signature getSignature(JSONObject json) {
        if (json == null) {
            return new Signature();
        }
        String label = json.optString("label");
        String name = json.optString("name");
        int score = json.optInt("score");
        String[] ttp = optStringArray(json.optJSONArray("ttp"));
        String[] tags = optStringArray(json.optJSONArray("tags"));
        Indicator[] indicators = optIndicatorArray(json.optJSONArray("indicators"));
        String yaraRule = json.optString("yara_rule");
        String description = json.optString("desc");
        String url = json.optString("url");
        return new Signature(label, name, score, ttp, tags, indicators, yaraRule, description, url);
    }

    private NetworkReport getNetworkReport(JSONObject json) {
        if (json == null) {
            return new NetworkReport();
        }

        NetworkFlow[] flows = optNetworkFlowArray(json.optJSONArray("flows"));
        NetworkRequest[] requests = optNetworkRequestArray(json.optJSONArray("requests"));
        return new NetworkReport(flows, requests);
    }

    private Dump getDump(JSONObject json) {
        if (json == null) {
            return new Dump();
        }

        int at = json.optInt("at");
        int pid = json.optInt("pid");
        int procId = json.optInt("procid");
        String path = json.optString("path");
        String name = json.optString("name");
        String kind = json.optString("kind");
        int addr = json.optInt("addr");
        int length = json.optInt("length");
        return new Dump(at, pid, procId, path, name, kind, addr, length);
    }

    private Extract getExtract(JSONObject json) {
        if (json == null) {
            return new Extract();
        }

        String dumpedFile = json.optString("dumped_file");
        String resource = json.optString("resource");
        Config config = getConfig(json.optJSONObject("config"));
        String path = json.optString("path");
        Ransom ransom = getRansom(json.optJSONObject("ransom_note"));
        Dropper dropper = getDropper(json.optJSONObject("dropper"));
        return new Extract(dumpedFile, resource, config, path, ransom, dropper);
    }

    private Indicator getIndicator(JSONObject json) {
        if (json == null) {
            return new Indicator();
        }
        String ioc = json.optString("ioc");
        String description = json.optString("description");
        int at = json.optInt("at");
        int sourcePid = json.optInt("pid");
        int sourceProcId = json.optInt("procid");
        int targetPid = json.optInt("pid_target");
        int targetProcId = json.optInt("procid_target");
        int flow = json.optInt("flow");
        String dumpFile = json.optString("dump_file");
        String resource = json.optString("resource");
        String yaraRule = json.optString("yara_rule");
        return new Indicator(ioc, description, at, sourcePid, sourceProcId, targetPid, targetProcId, flow, dumpFile, resource, yaraRule);

    }

    private NetworkFlow getNetworkFlow(JSONObject json) {
        if (json == null) {
            return new NetworkFlow();
        }
        int id = json.optInt("id");
        String source = json.optString("src");
        String dest = json.optString("dst");
        String proto = json.optString("proto");
        int pid = json.optInt("pid");
        int procId = json.optInt("procid");
        int firstSeen = json.optInt("first_seen");
        int lastSeen = json.optInt("last_seen");
        int rxBytes = json.optInt("rx_bytes");
        int rxPackets = json.optInt("rx_packets");
        int txBytes = json.optInt("tx_bytes");
        int txPackets = json.optInt("tx_packets");
        String domain = json.optString("domain");
        String ja3 = json.optString("tls_ja3");
        String sni = json.optString("sni");
        String country = json.optString("country");
        String as = json.optString("as_num");
        String org = json.optString("as_org");
        return new NetworkFlow(id, source, dest, proto, pid, procId, firstSeen, lastSeen, rxBytes, rxPackets, txBytes, txPackets, domain, ja3, sni, country, as, org);
    }

    private NetworkRequest getNetworkRequest(JSONObject json) {
        if (json == null) {
            return new NetworkRequest();
        }
        int flow = json.optInt("flow");
        int at = json.optInt("at");
        NetworkDomainRequest domainReq = getNetworkDomainRequest(json.optJSONObject("dns_request"));
        NetworkDomainResponse domainResp = getNetworkDomainResponse(json.optJSONObject("dns_response"));
        NetworkWebRequest webReq = getNetworkWebRequest(json.optJSONObject("http_request"));
        NetworkWebResponse webResp = getNetworkWebResponse(json.optJSONObject("http_response"));
        return new NetworkRequest(flow, at, domainReq, domainResp, webReq, webResp);
    }

    private Config getConfig(JSONObject json) {
        if (json == null) {
            return new Config();
        }
        String family = json.optString("family");
        String[] tags = optStringArray(json.optJSONArray("tags"));
        String rule = json.optString("rule");
        String[] c2 = optStringArray(json.optJSONArray("c2"));
        String[] decoy = optStringArray(json.optJSONArray("decoy"));
        String version = json.optString("version");
        String botnet = json.optString("botnet");
        String campaign = json.optString("campaign");
        String[] mutex = optStringArray(json.optJSONArray("mutex"));
        String[] dns = optStringArray(json.optJSONArray("dns"));
        Key[] keys = optKeyArray(json.optJSONArray("keys"));
        String[] webInject = optStringArray(json.optJSONArray("webinject"));
        String[] commandLines = optStringArray(json.optJSONArray("command_lines"));
        String listenAddr = json.optString("listen_addr");
        int listenPort = json.optInt("listen_port");
        String[] listenFor = optStringArray(json.optJSONArray("listen_for"));
        byte[][] shellcode = optByteArrayArray(json.optJSONArray("shellcode"));
        String[] extractedPe = optStringArray(json.optJSONArray("extracted_pe"));
        Object attributes = json.optString("attr");
        return new Config(family, tags, rule, c2, decoy, version, botnet, campaign, mutex, dns, keys, webInject, commandLines, listenAddr, listenAddr, listenFor, shellcode, extractedPe, attributes);
    }

    private Ransom getRansom(JSONObject json) {
        if (json == null) {
            return new Ransom();
        }
        String family = json.optString("family");
        String target = json.optString("target");
        String[] emails = optStringArray(json.optJSONArray("emails"));
        String[] wallets = optStringArray(json.optJSONArray("wallets"));
        String[] urls = optStringArray(json.optJSONArray("urls"));
        String note = json.optString("note");
        return new Ransom(family, target, emails, wallets, urls, note);

    }

    private Dropper getDropper(JSONObject json) {
        if (json == null) {
            return new Dropper();
        }
        String family = json.optString("family");
        String language = json.optString("language");
        String source = json.optString("source");
        DropperURL[] urls;

        JSONArray array = json.optJSONArray("urls");
        if (array == null) {
            urls = new DropperURL[0];
        } else {
            urls = optDropperUrlArray(json.optJSONArray("urls"));
        }
        return new Dropper(family, language, source, urls);
    }

    private NetworkDomainRequest getNetworkDomainRequest(JSONObject json) {
        if (json == null) {
            return new NetworkDomainRequest();
        }
        String[] domains = optStringArray(json.optJSONArray("domains"));
        return new NetworkDomainRequest(domains);
    }

    private NetworkDomainResponse getNetworkDomainResponse(JSONObject json) {
        if (json == null) {
            return new NetworkDomainResponse();
        }
        String[] domains = optStringArray(json.optJSONArray("domains"));
        String[] ip = optStringArray(json.optJSONArray("ip"));
        return new NetworkDomainResponse(domains, ip);
    }

    private NetworkWebRequest getNetworkWebRequest(JSONObject json) {
        if (json == null) {
            return new NetworkWebRequest();
        }
        String method = json.optString("method");
        String url = json.optString("url");
        String[] headers = optStringArray(json.optJSONArray("headers"));
        return new NetworkWebRequest(method, url, headers);
    }

    private NetworkWebResponse getNetworkWebResponse(JSONObject json) {
        if (json == null) {
            return new NetworkWebResponse();
        }
        String status = json.optString("status");
        String[] headers = optStringArray(json.optJSONArray("headers"));
        return new NetworkWebResponse(status, headers);
    }

    private Key getKey(JSONObject json) {
        if (json == null) {
            return new Key();
        }
        String kind = json.optString("kind");
        String key = json.optString("key");
        String value = json.optString("value");
        return new Key(kind, key, value);
    }

    private DropperURL getDropperURL(JSONObject json) {
        if (json == null) {
            return new DropperURL();
        }
        String type = json.optString("type");
        String url = json.optString("url");
        return new DropperURL(type, url);
    }
}
