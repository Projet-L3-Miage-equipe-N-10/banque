package com.octest.banque.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;

import com.octest.banque.bean.AccountBean;
import com.octest.banque.bean.BaseBean;
import com.octest.banque.bean.CustomerBean;
import com.octest.banque.bean.TransactionBean;
import com.octest.banque.bean.UserBean;
import com.octest.banque.exception.ApplicationException;
import com.octest.banque.exception.DuplicateRecordException;
import com.octest.banque.model.AccountModel;
import com.octest.banque.model.CustomerModel;
import com.octest.banque.model.TransactionModel;
import com.octest.banque.util.DataUtility;
import com.octest.banque.util.DataValidator;
import com.octest.banque.util.PropertyReader;
import com.octest.banque.util.ServletUtility;

/**
 * Servlet implementation class TransactionCtl
 */

@WebServlet(name = "TransactionCtl", urlPatterns = { "/controller/TransactionCtl" })
public class TransactionCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(TransactionCtl.class);

	/**
	 * Validate input Data Entered By Transaction
	 * 
	 */

	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("TransactionRegistrationCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("tAmount"))) {
			request.setAttribute("tAmount", PropertyReader.getValue("error.require", "Amount"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			pass = false;
		}

		log.debug("TransactionRegistrationCtl Method validate Ended");
		return pass;
	}

	/**
	 * Populates bean object from request parameters
	 * 
	 */

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("TransactionRegistrationCtl Method populatebean Started");
		TransactionBean bean = new TransactionBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setTransactionType(DataUtility.getString(request.getParameter("tType")));
		bean.setTransactionAmount(DataUtility.getDouble(request.getParameter("tAmount")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));
		populateDTO(bean, request);
		log.debug("TransactionRegistrationCtl Method populatebean Ended");
		return bean;
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Contains display logic
	 */

	

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("TransactionRegistrationCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		TransactionModel model = new TransactionModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		long acNo = DataUtility.getLong(request.getParameter("acN"));
		if (acNo > 0) {
			request.getSession().setAttribute("bAc", acNo);
		}
		if (id > 0 || op != null) {
			System.out.println("in id>0 condition");
			TransactionBean bean;
			try {
				bean = model.findByUserId(id);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("TransactionRegistrationCtl Method doGet Ended");
	}

	/**
	 * Contains submit logic
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("in post method");
		log.debug("TransactionRegistrationCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		TransactionModel model = new TransactionModel();

		long id = DataUtility.getLong(request.getParameter("id"));
		HttpSession session = request.getSession();
		if (OP_SAVE.equalsIgnoreCase(op)) {
			TransactionBean bean = (TransactionBean) populateBean(request);
			try {
				AccountBean acBean = null;

				UserBean uBean = (UserBean) session.getAttribute("user");
				long acNo = DataUtility.getLong(String.valueOf(session.getAttribute("bAc")));
				CustomerBean cBean = new CustomerModel().findByUserId(uBean.getId());
				if (cBean != null) {
					acBean = new AccountModel().findByAccountNo(cBean.getAcc_No());
					if (acBean == null) {
						ServletUtility.setErrorMessage("Your account is not Exist Please wait to admin responce   ",
								request);
						ServletUtility.forward(getView(), request, response);
						return;
					}
				} else {
					ServletUtility.setErrorMessage(
							"Please fill your customer form and wait for admin create your account", request);
					ServletUtility.forward(getView(), request, response);
					return;
				}
				if (acBean.getBalance() < bean.getTransactionAmount()) {
					ServletUtility.setErrorMessage("Insufficiant Balance", request);
					ServletUtility.forward(getView(), request, response);
					return;
				}
				bean.setTransactionType("Creadit");
				bean.setTrasactionDate(new Date());
				bean.setToAccountNo(acNo);
				bean.setFromAccountNo(acBean.getAcc_No());
				bean.setTrasactionId(DataUtility.getRandom());
				
				
				acBean.setBalance(acBean.getBalance() - bean.getTransactionAmount());
					
				
				new AccountModel().update(acBean);
				
				
				
				
				long pk = model.add(bean);
				session.setAttribute("tbean", bean);
				ServletUtility.setSuccessMessage("Transaction Done Successfully", request);
				ServletUtility.forward(BSView.RECEIPT_VIEW, request, response);
				return;
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
			ServletUtility.redirect(BSView.TRANSACTION_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("TransactionRegistrationCtl Method doPost Ended");
	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * 
	 */
	@Override
	protected String getView() {
		return BSView.TRANSACATION_VIEW;
	}

}
