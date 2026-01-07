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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import triageapi.model.Config;
import triageapi.model.Credentials;
import triageapi.model.Dropper;
import triageapi.model.DropperURL;
import triageapi.model.Dump;
import triageapi.model.Extract;
import triageapi.model.Indicator;
import triageapi.model.Key;
import triageapi.model.NetworkDomainRequest;
import triageapi.model.NetworkDomainResponse;
import triageapi.model.NetworkFlow;
import triageapi.model.NetworkReport;
import triageapi.model.NetworkRequest;
import triageapi.model.NetworkWebRequest;
import triageapi.model.NetworkWebResponse;
import triageapi.model.Ransom;
import triageapi.model.ReportAnalysisInfo;
import triageapi.model.ReportTaskFailure;
import triageapi.model.Signature;
import triageapi.model.TargetDesc;

/**
 * This abstract class contains functions that are used by other parsers
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 */
public abstract class GenericParser {

    /**
     * Converts a given JSONArray object into a String array with all values
     *
     * @param input the JSONArray to convert
     * @return the string array with all values
     */
    protected String[] optStringArray(JSONArray input) {
        if (input == null) {
            return new String[0];
        }
        String[] output = new String[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = input.optString(i);
        }
        return output;
    }

    /**
     * Converts a given JSONArray object into a list of strings
     *
     * @param input the JSONArray to convert
     * @return the same values in a list
     */
    public List<String> optListString(JSONArray input) {
        if (input == null) {
            return new ArrayList<>();
        }
        List<String> output = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            output.add(input.optString(i));
        }
        return output;
    }

    protected LocalDateTime epochSecondsToLocalDateTime(int epoch) {
        Instant instant;
        if (epoch >= 0) {
            instant = Instant.ofEpochSecond(epoch);
        } else {
            instant = Instant.MIN;
        }
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    protected Signature[] optSignatureArray(JSONArray input) {
        if (input == null) {
            return new Signature[0];
        }
        Signature[] output = new Signature[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getSignature(input.getJSONObject(i));
        }
        return output;
    }

    protected Signature getSignature(JSONObject json) {
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

    protected Indicator[] optIndicatorArray(JSONArray input) {
        if (input == null) {
            return new Indicator[0];
        }
        Indicator[] output = new Indicator[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getIndicator(input.optJSONObject(i));
        }
        return output;
    }

    protected Indicator getIndicator(JSONObject json) {
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

    protected Extract[] optExtractArray(JSONArray input) {
        if (input == null) {
            return new Extract[0];
        }
        Extract[] output = new Extract[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getExtract(input.getJSONObject(i));
        }
        return output;
    }

    protected Dump[] optDumpArray(JSONArray input) {
        if (input == null) {
            return new Dump[0];
        }
        Dump[] output = new Dump[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getDump(input.getJSONObject(i));
        }
        return output;
    }

    protected triageapi.model.Process[] optProcessArray(JSONArray input) {
        if (input == null) {
            return new triageapi.model.Process[0];
        }
        triageapi.model.Process[] output = new triageapi.model.Process[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getProcess(input.getJSONObject(i));
        }
        return output;
    }

    protected ReportTaskFailure[] optReportTaskFailureArray(JSONArray input) {
        if (input == null) {
            return new ReportTaskFailure[0];
        }
        ReportTaskFailure[] output = new ReportTaskFailure[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getReportTaskFailure(input.getJSONObject(i));
        }
        return output;
    }

    protected DropperURL[] optDropperUrlArray(JSONArray input) {
        if (input == null) {
            return new DropperURL[0];
        }
        DropperURL[] output = new DropperURL[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getDropperURL(input.optJSONObject(i));
        }
        return output;
    }

    protected Key[] optKeyArray(JSONArray input) {
        if (input == null) {
            return new Key[0];
        }
        Key[] output = new Key[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getKey(input.optJSONObject(i));
        }
        return output;
    }

    protected byte[][] optByteArrayArray(JSONArray input) {
        if (input == null) {
            return new byte[0][0];
        }
        byte[][] output = new byte[0][input.length()];
        for (int i = 0; i < output.length; i++) {
            //output[i] = getKey(input.optJSONObject(i));
        }
        return output;
    }

    protected NetworkFlow[] optNetworkFlowArray(JSONArray input) {
        if (input == null) {
            return new NetworkFlow[0];
        }
        NetworkFlow[] output = new NetworkFlow[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getNetworkFlow(input.optJSONObject(i));
        }
        return output;
    }

    protected NetworkRequest[] optNetworkRequestArray(JSONArray input) {
        if (input == null) {
            return new NetworkRequest[0];
        }
        NetworkRequest[] output = new NetworkRequest[input.length()];
        for (int i = 0; i < output.length; i++) {
            output[i] = getNetworkRequest(input.optJSONObject(i));
        }
        return output;
    }

    protected TargetDesc getTargetDesc(JSONObject json) {
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
        String ssdeep = json.optString("ssdeep");
        String fileType = json.optString("filetype");
        //static_tags
        String[] staticTags = optStringArray(json.optJSONArray("static_tags"));
        String compatFamily = json.optString("family");

        return new TargetDesc(id, score, submitted, compatCompleted, target, pick, type, size, md5, sha1, sha256, sha512, ssdeep, fileType, staticTags, compatFamily);
    }

    protected ReportTaskFailure getReportTaskFailure(JSONObject json) {
        if (json == null) {
            return new ReportTaskFailure();
        }

        String task = json.optString("task");
        String backend = json.optString("backend");
        String reason = json.optString("reason");
        return new ReportTaskFailure(task, backend, reason);
    }

    protected ReportAnalysisInfo getReportAnalysisInfo(JSONObject json) {
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

    protected triageapi.model.Process getProcess(JSONObject json) {
        if (json == null) {
            return new triageapi.model.Process();
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
        return new triageapi.model.Process(procId, parentProcId, pid, ppid, cmd, image, orig, system, started, terminated);
    }

    protected NetworkReport getNetworkReport(JSONObject json) {
        if (json == null) {
            return new NetworkReport();
        }

        NetworkFlow[] flows = optNetworkFlowArray(json.optJSONArray("flows"));
        NetworkRequest[] requests = optNetworkRequestArray(json.optJSONArray("requests"));
        return new NetworkReport(flows, requests);
    }

    protected Dump getDump(JSONObject json) {
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

    protected Extract getExtract(JSONObject json) {
        if (json == null) {
            return new Extract();
        }

        String dumpedFile = json.optString("dumped_file");
        String resource = json.optString("resource");
        Config config = getConfig(json.optJSONObject("config"));
        String path = json.optString("path");
        Ransom ransom = getRansom(json.optJSONObject("ransom_note"));
        Dropper dropper = getDropper(json.optJSONObject("dropper"));
        Credentials credentials = getCredentials(json.optJSONObject("credentials"));
        return new Extract(dumpedFile, resource, config, path, ransom, dropper, credentials);
    }

    protected NetworkFlow getNetworkFlow(JSONObject json) {
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

    protected NetworkRequest getNetworkRequest(JSONObject json) {
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

    protected Config getConfig(JSONObject json) {
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
        Credentials[] credentials = optCredentialsArray(json.optJSONArray("credentials"));
        String[] extractedPe = optStringArray(json.optJSONArray("extracted_pe"));
        Map<String, String> attributes = optMapStringString(json.optJSONObject("attr"));
        return new Config(family, tags, rule, c2, decoy, version, botnet, campaign, mutex, dns, keys, webInject, commandLines, listenAddr, listenPort, listenFor, shellcode, extractedPe, credentials, attributes);
    }

    protected Map<String, String> optMapStringString(JSONObject json) {
        //TODO optmap string string
        Map<String, String> mapping = new HashMap<>();
        if (json == null) {
            return mapping;
        }

        for (String key : json.keySet()) {
            String value = json.optString(key);
            mapping.put(key, value);
        }

        return mapping;
    }

    protected Ransom getRansom(JSONObject json) {
        if (json == null) {
            return new Ransom();
        }
        String family = json.optString("family");
        String target = json.optString("target");
        String[] emails = optStringArray(json.optJSONArray("emails"));
        String[] wallets = optStringArray(json.optJSONArray("wallets"));
        String[] urls = optStringArray(json.optJSONArray("urls"));
        String[] contact = optStringArray(json.optJSONArray("contact"));
        String note = json.optString("note");
        return new Ransom(family, target, emails, wallets, urls, contact, note);

    }

    protected Dropper getDropper(JSONObject json) {
        if (json == null) {
            return new Dropper();
        }
        String family = json.optString("family");
        String language = json.optString("language");
        String source = json.optString("source");
        String deobfuscated = json.optString("deobfuscated");
        DropperURL[] urls;

        JSONArray array = json.optJSONArray("urls");
        if (array == null) {
            urls = new DropperURL[0];
        } else {
            urls = optDropperUrlArray(json.optJSONArray("urls"));
        }
        return new Dropper(family, language, source, deobfuscated, urls);
    }

    protected NetworkDomainRequest getNetworkDomainRequest(JSONObject json) {
        if (json == null) {
            return new NetworkDomainRequest();
        }
        String[] domains = optStringArray(json.optJSONArray("domains"));
        return new NetworkDomainRequest(domains);
    }

    protected NetworkDomainResponse getNetworkDomainResponse(JSONObject json) {
        if (json == null) {
            return new NetworkDomainResponse();
        }
        String[] domains = optStringArray(json.optJSONArray("domains"));
        String[] ip = optStringArray(json.optJSONArray("ip"));
        return new NetworkDomainResponse(domains, ip);
    }

    protected NetworkWebRequest getNetworkWebRequest(JSONObject json) {
        if (json == null) {
            return new NetworkWebRequest();
        }
        String method = json.optString("method");
        String url = json.optString("url");
        String[] headers = optStringArray(json.optJSONArray("headers"));
        return new NetworkWebRequest(method, url, headers);
    }

    protected NetworkWebResponse getNetworkWebResponse(JSONObject json) {
        if (json == null) {
            return new NetworkWebResponse();
        }
        String status = json.optString("status");
        String[] headers = optStringArray(json.optJSONArray("headers"));
        return new NetworkWebResponse(status, headers);
    }

    protected Key getKey(JSONObject json) {
        if (json == null) {
            return new Key();
        }
        String kind = json.optString("kind");
        String key = json.optString("key");
        String value = json.optString("value");
        return new Key(kind, key, value);
    }

    protected DropperURL getDropperURL(JSONObject json) {
        if (json == null) {
            return new DropperURL();
        }
        String type = json.optString("type");
        String url = json.optString("url");
        return new DropperURL(type, url);
    }

    protected Credentials getCredentials(JSONObject json) {
        if (json == null) {
            return new Credentials();
        }

        int flow = json.optInt("flow");
        String protocol = json.optString("protocol");
        String host = json.optString("host");
        int port = json.optInt("port");
        String username = json.optString("username");
        String password = json.optString("password");

        return new Credentials(flow, protocol, host, port, username, password);
    }

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
