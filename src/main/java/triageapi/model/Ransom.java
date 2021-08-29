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
public class Ransom {

    private String family;
    private String target;
    private String[] emails;
    private String[] wallets;
    private String[] urls;
    private String note;
    private boolean empty;

    public Ransom() {
        empty = true;
        family = "";
        target = "";
        emails = new String[0];
        wallets = new String[0];
        urls = new String[0];
        note = "";
    }

    public Ransom(String family, String target, String[] emails, String[] wallets, String[] urls, String note) {
        empty = false;
        this.family = family;
        this.target = target;
        this.emails = emails;
        this.wallets = wallets;
        this.urls = urls;
        this.note = note;
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String[] getEmails() {
        return emails;
    }

    public void setEmails(String[] emails) {
        this.emails = emails;
    }

    public String[] getWallets() {
        return wallets;
    }

    public void setWallets(String[] wallets) {
        this.wallets = wallets;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
