package com.annotation.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.annotation.MyCloumn;
import com.annotation.MyEntiy;
import com.annotation.MyId;

public class AnnotationImpl {
	@SuppressWarnings("all")
	public String returnSql(Class cls) throws Exception {
		StringBuilder sql = new StringBuilder("create table ");
		addClass(sql, cls);
		return formatSql(sql);
	}

	// 添加类名
	@SuppressWarnings("unchecked")
	private void addClass(StringBuilder sql, Class cls) throws Exception {
		Annotation clsAnnotation = cls.getAnnotation(MyEntiy.class);
		if (clsAnnotation != null) {
			MyEntiy myEntiy = (MyEntiy) clsAnnotation;
			// 获取创建的表名
			String tab_name = myEntiy.name();
			if (tab_name.equals(""))
				tab_name = cls.getSimpleName();
			sql.append(tab_name + "( \n");
			// 创建字段
			addColumns(sql, cls);
		}
	}

	// 添加字段
	@SuppressWarnings("rawtypes")
	private void addColumns(StringBuilder sql, Class cls) throws Exception {
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			//添加主键
			if(field.getAnnotation(MyId.class) != null)
			{
				addPrimaryKey(field, sql);
				continue;
			}
			//添加普通字段
			addCommonAttr(field, sql);
		}
		// 格式化sql
		formatSql(sql);
	}

	//添加普通属性
	@SuppressWarnings("rawtypes")
	private void addCommonAttr(Field field, StringBuilder sql) throws Exception
	{
		String fieldName = getColumnName(field);
		Class fieldType = field.getType();
		// 创建其它字段
		switch (checkFiledType(fieldType)) {
		case 1:
			sql.append(fieldName + " int, \n");
			break;
		case 2:
			sql.append(fieldName + " float, \n");
			break;
		case 3:
			sql.append(fieldName + " double, \n");
			break;
		case 4:
			sql.append(fieldName + " varchar(255), \n");
			break;
		default:
			throw new Exception("对应的数据类型找不到");
		}
	}
	
	// 添加主键
	@SuppressWarnings("rawtypes")
	private void addPrimaryKey(Field field, StringBuilder sql) throws Exception {
		// 字段名
		String fieldName = getColumnName(field);
		Annotation myId = field.getAnnotation(MyId.class);
		Class fieldType = field.getType();
		// 创建主键
		if (myId != null) {
			switch (checkFiledType(fieldType)) {
			case 1:
				sql.append(fieldName + " int not null primary key auto_increment, \n");
				break;
			case 2:
				sql.append(fieldName + " float not null primary key, \n");
				break;
			case 3:
				sql.append(fieldName + " double not null primary key, \n");
				break;
			case 4:
				sql.append(fieldName + " varchar(255) not null primary key, \n");
				break;
			default:
				throw new Exception("对应的数据类型找不到");
			}
		}
	}

	// 得到字段名称
	private String getColumnName(Field field) {
		String fieldName = field.getName();
		Annotation myCloumn = field.getAnnotation(MyCloumn.class);
		if (myCloumn != null) {
			MyCloumn cloumn = (MyCloumn) myCloumn;
			if (!cloumn.name().equals(""))
				fieldName = cloumn.name();
		}
		return fieldName;
	}

	// 判断字段的属性类型
	@SuppressWarnings("rawtypes")
	private int checkFiledType(Class fieldType) {
		if (fieldType == int.class || fieldType == Integer.class)
			return 1;
		if (fieldType == float.class || fieldType == Float.class)
			return 2;
		if (fieldType == double.class || fieldType == Double.class)
			return 3;
		if (fieldType == String.class)
			return 4;
		return 0;
	}

	// 格式化sql语句
	private String formatSql(StringBuilder sql) {
		String sql1 = sql.substring(0, sql.length() - 3);
		return sql1 + ")";
	}
}
