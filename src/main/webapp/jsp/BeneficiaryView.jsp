
<%@page import="com.octest.banque.controller.BeneficiaryCtl"%>
<%@page import="com.octest.banque.controller.AccountCtl"%>
<%@page import="com.octest.banque.util.HTMLUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.octest.banque.controller.CustomerCtl"%>
<%@page import="com.octest.banque.controller.UserRegistrationCtl"%>
<%@page import="com.octest.banque.util.ServletUtility"%>
<%@page import="com.octest.banque.util.DataUtility"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Beneficiary</title>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet"
	href="http://jqueryui.com/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script type="text/javascript">
	$(function() {
		$("#datepicker").datepicker({
			changeMonth : true,
			changeYear : true
		});
	});
</script>
</head>
<body>
	<%@ include file="Header.jsp"%>
	<br>
	<nav aria-label="breadcrumb" role="navigation">
	<ol class="breadcrumb">

		<li class="breadcrumb-item active" aria-current="page">Add Beneficiary Account</li>
	</ol>
	</nav>

	<divcol-md-5img-thumbnail">
		<div id="feedback">
			<div class="container">
				<div class="col-md-8">
					<div class="form-area">
						<form role="form" action="<%=BSView.BENEFICIARY_CTL%>"
							method="post">

							<jsp:useBean id="bean" class="com.octest.banque.bean.BeneficiaryBean"
								scope="request"></jsp:useBean>

							<input type="hidden" name="id" value="<%=bean.getId()%>">
							<input type="hidden" name="createdBy"
								value="<%=bean.getCreatedBy()%>"> <input type="hidden"
								name="modifiedBy" value="<%=bean.getModifiedBy()%>"> <input
								type="hidden" name="createdDatetime"
								value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
							<input type="hidden" name="modifiedDatetime"
								value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

							<br style="clear: both">
							<h3 style="margin-bottom: 15px; text-align: left:;">Add Account</h3>
							<b><font color="red"> <%=ServletUtility.getErrorMessage(request)%>
							</font></b> <b><font color="Green"> <%=ServletUtility.getSuccessMessage(request)%>
							</font></b>
							
							<div class="form-group">
								<input type="text" class="form-control"  name="accNo"
									placeholder="Beneficiary Account No"
									value="<%=(bean.getAcc_No()>0)?bean.getAcc_No():""%>">
								<font color="red"><%=ServletUtility.getErrorMessage("accNo", request)%></font>
							</div>
							
							<div class="form-group">
								<input type="text" class="form-control"  name="confirmAccNo"
									placeholder="Confirm Account No"
									value="<%=DataUtility.getStringData(bean.getConfirmAccNo())%>">
								<font color="red"><%=ServletUtility.getErrorMessage("confirmAccNo", request)%></font>
							</div>
							
							<div class="form-group">
								<input type="text" class="form-control"  name="name"
									placeholder="Name"
									value="<%=DataUtility.getStringData(bean.getName())%>">
								<font color="red"><%=ServletUtility.getErrorMessage("name", request)%></font>
							</div>
							
							<div class="form-group">
								<input type="text" class="form-control"  name="bName"
									placeholder="Bank Name"
									value="<%=DataUtility.getStringData(bean.getBankName())%>">
								<font color="red"><%=ServletUtility.getErrorMessage("bName", request)%></font>
							</div>
							
							<div class="form-group">
								<input type="text" class="form-control"  name="iban"
									placeholder="IBAN"
									value="<%=DataUtility.getStringData(bean.getBankName())%>">
								<font color="red"><%=ServletUtility.getErrorMessage("iban", request)%></font>
							</div>
							

							<input type="submit" name="operation"
								class="btn btn-primary pull-right"
								value="<%=BeneficiaryCtl.OP_SAVE%>">&nbsp;or&nbsp;
							<input type="submit" name="operation"
								class="btn btn-primary pull-right"
								value="<%=BeneficiaryCtl.OP_RESET%>">
						</form>
					</div>
				</div>
			</div>
		</div>
		<!--feedback-->
		<br>

		<%@ include file="Footer.jsp"%>
</body>
</html>