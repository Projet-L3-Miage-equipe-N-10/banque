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
import com.octest.banque.bean.TransactionBean;
import com.octest.banque.bean.UserBean;
import com.octest.banque.exception.ApplicationException;
import com.octest.banque.model.CustomerModel;
import com.octest.banque.model.TransactionModel;
import com.octest.banque.util.DataUtility;
import com.octest.banque.util.PropertyReader;
import com.octest.banque.util.ServletUtility;

 
/**
 * Servlet implementation class TransactionListCtl
 */


@SuppressWarnings("serial")
@WebServlet(name = "TransactionListCtl", urlPatterns = { "/controller/TransactionListCtl" })
public class TransactionListCtl extends BaseCtl {
	private static Logger log = Logger.getLogger(TransactionListCtl.class);
	/**
	 * Populates bean object from request parameters
	 * 
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("TransactionListCtl populateBean method start");
		TransactionBean bean = new TransactionBean();
		bean.setToAccountNo(DataUtility.getLong(request.getParameter("accNo")));
		bean.setFromAccountNo(DataUtility.getLong(request.getParameter("fromAccountNo")));
		bean.setTrasactionId(DataUtility.getLong(request.getParameter("tId")));
		log.debug("TransactionListCtl populateBean method end");
		return bean;
	}

	/**
	 * Contains Display logics
	 */

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("TransactionListCtl doGet Start");
		@SuppressWarnings("rawtypes")
		List list = null;

		int pageNo = 1;

		int pageSize = DataUtility.getInt(PropertyReader
				.getValue("page.size"));

		TransactionBean bean = (TransactionBean) populateBean(request);

		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");

		TransactionModel model = new TransactionModel();
		try {
			
			UserBean uBean=(UserBean)request.getSession().getAttribute("user");
			if(uBean.getRoleId()==2) {
				CustomerBean cBean=new CustomerModel().findByUserId(uBean.getId());
				if(cBean!=null) {
					bean.setFromAccountNo(cBean.getAcc_No());
				}else {
					bean.setFromAccountNo(1);
				}
			}else {
				bean=null;
			}
			
			list = model.search(bean, pageNo, pageSize);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			request.setAttribute("size", model.search(bean).size());
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		log.debug("TransactionListCtl doPOst End");
	}

	/**
	 * Contains Submit logics
	 */
	@Override

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		log.debug("TransactionListCtl doPost Start");
		
		
		List list = null;
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		
		TransactionBean bean = (TransactionBean) populateBean(request);
		
		String op = DataUtility.getString(request.getParameter("operation"));
		
		// get the selected checkbox ids array for delete list
		String[] ids = request.getParameterValues("ids");
		
		TransactionModel model = new TransactionModel();
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
				ServletUtility.redirect(BSView.TRANSACTION_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					TransactionBean deletebean = new TransactionBean();
					for (String id : ids) {
						deletebean.setId(DataUtility.getInt(id));
						model.delete(deletebean);
					}
					ServletUtility.setSuccessMessage("Data Deleted Successfully", request);
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(BSView.TRANSACTION_LIST_CTL, request, response);
				return;

			}
			
			UserBean uBean=(UserBean)request.getSession().getAttribute("user");
			if(uBean.getRoleId()==2) {
				CustomerBean cBean=new CustomerModel().findByUserId(uBean.getId());
				if(cBean!=null) {
					bean.setFromAccountNo(cBean.getAcc_No());
				}else {
					bean.setFromAccountNo(1);
				}
			}

			list = model.search(bean, pageNo, pageSize);
			
			
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			request.setAttribute("size", model.search(bean).size());
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}
		log.debug("TransactionListCtl doGet End");

	}

	/**
	 * Returns the VIEW page of this Controller
	 * 
	 * 
	 */
	@Override
	protected String getView() {
		return BSView.TRANSACTION_LIST_VIEW;
	}
}
