package com.tingleff.yassg.dynamic.likes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.tingleff.yassg.search.types.TDevice;

public class LikeJDBCStorageBackend implements LikeStorageBackend {

	private String driverClass = "com.mysql.jdbc.Driver";

	private String url = "jdbc:mysql://localhost/test";

	private String username = "";

	private String password = "";

	private boolean initialized = false;

	@Override
	public void like(TDevice device, long page) throws IOException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = connect();
			boolean liked = doLiked(conn, device, page);
			if (liked)
				return;
			ps = conn.prepareStatement("insert into likes (page_id, device_id, ip) values (?, ?, ?)");
			ps.setLong(1, page);
			ps.setLong(2, device.getId().getId());
			ps.setString(3, device.getIp());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			close(ps);
			close(conn);
		}
	}

	@Override
	public int count(long page) throws IOException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = connect();
			ps = conn.prepareStatement("select count(device_id) from likes where page_id = ?");
			ps.setLong(1, page);
			rs = ps.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			return count;
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			close(rs);
			close(ps);
			close(conn);
		}
	}

	@Override
	public boolean liked(TDevice device, long page) throws IOException {
		Connection conn = null;
		try {
			conn = connect();
			return doLiked(conn, device, page);
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			close(conn);
		}
	}

	private boolean doLiked(Connection conn, TDevice device, long page) throws IOException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("select count(device_id) from likes where page_id = ? and device_id = ?");
			ps.setLong(1, page);
			ps.setLong(2, device.getId().getId());
			rs = ps.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			return (count > 0);
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			close(rs);
			close(ps);
		}
	}

	public void init() throws Exception {
		Class.forName(driverClass).newInstance();
	}

	void createTables() throws IOException {
		if (initialized)
			return;
		Connection conn = null;
		try {
			conn = connect();
			Statement st = conn.createStatement();
			st.execute("CREATE TABLE IF NOT EXISTS likes (page_id BIGINT NOT NULL, device_id BIGINT NOT NULL, ip VARCHAR(15) NOT NULL, PRIMARY KEY(page_id, device_id))");
			conn.commit();
			initialized = true;
		} catch (SQLException e) {
			throw new IOException(e);
		} finally {
			close(conn);
		}
	}

	private Connection connect() throws SQLException {
		Connection conn = null;
		conn = DriverManager.getConnection(url, username, password);
		return conn;
	}

	private void close(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch(Exception e) { }
	}

	private void close(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch(Exception e) { }		
	}

	private void close(PreparedStatement ps) {
		try {
			if (ps != null)
				ps.close();
		} catch(Exception e) { }
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
