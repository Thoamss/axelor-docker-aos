package com.axelor.apps.traitement.service.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class ProfilesExport {

  public static void initProfiles() throws SQLException {
    Connection c =
        DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/Matching_Axelor", "app-axelor", "app-pass");
    Statement s = c.createStatement();

    s.executeUpdate("DROP TABLE IF EXISTS Applicant_RolesTest");

    s.executeUpdate("DROP TABLE IF EXISTS Applicant_CompetencesTest");

    s.executeUpdate("DROP TABLE IF EXISTS ApplicantTest");

    s.executeUpdate("DROP TABLE IF EXISTS RolesTest");

    s.executeUpdate("DROP TABLE IF EXISTS CompetencesTest");

    s.executeUpdate(
        "CREATE TABLE Applicant(\r\n"
            + "  id int4 PRIMARY KEY,\r\n"
            + "  version int4,\r\n"
            + "  created_on timestamp,\r\n"
            + "  experience int4,\r\n"
            + "  localisation varchar,\r\n"
            + "  mail varchar,\r\n"
            + "  name varchar,\r\n"
            + "  phone varchar,\r\n"
            + "  surname varchar,\r\n"
            + "  title varchar,\r\n"
            + "  created_by int8,\r\n"
            + "  CONSTRAINT fk_created_by\r\n"
            + "      FOREIGN KEY(created_by) \r\n"
            + "      REFERENCES auth_user(id)\r\n"
            + ");");

    s.executeUpdate(
        "CREATE TABLE Roles(\r\n"
            + "  id int4 PRIMARY KEY,\r\n"
            + "  version int4,\r\n"
            + "  created_on timestamp,\r\n"
            + "  title varchar,\r\n"
            + "  created_by int8,\r\n"
            + "  CONSTRAINT fk_created_by\r\n"
            + "      FOREIGN KEY(created_by) \r\n"
            + "      REFERENCES auth_user(id)\r\n"
            + ");");

    s.executeUpdate(
        "CREATE TABLE Competences(\r\n"
            + "  id int4 PRIMARY KEY,\r\n"
            + "  version int4,\r\n"
            + "  created_on timestamp,\r\n"
            + "  title varchar,\r\n"
            + "  created_by int8,\r\n"
            + "  CONSTRAINT fk_created_by\r\n"
            + "      FOREIGN KEY(created_by) \r\n"
            + "      REFERENCES auth_user(id)\r\n"
            + ");");

    s.executeUpdate(
        "CREATE TABLE Applicant_Roles(\r\n"
            + "  applicant int4,\r\n"
            + "  roles int4,\r\n"
            + "  CONSTRAINT fk_applicant\r\n"
            + "      FOREIGN KEY(applicant) \r\n"
            + "      REFERENCES ApplicantTest(id),\r\n"
            + "  CONSTRAINT fk_roles\r\n"
            + "      FOREIGN KEY(roles) \r\n"
            + "      REFERENCES RolesTest(id)\r\n"
            + ");");

    s.executeUpdate(
        "CREATE TABLE Applicant_Competences(\r\n"
            + "  applicant int4,\r\n"
            + "  competences int4,\r\n"
            + "  CONSTRAINT fk_applicant\r\n"
            + "      FOREIGN KEY(applicant) \r\n"
            + "      REFERENCES ApplicantTest(id),\r\n"
            + "  CONSTRAINT fk_competences\r\n"
            + "      FOREIGN KEY(competences) \r\n"
            + "      REFERENCES CompetencesTest(id)\r\n"
            + ");");

    s.close();
    c.close();
  }

  public static void executeQueries(List<String> profiles) throws SQLException {
    Connection c =
        DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/Matching_Axelor", "app-axelor", "app-pass");
    Timestamp dateTime = Timestamp.from(Instant.now());
    for (int i = 1; i < profiles.size(); i++) {
      String[] splittedProfile = profiles.get(i).split(";");
      String[] newProfile = {
        splittedProfile[0],
        splittedProfile[1],
        splittedProfile[2],
        splittedProfile[3],
        splittedProfile[4],
        splittedProfile[5],
        splittedProfile[6],
        splittedProfile[7],
        splittedProfile[9]
      };
      String lastName = newProfile[2];
      String localisation = newProfile[7];
      if (lastName.matches("(.)*['](.)*")) {
        String[] temp = lastName.split("'");
        int tempLength = temp.length;
        lastName = "";
        for (int j = 0; j < tempLength - 1; j++) {
          lastName += temp[j] + "''";
        }
        lastName += temp[tempLength - 1];
      }
      if (localisation.matches("(.)*['](.)*")) {
        String[] temp = localisation.split("'");
        int tempLength = temp.length;
        localisation = "";
        for (int j = 0; j < tempLength - 1; j++) {
          localisation += temp[j] + "''";
        }
        localisation += temp[tempLength - 1];
      }

      int applicantID = 0;
      String appCheckQuery = "SELECT id FROM Applicant WHERE mail LIKE ? ";
      PreparedStatement psAppCheck = c.prepareStatement(appCheckQuery);
      psAppCheck.setString(1, newProfile[3]);
      ResultSet rsCheck = psAppCheck.executeQuery();
      if (!rsCheck.next()) {
        String appIndexQuery = "SELECT MAX( id ) FROM Applicant";
        int appIndex = 0;
        PreparedStatement psAppIndex = c.prepareStatement(appIndexQuery);
        ResultSet rsApp = psAppIndex.executeQuery();
        if (rsApp.next()) {
          appIndex = rsApp.getInt(1);
        }
        rsApp.close();
        applicantID = appIndex + 1;

        String applicantQuery =
            "INSERT INTO Applicant (id,version,created_on,experience,localisation,mail,name,phone,surname,title,created_by,status) "
                + "VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? );";
        PreparedStatement psApplicant = c.prepareStatement(applicantQuery);
        psApplicant.setInt(1, appIndex + 1);
        psApplicant.setInt(2, 0);
        psApplicant.setTimestamp(3, dateTime);
        psApplicant.setInt(4, Integer.parseInt(newProfile[8]));
        psApplicant.setString(5, localisation);
        psApplicant.setString(6, newProfile[3]);
        psApplicant.setString(7, lastName);
        psApplicant.setString(8, newProfile[4]);
        psApplicant.setString(9, newProfile[1]);
        psApplicant.setString(10, newProfile[0]);
        psApplicant.setInt(11, 1);
        psApplicant.setString(12, "EnTraitement");
        psApplicant.executeUpdate();

        String roles = newProfile[5];
        String[] rolesSplitted = roles.split("[,]\\s");

        String rolesIndexQuery = "SELECT MAX( id ) FROM Rolesc";
        int rolesIndex = 0;
        PreparedStatement psRolesIndex = c.prepareStatement(rolesIndexQuery);
        ResultSet rsRoles = psRolesIndex.executeQuery();
        if (rsRoles.next()) {
          rolesIndex = rsRoles.getInt(1);
        }
        rsRoles.close();

        for (int j = 0; j < rolesSplitted.length; j++) {
          String rolesCheckQuery = "SELECT id FROM Rolesc WHERE title LIKE ? ";
          PreparedStatement psRolesCheck = c.prepareStatement(rolesCheckQuery);
          psRolesCheck.setString(1, rolesSplitted[j]);
          ResultSet rsRolesCheck = psRolesCheck.executeQuery();
          if (!rsRolesCheck.next()) {
            String rolesQuery =
                "INSERT INTO Rolesc (id,version,created_on,title,created_by) "
                    + "VALUES ( ? , ? , ? , ? , ? );";
            PreparedStatement psRoles = c.prepareStatement(rolesQuery);
            psRoles.setInt(1, rolesIndex + 1);
            psRoles.setInt(2, 0);
            psRoles.setTimestamp(3, dateTime);
            psRoles.setString(4, rolesSplitted[j]);
            psRoles.setInt(5, 1);
            psRoles.executeUpdate();
            rolesIndex++;
          }
          rsRolesCheck.close();

          String roleIdQuery = "select id from rolesc where title like ? ";
          PreparedStatement psRoleIdQuery = c.prepareStatement(roleIdQuery);
          psRoleIdQuery.setString(1, rolesSplitted[j]);
          ResultSet rsRolesId = psRoleIdQuery.executeQuery();
          if (rsRolesId.next()) {
            int currentRoleId = rsRolesId.getInt(1);
            String roleIdInsert = "insert into applicant_rolesc(applicant,rolesc) values ( ? , ? )";
            PreparedStatement psRoleIdInsert = c.prepareStatement(roleIdInsert);
            psRoleIdInsert.setInt(1, applicantID);
            psRoleIdInsert.setInt(2, currentRoleId);
            psRoleIdInsert.executeUpdate();
          }
          rsRolesId.close();
        }

        String competences = newProfile[6];
        String[] competencesSplitted = competences.split("[,]\\s");

        String compIndexQuery = "SELECT MAX( id ) FROM Competences";
        int compIndex = 0;
        PreparedStatement psCompIndex = c.prepareStatement(compIndexQuery);
        ResultSet rsComps = psRolesIndex.executeQuery();
        rsComps = psCompIndex.executeQuery();
        if (rsComps.next()) {
          compIndex = rsComps.getInt(1);
        }
        rsComps.close();

        for (int j = 0; j < competencesSplitted.length; j++) {
          String compsCheckQuery = "SELECT id FROM Competences WHERE title LIKE ? ";
          PreparedStatement psCompsCheck = c.prepareStatement(compsCheckQuery);
          psCompsCheck.setString(1, competencesSplitted[j]);
          ResultSet rsCompsCheck = psCompsCheck.executeQuery();
          if (!rsCompsCheck.next()) {
            String competencesQuery =
                "INSERT INTO Competences (id,version,created_on,title,created_by) "
                    + "VALUES ( ? , ? , ? , ? , ? );";
            PreparedStatement psComp = c.prepareStatement(competencesQuery);
            psComp.setInt(1, compIndex + 1);
            psComp.setInt(2, 0);
            psComp.setTimestamp(3, dateTime);
            psComp.setString(4, competencesSplitted[j]);
            psComp.setInt(5, 1);
            psComp.executeUpdate();
            compIndex++;
          }
          rsCompsCheck.close();

          String compIdQuery = "select id from competences where title like ? ";
          PreparedStatement psCompIdQuery = c.prepareStatement(compIdQuery);
          psCompIdQuery.setString(1, competencesSplitted[j]);
          ResultSet rsCompId = psCompIdQuery.executeQuery();
          if (rsCompId.next()) {
            int currentCompId = rsCompId.getInt(1);
            String compIdInsert =
                "insert into applicant_competences(applicant,competences) values ( ? , ? )";
            PreparedStatement psCompIdInsert = c.prepareStatement(compIdInsert);
            psCompIdInsert.setInt(1, applicantID);
            psCompIdInsert.setInt(2, currentCompId);
            psCompIdInsert.executeUpdate();
          }
          rsCompId.close();
        }
      }
      rsCheck.close();
    }
    c.close();
  }
}
