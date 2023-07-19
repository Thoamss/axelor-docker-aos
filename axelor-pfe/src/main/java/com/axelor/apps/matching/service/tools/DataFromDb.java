package com.axelor.apps.matching.service.tools;

import com.axelor.apps.matching.service.models.Applicant;
import com.axelor.apps.matching.service.models.Opportunity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataFromDb {

  public static List<Applicant> getApplicantsFromDb(Connection c, int maxId) throws SQLException {
    List<Applicant> apps = new ArrayList<Applicant>();
    String applicantQuery =
        "select a.id,a.localisation,a.experience,a.salary,r.rolesc,c.competences\r\n"
            + "from applicant as a \r\n"
            + "right join applicant_rolesc as r\r\n"
            + "on r.applicant=a.id\r\n"
            + "right join applicant_competences as c \r\n"
            + "on c.applicant = a.id "
            + "where a.id > ? ";
    PreparedStatement psApp = c.prepareStatement(applicantQuery);
    psApp.setInt(1, maxId);
    ResultSet rsApp = psApp.executeQuery();

    while (rsApp.next()) {
      Applicant app = new Applicant();
      app.setId(rsApp.getInt(1));
      if (!rsApp.getString(2).isEmpty()) {
        app.setLocation(rsApp.getString(2));
      }
      if (rsApp.getInt(3) != 0) {
        app.setExperience(rsApp.getInt(3));
      }
      if (rsApp.getInt(4) != 0) {
        app.setSalary(convertSalary(rsApp.getInt(4)));
      }
      if (rsApp.getInt(5) != 0) {
        app.addRole(rsApp.getInt(5));
      }
      if (rsApp.getInt(6) != 0) {
        app.addComps(rsApp.getInt(6));
      }
      apps.add(app);
    }
    apps = cleanListApp(apps);
    return apps;
  }

  public static List<Applicant> cleanListApp(List<Applicant> list) {
    List<Applicant> finalList = new ArrayList<Applicant>();
    Applicant tempApp = list.get(0);
    for (int i = 1; i < list.size(); i++) {
      if (tempApp.getId() == list.get(i).getId()) {
        for (int tempR : list.get(i).getRoles()) {
          if (!tempApp.getRoles().contains(tempR)) {
            tempApp.addRole(tempR);
          }
        }
        for (int tempC : list.get(i).getComps()) {
          if (!tempApp.getComps().contains(tempC)) {
            tempApp.addComps(tempC);
          }
        }
      } else {
        finalList.add(tempApp);
        tempApp = list.get(i);
      }
    }
    return finalList;
  }

  public static int convertSalary(int s) {
    return 7 * s * 1000 / (47 * 35);
  }

  public static List<Opportunity> getOpportunitiessFromDb(Connection c) throws SQLException {
    List<Opportunity> opps = new ArrayList<Opportunity>();
    String opportunityQuery =
        "select o.id,o.localisation,o.xp,o.adrmax,r.roles,c.competences\r\n"
            + "from opportunity as o\r\n"
            + "right join opportunity_roles as r\r\n"
            + "on r.opportunity=o.id\r\n"
            + "right join opportunity_competences as c \r\n"
            + "on c.opportunity = o.id ";
    PreparedStatement psOpp = c.prepareStatement(opportunityQuery);
    ResultSet rsOpp = psOpp.executeQuery();

    while (rsOpp.next()) {
      Opportunity opp = new Opportunity();
      opp.setId(rsOpp.getInt(1));
      if (!rsOpp.getString(2).isEmpty()) {
        opp.setLocation(rsOpp.getString(2));
      }
      if (rsOpp.getInt(3) != 0) {
        opp.setExperience(rsOpp.getInt(3));
      }
      if (rsOpp.getInt(4) != 0) {
        opp.setTjm(rsOpp.getInt(4));
      }
      if (rsOpp.getInt(5) != 0) {
        opp.addRole(rsOpp.getInt(5));
      }
      if (rsOpp.getInt(6) != 0) {
        opp.addComps(rsOpp.getInt(6));
      }
      opps.add(opp);
    }
    opps = cleanListOpp(opps);
    return opps;
  }

  public static List<Opportunity> cleanListOpp(List<Opportunity> list) {
    List<Opportunity> finalList = new ArrayList<Opportunity>();
    Opportunity tempOpp = list.get(0);
    for (int i = 1; i < list.size(); i++) {
      if (tempOpp.getId() == list.get(i).getId()) {
        for (int tempR : list.get(i).getRoles()) {
          if (!tempOpp.getRoles().contains(tempR)) {
            tempOpp.addRole(tempR);
          }
        }
        for (int tempC : list.get(i).getComps()) {
          if (!tempOpp.getComps().contains(tempC)) {
            tempOpp.addComps(tempC);
          }
        }
      } else {
        finalList.add(tempOpp);
        tempOpp = list.get(i);
      }
    }
    return finalList;
  }
}
