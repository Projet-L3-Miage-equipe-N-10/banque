package com.octest.banque.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.octest.banque.bean.BaseBean;
import com.octest.banque.bean.UserBean;
import com.octest.banque.exception.ApplicationException;
import com.octest.banque.exception.DuplicateRecordException;
import com.octest.banque.model.UserModel;
import com.octest.banque.util.DataUtility;
import com.octest.banque.util.DataValidator;
import com.octest.banque.util.PropertyReader;
import com.octest.banque.util.ServletUtility;


/**
 * Servlet implementation class LoginCtl
 */
/**
 * Login functionality Controller. Performs operation for Authentication, and
 * Session Creation and Give permission to access all Functionality regarding
 * Role
 * 
 */
@WebServlet(name = "LoginCtl", urlPatterns = { "/LoginCtl" })
public class LoginCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	public static final String OP_REGISTER = "Register";
	public static final String OP_SIGN_IN = "SignIn";
	public static final String OP_SIGN_UP = "SignUp";
	public static final String OP_LOG_OUT = "logout";
	public static String HIT_URI = null;

	private static Logger log = Logger.getLogger(LoginCtl.class);

	public LoginCtl() {
		super();
		
	}

	/**
	 * Validate input Data Entered By User
	 * 
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		log.debug("LoginCtl Method validate Started");

		boolean pass = true;

		String op = request.getParameter("operation");

		if (OP_SIGN_UP.equals(op) || OP_LOG_OUT.equals(op)) {
			return pass;
		}

		String login = request.getParameter("login");

		if (DataValidator.isNull(login)) {
			request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
			pass = false;

		}
		if (DataValidator.isNull(request.getParameter("password"))) {
			request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
			pass = false;
		}
		log.debug("LoginCtl Method validate Ended");
		return pass;
	}

	/**
	 * Populates bean object from request parameters
	 * 
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("LoginCtl Method populateBean Started");

		UserBean bean = new UserBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setLogin(DataUtility.getString(request.getParameter("login")));

		bean.setPassword(DataUtility.getString(request.getParameter("password")));

		log.debug("LOginCtl Method PopulatedBean End");

		return bean;
	}

	/**
	 * Display Login form
	 * 
	 */
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("Method doGet Started");
		String page = DataUtility.getString(request.getParameter("page"));

		if (page != null) {
			response.sendRedirect(page);
			return;
		}

		HttpSession session = request.getSession(true);
		String op = DataUtility.getString(request.getParameter("operation"));

		UserModel model = new UserModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0) {
			UserBean userBean;
			try {
				userBean = model.findByPK(id);
				ServletUtility.setBean(userBean, request);

			} catch (Exception e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		} else if (OP_LOG_OUT.equals(op)) {
			session = request.getSession(false);
			session.invalidate();
			ServletUtility.setSuccessMessage("You have been logged out successfully", request);
			ServletUtility.forward(BSView.LOGIN_VIEW, request, response);
			return;
		}
		if (session.getAttribute("user") != null) {
			ServletUtility.redirect(BSView.WELCOME_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("Method doGet end");
	}

	/**
	 * Submit Logic
	 */

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug(" LoginCtl Method doPost Started");

		HttpSession session = request.getSession(true);

		String op = DataUtility.getString(request.getParameter("operation"));
		// get Model
		UserModel model = new UserModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SIGN_IN.equalsIgnoreCase(op)) {
			UserBean bean = (UserBean) populateBean(request);
			try {
				bean = model.authenticate(bean.getLogin(), bean.getPassword());
				if (bean != null) {
					
					session.setAttribute("user", bean);
					session.setMaxInactiveInterval(10 * 6000);
						
					
					
					// save state of session remember URL
					String uri = request.getParameter("uri");
					if (uri == null || "null".equalsIgnoreCase(uri)) {
						ServletUtility.redirect(BSView.WELCOME_CTL, request, response);
						return;
					} else {
						ServletUtility.redirect(uri, request, response);
					}
					return;
				} else {
					bean = (UserBean) populateBean(request);
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Invalid LoginId And Password", request);
				}

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);

				return;
			} 
		} else if (OP_SIGN_UP.equalsIgnoreCase(op)) {
			ServletUtility.redirect(BSView.USER_REGISTRATION_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("UserCtl Method doPost Ended");
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 */
	@Override
	protected String getView() {
		return BSView.LOGIN_VIEW;
	}

}
