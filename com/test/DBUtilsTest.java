package com.test;

import org.junit.Test;

import jdbc.utils.DBUtils;

public class DBUtilsTest {
	
	@Test
	public void testDB() throws Exception
	{
		DBUtils db = new DBUtils();
		String sql = "drop table Employee";
		db.updateByPreparedStatement(sql, null);
	}
}
