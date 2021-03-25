package com.octest.banque.controller;

import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import com.octest.banque.bean.BaseBean;
import com.octest.banque.bean.CustomerBean;
import com.octest.banque.bean.UserBean;
import com.octest.banque.exception.ApplicationException;
import com.octest.banque.exception.DuplicateRecordException;
import com.octest.banque.model.CustomerModel;
import com.octest.banque.model.UserModel;
import com.octest.banque.util.DataUtility;
import com.octest.banque.util.DataValidator;
import com.octest.banque.util.PropertyReader;
import com.octest.banque.util.ServletUtility;


/**
 * Servlet implementation class CustomerCtl
 */

@WebServlet(name = "Customer", urlPatterns = { "/controller/CustomerCtl" })
public class CustomerCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(CustomerCtl.class);

	/**
	 * Validate input Data Entered By Customer
	 * 
	 * 
	 * 
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("CustomerRegistrationCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		} 
		

		if (DataValidator.isNull(request.getParameter("email"))) {
			request.setAttribute("email", PropertyReader.getValue("error.require", "Email Id"));
			pass = false;
		} else if (!DataValidator.isEmail(request.getParameter("email"))) {
			request.setAttribute("email", PropertyReader.getValue("error.email", "Email"));
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("mobile"))) {
			request.setAttribute("mobile", PropertyReader.getValue("error.require", "Phone No"));
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("address"))) {
			request.setAttribute("address", PropertyReader.getValue("error.require", "Address"));
			pass = false;
		}
		log.debug("CustomerRegistrationCtl Method validate Ended");
		return pass;
	}

	/**
	 * Populates bean object from request parameters
	 * 
	 * @param request
	 * @return
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("CustomerRegistrationCtl Method populatebean Started");
		CustomerBean bean = new CustomerBean();
		//Lire les donner de la requette 

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setEmailId(DataUtility.getString(request.getParameter("email")));
		bean.setAddress(DataUtility.getString(request.getParameter("address")));
		bean.setMobileNo(DataUtility.getString(request.getParameter("mobile")));
		
		populateDTO(bean, request);
		log.debug("CustomerRegistrationCtl Method populatebean Ended");
		return bean;
	}

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CustomerCtl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Contains display logic
	 */
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CustomerRegistrationCtl Method doGet Started");
		
		HttpSession session=request.getSession(true);
		UserBean userBean=(UserBean) session.getAttribute("user");
		long id=userBean.getId();
		String op=DataUtility.getString(request.getParameter("operation"));
		CustomerModel model=new CustomerModel();
		
		if(id>0||op !=null){
			System.out.println("in id>0 condition");
			CustomerBean bean;
			try{
				bean=model.findByUserId(id);
				ServletUtility.setBean(bean, request);
				
			}catch(ApplicationException e){
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
		 log.debug("CustomerRegistrationCtl Method doGet Ended");

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
		log.debug("CustomerRegistrationCtl Method doPost Started");
		// stocker les données saisiee dans le model

		String op = DataUtility.getString(request.getParameter("operation"));
		CustomerModel model = new CustomerModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {
			CustomerBean bean = (CustomerBean) populateBean(request);
			HttpSession session=request.getSession(true);
			UserBean uBean=(UserBean)session.getAttribute("user");
			try {
				bean.setUserId(uBean.getId());
				 if (id > 0) {
	                    model.update(bean);
	                    uBean.setCustomer(bean);
	                    ServletUtility.setSuccessMessage("Data is successfully Updated", request);
	                } else {
	                    long pk = model.add(bean);
	                    uBean.setCustomer(model.findByPK(pk));
	                    ServletUtility.setSuccessMessage("Data is successfully saved",request);
	                }
				 session.setAttribute("user",uBean);
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
			ServletUtility.redirect(BSView.CUSTOMER_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("CustomerRegistrationCtl Method doPost Ended");
	}

	/**
	 * Faire in forward vers la vue JSP
	 * 
	 * 
	 */
	@Override
	protected String getView() {
		return BSView.CUSTOMER_VIEW;
	}

}
