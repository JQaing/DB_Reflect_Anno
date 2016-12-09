package jdbc.utils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.model.User;

public class DBUtils {
	private static String USERNAME;
	private static String PASSWORD;
	private static String URL;

	private Connection connection;
	private PreparedStatement pst;
	private ResultSet rs;

	static {
		init();
	}

	public DBUtils() {
		getConnection();
	}

	private static void init() {
		try {
			InputStream inputStream = DBUtils.class.getResourceAsStream("/dev/mysql.properties");
			Properties properties = new Properties();
			properties.load(inputStream);
			// 加载驱动
			Class.forName(properties.getProperty("jdbc.driver"));
			USERNAME = properties.getProperty("jdbc.user");
			PASSWORD = properties.getProperty("jdbc.password");
			URL = properties.getProperty("jdbc.url");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			return connection;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// sql语句中参数的个数
	public int number(String sql) {
		int num = 0;
		char[] c = sql.toCharArray();
		for (int i = 0; i < c.length - 1; i++)
			if (c[i] == '?')
				num++;
		return num;
	}

	// 执行更新，删除，修改操作
	public boolean updateByPreparedStatement(String sql, List<?> params) throws Exception {
		int flag = -1;
		// 参数占位符
		int index = 1;
		pst = connection.prepareStatement(sql);
		// 设置参数
		if (params != null && !params.isEmpty())
			for (int i = 0; i < params.size(); i++)
				pst.setObject(index++, params.get(i));
		flag = pst.executeUpdate();
		System.out.println(flag);
		return (flag > 0) ? true : false;
	}

	// 更据id删除数据
	@SuppressWarnings("rawtypes")
	public boolean deleteById(Integer id, Class cls) throws Exception {
		String sql = "DELETE FROM ? WHERE id = ?";
		String table_name = cls.getSimpleName();
		pst = connection.prepareStatement(sql);
		pst.setString(1, table_name);
		pst.setObject(2, id);
		return pst.execute();
		// int flag = pst.execute();
		// return (flag >= 0) ? true : false;
	}

	// 获取数据
	public List<Map<String, Object>> findResult(String sql, List<?> params) throws Exception {
		int index = 1;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		pst = connection.prepareStatement(sql);
		// 设置参数
		if (params != null && !params.isEmpty())
			for (int i = 0; i < params.size(); i++)
				pst.setObject(index++, params.get(i));
		rs = pst.executeQuery();
		// 获取元数据对象
		ResultSetMetaData metaData = rs.getMetaData();
		int col_count = metaData.getColumnCount();
		while (rs.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i < col_count; i++)
				map.put(metaData.getColumnName(i), rs.getObject(metaData.getColumnName(i)));
			list.add(map);
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T findOneResult(String sql, List<?> params, Class cls) throws Exception {
		T resultObj = null;
		pst = connection.prepareStatement(sql);
		if (params != null && !params.isEmpty()) {
			int index = 1;
			for (int i = 0; i < params.size(); i++)
				pst.setObject(index++, params.get(i));
		}
		rs = pst.executeQuery();
		ResultSetMetaData rsm = rs.getMetaData();
		int rsm_count = rsm.getColumnCount();
		while (rs.next()) {
			resultObj = (T) cls.newInstance();
			for (int i = 0; i < rsm_count; i++) {
				String colummName = rsm.getColumnName(i + 1);
				Object col_value = rs.getObject(colummName);
				Field filed = cls.getDeclaredField(colummName);
				filed.setAccessible(true); // 打开javabean的private访问权限
				filed.set(resultObj, col_value);
			}
		}
		return resultObj;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findMoreResult(String sql, List<?> params, Class cls) throws Exception {
		T resultObj = null;
		pst = connection.prepareStatement(sql);
		if (params != null && !params.isEmpty()) {
			int index = 1;
			for (int i = 0; i < params.size(); i++)
				pst.setObject(index++, params.get(i));
		}
		rs = pst.executeQuery();
		ResultSetMetaData rsm = rs.getMetaData();
		int rsm_count = rsm.getColumnCount();
		List<T> list = new ArrayList<T>();

		while (rs.next()) {
			resultObj = (T) cls.newInstance();
			for (int i = 0; i < rsm_count; i++) {
				String colummName = rsm.getColumnName(i + 1);
				Object col_value = rs.getObject(colummName);
				Field filed = cls.getDeclaredField(colummName);
				filed.setAccessible(true); // 打开javabean的private访问权限
				filed.set(resultObj, col_value);
			}
			list.add(resultObj);
		}
		return list;
	}

	// 关闭资源
	public void closed() {
		try {
			rs.close();
			pst.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// @org.junit.Test
	// public void test() throws Exception
	// {
	// System.out.println(deleteById(3, User.class));
	//// String sql = "select * from user where id = 1";
	//// List<Object> params = new ArrayList<Object>();
	//// user u = findOneResult(sql, params, user.class);
	//// System.out.println(u.toString());
	// }
}
