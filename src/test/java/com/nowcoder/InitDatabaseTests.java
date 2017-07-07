package com.nowcoder;

import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {
@Autowired
	UserDAO userDAO;

	@Test
	public void initDatabase() {
	    Random random = new Random();

        for (int i = 0; i < 11; i++) {
            User user = new User();
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
            userDAO.addUser(user);

            user.setPassword("xxx");
            userDAO.updatePassword(user);
        }

        Assert.assertEquals("xxx", userDAO.selectById(1).getPassword());
        userDAO.deleteById(1);
        Assert.assertNull("xxx", userDAO.selectById(1));
    }

}
