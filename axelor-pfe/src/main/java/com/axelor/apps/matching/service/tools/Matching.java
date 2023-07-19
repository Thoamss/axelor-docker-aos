package com.axelor.apps.matching.service.tools;

import com.axelor.apps.matching.service.models.Match;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class Matching {

  public static void match() throws SQLException {
    Connection c =
        DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/Matching_Axelor", "app-axelor", "app-pass");
    int id = getLastIdFromDb(c);
    List<Match> matches =
        Calculate.scoreIt(
            id,
            DataFromDb.getApplicantsFromDb(c, getLastApplicantFromDb(c)),
            DataFromDb.getOpportunitiessFromDb(c));
    saveMatches(c, matches);
    c.close();
  }

  private static void saveMatches(Connection c, List<Match> matches) throws SQLException {
    for (Match match : matches) {
      Timestamp dateTime = Timestamp.from(Instant.now());
      String saveQuery =
          "insert into matching(id,version,created_on,match_rate,title,applicant,opportunity) values( ? , ? , ? , ? , ? , ? , ? )";
      PreparedStatement psSave = c.prepareStatement(saveQuery);
      psSave.setInt(1, match.getId());
      psSave.setInt(2, 0);
      psSave.setTimestamp(3, dateTime);
      psSave.setInt(4, match.getMatchRate());
      psSave.setString(5, match.getTitle());
      psSave.setInt(6, match.getApplicant());
      psSave.setInt(7, match.getOpportunity());
      psSave.executeUpdate();
    }
  }

  public static int getLastIdFromDb(Connection c) throws SQLException {
    int id = 1;
    String getIdQuery = "select max(id) from matching";
    PreparedStatement psId = c.prepareStatement(getIdQuery);
    ResultSet rsId = psId.executeQuery();
    if (rsId.next()) {
      id = rsId.getInt(1);
    }
    rsId.close();
    return id;
  }

  public static int getLastApplicantFromDb(Connection c) throws SQLException {
    int id = 1;
    String getIdQuery = "select max(applicant) from matching";
    PreparedStatement psId = c.prepareStatement(getIdQuery);
    ResultSet rsId = psId.executeQuery();
    if (rsId.next()) {
      id = rsId.getInt(1);
    }
    rsId.close();
    return id;
  }
}
