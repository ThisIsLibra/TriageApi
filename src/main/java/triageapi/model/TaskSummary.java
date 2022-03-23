/*
 * Copyright (C) 2022 Max 'Libra' Kersten [@Libranalysis, https://maxkersten.nl]
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
public class TaskSummary {

    private String sample;
    private String kind;
    private String name;
    private String status;
    private String[] ttps;
    private String[] tags;
    private int score;
    private String target;
    private String backend;
    private String resource;
    private String platform;
    private String taskName;
    private String failure;
    private int queueId;
    private String pick;
    private boolean empty;

    public TaskSummary() {
        empty = true;
        this.sample = "";
        this.kind = "";
        this.name = "";
        this.status = "";
        this.ttps = new String[0];
        this.tags = new String[0];
        this.score = -1;
        this.target = "";
        this.backend = "";
        this.resource = "";
        this.platform = "";
        this.taskName = "";
        this.failure = "";
        this.queueId = -1;
        this.pick = "";
    }

    public TaskSummary(String sample, String kind, String name, String status, String[] ttps, String[] tags, int score, String target, String backend, String resource, String platform, String taskName, String failure, int queueId, String pick) {
        empty = false;
        this.sample = sample;
        this.kind = kind;
        this.name = name;
        this.status = status;
        this.ttps = ttps;
        this.tags = tags;
        this.score = score;
        this.target = target;
        this.backend = backend;
        this.resource = resource;
        this.platform = platform;
        this.taskName = taskName;
        this.failure = failure;
        this.queueId = queueId;
        this.pick = pick;
    }

    public boolean isEmpty() {
        return empty;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getTtps() {
        return ttps;
    }

    public void setTtps(String[] ttps) {
        this.ttps = ttps;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public String getPick() {
        return pick;
    }

    public void setPick(String pick) {
        this.pick = pick;
    }

}
