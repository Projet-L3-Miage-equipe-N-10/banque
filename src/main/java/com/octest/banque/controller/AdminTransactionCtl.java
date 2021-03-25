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
 * Servlet implementation class AdminTransactionCtl
 */

@WebServlet(name = "AdminTransactionCtl", urlPatterns = { "/controller/AdminTransactionCtl" })
public class AdminTransactionCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(AdminTransactionCtl.class);

	/**
	 * Validate input Data Entered By Transaction
	 * 
	 *
	 */

	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("AdminTransactionCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("accNo"))) {
			request.setAttribute("accNo", PropertyReader.getValue("error.require", "Account No"));
			pass = false;
		}
		
		if ("-----Select-----".equalsIgnoreCase(request.getParameter("tType"))) {
			request.setAttribute("tType", PropertyReader.getValue("error.require", "Transaction Type"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("tAmount"))) {
			request.setAttribute("tAmount", PropertyReader.getValue("error.require", "Amount"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			pass = false;
		}

		

		log.debug("AdminTransactionCtl Method validate Ended");
		return pass;
	}

	/**
	 * Populates bean object from request parameters
	 * 
	 * 
	 */

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("AdminTransactionCtl Method populatebean Started");
		TransactionBean bean = new TransactionBean();
		
		//Lire les donner de la requette 
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setTransactionType(DataUtility.getString(request.getParameter("tType")));
		bean.setTransactionAmount(DataUtility.getDouble(request.getParameter("tAmount")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));
		bean.setFromAccountNo(DataUtility.getLong(request.getParameter("accNo")));
		populateDTO(bean, request);
		log.debug("AdminTransactionCtl Method populatebean Ended");
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
		log.debug("AdminTransactionCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		TransactionModel model = new TransactionModel();
		long id = DataUtility.getLong(request.getParameter("id"));
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
		log.debug("AdminTransactionCtl Method doGet Ended");
	}

	/**
	 * Contains submit logic
	 */
  
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("in post method");
		log.debug("AdminTransactionCtl Method doPost Started");
		
		// stocker les données saisiee dans le model
		String op = DataUtility.getString(request.getParameter("operation"));
		TransactionModel model = new TransactionModel();

		long id = DataUtility.getLong(request.getParameter("id"));
		HttpSession session=request.getSession();
		if (OP_SAVE.equalsIgnoreCase(op)) {
			TransactionBean bean = (TransactionBean) populateBean(request);
			try {
						AccountBean acBean=new AccountModel().findByAccountNo(bean.getFromAccountNo());
						if(acBean==null) {
							ServletUtility.setErrorMessage("Account no not is available please check customer list and add to account", request);
							ServletUtility.forward(getView(), request, response);
							return;
						}
						
						if(bean.getTransactionType().equalsIgnoreCase("Credit")) {
							acBean.setBalance(acBean.getBalance()-bean.getTransactionAmount());
						}if(bean.getTransactionType().equalsIgnoreCase("Debit")) {
							if(acBean.getBalance()<bean.getTransactionAmount()) {
								ServletUtility.setErrorMessage("Insufficiant Balance", request);
								ServletUtility.forward(getView(), request, response);
								return;
							}
							acBean.setBalance(acBean.getBalance()+bean.getTransactionAmount());
						}
						bean.setTrasactionDate(new Date());
						bean.setToAccountNo(bean.getFromAccountNo());
						bean.setTrasactionId(DataUtility.getRandom());
						new AccountModel().update(acBean);
	                    long pk = model.add(bean);
	                    ServletUtility.setSuccessMessage("Data Save SuccesFully !!",request);
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
			ServletUtility.redirect(BSView.ADMIN_TRANSACTION_CTL, request, response);
			return;
		}
		
		ServletUtility.forward(getView(), request, response);
		log.debug("AdminTransactionCtl Method doPost Ended");
	}

	/**
	 *  Faire in forward vers la vue JSP
	 * 
	 */
	@Override
	protected String getView() {
		return BSView.ADMIN_TRANSACATION_VIEW;
	}

}
