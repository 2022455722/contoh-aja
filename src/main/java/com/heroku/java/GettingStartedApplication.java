package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model; // haziq tambah utk part business
import com.heroku.java.model.businessModel; // haziq tambah utk part business
import org.springframework.boot.autoconfigure.domain.EntityScan;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.jscience.physics.amount.Amount;
import org.jscience.physics.model.RelativisticModel;
import javax.measure.unit.SI;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Logger; // haziq tambah utk part business
import java.util.logging.Level; // haziq tambah utk part business

@SpringBootApplication
@Controller
public class GettingStartedApplication {
    private final DataSource dataSource;

    private static final Logger LOGGER = Logger.getLogger(GettingStartedApplication.class.getName()); // haziq tambah utk part business


    @Autowired
    public GettingStartedApplication(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //GetMapping for pages 
    @GetMapping("/")
    public String index() {
        return "index";
    }

    //haziq tmbah (possible delete) 
    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/StaffRegister")
    public String StaffRegister() {
        return "StaffRegister";
    }

    @GetMapping("/Register")
    public String Register() {
        return "Register";
    }

    @GetMapping("/MemberRegister")
    public String MemberRegister() {
        return "MemberRegister";
    }

    @GetMapping("/Announcement")
    public String Announcement() {
        return "Announcement";
    }

    // @GetMapping("/EditAccountDetail")
    // public String EditAccountDetail() {
    //     return "EditAccountDetail";
    // }

    @GetMapping("/StaffEditAccount")
    public String StaffEditAccount() {
        return "StaffEditAccount";
    }


    @GetMapping("/StaffViewAccountList")
    public String StaffViewAccountList() {
        return "StaffViewAccountList";
    }

    @GetMapping("/ViewAccountDetail")
    public String ViewAccountDetail() {
        return "ViewAccountDetail";
    }

    // @GetMapping("/AdminViewAccountList")
    // public String AdminViewAccountList() {
    //     return "AdminViewAccountList";
    // }

    // @GetMapping("/AdminViewAccount")
    // public String AdminViewAccount() {
    //     return "AdminViewAccount";
    // }

    @GetMapping("/MemberEditAccount")
    public String MemberEditAccount() {
        return "MemberEditAccount";
    }

    @GetMapping("/StaffViewAccount")
    public String StaffViewAccount() {
        return "StaffViewAccount";
    }

    @GetMapping("/MakePayment")
    public String MakePayment() {
        return "MakePayment";
    }

    @GetMapping("/ViewPaymentStat")
    public String ViewPaymentStat() {
        return "ViewPaymentStat";
    }

    @GetMapping("/testpage")
    public String testpage() {
        return "testpage";
    }

    @GetMapping("/payments")
    public String payments() {
        return "payments";
    }

    //GetMapping for business - Haziq start tambah sini
    @GetMapping("/createBusiness")
    public String createBusiness() {
        return "createBusiness";
    }




    @GetMapping("/convert")
    String convert(Map<String, Object> model) {
        RelativisticModel.select();

        final var result = java.util.Optional
                .ofNullable(System.getenv().get("ENERGY"))
                .map(Amount::valueOf)
                .map(energy -> "E=mc^2: " + energy + " = " + energy.to(SI.KILOGRAM))
                .orElse("ENERGY environment variable is not set!");

        model.put("result", result);
        return "convert";
    }

    @GetMapping("/database")
    String database(Map<String, Object> model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            statement.executeUpdate("INSERT INTO ticks VALUES (now())");

            final var resultSet = statement.executeQuery("SELECT tick FROM ticks");
            final var output = new ArrayList<>();
            while (resultSet.next()) {
                output.add("Read from DB: " + resultSet.getTimestamp("tick"));
            }

            model.put("records", output);
            return "database";

        } catch (Throwable t) {
            model.put("message", t.getMessage());
            return "error";
        }
    }


    

    public static void main(String[] args) {
        SpringApplication.run(GettingStartedApplication.class, args);
    }

}


