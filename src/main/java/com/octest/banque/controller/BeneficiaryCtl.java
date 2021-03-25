
package com.octest.banque.controller;

import java.io.IOException;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.octest.banque.bean.BaseBean;
import com.octest.banque.bean.BeneficiaryBean;
import com.octest.banque.bean.UserBean;
import com.octest.banque.exception.ApplicationException;
import com.octest.banque.exception.DuplicateRecordException;
import com.octest.banque.model.BeneficiaryModel;
import com.octest.banque.util.DataUtility;
import com.octest.banque.util.DataValidator;
import com.octest.banque.util.PropertyReader;
import com.octest.banque.util.ServletUtility;



/**
 * Servlet implementation class BeneficiaryCtl
 */

@WebServlet(name = "BeneficiaryCtl", urlPatterns = { "/controller/BeneficiaryCtl" })
public class BeneficiaryCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(BeneficiaryCtl.class);

	/**
	 * Validate input Data Entered By Beneficiary
	 * 
	 * 
	 */

	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("BeneficiaryRegistrationCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("accNo"))) {
			request.setAttribute("accNo", PropertyReader.getValue("error.require", "Beneficiary No."));
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("confirmAccNo"))) {
			request.setAttribute("confirmAccNo", PropertyReader.getValue("error.require", "Confirm Account No."));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("bName"))) {
			request.setAttribute("bName", PropertyReader.getValue("error.require", "Bank Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("iban"))) {
			request.setAttribute("iban", PropertyReader.getValue("error.require", "IBAN CODE"));
			pass = false;
		}

		if (!request.getParameter("accNo").equals(request.getParameter("confirmAccNo"))
				&& !"".equals(request.getParameter("confirmAccNo"))) {
			
			request.setAttribute("confirmAccNo",
					PropertyReader.getValue("error.confirmPassword", "Confirm Account No"));
			pass = false;
		}

		log.debug("BeneficiaryRegistrationCtl Method validate Ended");
		return pass;
	}

	/**
	 * Populates bean object from request parameters
	 * 
	 */

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("BeneficiaryRegistrationCtl Method populatebean Started");
		BeneficiaryBean bean = new BeneficiaryBean();
		
		//Lire les donner de la requette 
        bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setAcc_No(DataUtility.getLong(request.getParameter("accNo")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setBankName(DataUtility.getString(request.getParameter("bName")));
		bean.setIBAN(DataUtility.getString(request.getParameter("iban")));
		populateDTO(bean, request);
		log.debug("BeneficiaryRegistrationCtl Method populatebean Ended");
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
		log.debug("BeneficiaryRegistrationCtl Method doGet Started");
		
		String op = DataUtility.getString(request.getParameter("operation"));
		BeneficiaryModel model = new BeneficiaryModel();
		long id = DataUtility.getLong(request.getParameter("id"));
					
		if (id > 0 || op != null) {
			System.out.println("in id>0 condition");
			BeneficiaryBean bean;
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
		log.debug("BeneficiaryRegistrationCtl Method doGet Ended");
	}

	/**
	 * Contains submit logic
	 */

  
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("in post method");
		log.debug("BeneficiaryRegistrationCtl Method doPost Started");
		
		// stocker les données saisiee dans le model
        String op = DataUtility.getString(request.getParameter("operation"));
		BeneficiaryModel model = new BeneficiaryModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {
			BeneficiaryBean bean = (BeneficiaryBean) populateBean(request);
			try {
				UserBean uBean=(UserBean)request.getSession().getAttribute("user");
				bean.setUserId(uBean.getId());
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
			ServletUtility.redirect(BSView.BENEFICIARY_CTL, request, response);
			return;
		}
		
		ServletUtility.forward(getView(), request, response);
		log.debug("BeneficiaryRegistrationCtl Method doPost Ended");
	}

	/**
	 * Faire in forward vers la vue JSP
	 * 
	 * 
	 */
	@Override
	protected String getView() {
		return BSView.BENEFICIARY_VIEW;
	}

}
