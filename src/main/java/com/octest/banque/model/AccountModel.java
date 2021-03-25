package com.octest.banque.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.octest.banque.bean.AccountBean;
import com.octest.banque.exception.ApplicationException;
import com.octest.banque.exception.DatabaseException;
import com.octest.banque.exception.DuplicateRecordException;
import com.octest.banque.util.JDBCDataSource;
/**
 * JDBC Implementation of AccountModel
 * 
 */
public class AccountModel {
	private static Logger log = Logger.getLogger(AccountModel.class);

	/**
	 * NextPk a Account
	 * 
	 * 
	 */
	public Integer nextPK() throws DatabaseException {
		log.debug("Model nextPK Started");
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM Account");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK End");
		return pk + 1;
	}

	/**
	 * Add a Account
	 * 
	 * 
	 * 
	 * 
	 */
	public long add(AccountBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;
		int pk = 0;

		AccountBean beanExist = findByAccountNo(bean.getAcc_No());
		if (beanExist != null ) {
			throw new DuplicateRecordException("Account is already exist");
		}

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Account VALUES(?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setLong(2, bean.getAcc_No());
			pstmt.setDate(3, new java.sql.Date(new Date().getTime()));
			pstmt.setDouble(4, bean.getBalance());
			pstmt.setDouble(5, bean.getOverDraftLimit());
			pstmt.setString(6, bean.getAccType());
			pstmt.setDouble(7, bean.getIntrestRate());
			pstmt.setString(8, bean.getCreatedBy());
			pstmt.setString(9, bean.getModifiedBy());
			pstmt.setTimestamp(10, bean.getCreatedDatetime());
			pstmt.setTimestamp(11, bean.getModifiedDatetime());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add Account");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		return pk;
	}

	/**
	 * Delete a Account
	 * 
	 * 
	 * 
	 * 
	 */
	public void delete(AccountBean bean) throws ApplicationException {

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM  Account WHERE ID=?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete Account");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}

	/**
	 * Find Account by Name
	 * 
	 * @param login
	 *            : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */
	public AccountBean findByName(String name) throws ApplicationException {
		log.debug("Model findByLogin Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM Account WHERE NAME=?");
		AccountBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new AccountBean();
				bean.setId(rs.getLong(1));
				bean.setAcc_No(rs.getLong(2));
				bean.setOpenDate(rs.getDate(3));
				bean.setBalance(rs.getDouble(4));
				bean.setOverDraftLimit(rs.getDouble(5));
				bean.setAccType(rs.getString(6));
				bean.setIntrestRate(rs.getDouble(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting Account by login");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByLogin End");
		return bean;
	}

	/**
	 * Find Account by Pk
	 * 
	 * @param login
	 *            : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */
	public AccountBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findByPK Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM Account WHERE ID=?");
		AccountBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new AccountBean();
				bean.setId(rs.getLong(1));
				bean.setAcc_No(rs.getLong(2));
				bean.setOpenDate(rs.getDate(3));
				bean.setBalance(rs.getDouble(4));
				bean.setOverDraftLimit(rs.getDouble(5));
				bean.setAccType(rs.getString(6));
				bean.setIntrestRate(rs.getDouble(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting Account by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByPK End");
		return bean;
	}
	
	/**
	 * Find Account by AccountNo
	 * 
	 * @param login
	 *            : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */
	public AccountBean findByAccountNo(long pk) throws ApplicationException {
		log.debug("Model findByPK Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM Account WHERE Acc_No=?");
		AccountBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new AccountBean();
				bean.setId(rs.getLong(1));
				bean.setAcc_No(rs.getLong(2));
				bean.setOpenDate(rs.getDate(3));
				bean.setBalance(rs.getDouble(4));
				bean.setOverDraftLimit(rs.getDouble(5));
				bean.setAccType(rs.getString(6));
				bean.setIntrestRate(rs.getDouble(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting Account by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByPK End");
		return bean;
	}
	
	/**
	 * Find Account by UserId
	 * 
	 * @param login
	 *            : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */

	public AccountBean findByUserId(long userId) throws ApplicationException {
		log.debug("Model findByPK Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM Account WHERE USerID=?");
		AccountBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, userId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new AccountBean();
				bean.setId(rs.getLong(1));
				bean.setAcc_No(rs.getLong(2));
				bean.setOpenDate(rs.getDate(3));
				bean.setBalance(rs.getDouble(4));
				bean.setOverDraftLimit(rs.getDouble(5));
				bean.setAccType(rs.getString(6));
				bean.setIntrestRate(rs.getDouble(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting Account by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model findByPK End");
		return bean;
	}

	/**
	 * Update a Account
	 * 
	 * @param bean
	 * @throws DatabaseException
	 */
	public void update(AccountBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model update Started");
		Connection conn = null;

		AccountBean beanExist = findByAccountNo(bean.getAcc_No());
		if (beanExist != null && !(beanExist.getId() == bean.getId())) {
			throw new DuplicateRecordException("Account is already exist");
		}
		
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE Account SET ACC_NO=?,openDate=?,Balance=?,OverDraftLimit=?,accType=?,IntrestRate=?,"
							+ "CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? WHERE ID=?");
			pstmt.setLong(1, bean.getAcc_No());
			pstmt.setDate(2, new java.sql.Date(beanExist.getOpenDate().getTime()));
			pstmt.setDouble(3, bean.getBalance());
			pstmt.setDouble(4, bean.getOverDraftLimit());
			pstmt.setString(5, bean.getAccType());
			pstmt.setDouble(6, bean.getIntrestRate());
			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDatetime());
			pstmt.setTimestamp(10, bean.getModifiedDatetime());
			pstmt.setLong(11, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Database Exception..", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in updating Account ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model update End");
	}

	/**
	 * Search Account
	 * 
	 * @param bean
	 *            : Search Parameters
	 * @throws DatabaseException
	 */
	public List search(AccountBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Search Account with pagination
	 * 
	 * @return list : List of Users
	 * @param bean
	 *            : Search Parameters
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * 
	 * @throws DatabaseException
	 */
	public List search(AccountBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model search Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM Account WHERE 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}

			if (bean.getAcc_No() > 0) {
				sql.append(" AND ACC_NO = " + bean.getAcc_No());
			}
		}
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" Limit " + pageNo + ", " + pageSize);
		}
		ArrayList list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new AccountBean();
				bean.setId(rs.getLong(1));
				bean.setAcc_No(rs.getLong(2));
				bean.setOpenDate(rs.getDate(3));
				bean.setBalance(rs.getDouble(4));
				bean.setOverDraftLimit(rs.getDouble(5));
				bean.setAccType(rs.getString(6));
				bean.setIntrestRate(rs.getDouble(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in search Account");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model search End");
		return list;
	}
	
	/**
	 * Get List of Account
	 * 
	 * @return list : List of User
	 * @throws DatabaseException
	 */
	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get List of Account with pagination
	 * 
	 * @return list : List of users
	 * @param pageNo
	 *            : Current Page No.
	 * @param pageSize
	 *            : Size of Page
	 * @throws DatabaseException
	 */
	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from Account");
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AccountBean bean = new AccountBean();
				bean.setId(rs.getLong(1));
				bean.setAcc_No(rs.getLong(2));
				bean.setOpenDate(rs.getDate(3));
				bean.setBalance(rs.getDouble(4));
				bean.setOverDraftLimit(rs.getDouble(5));
				bean.setAccType(rs.getString(6));
				bean.setIntrestRate(rs.getDouble(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
				list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting list of Accounts");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model list End");
		return list;

	}

}
