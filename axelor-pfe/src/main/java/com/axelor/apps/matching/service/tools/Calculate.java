package com.axelor.apps.matching.service.tools;

import com.axelor.apps.matching.service.models.Applicant;
import com.axelor.apps.matching.service.models.Match;
import com.axelor.apps.matching.service.models.Opportunity;
import java.util.ArrayList;
import java.util.List;

public class Calculate {

  public static List<Match> scoreIt(int id, List<Applicant> apps, List<Opportunity> opps) {
    List<Match> matches = new ArrayList<Match>();
    int index = id + 1;
    for (Applicant app : apps) {
      for (Opportunity opp : opps) {
        int score = 0;
        Match m = new Match();
        score += scoreLoc(app.getLocation(), opp.getLocation());
        score += scoreExp(app.getExperience(), opp.getExperience());
        score += scoreMargin(opp.getTjm(), app.getSalary());
        score += scoreRoles(app.getRoles(), opp.getRoles());
        score += scoreComps(app.getComps(), opp.getComps());
        m.setId(index);
        m.setTitle("Matching " + index);
        m.setApplicant(app.getId());
        m.setOpportunity(opp.getId());
        m.setMatchRate(score);
        matches.add(m);
        index++;
      }
    }

    return matches;
  }

  public static int scoreLoc(String locApp, String locOpp) {
    int s = 0;
    if (locApp != null && locOpp != null) {
      if (locApp.equals(locOpp)) {
        s = 50;
      }
    }

    return s;
  }

  public static int scoreExp(int xpApp, int xpOpp) {
    int s = 0;
    if (xpApp >= xpOpp) {
      s = 10;
    }
    return s;
  }

  public static int scoreMargin(int tjm, int sal) {
    int s = 0;
    if (sal <= 0.7 * tjm) {
      s += 10;
    }
    return s;
  }

  public static int scoreRoles(List<Integer> appRoles, List<Integer> oppRoles) {
    int s = 0;
    for (int role : appRoles) {
      if (oppRoles.contains(role)) {
        s = 20;
      }
    }
    return s;
  }

  public static int scoreComps(List<Integer> appComps, List<Integer> oppComps) {
    int s = 0;
    for (int role : appComps) {
      if (oppComps.contains(role)) {
        s = 10;
      }
    }
    return s;
  }
}
