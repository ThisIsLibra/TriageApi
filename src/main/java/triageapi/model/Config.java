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
package triageapi.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 */
public class Config {

    private String family;
    private String[] tags;
    private String rule;
    private String[] c2;
    private String[] decoy;
    private String version;
    private String botnet;
    private String campaign;
    private String[] mutex;
    private String[] dns;
    private Key[] keys;
    private String[] webInject;
    private String[] commandLines;
    private String listenAddr;
    private int listenPort;
    private String[] listenFor;
    private byte[][] shellcode;
    private String[] extractedPe;
    private Credentials[] credentials;
    private Map<String, String> attributes;

    private boolean empty;

    public Config() {
        empty = true;
        family = "";
        tags = new String[0];
        rule = "";
        c2 = new String[0];
        decoy = new String[0];
        version = "";
        botnet = "";
        campaign = "";
        mutex = new String[0];
        dns = new String[0];
        keys = new Key[0];
        webInject = new String[0];
        commandLines = new String[0];
        listenAddr = "";
        listenPort = -1;
        listenFor = new String[0];
        shellcode = new byte[0][0];
        extractedPe = new String[0];
        credentials = new Credentials[0];
        attributes = new HashMap<>();
    }

    public Config(String family, String[] tags, String rule, String[] c2, String[] decoy, String version, String botnet, String campaign, String[] mutex, String[] dns, Key[] keys, String[] webInject, String[] commandLines, String listenAddr, int listenPort, String[] listenFor, byte[][] shellcode, String[] extractedPe, Credentials[] credentials, Map<String, String> attributes) {
        empty = false;
        this.family = family;
        this.tags = tags;
        this.rule = rule;
        this.c2 = c2;
        this.decoy = decoy;
        this.version = version;
        this.botnet = botnet;
        this.campaign = campaign;
        this.mutex = mutex;
        this.dns = dns;
        this.keys = keys;
        this.webInject = webInject;
        this.commandLines = commandLines;
        this.listenAddr = listenAddr;
        this.listenPort = listenPort;
        this.listenFor = listenFor;
        this.shellcode = shellcode;
        this.extractedPe = extractedPe;
        this.credentials = credentials;
        this.attributes = attributes;
    }

    public boolean isEmpty() {
        return empty;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String[] getC2() {
        return c2;
    }

    public void setC2(String[] c2) {
        this.c2 = c2;
    }

    public String[] getDecoy() {
        return decoy;
    }

    public void setDecoy(String[] decoy) {
        this.decoy = decoy;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBotnet() {
        return botnet;
    }

    public void setBotnet(String botnet) {
        this.botnet = botnet;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String[] getMutex() {
        return mutex;
    }

    public void setMutex(String[] mutex) {
        this.mutex = mutex;
    }

    public String[] getDns() {
        return dns;
    }

    public void setDns(String[] dns) {
        this.dns = dns;
    }

    public Key[] getKeys() {
        return keys;
    }

    public void setKeys(Key[] keys) {
        this.keys = keys;
    }

    public String[] getWebInject() {
        return webInject;
    }

    public void setWebInject(String[] webInject) {
        this.webInject = webInject;
    }

    public String[] getCommandLines() {
        return commandLines;
    }

    public void setCommandLines(String[] commandLines) {
        this.commandLines = commandLines;
    }

    public String getListenAddr() {
        return listenAddr;
    }

    public void setListenAddr(String listenAddr) {
        this.listenAddr = listenAddr;
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public String[] getListenFor() {
        return listenFor;
    }

    public void setListenFor(String[] listenFor) {
        this.listenFor = listenFor;
    }

    public byte[][] getShellcode() {
        return shellcode;
    }

    public void setShellcode(byte[][] shellcode) {
        this.shellcode = shellcode;
    }

    public String[] getExtractedPe() {
        return extractedPe;
    }

    public void setExtractedPe(String[] extractedPe) {
        this.extractedPe = extractedPe;
    }

    public Credentials[] getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials[] credentials) {
        this.credentials = credentials;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

}
