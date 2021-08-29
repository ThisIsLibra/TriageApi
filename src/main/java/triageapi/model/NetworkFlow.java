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

/**
 *
 * @author Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
 */
public class NetworkFlow {

    private int id;
    private String source;
    private String dest;
    private String proto;
    private int pid;
    private int procId;
    private int firstSeen;
    private int lastSeen;
    private int rxBytes;
    private int rxPackets;
    private int txBytes;
    private int txPackets;
    private String domain;
    private String ja3;
    private String sni;
    private String country;
    private String as;
    private String org;
    private boolean empty;

    public NetworkFlow() {
        empty = true;
        id = -1;
        source = "";
        dest = "";
        proto = "";
        pid = -1;
        procId = -1;
        firstSeen = -1;
        lastSeen = -1;
        rxBytes = -1;
        rxPackets = -1;
        txBytes = -1;
        txPackets = -1;
        domain = "";
        ja3 = "";
        sni = "";
        country = "";
        as = "";
        org = "";
    }

    public NetworkFlow(int id, String source, String dest, String proto, int pid, int procId, int firstSeen, int lastSeen, int rxBytes, int rxPackets, int txBytes, int txPackets, String domain, String ja3, String sni, String country, String as, String org) {
        empty = false;
        this.id = id;
        this.source = source;
        this.dest = dest;
        this.proto = proto;
        this.pid = pid;
        this.procId = procId;
        this.firstSeen = firstSeen;
        this.lastSeen = lastSeen;
        this.rxBytes = rxBytes;
        this.rxPackets = rxPackets;
        this.txBytes = txBytes;
        this.txPackets = txPackets;
        this.domain = domain;
        this.ja3 = ja3;
        this.sni = sni;
        this.country = country;
        this.as = as;
        this.org = org;
    }

    public boolean isEmpty() {
        return empty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getProcId() {
        return procId;
    }

    public void setProcId(int procId) {
        this.procId = procId;
    }

    public int getFirstSeen() {
        return firstSeen;
    }

    public void setFirstSeen(int firstSeen) {
        this.firstSeen = firstSeen;
    }

    public int getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(int lastSeen) {
        this.lastSeen = lastSeen;
    }

    public int getRxBytes() {
        return rxBytes;
    }

    public void setRxBytes(int rxBytes) {
        this.rxBytes = rxBytes;
    }

    public int getRxPackets() {
        return rxPackets;
    }

    public void setRxPackets(int rxPackets) {
        this.rxPackets = rxPackets;
    }

    public int getTxBytes() {
        return txBytes;
    }

    public void setTxBytes(int txBytes) {
        this.txBytes = txBytes;
    }

    public int getTxPackets() {
        return txPackets;
    }

    public void setTxPackets(int txPackets) {
        this.txPackets = txPackets;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getJa3() {
        return ja3;
    }

    public void setJa3(String ja3) {
        this.ja3 = ja3;
    }

    public String getSni() {
        return sni;
    }

    public void setSni(String sni) {
        this.sni = sni;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

}
