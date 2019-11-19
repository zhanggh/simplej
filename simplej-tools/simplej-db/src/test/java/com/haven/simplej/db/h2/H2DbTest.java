package com.haven.simplej.db.h2;

import org.junit.Test;

import java.sql.*;

/**
 * @author: havenzhang
 * @date: 2019/9/6 19:35
 * @version 1.0
 */
public class H2DbTest {

	@Test
	public void test() throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.
				getConnection("jdbc:h2:mem:cib", "root", "123456");
		// add application code here
		Statement stmt = conn.createStatement();

		stmt.executeUpdate("CREATE TABLE TEST_MEM(ID INT PRIMARY KEY,NAME VARCHAR(255)  NOT NULL DEFAULT '' COMMENT '部门编号');");
		stmt.executeUpdate("INSERT INTO TEST_MEM VALUES(1, 'Hello_Mem');");
		ResultSet rs = stmt.executeQuery("SELECT * FROM TEST_MEM");
		while (rs.next()) {
			System.out.println(rs.getInt("ID") + "," + rs.getString("NAME"));
		}
	}
}
