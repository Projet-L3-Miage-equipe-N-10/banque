package com.octest.banque.controller;

import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

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
 *une servlet qui recevra la requÃªte pour effectuer
 * l'operation et enverra la reponse a la� view (useregistration.jsp)
 * Servlet implementation class UserRegistrationCtl
 * une servlet qui obtiendra la requête pour effectuer l'opération(request) et enverra la réponse(answer) à view (registration.jsp).
 */

@WebServlet(name = "UserRegistrationCtl", urlPatterns = { "/UserRegistrationCtl" })
public class UserRegistrationCtl extends BaseCtl {
	public static final String OP_SIGN_UP = "SignUp";

	private static Logger log = Logger.getLogger(UserRegistrationCtl.class);

	/**
	 *Valider les donnees d'entree saisies par l'utilisateur
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("UserRegistrationCtl Method validate Started");

		boolean pass = true;

		String login = request.getParameter("login");
		
		if (DataValidator.isNull(login)) {
			request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("confirmPassword"))) {
			request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("password"))) {
			request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
			pass = false;

		}

		if (!request.getParameter("password").equals(request.getParameter("confirmPassword"))
				&& !"".equals(request.getParameter("confirmPassword"))) {
			/*
			 * ServletUtility.setErrorMessage("Confirm Password did not match", request);
			 */
			request.setAttribute("confirmPassword",
					PropertyReader.getValue("error.confirmPassword", "Confirm Password"));
			pass = false;
		}

		

		log.debug("UserRegistrationCtl Method validate Ended");
		return pass;
	}

	/**
	 * 
	 *Remplit l'objet bean à partir des parametres de la requete
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("UserRegistrationCtl Method populatebean Started");

		UserBean bean = new UserBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setRoleId(2L);

		bean.setLogin(DataUtility.getString(request.getParameter("login")));

		bean.setPassword(DataUtility.getString(request.getParameter("password")));

		bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));

		populateDTO(bean, request);

		log.debug("UserRegistrationCtl Method populatebean Ended");

		return bean;
	}

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserRegistrationCtl() {
		super();
		// TODO Auto-generated constructor stub
	}

	//Methode doGet

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("UserRegistrationCtl Method doGet Started");

		ServletUtility.forward(getView(), request, response);

	}

	/**
	 * Contient la logique de soumission
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("in post method");
		log.debug("UserRegistrationCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		UserModel model = new UserModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SIGN_UP.equalsIgnoreCase(op)) {

			UserBean bean = (UserBean) populateBean(request);
			try {
				long pk = model.registerUser(bean);
				bean.setId(pk);
				ServletUtility.setSuccessMessage("User Successfully Registered", request);
				
			} catch (DuplicateRecordException e) {
				log.error(e);
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
				ServletUtility.forward(getView(), request, response);
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				e.printStackTrace();
				return;
			}
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(BSView.USER_REGISTRATION_CTL, request, response);
			return;
		}
		
		ServletUtility.forward(BSView.USER_REGISTRATION_VIEW, request, response);
		log.debug("UserRegistrationCtl Method doPost Ended");
	}

	/**
	 *Renvoie la page VIEW de ce controleur
	 * 
	 * @return
	 */
	@Override
	protected String getView() {
		return BSView.USER_REGISTRATION_VIEW;
	}

}
