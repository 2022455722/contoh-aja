package com.heroku.java.controller;

import com.heroku.java.model.businessModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class businessController {

    private final DataSource dataSource;
    private static final Logger LOGGER = Logger.getLogger(businessController.class.getName());

    @Autowired
    public businessController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Create a new business
    @PostMapping("/createBusiness")
    public String createBusiness(@ModelAttribute businessModel business, Model model) {
        LOGGER.info("Creating business...");

        try (Connection connection = dataSource.getConnection()) {
            if (connection != null) {
                LOGGER.info("Connection established.");

                // Generate businessID based on current year and sequence
                String currentYear = String.valueOf(LocalDate.now().getYear()).substring(2);
                String newBusinessID = generateBusinessID(connection, currentYear);

                // Set businessID into the businessModel object
                business.setBusinessID(newBusinessID);

                // SQL insert statement
                String insertSQL = "INSERT INTO public.Business (businessID, ownerName, ownerPhone, ownerNRIC, ownerEmail, businessType) VALUES (?, ?, ?, ?, ?, ?)";

                // Prepare the statement
                try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
                    statement.setString(1, business.getBusinessID());
                    statement.setString(2, business.getOwnerName());
                    statement.setString(3, business.getOwnerPhone());
                    statement.setString(4, business.getOwnerNRIC());
                    statement.setString(5, business.getOwnerEmail());
                    statement.setString(6, business.getBusinessType());

                    // Execute the update
                    statement.executeUpdate();
                }

                return "redirect:/viewBusinesses"; // Redirect to a relevant page after creation
            } else {
                LOGGER.severe("Failed to establish a connection to the database.");
                model.addAttribute("error", "Failed to connect to the database.");
                return "error"; // Ensure you have an error.html Thymeleaf template
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error while creating business: {0}", e.getMessage());
            model.addAttribute("error", "An error occurred while creating the business.");
            return "error"; // Ensure you have an error.html Thymeleaf template
        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Data access error while creating business: {0}", e.getMessage());
            model.addAttribute("error", "A database access error occurred while creating the business.");
            return "error"; // Ensure you have an error.html Thymeleaf template
        }
    }

    private String generateBusinessID(Connection connection, String currentYear) throws SQLException {
        String selectSQL = "SELECT businessID FROM public.Business WHERE businessID LIKE ? ORDER BY businessID DESC LIMIT 1";
        String prefix = "BIZ" + currentYear;

        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, prefix + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String lastBusinessID = resultSet.getString("businessID");
                    int lastIncrement = Integer.parseInt(lastBusinessID.substring(7)); // Adjust index as per your prefix length
                    int newIncrement = lastIncrement + 1;
                    return prefix + String.format("%04d", newIncrement);
                } else {
                    return prefix + "0001";
                }
            }
        }
    }

    // View all businesses
    @GetMapping("/viewBusinesses")
    public String viewBusinesses(Model model) {
        List<businessModel> businesses = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String selectSQL = "SELECT * FROM public.Business";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSQL)) {
                while (resultSet.next()) {
                    businessModel business = new businessModel();
                    business.setBusinessID(resultSet.getString("businessID"));
                    business.setOwnerName(resultSet.getString("ownerName"));
                    business.setOwnerPhone(resultSet.getString("ownerPhone"));
                    business.setOwnerNRIC(resultSet.getString("ownerNRIC"));
                    business.setOwnerEmail(resultSet.getString("ownerEmail"));
                    business.setBusinessType(resultSet.getString("businessType"));
                    businesses.add(business);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error while fetching businesses: {0}", e.getMessage());
            model.addAttribute("error", "An error occurred while fetching businesses. Please try again later.");
            return "error";
        }
        model.addAttribute("businesses", businesses);
        return "viewBusinesses";
    }

    // View details of a single business
    @GetMapping("/viewBusinessDetails")
    public String viewBusinessDetails(@RequestParam("businessID") String businessID, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String selectSQL = "SELECT * FROM public.Business WHERE businessID = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
                statement.setString(1, businessID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        businessModel business = new businessModel();
                        business.setBusinessID(resultSet.getString("businessID"));
                        business.setOwnerName(resultSet.getString("ownerName"));
                        business.setOwnerPhone(resultSet.getString("ownerPhone"));
                        business.setOwnerNRIC(resultSet.getString("ownerNRIC"));
                        business.setOwnerEmail(resultSet.getString("ownerEmail"));
                        business.setBusinessType(resultSet.getString("businessType"));
                        model.addAttribute("business", business);
                        return "viewBusinessDetails";
                    } else {
                        model.addAttribute("error", "Business not found.");
                        return "error";
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error while fetching business details: {0}", e.getMessage());
            model.addAttribute("error", "An error occurred while fetching business details.");
            return "error";
        }
    }

    // Display edit form for selected business
    @GetMapping("/editBusiness")
    public String editBusiness(@RequestParam("businessID") String businessID, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String selectSQL = "SELECT * FROM public.Business WHERE businessID = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
                statement.setString(1, businessID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        businessModel business = new businessModel();
                        business.setBusinessID(resultSet.getString("businessID"));
                        business.setOwnerName(resultSet.getString("ownerName"));
                        business.setOwnerPhone(resultSet.getString("ownerPhone"));
                        business.setOwnerNRIC(resultSet.getString("ownerNRIC"));
                        business.setOwnerEmail(resultSet.getString("ownerEmail"));
                        business.setBusinessType(resultSet.getString("businessType"));
                        model.addAttribute("business", business);
                        return "editBusiness";
                    } else {
                        model.addAttribute("error", "Business not found.");
                        return "error";
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error while fetching business details: {0}", e.getMessage());
            model.addAttribute("error", "An error occurred while fetching business details.");
            return "error";
        }
    }

    // Save updated business details
    @PostMapping("/saveBusinessDetails")
    public String saveBusinessDetails(@ModelAttribute("business") businessModel updatedBusiness, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String updateSQL = "UPDATE public.Business SET ownerName = ?, ownerPhone = ?, ownerNRIC = ?, ownerEmail = ?, businessType = ? WHERE businessID = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateSQL)) {
                statement.setString(1, updatedBusiness.getOwnerName());
                statement.setString(2, updatedBusiness.getOwnerPhone());
                statement.setString(3, updatedBusiness.getOwnerNRIC());
                statement.setString(4, updatedBusiness.getOwnerEmail());
                statement.setString(5, updatedBusiness.getBusinessType());
                statement.setString(6, updatedBusiness.getBusinessID());
                statement.executeUpdate();
            }
            return "redirect:/viewBusinessDetails?businessID=" + updatedBusiness.getBusinessID();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error while updating business details: {0}", e.getMessage());
            model.addAttribute("error", "An error occurred while updating business details. Please try again later.");
            return "error";
        }
    }

    // Delete a business
    @PostMapping("/deleteBusiness")
    public String deleteBusiness(@RequestParam("businessID") String businessID, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String deleteSQL = "DELETE FROM public.Business WHERE businessID = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
                statement.setString(1, businessID);
                statement.executeUpdate();
                return "redirect:/viewBusinesses"; // Redirect to refresh the business list
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error while deleting business: {0}", e.getMessage());
            model.addAttribute("error", "An error occurred while deleting the business.");
            return "error";
        }
    }
}
