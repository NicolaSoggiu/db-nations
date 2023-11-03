package org.lessons.java.nations;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/db_nations";
        String user = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query =
                    "SELECT c.name as Country_name, c.country_id as Id, r.name as Region_name, cn.name as Continent_name " +
                            "FROM countries c " +
                            "JOIN regions r ON c.region_id = r.region_id " +
                            "JOIN continents cn ON r.region_id = cn.continent_id " +
                            "ORDER BY c.name ;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String countryName = resultSet.getString("Country_name");
                        String countryId = resultSet.getString("Id");
                        String regionName = resultSet.getString("Region_name");
                        String continentName = resultSet.getString("Continent_name");
                        System.out.println("\tCountry Name: " + countryName + " " +
                                "\tCountry Id: " + countryId + " " +
                                "\tRegion Name: " + regionName + " " +
                                "\tContinent Name: " + continentName);
                    }
                } catch (SQLException e) {
                    System.out.println("Unable to execute query");
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                System.out.println("Unable to prepare statement");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Unable to open connection");
            e.printStackTrace();
        }
    }
}