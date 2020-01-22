package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UserDAO {

	DataSource dataSource;

	public UserDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/JSP-BBS-Chat");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	// ログイン
	public int login(String userID, String userPassword) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM USER WHERE userID = ?";
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				if(rs.getString("userPassword").equals(userPassword)) {
					return 1;	// ログリン成功
				}
				return 2;	// パスワード不一致
			} else {
				return 0; // 該当ユーザーが存在しない
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		// データベースエラー
		return -1;
	}

	// ID重複チェック
	public int registerCheck(String userID) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM USER WHERE userID = ?";
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();

			if(rs.next() || userID.equals("")) {
				return 0;	// すでに存在しているユーザー
			} else {
				return 1;	// 登録可能なID
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		// データベースエラー
		return -1;
	}

	// DB登録
	public int register(String userID, String userPassword, String userName, String userAge, String userGender, String userEmail, String userProfile) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String SQL = "INSERT INTO USER VALUES (?, ?, ?, ?, ?, ?, ?);";
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, userPassword);
			pstmt.setString(3, userName);
			pstmt.setInt(4, Integer.parseInt(userAge));
			pstmt.setString(5, userGender);
			pstmt.setString(6, userEmail);
			pstmt.setString(7, userProfile);

			return pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		// データベースエラー
		return -1;
	}
}
