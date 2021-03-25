package com.octest.banque.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.octest.banque.bean.BaseBean;
import com.octest.banque.bean.CustomerBean;
import com.octest.banque.exception.ApplicationException;
import com.octest.banque.model.CustomerModel;
import com.octest.banque.util.DataUtility;
import com.octest.banque.util.PropertyReader;
import com.octest.banque.util.ServletUtility;



/**
 * Servlet implementation class CustomerListCtl
 */

@WebServlet(name = "CustomerListCtl", urlPatterns = { "/controller/CustomerListCtl" })
public class CustomerListCtl extends BaseCtl {
	private static Logger log = Logger.getLogger(CustomerListCtl.class);
	/**
	 * Populates bean object from request parameters
	 * 
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("CustomerListCtl populateBean method start");
		CustomerBean bean = new CustomerBean();
		bean.setAcc_No(DataUtility.getLong(request.getParameter("accNo")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		log.debug("CustomerListCtl populateBean method end");
		return bean;
	}

	/**
	 * Contains Display logics
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CustomerListCtl doGet Start");
		List list = null;

		int pageNo = 1;

		int pageSize = DataUtility.getInt(PropertyReader
				.getValue("page.size"));

		CustomerBean bean = (CustomerBean) populateBean(request);

		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");

		CustomerModel model = new CustomerModel();
		try {
			list = model.search(bean, pageNo, pageSize);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		log.debug("CustomerListCtl doPOst End");
	}

	/**
	 * Contains Submit logics
	 */
	@Override

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		log.debug("CustomerListCtl doPost Start");
		
		
		List list = null;
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		
		CustomerBean bean = (CustomerBean) populateBean(request);
		
		String op = DataUtility.getString(request.getParameter("operation"));
		// get the selected checkbox ids array for delete list
		
		String[] ids = request.getParameterValues("ids");
		
		CustomerModel model = new CustomerModel();
		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op) || "Previous".equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(BSView.CUSTOMER_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					CustomerBean deletebean = new CustomerBean();
					for (String id : ids) {
						deletebean.setId(DataUtility.getInt(id));
						model.delete(deletebean);
					}
					ServletUtility.setSuccessMessage("Data Deleted Successfully", request);
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(BSView.CUSTOMER_LIST_CTL, request, response);
				return;

			}

			list = model.search(bean, pageNo, pageSize);
			
			
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		log.debug("CustomerListCtl doGet End");

	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * @return
	 */
	@Override
	protected String getView() {
		return BSView.CUSTOMER_LIST_VIEW;
	}
}
