package test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {

	public static int numberOfUsers = 2000;
	public static int numberOfBlogs = 2000;
	public static int numberOfComments = 2000;
	public static int numberOfLikes = 90000;

	public static void main(String[] args) {

		// Connection Details
		String urlNormalized = "jdbc:mysql://192.168.122.71:3306/normalized";
		String urlDenormalized = "jdbc:mysql://192.168.122.71:3306/denormalized";
		String user = "proxyuser";
		String password = "test1234";

		// executeInsertNormalized(urlNormalized, user, password);

		// // Tag Top Blogger From Normalized Table
		// long startTime = System.nanoTime();
		// HashMap<String, String> resultSingle =
		// tagTopBlogPosterNormalized(urlNormalized,
		// user, password);
		// long estimatedTime = System.nanoTime() - startTime;
		// double seconds = (double) estimatedTime / 1000000000.0;
		// System.out.println(resultSingle);
		// System.out.println("Duration: " + seconds);

		// Loop through Normalized Table
		// long startTime = System.nanoTime();
		// ArrayList<HashMap<String, String>> result =
		// selectBlogWithAssociatesNormalized(
		// urlNormalized, user, password);
		// long estimatedTime = System.nanoTime() - startTime;
		// double seconds = (double) estimatedTime / 1000000000.0;
		// System.out.println(result);
		// System.out.println("Duration: " + seconds);

		// // Get Single Instance From Normalized Table
		// long startTime = System.nanoTime();
		// HashMap<String, String> resultSingle =
		// selectBlogWithAssociatesNormalizedSingle(
		// urlNormalized, user, password, 22);
		// long estimatedTime = System.nanoTime() - startTime;
		// double seconds = (double) estimatedTime / 1000000000.0;
		// System.out.println(resultSingle);
		// System.out.println("Duration: " + seconds);

		// updateUserNormalizedWithSwords(urlNormalized, user, password);

		// // INNO DB Transaction
		// long startTime = System.nanoTime();
		// HashMap<String, String> resultSingle =
		// moveItemBetweenUserNormalizedinnoDBTransaction(
		// urlNormalized, user, password, 1, 2, 3);
		// long estimatedTime = System.nanoTime() - startTime;
		// double seconds = (double) estimatedTime / 1000000000.0;
		// System.out.println(resultSingle);
		// System.out.println("Duration: " + seconds);

		// // Application based Transaction
		// long startTime = System.nanoTime();
		// HashMap<String, String> resultSingle =
		// moveItemBetweenUserNormalizedTransaction(
		// urlNormalized, user, password, 1, 2, 3);
		// long estimatedTime = System.nanoTime() - startTime;
		// double seconds = (double) estimatedTime / 1000000000.0;
		// System.out.println(resultSingle);
		// System.out.println("Duration: " + seconds);

		// // With no Transaction
		// long startTime = System.nanoTime();
		// HashMap<String, String> resultSingle = moveItemBetweenUserNormalized(
		// urlNormalized, user, password, 1, 2, 3);
		// long estimatedTime = System.nanoTime() - startTime;
		// double seconds = (double) estimatedTime / 1000000000.0;
		// System.out.println(resultSingle);
		// System.out.println("Duration: " + seconds);

		// executeInsertDenormalized(urlDenormalized, user, password);

		// // Tag Top Blogger From Denormalized Table
		 long startTime = System.nanoTime();
		 HashMap<String, String> resultSingle =
		 tagTopBlogPosterDenormalized(urlDenormalized,
		 user, password);
		 long estimatedTime = System.nanoTime() - startTime;
		 double seconds = (double) estimatedTime / 1000000000.0;
		 System.out.println(resultSingle);
		 System.out.println("Duration: " + seconds);

		// // Loop through Denormalized Table
		// long startTime = System.nanoTime();
		// ArrayList<HashMap<String, String>> result =
		// selectBlogWithAssociatesDenormalized(
		// urlDenormalized, user, password);
		// long estimatedTime = System.nanoTime() - startTime;
		// double seconds = (double) estimatedTime / 1000000000.0;
		// System.out.println(result);
		// System.out.println("Duration: " + seconds);

		// Get Single Instance From Denormalized Table
		// long startTime = System.nanoTime();
		// HashMap<String, String> resultSingle =
		// selectBlogWithAssociatesDenormalizedSingle(
		// urlDenormalized, user, password, 98765);
		// long estimatedTime = System.nanoTime() - startTime;
		// double seconds = (double) estimatedTime / 1000000000.0;
		// System.out.println(resultSingle);
		// System.out.println("Duration: " + seconds);

	}

	public static String randomText(Integer bits) {

		SecureRandom random = new SecureRandom();
		String randomString = new BigInteger(bits, random).toString(32);
		return randomString;

	}

	public static int randomNumber(Integer min, Integer max) {

		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static void executeInsertDenormalized(String url, String user,
			String password) {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			// Create Connection
			con = DriverManager.getConnection(url, user, password);

			// Helper Variable
			int count;
			Map<String, String> resultSet = null;
			Map<String, String> resultSetComment = null;
			Map<String, String> resultSetUser = null;

			// try {
			// con.setAutoCommit(false);
			// con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }

			// Insert User
			for (count = 0; count < numberOfUsers; count++) {
				String insertUserStmt = "insert into User(vorname, nachname, email) values(?, ?, ?)";
				PreparedStatement preparedUserStmt = con
						.prepareStatement(insertUserStmt);
				preparedUserStmt.setString(1, randomText(100));
				preparedUserStmt.setString(2, randomText(150));
				preparedUserStmt.setString(3, randomText(120) + "@"
						+ randomText(20) + "." + randomText(10));
				preparedUserStmt.execute();
			}

			// Insert Blog
			for (count = 0; count < numberOfBlogs; count++) {
				// get existing user
				resultSet = selectUserById(url, user, password,
						randomNumber(1, numberOfUsers), con);
				String insertBlogStmt = "insert into Blog(blogpost, u_id, u_vorname, u_nachname, u_email) values(?,?,?,?,?)";
				PreparedStatement preparedBlogStmt = con
						.prepareStatement(insertBlogStmt);
				preparedBlogStmt.setString(1, randomText(5000));
				preparedBlogStmt
						.setInt(2, Integer.valueOf(resultSet.get("id")));
				preparedBlogStmt.setString(3, resultSet.get("vorname"));
				preparedBlogStmt.setString(4, resultSet.get("nachname"));
				preparedBlogStmt.setString(5, resultSet.get("email"));
				preparedBlogStmt.execute();

			}

			// Insert Comment
			for (count = 0; count < numberOfComments; count++) {
				String insertCommentStmt = "insert into Comment(comment, u_id, u_vorname, u_nachname, u_email, b_id, b_blogpost, b_user_id, b_vorname, b_nachname, b_email) values(?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement preparedCommentStmt = con
						.prepareStatement(insertCommentStmt);
				preparedCommentStmt.setString(1, randomText(2000));
				// get existing blog and user
				resultSet = selectUserById(url, user, password,
						randomNumber(1, numberOfUsers), con);
				preparedCommentStmt.setInt(2,
						Integer.valueOf(resultSet.get("id")));
				preparedCommentStmt.setString(3, resultSet.get("vorname"));
				preparedCommentStmt.setString(4, resultSet.get("nachname"));
				preparedCommentStmt.setString(5, resultSet.get("email"));
				resultSet = selectBlogByIdDenormalized(url, user, password,
						randomNumber(1, numberOfBlogs), con);
				preparedCommentStmt.setInt(6,
						Integer.valueOf(resultSet.get("id")));
				preparedCommentStmt.setString(7, resultSet.get("blogpost"));
				preparedCommentStmt.setInt(8,
						Integer.valueOf(resultSet.get("u_id")));
				preparedCommentStmt.setString(9, resultSet.get("u_vorname"));
				preparedCommentStmt.setString(10, resultSet.get("u_nachname"));
				preparedCommentStmt.setString(11, resultSet.get("u_email"));
				preparedCommentStmt.execute();
			}

			// Insert Likes
			for (count = 0; count < numberOfLikes; count++) {
				String insertLikeStmt = " insert into Likes(u_id, u_vorname, u_nachname, u_email, c_id, c_comment, c_user_id, c_vorname, c_nachname, c_email, "
						+ "b_id, b_blogpost, b_user_id, b_vorname, b_nachname, b_email) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				PreparedStatement preparedLikeStmt = con
						.prepareStatement(insertLikeStmt);
				// get existing user
				resultSetUser = selectUserById(url, user, password,
						randomNumber(1, numberOfUsers), con);
				preparedLikeStmt.setInt(1,
						Integer.valueOf(resultSetUser.get("id")));
				preparedLikeStmt.setString(2, resultSetUser.get("vorname"));
				preparedLikeStmt.setString(3, resultSetUser.get("nachname"));
				preparedLikeStmt.setString(4, resultSetUser.get("email"));
				// get existing comment
				resultSetComment = selectCommentByIdDenormalized(url, user,
						password, randomNumber(1, numberOfComments), con);
				preparedLikeStmt.setInt(5,
						Integer.valueOf(resultSetComment.get("id")));
				preparedLikeStmt.setString(6, resultSetComment.get("comment"));
				preparedLikeStmt.setInt(7,
						Integer.valueOf(resultSetComment.get("u_id")));
				preparedLikeStmt
						.setString(8, resultSetComment.get("u_vorname"));
				preparedLikeStmt.setString(9,
						resultSetComment.get("u_nachname"));
				preparedLikeStmt.setString(10, resultSetComment.get("u_email"));
				preparedLikeStmt.setInt(11,
						Integer.valueOf(resultSetComment.get("b_id")));
				preparedLikeStmt.setString(12,
						resultSetComment.get("b_blogpost"));
				preparedLikeStmt.setInt(13,
						Integer.valueOf(resultSetComment.get("b_user_id")));
				preparedLikeStmt.setString(14,
						resultSetComment.get("b_vorname"));
				preparedLikeStmt.setString(15,
						resultSetComment.get("b_nachname"));
				preparedLikeStmt.setString(16, resultSetComment.get("b_email"));
				try {
					preparedLikeStmt.execute();
				} catch (SQLException ex) {
					if (ex instanceof SQLIntegrityConstraintViolationException) {
						System.out.println(preparedLikeStmt.toString());
						System.out.println("Duplicate Key Error --> b_id = "
								+ resultSetUser.get("id") + " c_id = "
								+ resultSetComment.get("id"));
					} else {
						Logger lgr = Logger.getLogger(Test.class.getName());
						lgr.log(Level.SEVERE, ex.getMessage(), ex);
					}
				}
			}

			// con.commit();

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(Test.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(Test.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

	}

	public static void executeInsertNormalized(String url, String user,
			String password) {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			// Create Connection
			con = DriverManager.getConnection(url, user, password);

			// Helpver Variable
			int count;

			try {
				con.setAutoCommit(false);
				con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Insert User
			for (count = 0; count < numberOfUsers; count++) {
				String insertUserStmt = "insert into User(vorname, nachname, email) values(?, ?, ?)";
				PreparedStatement preparedUserStmt = con
						.prepareStatement(insertUserStmt);
				preparedUserStmt.setString(1, randomText(100));
				preparedUserStmt.setString(2, randomText(150));
				preparedUserStmt.setString(3, randomText(120) + "@"
						+ randomText(20) + "." + randomText(10));
				preparedUserStmt.execute();
			}

			// Insert Blog
			for (count = 0; count < numberOfBlogs; count++) {
				String insertBlogStmt = "insert into Blog(blogpost, user_id) values(?,?)";
				PreparedStatement preparedBlogStmt = con
						.prepareStatement(insertBlogStmt);
				preparedBlogStmt.setString(1, randomText(5000));
				preparedBlogStmt.setInt(2, randomNumber(1, numberOfUsers));
				preparedBlogStmt.execute();
			}

			// Insert Comment
			for (count = 0; count < numberOfComments; count++) {
				String insertCommentStmt = "insert into Comment(text, user_id, blog_id) values(?,?,?)";
				PreparedStatement preparedCommentStmt = con
						.prepareStatement(insertCommentStmt);
				preparedCommentStmt.setString(1, randomText(2000));
				preparedCommentStmt.setInt(2, randomNumber(1, numberOfUsers));
				preparedCommentStmt.setInt(3, randomNumber(1, numberOfBlogs));
				preparedCommentStmt.execute();
			}

			// Insert Likes
			for (count = 0; count < numberOfLikes; count++) {
				String insertLikeStmt = "insert into Likes(user_id, comment_id) values(?,?)";
				PreparedStatement preparedLikeStmt = con
						.prepareStatement(insertLikeStmt);
				preparedLikeStmt.setInt(1, randomNumber(1, numberOfUsers));
				preparedLikeStmt.setInt(2, randomNumber(1, numberOfBlogs));
				try {
					preparedLikeStmt.execute();
				} catch (SQLException ex) {
					if (ex instanceof SQLIntegrityConstraintViolationException) {
						System.out.println("Duplicate Key Error");
					} else {
						Logger lgr = Logger.getLogger(Test.class.getName());
						lgr.log(Level.SEVERE, ex.getMessage(), ex);
					}
				}
			}

			// con.commit();

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(Test.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(Test.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

	}

	// Helper Method
	private static Map<String, String> selectUserById(String url, String user,
			String password, int id, Connection con) {

		// Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<String, String> resultSet = null;

		try {
			// Create Connection
			// con = DriverManager.getConnection(url, user, password);

			// Create Prepared Query Statement
			pst = con
					.prepareStatement("select id, vorname, nachname, email from User where id=?;");
			pst.setInt(1, id);

			// Execute Query
			rs = pst.executeQuery();

			// Loop through Result and build Result Set
			resultSet = new HashMap<String, String>();
			while (rs.next()) {
				resultSet.put("id", String.valueOf(rs.getInt(1)));
				resultSet.put("vorname", rs.getString(2));
				resultSet.put("nachname", rs.getString(3));
				resultSet.put("email", rs.getString(4));
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(Test.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				// if (con != null) {
				// con.close();
				// }

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(Test.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

		return resultSet;
	}

	// Helper Method
	private static Map<String, String> selectBlogByIdDenormalized(String url,
			String user, String password, int id, Connection con) {

		// Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<String, String> resultSet = null;

		try {
			// Create Connection
			// con = DriverManager.getConnection(url, user, password);

			// Create Prepared Query Statement
			pst = con
					.prepareStatement("select id, blogpost, u_id, u_vorname, u_nachname, u_email from Blog where id=?;");
			pst.setInt(1, id);

			// Execute Query
			rs = pst.executeQuery();

			// Loop through Result and build Result Set
			resultSet = new HashMap<String, String>();
			while (rs.next()) {
				resultSet.put("id", String.valueOf(rs.getInt(1)));
				resultSet.put("blogpost", rs.getString(2));
				resultSet.put("u_id", String.valueOf(rs.getInt(3)));
				resultSet.put("u_vorname", rs.getString(4));
				resultSet.put("u_nachname", rs.getString(5));
				resultSet.put("u_email", rs.getString(6));
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(Test.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				// if (con != null) {
				// con.close();
				// }

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(Test.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

		return resultSet;
	}

	// Helper Method
	private static Map<String, String> selectCommentByIdDenormalized(
			String url, String user, String password, int id, Connection con) {

		// Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<String, String> resultSet = null;

		try {
			// Create Connection
			// con = DriverManager.getConnection(url, user, password);

			// Create Prepared Query Statement
			pst = con
					.prepareStatement("select id, comment, u_id, u_vorname, u_nachname, u_email, "
							+ "b_id, b_blogpost, b_user_id, b_vorname, b_nachname, b_email from Comment where id=?;");
			pst.setInt(1, id);

			// Execute Query
			rs = pst.executeQuery();

			// Loop through Result and build Result Set
			resultSet = new HashMap<String, String>();
			while (rs.next()) {
				resultSet.put("id", String.valueOf(rs.getInt(1)));
				resultSet.put("comment", rs.getString(2));
				resultSet.put("u_id", String.valueOf(rs.getInt(3)));
				resultSet.put("u_vorname", rs.getString(4));
				resultSet.put("u_nachname", rs.getString(5));
				resultSet.put("u_email", rs.getString(6));
				resultSet.put("b_id", String.valueOf(rs.getInt(7)));
				resultSet.put("b_blogpost", rs.getString(8));
				resultSet.put("b_user_id", String.valueOf(rs.getInt(9)));
				resultSet.put("b_vorname", rs.getString(10));
				resultSet.put("b_nachname", rs.getString(11));
				resultSet.put("b_email", rs.getString(12));
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(Test.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				// if (con != null) {
				// con.close();
				// }

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(Test.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

		return resultSet;
	}

	public static ArrayList<HashMap<String, String>> selectBlogWithAssociatesDenormalized(
			String url, String user, String password) {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		try {
			// Create Connection
			con = DriverManager.getConnection(url, user, password);

			int i;
			for (i = 1; i <= numberOfBlogs; i++) {

				try {
					// Create Prepared Query Statement
					pst = con
							.prepareStatement("select * from Likes where b_id=?;");
					pst.setInt(1, i);

					// Execute Query
					rs = pst.executeQuery();

					// Loop through Result and build Result Set
					HashMap<String, String> resultSet = new HashMap<String, String>();
					while (rs.next()) {
						resultSet.put("id", String.valueOf(rs.getInt(1)));
					}
					result.add(resultSet);

				} catch (SQLException ex) {
					Logger lgr = Logger.getLogger(Test.class.getName());
					lgr.log(Level.SEVERE, ex.getMessage(), ex);

				} finally {
					try {
						if (rs != null) {
							rs.close();
						}
						if (pst != null) {
							pst.close();
						}

					} catch (SQLException ex) {
						Logger lgr = Logger.getLogger(Test.class.getName());
						lgr.log(Level.WARNING, ex.getMessage(), ex);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

		return result;

	}

	public static HashMap<String, String> selectBlogWithAssociatesDenormalizedSingle(
			String url, String user, String password, Integer id) {

		HashMap<String, String> resultSet = null;
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			// Create Connection
			con = DriverManager.getConnection(url, user, password);

			// Create Prepared Query Statement
			pst = con.prepareStatement("select * from Likes where b_id=?;");
			pst.setInt(1, id);

			// Execute Query
			rs = pst.executeQuery();

			// Loop through Result and build Result Set
			resultSet = new HashMap<String, String>();
			while (rs.next()) {
				resultSet.put("id", String.valueOf(rs.getInt(1)));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

		return resultSet;

	}

	public static HashMap<String, String> selectBlogWithAssociatesNormalizedSingle(
			String url, String user, String password, Integer id) {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		HashMap<String, String> resultSet = null;

		try {
			// Create Connection
			con = DriverManager.getConnection(url, user, password);

			// Create Prepared Query Statement
			pst = con
					.prepareStatement("SELECT id_blog, blogpost as blog_blogpost, user_id as blog_user_id, vorname as blog_vorname, nachname as blog_nachname, email as blog_email, comment_text, comment_user_id, comment_blog_id, comment_vorname, comment_nachname, comment_email, like_user_id, like_comment_id, like_vorname, like_nachname, like_email FROM ( "
							+ " SELECT text as comment_text,user_id as comment_user_id,blog_id as comment_blog_id,vorname as comment_vorname,nachname as comment_nachname,email as comment_email,like_user_id,like_comment_id,like_vorname,like_nachname,like_email FROM ( "
							+ " SELECT user_id as like_user_id,comment_id as like_comment_id,vorname as like_vorname,nachname as like_nachname,email as like_email FROM Likes JOIN User ON Likes.user_id = User.id_user "
							+ " ) AS LU RIGHT JOIN Comment ON LU.like_comment_id = Comment.id_comment JOIN User ON Comment.user_id = User.id_user "
							+ " ) AS CLU RIGHT JOIN Blog ON CLU.comment_blog_id = Blog.id_blog JOIN User ON Blog.user_id = User.id_user "
							+ " where id_blog=?; ");
			pst.setInt(1, id);

			// Execute Query
			rs = pst.executeQuery();

			// Loop through Result and build Result Set
			resultSet = new HashMap<String, String>();
			while (rs.next()) {
				resultSet.put("id", String.valueOf(rs.getInt(1)));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

		return resultSet;

	}

	public static ArrayList<HashMap<String, String>> selectBlogWithAssociatesNormalized(
			String url, String user, String password) {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

		try {
			// Create Connection
			con = DriverManager.getConnection(url, user, password);

			int i;
			for (i = 1; i <= numberOfBlogs; i++) {

				try {
					// Create Prepared Query Statement
					pst = con
							.prepareStatement("SELECT id_blog, blogpost as blog_blogpost, user_id as blog_user_id, vorname as blog_vorname, nachname as blog_nachname, email as blog_email, comment_text, comment_user_id, comment_blog_id, comment_vorname, comment_nachname, comment_email, like_user_id, like_comment_id, like_vorname, like_nachname, like_email FROM ( "
									+ " SELECT text as comment_text,user_id as comment_user_id,blog_id as comment_blog_id,vorname as comment_vorname,nachname as comment_nachname,email as comment_email,like_user_id,like_comment_id,like_vorname,like_nachname,like_email FROM ( "
									+ " SELECT user_id as like_user_id,comment_id as like_comment_id,vorname as like_vorname,nachname as like_nachname,email as like_email FROM Likes JOIN User ON Likes.user_id = User.id_user "
									+ " ) AS LU RIGHT JOIN Comment ON LU.like_comment_id = Comment.id_comment JOIN User ON Comment.user_id = User.id_user "
									+ " ) AS CLU RIGHT JOIN Blog ON CLU.comment_blog_id = Blog.id_blog JOIN User ON Blog.user_id = User.id_user "
									+ " where id_blog=?; ");
					pst.setInt(1, i);

					// Execute Query
					rs = pst.executeQuery();

					// Loop through Result and build Result Set
					HashMap<String, String> resultSet = new HashMap<String, String>();
					while (rs.next()) {
						resultSet.put("id", String.valueOf(rs.getInt(1)));
					}
					result.add(resultSet);

				} catch (SQLException ex) {
					Logger lgr = Logger.getLogger(Test.class.getName());
					lgr.log(Level.SEVERE, ex.getMessage(), ex);

				} finally {
					try {
						if (rs != null) {
							rs.close();
						}
						if (pst != null) {
							pst.close();
						}

					} catch (SQLException ex) {
						Logger lgr = Logger.getLogger(Test.class.getName());
						lgr.log(Level.WARNING, ex.getMessage(), ex);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

		return result;

	}

	public static HashMap<String, String> tagTopBlogPosterNormalized(
			String url, String user, String password) {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		HashMap<String, String> resultSet = new HashMap<String, String>();

		try {
			// Create Connection
			con = DriverManager.getConnection(url, user, password);

			pst = con
					.prepareStatement("SELECT user_id FROM Blog GROUP BY user_id ORDER BY count(user_id) DESC, user_id limit 1;");

			// Execute Query
			rs = pst.executeQuery();

			// Loop through Result and build Result Set

			while (rs.next()) {
				pst = con
						.prepareStatement("UPDATE User SET rank = ? WHERE id_user = ?;");
				pst.setString(1, "Top Blogger");
				pst.setInt(2, rs.getInt(1));

				resultSet.put("Result Code",
						String.valueOf(pst.executeUpdate()));
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(Test.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(Test.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		return resultSet;

	}

	public static HashMap<String, String> tagTopBlogPosterDenormalized(
			String url, String user, String password) {

		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		HashMap<String, String> resultSet = new HashMap<String, String>();

		try {
			// Create Connection
			con = DriverManager.getConnection(url, user, password);

			pst = con
					.prepareStatement("SELECT b_user_id FROM (SELECT b_user_id FROM Likes GROUP BY b_id) AS BlogReduced GROUP BY b_user_id ORDER BY count(b_user_id) DESC, b_user_id LIMIT 1;");

			// Execute Query
			rs = pst.executeQuery();

			// Loop through Result and build Result Set

			while (rs.next()) {
				pst = con
						.prepareStatement("UPDATE Likes SET rank = ? WHERE u_id = ?;");
				pst.setString(1, "Top Blogger");
				pst.setInt(2, rs.getInt(1));

				resultSet.put("Result Code",
						String.valueOf(pst.executeUpdate()));
				pst.close();

				pst = con
						.prepareStatement("UPDATE Likes SET rank = ? WHERE b_user_id = ?;");
				pst.setString(1, "Top Blogger");
				pst.setInt(2, rs.getInt(1));

				resultSet.put("Result Code",
						String.valueOf(pst.executeUpdate()));
				pst.close();

				pst = con
						.prepareStatement("UPDATE Likes SET rank = ? WHERE c_user_id = ?;");
				pst.setString(1, "Top Blogger");
				pst.setInt(2, rs.getInt(1));

				resultSet.put("Result Code",
						String.valueOf(pst.executeUpdate()));
			}

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(Test.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(Test.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		return resultSet;

	}

	public static HashMap<String, String> moveItemBetweenUserNormalized(
			String url, String user, String password, Integer from_id,
			Integer to_id, Integer itemcount) {

		// Helper Variables
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		HashMap<String, String> resultSet = new HashMap<String, String>();

		Integer id = 1;

		try {
			// Create Connection
			con = DriverManager.getConnection(url, user, password);

			// Update Item Count
			String transactionStmt = "UPDATE User SET swords = swords + ? WHERE id_user = ?";
			PreparedStatement preparedTransactionStmt = con
					.prepareStatement(transactionStmt);
			preparedTransactionStmt.setInt(1, itemcount);
			preparedTransactionStmt.setInt(2, from_id);
			preparedTransactionStmt.execute();

			// Update Item Count
			transactionStmt = "UPDATE User SET swords = swords - ? WHERE id_user = ?";
			preparedTransactionStmt = con.prepareStatement(transactionStmt);
			preparedTransactionStmt.setInt(1, itemcount);
			preparedTransactionStmt.setInt(2, to_id);
			preparedTransactionStmt.execute();

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(Test.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(Test.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

		return resultSet;

	}

	public static HashMap<String, String> moveItemBetweenUserNormalizedinnoDBTransaction(
			String url, String user, String password, Integer from_id,
			Integer to_id, Integer itemcount) {

		// Helper Variables
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		HashMap<String, String> resultSet = new HashMap<String, String>();

		Integer id = 1;

		try {
			// Create Connection
			con = DriverManager.getConnection(url, user, password);
			con.setAutoCommit(false);

			// Update Item Count
			String transactionStmt = "UPDATE User SET swords = swords + ? WHERE id_user = ?";
			PreparedStatement preparedTransactionStmt = con
					.prepareStatement(transactionStmt);
			preparedTransactionStmt.setInt(1, itemcount);
			preparedTransactionStmt.setInt(2, from_id);
			preparedTransactionStmt.execute();

			// Update Item Count
			transactionStmt = "UPDATE User SET swords = swords - ? WHERE id_user = ?";
			preparedTransactionStmt = con.prepareStatement(transactionStmt);
			preparedTransactionStmt.setInt(1, itemcount);
			preparedTransactionStmt.setInt(2, to_id);
			preparedTransactionStmt.execute();

			con.commit();

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(Test.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(Test.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

		return resultSet;

	}

	public static HashMap<String, String> moveItemBetweenUserNormalizedTransaction(
			String url, String user, String password, Integer from_id,
			Integer to_id, Integer itemcount) {

		// Helper Variables
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		HashMap<String, String> resultSet = new HashMap<String, String>();

		Integer id = 1;

		try {
			// Create Connection
			con = DriverManager.getConnection(url, user, password);

			// Create Transaction Object and set state to INIT
			String transactionStmt = "INSERT INTO Transaction(transaction_id, from_user_id, to_user_id, itemcount, state) VALUES (?,?,?,?,?)";
			PreparedStatement preparedTransactionStmt = con
					.prepareStatement(transactionStmt);
			preparedTransactionStmt.setInt(1, 1);
			preparedTransactionStmt.setInt(2, 1);
			preparedTransactionStmt.setInt(3, 2);
			preparedTransactionStmt.setInt(4, itemcount);
			preparedTransactionStmt.setString(5, "init");
			preparedTransactionStmt.execute();

			// Retrieve Transaction
			pst = con
					.prepareStatement("SELECT * FROM Transaction WHERE transaction_id = ?");
			pst.setInt(1, id);
			rs = pst.executeQuery();

			while (rs.next()) {
				from_id = rs.getInt(2);
				to_id = rs.getInt(3);
				itemcount = rs.getInt(4);
			}

			// Set State to PENDING
			transactionStmt = "UPDATE Transaction SET state = ? WHERE transaction_id = ?";
			preparedTransactionStmt = con.prepareStatement(transactionStmt);
			preparedTransactionStmt.setString(1, "pending");
			preparedTransactionStmt.setInt(2, id);
			preparedTransactionStmt.execute();

			// Set Transaction to 1 and Update Item Count
			transactionStmt = "UPDATE User SET transaction = 1, swords = swords + ? WHERE id_user = ?";
			preparedTransactionStmt = con.prepareStatement(transactionStmt);
			preparedTransactionStmt.setInt(1, itemcount);
			preparedTransactionStmt.setInt(2, from_id);
			preparedTransactionStmt.execute();

			// Set Transaction to 1 and Update Item Count
			transactionStmt = "UPDATE User SET transaction = 1, swords = swords - ? WHERE id_user = ?";
			preparedTransactionStmt = con.prepareStatement(transactionStmt);
			preparedTransactionStmt.setInt(1, itemcount);
			preparedTransactionStmt.setInt(2, to_id);
			preparedTransactionStmt.execute();

			// Set State to COMMITTED
			transactionStmt = "UPDATE Transaction SET state = ? WHERE transaction_id = ?";
			preparedTransactionStmt = con.prepareStatement(transactionStmt);
			preparedTransactionStmt.setString(1, "committed");
			preparedTransactionStmt.setInt(2, id);
			preparedTransactionStmt.execute();

			// Set Transaction to 0
			transactionStmt = "UPDATE User SET transaction = 0 WHERE id_user = ?";
			preparedTransactionStmt = con.prepareStatement(transactionStmt);
			preparedTransactionStmt.setInt(1, from_id);
			preparedTransactionStmt.execute();

			// Set Transaction to 0
			transactionStmt = "UPDATE User SET transaction = 0 WHERE id_user = ?";
			preparedTransactionStmt = con.prepareStatement(transactionStmt);
			preparedTransactionStmt.setInt(1, from_id);
			preparedTransactionStmt.execute();

			// Set State to DONE
			transactionStmt = "UPDATE Transaction SET state = ? WHERE transaction_id = ?";
			preparedTransactionStmt = con.prepareStatement(transactionStmt);
			preparedTransactionStmt.setString(1, "done");
			preparedTransactionStmt.setInt(2, id);
			preparedTransactionStmt.execute();

			// Delete Transaction Object
			transactionStmt = "DELETE FROM Transaction WHERE transaction_id = ?";
			preparedTransactionStmt = con.prepareStatement(transactionStmt);
			preparedTransactionStmt.setInt(1, id);
			preparedTransactionStmt.execute();

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(Test.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(Test.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

		return resultSet;

	}

	public static HashMap<String, String> updateUserNormalizedWithSwords(
			String url, String user, String password) {

		// Helper Variables
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		HashMap<String, String> resultSet = new HashMap<String, String>();

		Integer id = 1;
		Integer from_id = null;
		Integer to_id = null;
		Integer itemcount = null;
		String state = null;

		try {
			// Create Connection
			con = DriverManager.getConnection(url, user, password);

			// Update Statement
			String transactionStmt = "UPDATE User SET swords = 5";
			PreparedStatement preparedTransactionStmt = con
					.prepareStatement(transactionStmt);
			preparedTransactionStmt.execute();

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(Test.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(Test.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

		return resultSet;

	}
}
