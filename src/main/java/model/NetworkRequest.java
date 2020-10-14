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
package model;

/**
 *
 * @author Max 'Libra' Kersten [@LibraAnalysis, https://maxkersten.nl]
 */
public class NetworkRequest {

    private int flow;
    private int at;
    private NetworkDomainRequest domainReq;
    private NetworkDomainResponse domainResp;
    private NetworkWebRequest webReq;
    private NetworkWebResponse webResp;
    private boolean empty;

    public NetworkRequest() {
        empty = true;
        flow = -1;
        at = -1;
        domainReq = new NetworkDomainRequest();
        domainResp = new NetworkDomainResponse();
        webReq = new NetworkWebRequest();
        webResp = new NetworkWebResponse();
    }

    public NetworkRequest(int flow, int at, NetworkDomainRequest domainReq, NetworkDomainResponse domainResp, NetworkWebRequest webReq, NetworkWebResponse webResp) {
        empty = false;
        this.flow = flow;
        this.at = at;
        this.domainReq = domainReq;
        this.domainResp = domainResp;
        this.webReq = webReq;
        this.webResp = webResp;
    }

    public boolean isEmpty() {
        return empty;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public int getAt() {
        return at;
    }

    public void setAt(int at) {
        this.at = at;
    }

    public NetworkDomainRequest getDomainReq() {
        return domainReq;
    }

    public void setDomainReq(NetworkDomainRequest domainReq) {
        this.domainReq = domainReq;
    }

    public NetworkDomainResponse getDomainResp() {
        return domainResp;
    }

    public void setDomainResp(NetworkDomainResponse domainResp) {
        this.domainResp = domainResp;
    }

    public NetworkWebRequest getWebReq() {
        return webReq;
    }

    public void setWebReq(NetworkWebRequest webReq) {
        this.webReq = webReq;
    }

    public NetworkWebResponse getWebResp() {
        return webResp;
    }

    public void setWebResp(NetworkWebResponse webResp) {
        this.webResp = webResp;
    }
}
