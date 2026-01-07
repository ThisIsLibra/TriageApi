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
public class TargetDesc {

    private String id;
    private int score;
    private String submitted;
    private String compatCompleted;
    private String target;
    private String pick;
    private String type;
    private int size;
    private String md5;
    private String sha1;
    private String sha256;
    private String sha512;
    private String ssdeep;
    private String fileType;
    private String[] staticTags;
    private String compatFamily;
    private boolean empty;

    public TargetDesc() {
        empty = true;
        id = "";
        score = -1;
        submitted = "";
        compatCompleted = "";
        target = "";
        pick = "";
        type = "";
        size = -1;
        md5 = "";
        sha1 = "";
        sha256 = "";
        sha512 = "";
        ssdeep = "";
        fileType = "";
        staticTags = new String[0];
        compatFamily = "";
    }

    public TargetDesc(String id, int score, String submitted, String compatCompleted, String target, String pick, String type, int size, String md5, String sha1, String sha256, String sha512, String ssdeep, String fileType, String[] staticTags, String compatFamily) {
        empty = false;
        this.id = id;
        this.score = score;
        this.submitted = submitted;
        this.compatCompleted = compatCompleted;
        this.target = target;
        this.pick = pick;
        this.type = type;
        this.size = size;
        this.md5 = md5;
        this.sha1 = sha1;
        this.sha256 = sha256;
        this.sha512 = sha512;
        this.ssdeep = ssdeep;
        this.fileType = fileType;
        this.staticTags = staticTags;
        this.compatFamily = compatFamily;
    }

    public boolean isEmpty() {
        return empty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setCompatScore(int compatScore) {
        this.score = compatScore;
    }

    public String getSubmitted() {
        return submitted;
    }

    public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }

    public String getCompatCompleted() {
        return compatCompleted;
    }

    public void setCompatCompleted(String compatCompleted) {
        this.compatCompleted = compatCompleted;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getPick() {
        return pick;
    }

    public void setPick(String pick) {
        this.pick = pick;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getSha512() {
        return sha512;
    }

    public void setSha512(String sha512) {
        this.sha512 = sha512;
    }

    public String getSsdeep() {
        return ssdeep;
    }

    public void setSsdeep(String ssdeep) {
        this.ssdeep = ssdeep;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String[] getStaticTags() {
        return staticTags;
    }

    public void setStaticTags(String[] staticTags) {
        this.staticTags = staticTags;
    }

    public String getCompatFamily() {
        return compatFamily;
    }

    public void setCompatFamily(String compatFamily) {
        this.compatFamily = compatFamily;
    }

}
