package com.octest.banque.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.octest.banque.bean.BaseBean;
import com.octest.banque.bean.UserBean;
import com.octest.banque.exception.ApplicationException;
import com.octest.banque.exception.RecordNotFoundException;
import com.octest.banque.model.UserModel;
import com.octest.banque.util.DataUtility;
import com.octest.banque.util.DataValidator;
import com.octest.banque.util.PropertyReader;
import com.octest.banque.util.ServletUtility;

/**
 * Servlet implementation class ChangePasswordCtl
 */
@WebServlet(name = "ChangePasswordCtl", urlPatterns = { "/controller/ChangePasswordCtl" })
public class ChangePasswordCtl extends BaseCtl {
	private static final long serialVersionUID = 1L;
	
	
/*Le flux de contrôle est composé d'un Logger et Handler. Le premier est appelé par l'application pour prendre en 
*charge le message à sauvegarder. Il le passe à Handler qui s'occupe de sauvegarder
*physiquement le message sur un support précis (fichier, mémoire, socket, stream, console).
*/

	private static final Logger log = Logger.getLogger(ChangePasswordCtl.class);

	public static final String OP_CHANGE_MY_PROFILE = "Change My Profile";
	public static final String OP_CHANGE_MY_PASSWORD = "ChangePassword";

	/**
	 * Validates input data entered by User
	 * 
	 *
	 */

	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("ChangePasswordCtl  validate method start");

		boolean pass = true;

		String op = request.getParameter("operation");

		if (OP_CHANGE_MY_PROFILE.equalsIgnoreCase(op)) {

			return pass;
		}
		
		if (DataValidator.isNull(request.getParameter("oldPassword"))) {
			request.setAttribute("oldPassword", PropertyReader.getValue("error.require", "Old Password"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("newPassword"))) {
			request.setAttribute("newPassword", PropertyReader.getValue("error.require", "New Password"));
			pass = false;
		} 
		if (DataValidator.isNull(request.getParameter("confirmPassword"))) {
			request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
			pass = false;
		}
		if (!request.getParameter("newPassword").equals(request.getParameter("confirmPassword"))
				&& !"".equals(request.getParameter("confirmPassword"))) {
			ServletUtility.setErrorMessage("New and confirm passwords not matched", request);

			pass = false;
		}

		log.debug("ChangePasswordCtl  validate method end");
		return pass;
	}
	/**
	 * Populates bean object from request parameters
	 * 
	 *
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("ChangePasswordCtl  populateBean method start");

		UserBean bean = new UserBean();
		bean.setPassword(DataUtility.getString(request.getParameter("oldPassword")));

		bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));
		populateDTO(bean, request);
		log.debug("ChangePasswordCtl  populateBean method end");
		return bean;
	}

	/**
	 * Display Logics inside this method
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("ChangePasswordCtl  doGet method start");
		ServletUtility.forward(getView(), request, response);
		log.debug("ChangePasswordCtl  doGet method end");
	}

	/**
	 * Submit logic inside it
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("ChangePasswordCtl  doPost method start");

		HttpSession session = request.getSession(true);

		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		UserModel model = new UserModel();
		UserBean bean = (UserBean) populateBean(request);

		UserBean UserBean = (UserBean) session.getAttribute("user");

		String newPassword = request.getParameter("newPassword");
		
		long id = UserBean.getId();
		
		if (OP_SAVE.equalsIgnoreCase(op)) {
			try {
				boolean flag = model.changePassword(id, bean.getPassword(), newPassword);
		
				if (flag == true) {
				
					bean = model.findByLogin(UserBean.getLogin());
					
					session.setAttribute("user", bean);
					
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Password has been changed Successfully", request);
				}
			} catch (ApplicationException e) {

				ServletUtility.handleException(e, request, response);
				return;

			} catch (RecordNotFoundException e) {
				ServletUtility.setErrorMessage("Old Password is Invalid", request);
			}
		} else if (OP_CHANGE_MY_PROFILE.equalsIgnoreCase(op)) {
			ServletUtility.redirect(BSView.MY_PROFILE_CTL, request, response);
			return;
		}

		ServletUtility.forward(BSView.CHANGE_PASSWORD_VIEW, request, response);
		log.debug("ChangePasswordCtl  doPost method end");
	}

	/**
	 *  Faire in forward vers la vue JSP
	 * 
	 *
	 */
	@Override
	protected String getView() {
		
		return BSView.CHANGE_PASSWORD_VIEW;
	}

}
