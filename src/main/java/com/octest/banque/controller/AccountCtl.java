 
package com.octest.banque.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.octest.banque.bean.AccountBean;
import com.octest.banque.bean.BaseBean;
import com.octest.banque.exception.ApplicationException;
import com.octest.banque.exception.DuplicateRecordException;
import com.octest.banque.model.AccountModel;
import com.octest.banque.util.DataUtility;
import com.octest.banque.util.DataValidator;
import com.octest.banque.util.PropertyReader;
import com.octest.banque.util.ServletUtility;

/**
 *  Servlet implementation class AccountCtl
 */

@WebServlet(name = "Account", urlPatterns = { "/controller/AccountCtl" })
public class AccountCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(AccountCtl.class);

	/**
	 * Validate input Data Entered By Account
	 * 
	 * 
	 * 
	 */

	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("AccountRegistrationCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("accNo"))) {
			request.setAttribute("accNo", PropertyReader.getValue("error.require", "Account No."));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("balance"))) {
			request.setAttribute("balance", PropertyReader.getValue("error.require", "Balance"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("dfLimit"))) {
			request.setAttribute("dfLimit", PropertyReader.getValue("error.require", "Over Draft Limit"));
			pass = false;
		}

		if ("-----Select-----".equalsIgnoreCase(request.getParameter("accType"))) {
			request.setAttribute("accType", PropertyReader.getValue("error.require", "Account Type"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("iRate"))) {
			request.setAttribute("iRate", PropertyReader.getValue("error.require", "Intrest Rate"));
			pass = false;
		}

		log.debug("AccountRegistrationCtl Method validate Ended");
		return pass;
	}

	/**
	 * Populates bean object from request parameters
	 * 
	 *
	 * 
	 */

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("AccountRegistrationCtl Method populatebean Started");
		AccountBean bean = new AccountBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setAcc_No(DataUtility.getLong(request.getParameter("accNo")));
		bean.setBalance(DataUtility.getDouble(request.getParameter("balance")));
		bean.setOverDraftLimit(DataUtility.getDouble(request.getParameter("dfLimit")));
		bean.setIntrestRate(DataUtility.getDouble(request.getParameter("iRate")));
		bean.setAccType(DataUtility.getString(request.getParameter("accType")));
		populateDTO(bean, request);
		log.debug("AccountRegistrationCtl Method populatebean Ended");
		return bean;
	}

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	/**
	 * Contains display logic
	 */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("AccountRegistrationCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		AccountModel model = new AccountModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		long accNo=DataUtility.getLong(request.getParameter("acN"));		

		if(accNo>0) {
				AccountBean acbean=new AccountBean();
				acbean.setAcc_No(accNo);
				ServletUtility.setBean(acbean, request);
			}
			
		if (id > 0 || op != null) {
			System.out.println("in id>0 condition");
			AccountBean bean;
			try {
				bean = model.findByPK(id);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("AccountRegistrationCtl Method doGet Ended");
	}

	/**
	 * Contains submit logic
	 */

/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

  
  
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("in post method");
		log.debug("AccountRegistrationCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		AccountModel model = new AccountModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {
			AccountBean bean = (AccountBean) populateBean(request);
			try {
				 if (id > 0) {
	                    model.update(bean);
	                    ServletUtility.setSuccessMessage("Data is successfully Updated", request);
	                } else {
	                    long pk = model.add(bean);
	                    ServletUtility.setSuccessMessage("Data is successfully saved",request);
	                }
				 ServletUtility.setBean(bean, request);
			} catch (DuplicateRecordException e) {
				log.error(e);
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage(e.getMessage(), request);
				ServletUtility.forward(getView(), request, response);
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				e.printStackTrace();
				return;
			}
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(BSView.ACCOUNT_CTL, request, response);
			return;
		}
		
		ServletUtility.forward(getView(), request, response);
		log.debug("AccountRegistrationCtl Method doPost Ended");
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	@Override
	protected String getView() {
		return BSView.ACCOUNT_VIEW;
	}

}
