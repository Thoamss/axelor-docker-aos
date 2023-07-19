package com.axelor.apps.traitement.service.tools;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CityExport {

  public static void initCities() throws SQLException {
    Connection c =
        DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/Matching_Axelor", "app-axelor", "app-pass");
    Statement s = c.createStatement();
    s.executeUpdate("DROP TABLE IF EXISTS ImportedCities");

    s.executeUpdate(
        "CREATE TABLE ImportedCities(\r\n"
            + "  id int PRIMARY KEY,\r\n"
            + "  version int4,\r\n"
            + "  created_on timestamp,\r\n"
            + "  code_commune varchar,\r\n"
            + "  code_region varchar,\r\n"
            + "  code_departement varchar,\r\n"
            + "  nom_capital varchar,\r\n"
            + "  libelle varchar,\r\n"
            + "  created_by int4\r\n"
            + ");");
    s.close();
    c.close();
  }

  public static void executeCityQuery() throws SQLException, IOException {
    Connection c =
        DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/Matching_Axelor", "app-axelor", "app-pass");
    List<String> cities = CSVExtraction.getCities();
    for (int i = 1; i < cities.size(); i++) {
      String[] splittedCity = cities.get(i).split(";");
      String[] newCity = {
        splittedCity[0], splittedCity[1], splittedCity[2], splittedCity[3], splittedCity[5]
      };
      String libelle = newCity[4];
      if (libelle.matches("(.)*['](.)*")) {
        String[] tempLibelle = libelle.split("'");
        int libelleLength = tempLibelle.length;
        libelle = "";
        for (int j = 0; j < libelleLength - 1; j++) {
          libelle += tempLibelle[j] + "''";
        }
        libelle += tempLibelle[libelleLength - 1];
      }
      String cityQuery =
          "INSERT INTO ImportedCities (id,version,code_commune,code_region,code_departement,nom_capital,libelle,created_by) "
              + "VALUES ( ? , ? , ? , ? , ? , ? , ? , ? );";
      PreparedStatement psCities = c.prepareStatement(cityQuery);
      psCities.setInt(1, i);
      psCities.setInt(2, 1);
      psCities.setString(3, newCity[0]);
      psCities.setString(4, newCity[1]);
      psCities.setString(5, newCity[2]);
      psCities.setString(6, newCity[3]);
      psCities.setString(7, libelle);
      psCities.setInt(8, 1);
      psCities.executeUpdate();
    }
    c.close();
  }
}
