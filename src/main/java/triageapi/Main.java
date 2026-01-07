/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triageapi;

import triageapi.model.StaticReport;
import triageapi.model.TriageEnvironment;
import triageapi.model.TriageOverview;
import triageapi.model.TriageReport;

/**
 *
 * @author user
 */
public class Main {

    public static void main(String[] args) throws Exception {
        TriageApi api = new TriageApi("yeet", TriageEnvironment.PUBLIC);
        String sampleId = "220121-rhmc9ahhgr";
        //String sampleId = "220121-qbtnbahcf8";
        TriageOverview triageOverview = api.getTriageOverview(sampleId);
        TriageReport triageReport = api.getTriageReport(sampleId, "behavioral1");
        StaticReport staticReport = api.getStaticReport(sampleId);
        System.out.println("");
    }
}
