package com.heroku.java.controller;

import com.heroku.java.model.accountModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class accountController {

    private static final Logger LOGGER = Logger.getLogger(accountController.class.getName());

    private final DataSource dataSource;

    @Autowired
    public accountController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

   @PersistenceContext
    private EntityManager entityManager;

    // Login
    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password,
            HttpSession session, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null) {
                String selectSQL = "SELECT * FROM public.account WHERE email = ?";
                try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
                    statement.setString(1, email);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            String storedPassword = resultSet.getString("password");
                            if (storedPassword.equals(password)) {
                                // Password matches, create accountModel object and set all attributes
                                accountModel account = new accountModel();
                                account.setAccountid(resultSet.getString("accountid"));
                                account.setNric(resultSet.getString("nric"));
                                account.setName(resultSet.getString("name"));
                                account.setEmail(resultSet.getString("email"));
                                account.setPassword(resultSet.getString("password"));
                                account.setRole(resultSet.getString("role"));
                                account.setPhone(resultSet.getString("phone"));
                                account.setAddress(resultSet.getString("address"));
                                account.setGender(resultSet.getString("gender"));
                                account.setUserType(resultSet.getString("userType"));
                                account.setStatus(resultSet.getString("status"));

                                // Store account object in session
                                session.setAttribute("account", account);

                                return "redirect:/Announcement";
                            } else {
                                model.addAttribute("error", "Incorrect Password");
                                return "index"; // Return to login page with error message
                            }
                        } else {
                            model.addAttribute("error", "Email does not exist");
                            return "index"; // Return to login page with error message
                        }
                    }
                }
            } else {
                LOGGER.severe("Failed to establish a connection to the database.");
                model.addAttribute("error", "Failed to connect to the database.");
                return "error"; // Ensure you have an error.html Thymeleaf template
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error while fetching account details: {0}", e.getMessage());
            model.addAttribute("error", "An error occurred while fetching account details.");
            return "error"; // Ensure you have an error.html Thymeleaf template
        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Data access error while fetching account details: {0}", e.getMessage());
            model.addAttribute("error", "A database access error occurred while fetching account details.");
            return "error"; // Ensure you have an error.html Thymeleaf template
        }
    }

    // Register
    @PostMapping("/createAccount")
    public String createAccount(@ModelAttribute accountModel account, Model model) {
        LOGGER.info("Creating account...");
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null) {
                LOGGER.info("Connection established.");
                String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2);
                String newAccountID = generateAccountID(connection, currentYear);

                String insertSQL = "INSERT INTO public.account (accountid, nric, name, email, password, role, phone, address, gender, userType, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
                    statement.setString(1, newAccountID);
                    statement.setString(2, account.getNric());
                    statement.setString(3, account.getName());
                    statement.setString(4, account.getEmail());
                    statement.setString(5, account.getPassword());
                    statement.setString(6, account.getRole());
                    statement.setString(7, account.getPhone());
                    statement.setString(8, account.getAddress());
                    statement.setString(9, account.getGender());
                    statement.setString(10, account.getUserType());
                    statement.setString(11, "pending");
                    statement.executeUpdate();
                }
                return "redirect:/"; // Redirect to view accounts page
            } else {
                LOGGER.severe("Failed to establish a connection to the database.");
                model.addAttribute("error", "Failed to connect to the database.");
                return "error"; // Ensure you have an error.html Thymeleaf template
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error while creating account: {0}", e.getMessage());
            model.addAttribute("error", "An error occurred while creating the account.");
            return "error"; // Ensure you have an error.html Thymeleaf template
        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Data access error while creating account: {0}", e.getMessage());
            model.addAttribute("error", "A database access error occurred while creating the account.");
            return "error"; // Ensure you have an error.html Thymeleaf template
        }
    }

    private String generateAccountID(Connection connection, String currentYear) throws SQLException {
        String selectSQL = "SELECT accountid FROM public.account WHERE accountid LIKE ? ORDER BY accountid DESC LIMIT 1";
        String prefix = "AC" + currentYear;

        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, prefix + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String lastAccountID = resultSet.getString("accountid");
                    int lastIncrement = Integer.parseInt(lastAccountID.substring(4));
                    int newIncrement = lastIncrement + 1;
                    return prefix + String.format("%04d", newIncrement);
                } else {
                    return prefix + "0001";
                }
            }
        }
    }

    // Method to display selected account details
    @GetMapping("/AdminViewAccount")
    public String AdminViewAccount(@RequestParam("accountid") String accountid, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String selectSQL = "SELECT * FROM public.account WHERE accountid = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
                statement.setString(1, accountid);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        accountModel account = new accountModel();
                        account.setAccountid(resultSet.getString("accountid"));
                        account.setNric(resultSet.getString("nric"));
                        account.setName(resultSet.getString("name"));
                        account.setEmail(resultSet.getString("email"));
                        account.setPassword(resultSet.getString("password"));
                        account.setRole(resultSet.getString("role"));
                        account.setPhone(resultSet.getString("phone"));
                        account.setAddress(resultSet.getString("address"));
                        account.setGender(resultSet.getString("gender"));
                        account.setUserType(resultSet.getString("userType"));
                        account.setStatus(resultSet.getString("status"));

                        model.addAttribute("account", account);
                        return "AdminViewAccount"; // Return AdminViewAccount.html Thymeleaf template
                    } else {
                        model.addAttribute("error", "Account not found.");
                        return "error"; // Ensure you have an error.html Thymeleaf template
                    }
                }
            }
        } catch (SQLException e) {
            model.addAttribute("error", "An error occurred while fetching account details.");
            return "error"; // Ensure you have an error.html Thymeleaf template
        }
    }

    // GET method to display edit form for personal details
    @GetMapping("/EditAccountDetail")
    public String EditAccountDetail(HttpSession session, Model model) {
        accountModel account = (accountModel) session.getAttribute("account");
        if (account != null) {
            model.addAttribute("account", account);
            return "EditAccountDetail";
        } else {
            model.addAttribute("error", "Account details not found. Please log in again.");
            return "error";
        }
    }

    // Method to display edit form for selected account details
@GetMapping("/AdminEditAccount")
public String adminEditAccount(@RequestParam("accountid") String accountid, Model model) {
    try (Connection connection = dataSource.getConnection()) {
        String selectSQL = "SELECT * FROM public.account WHERE accountid = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, accountid);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    accountModel account = new accountModel();
                    account.setAccountid(resultSet.getString("accountid"));
                    account.setNric(resultSet.getString("nric"));
                    account.setName(resultSet.getString("name"));
                    account.setEmail(resultSet.getString("email"));
                    account.setPassword(resultSet.getString("password"));
                    account.setRole(resultSet.getString("role"));
                    account.setPhone(resultSet.getString("phone"));
                    account.setAddress(resultSet.getString("address"));
                    account.setGender(resultSet.getString("gender"));
                    account.setUserType(resultSet.getString("userType"));
                    account.setStatus(resultSet.getString("status"));

                    model.addAttribute("account", account);
                    return "AdminEditAccount"; // Return EditAccountDetail.html Thymeleaf template
                } else {
                    model.addAttribute("error", "Account not found.");
                    return "error"; // Ensure you have an error.html Thymeleaf template
                }
            }
        }
    } catch (SQLException e) {
        model.addAttribute("error", "An error occurred while fetching account details.");
        return "error"; // Ensure you have an error.html Thymeleaf template
    }
}

// POST method to save updated account details for a selected account
@PostMapping("/saveSelectedAccount")
public String saveSelectedAccount(@ModelAttribute("account") accountModel updatedAccount, Model model) {
    try (Connection connection = dataSource.getConnection()) {
        String updateSQL = "UPDATE public.account SET nric = ?, name = ?, email = ?, role = ?, phone = ?, address = ?, gender = ?, userType = ?, status = ? WHERE accountid = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, updatedAccount.getNric());
            statement.setString(2, updatedAccount.getName());
            statement.setString(3, updatedAccount.getEmail());
            statement.setString(4, updatedAccount.getRole());
            statement.setString(5, updatedAccount.getPhone());
            statement.setString(6, updatedAccount.getAddress());
            statement.setString(7, updatedAccount.getGender());
            statement.setString(8, updatedAccount.getUserType());
            statement.setString(9, updatedAccount.getStatus());
            statement.setString(10, updatedAccount.getAccountid());
            statement.executeUpdate();
        }
        
        return "redirect:/AdminViewAccount?accountid=" + updatedAccount.getAccountid();
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "SQL Error while updating account details: {0}", e.getMessage());
        model.addAttribute("error", "An error occurred while updating account details. Please try again later.");
        return "error";
    }
}

    // POST method to save updated account details for current session
    @PostMapping("/saveAccount")
    public String saveAccount(@ModelAttribute("account") accountModel updatedAccount, HttpSession session,
            Model model) {
        accountModel originalAccount = (accountModel) session.getAttribute("account");
        if (originalAccount != null) {
            originalAccount.setNric(updatedAccount.getNric());
            originalAccount.setName(updatedAccount.getName());
            originalAccount.setEmail(updatedAccount.getEmail());
            originalAccount.setRole(updatedAccount.getRole());
            originalAccount.setPhone(updatedAccount.getPhone());
            originalAccount.setAddress(updatedAccount.getAddress());
            originalAccount.setGender(updatedAccount.getGender());
            originalAccount.setUserType(updatedAccount.getUserType());
            originalAccount.setStatus(updatedAccount.getStatus());

            try (Connection connection = dataSource.getConnection()) {
                String updateSQL = "UPDATE public.account SET nric = ?, name = ?, email = ?, role = ?, phone = ?, address = ?, gender = ?, userType = ?, status = ? WHERE accountid = ?";
                try (PreparedStatement statement = connection.prepareStatement(updateSQL)) {
                    statement.setString(1, originalAccount.getNric());
                    statement.setString(2, originalAccount.getName());
                    statement.setString(3, originalAccount.getEmail());
                    statement.setString(4, originalAccount.getRole());
                    statement.setString(5, originalAccount.getPhone());
                    statement.setString(6, originalAccount.getAddress());
                    statement.setString(7, originalAccount.getGender());
                    statement.setString(8, originalAccount.getUserType());
                    statement.setString(9, originalAccount.getStatus());
                    statement.setString(10, originalAccount.getAccountid());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "SQL Error while updating account details: {0}", e.getMessage());
                model.addAttribute("error",
                        "An error occurred while updating account details. Please try again later.");
                return "error";
            }

            session.setAttribute("account", originalAccount);
            return "redirect:/ViewAccountDetail";
        } else {
            model.addAttribute("error", "Account details not found. Please log in again.");
            return "error";
        }
    }

    // Display the list of accounts
    @GetMapping("/AdminViewAccountList")
    public String listAccounts(Model model) {
        List<accountModel> accounts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String selectSQL = "SELECT accountid, name, role, userType FROM public.account";
            try (Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(selectSQL)) {
                while (resultSet.next()) {
                    accountModel account = new accountModel();
                    account.setAccountid(resultSet.getString("accountid"));
                    account.setName(resultSet.getString("name"));
                    account.setRole(resultSet.getString("role"));
                    account.setUserType(resultSet.getString("userType"));
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            // Handle SQL exception
            model.addAttribute("error", "An error occurred while fetching account details.");
            return "error";
        }

        model.addAttribute("accounts", accounts);
        return "AdminViewAccountList";
    }

    // Delete Account
    @PostMapping("/deleteAccount")
    public String deleteAccount(@RequestParam("accountid") String accountId, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String deleteSQL = "DELETE FROM public.account WHERE accountid = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
                statement.setString(1, accountId);
                statement.executeUpdate();
                return "redirect:/AdminViewAccountList"; // Redirect to account list after deletion
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error while deleting account: {0}", e.getMessage());
            model.addAttribute("error", "An error occurred while deleting account. Please try again later.");
            return "error"; // Ensure you have an error.html Thymeleaf template
        } catch (DataAccessException e) {
            LOGGER.log(Level.SEVERE, "Data access error while deleting account: {0}", e.getMessage());
            model.addAttribute("error", "A database access error occurred while deleting account.");
            return "error"; // Ensure you have an error.html Thymeleaf template
        }
    }

}



