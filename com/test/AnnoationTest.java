package com.test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Test;

import com.annotation.impl.AnnotationImpl;
import com.model.Employee;

import jdbc.utils.DBUtils;

public class AnnoationTest {
	
	@Test
	public void test() throws Exception
	{
		Connection connection = null;
		AnnotationImpl annotationTest = new AnnotationImpl();
		String sql = annotationTest.returnSql(Employee.class);
		System.out.println(sql);
		//获取数据连接
		DBUtils baseUtils = new DBUtils();
		connection = baseUtils.getConnection();
		PreparedStatement pst = connection.prepareStatement(sql);
		System.out.println(pst.execute());
	}

}
