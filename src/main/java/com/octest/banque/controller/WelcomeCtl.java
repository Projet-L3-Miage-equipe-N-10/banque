package com.octest.banque.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.octest.banque.util.DataUtility;
import com.octest.banque.util.ServletUtility;


/**
 * 
 * 
 * Servlet implementation class WelcomeCtl
 */

@WebServlet(name = "WelcomeCtl", urlPatterns = { "/WelcomeCtl" })
public class WelcomeCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String page = DataUtility.getString(request.getParameter("page"));

		if (page != null) {
			response.sendRedirect(page);
			return;
		}
		ServletUtility.forward(BSView.WELCOME_VIEW, request, response);
	}

	@Override
	protected String getView() {
	
		return BSView.WELCOME_VIEW;
	}

}
