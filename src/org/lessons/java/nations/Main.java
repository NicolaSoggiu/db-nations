package org.lessons.java.nations;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String url = "jdbc:mysql://localhost:3306/db_nations";
        String user = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Search a country: ");
            String userCountry = scan.nextLine();

            String query =
                    "SELECT c.name as Country_name, c.country_id as Id, r.name as Region_name, cn.name as Continent_name " +
                            "FROM countries c " +
                            "JOIN regions r ON c.region_id = r.region_id " +
                            "JOIN continents cn ON r.region_id = cn.continent_id " +
                            "WHERE c.name LIKE ? " +
                            "ORDER BY c.name;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + userCountry + "%");

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

                        System.out.println("Insert a country id: ");
                        String countryIdInput = scan.nextLine();

                        String queryLang =
                                "SELECT l.`language` as Languages " +
                                        "FROM country_languages cl " +
                                        "JOIN languages l ON cl.language_id = l.language_id " +
                                        "WHERE cl.country_id = ?";

                        try (PreparedStatement langPreparedStatement = connection.prepareStatement(queryLang)) {
                            langPreparedStatement.setString(1, countryIdInput);
                            ResultSet langResultSet = langPreparedStatement.executeQuery();
                            if (langResultSet.next()) {
                                String languages = langResultSet.getString("Languages");
                                System.out.println("Languages: " + languages);
                            }
                        } catch (SQLException e) {
                            System.out.println("Unable to execute language query");
                            e.printStackTrace();
                        }

                        String queryStats =
                                "SELECT `year`, population, gdp " +
                                        "FROM country_stats " +
                                        "WHERE country_id = ?";

                        try (PreparedStatement statsPreparedStatement = connection.prepareStatement(queryStats)) {
                            statsPreparedStatement.setString(1, countryIdInput);
                            ResultSet statsResultSet = statsPreparedStatement.executeQuery();
                            while (statsResultSet.next()) {
                                String year = statsResultSet.getString("year");
                                String population = statsResultSet.getString("population");
                                String gdp = statsResultSet.getString("gdp");
                                System.out.println("Year: " + year + ", Population: " + population + ", GDP: " + gdp);
                            }
                        } catch (SQLException e) {
                            System.out.println("Unable to execute country stats query");
                            e.printStackTrace();
                        }
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

        scan.close();
    }
}
