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
public class NetworkReport {

    private NetworkFlow[] flows;
    private NetworkRequest[] requests;
    private boolean empty;

    public NetworkReport() {
        empty = true;
        flows = new NetworkFlow[0];
        requests = new NetworkRequest[0];
    }

    public NetworkReport(NetworkFlow[] flows, NetworkRequest[] requests) {
        empty = false;
        this.flows = flows;
        this.requests = requests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public NetworkFlow[] getFlows() {
        return flows;
    }

    public void setFlows(NetworkFlow[] flows) {
        this.flows = flows;
    }

    public NetworkRequest[] getRequests() {
        return requests;
    }

    public void setRequests(NetworkRequest[] requests) {
        this.requests = requests;
    }

}
