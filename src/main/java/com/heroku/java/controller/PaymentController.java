package com.heroku.java.controller;

import com.heroku.java.model.paymentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PaymentController {

    @Autowired
    private DataSource dataSource; // Inject DataSource to directly access the database

    @GetMapping("/newPayment")
    public String showNewPaymentForm(Model model) {
        List<String> accountIds = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String selectSQL = "SELECT accountid FROM public.account";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSQL)) {
                while (resultSet.next()) {
                    accountIds.add(resultSet.getString("accountid"));
                }
            }
        } catch (SQLException e) {
            // Handle SQL exception
            model.addAttribute("error", "An error occurred while fetching account IDs.");
            return "error"; // Or handle differently based on your application's error handling strategy
        }

        model.addAttribute("payment", new paymentModel()); // Assuming paymentModel is your payment model class
        model.addAttribute("accountIds", accountIds);
        return "newPayment"; // Ensure this matches your HTML template name
    }

    @PostMapping("/newPayment")
    public String createNewPayment(@ModelAttribute paymentModel payment, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String insertSQL = "INSERT INTO public.payment (paymentTitle, amount, accountId) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setString(1, payment.getPaymentTitle());
                preparedStatement.setBigDecimal(2, payment.getAmount());
                preparedStatement.setString(3, payment.getAccountID()); // Assuming getAccountID() is correct in paymentModel
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            // Handle SQL exception
            model.addAttribute("error", "An error occurred while saving payment details.");
            return "error"; // Or handle differently based on your application's error handling strategy
        }

        return "redirect:/Announcement"; // Redirect to view payments page or any relevant page
    }

    // @GetMapping("/payments")
    // public String viewPayments(Model model) {
       
    //     return "viewPayments";
    // }

    @GetMapping("/payments/AdminViewAccount")
    public String adminViewAccount(Model model) {
        // Logic to fetch and add data to model for admin view
        return "adminViewAccount"; // Return the name of your Thymeleaf template
    }

    // Add other payment-related methods here if needed
}
