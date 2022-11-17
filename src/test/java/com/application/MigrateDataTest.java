package com.application;


import com.application.entity.kitchen.store.Purchase;
import com.application.service.store.IPurchaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MigrateDataTest {

    @Autowired
    private IPurchaseService purchaseService;

    @Test
    public void migrateDataFromHerokuToDatabase() {
        String databaseUrl = "jdbc:postgresql://ec2-44-206-137-96.compute-1.amazonaws.com:5432/d5hu17h19h49fm";
        String userName = "ursqyjhlppmxrn";
        String passWord = "54544337e619c4811590fe73434c644ed1b1dfea3b2650287f2f45deaee47c08";

        try {
            Connection connection = DriverManager.getConnection(databaseUrl, userName, passWord);

            String sql = "select * from kitchen_store_user_purchases";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                purchaseService.savePurchase(new Purchase(
                        resultSet.getInt("room_number"),
                        resultSet.getDouble("purchase_amount"),
                        resultSet.getString("brand"),
                        resultSet.getInt("quantity"),
                        resultSet.getDate("date")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
