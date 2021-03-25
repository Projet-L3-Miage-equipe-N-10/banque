package com.octest.banque.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.octest.banque.bean.BaseBean;
import com.octest.banque.bean.UserBean;
import com.octest.banque.exception.ApplicationException;
import com.octest.banque.exception.RecordNotFoundException;
import com.octest.banque.model.UserModel;
import com.octest.banque.util.DataUtility;
import com.octest.banque.util.ServletUtility;


/**
 * Servlet implementation class ForgetPasswordCtl
 */
/**
 * ForgetPassword functionality Controller
 * 
 * 
 */
@WebServlet(name = "ForgetPasswordCtl", urlPatterns = { "/ForgetPasswordCtl" })
public class ForgetPasswordCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ForgetPasswordCtl.class);

	/**
	 * Validate input Data Entered By User
	 * 
	 */

	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("ForgetPasswordCtl validate  Method Started");
		System.out.println("ForgetPasswordCtl validate  Method Started");
		boolean pass = true;


		return pass;
	}

	/**
	 * Populates bean object from request parameters
	 * 
	 * 
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("ForgetPasswordCtl Method populatebean Started");

		UserBean bean = new UserBean();

		bean.setLogin(DataUtility.getString(request.getParameter("login")));

		log.debug("ForgetPasswordCtl Method populatebean Ended");

		return bean;
	}

	/**
	 * Contains display logic
	 */
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("ForgetPasswordCtl Method doGet Started");
		String page = DataUtility.getString(request.getParameter("page"));

		if (page != null) {
			response.sendRedirect(page);
			return;
		}
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Contains submit logic
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("ForgetPasswordCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));

		UserBean bean = (UserBean) populateBean(request);

		// get model
		UserModel model = new UserModel();

		if (OP_GO.equalsIgnoreCase(op)) {

			try {
				model.forgetPassword(bean.getLogin());

				ServletUtility.setSuccessMessage("Password have to be sent to your email.", request);
			} catch (RecordNotFoundException e) {
				ServletUtility.setErrorMessage(e.getMessage(), request);
				log.error(e);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;

			}
			ServletUtility.forward(getView(), request, response);
		}

		log.debug("ForgetPasswordCtl Method doPost Ended");
	}

	@Override
	protected String getView() {
		return BSView.FORGET_PASSWORD_VIEW;
	}

}
