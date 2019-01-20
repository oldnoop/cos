<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>OLDNOOP 管理系统 | 登陆</title>
<%@include file="../common.jsp" %>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<link rel="stylesheet" href="${ctx}/static/js/AdminLTE-2.4.2/bower_components/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${ctx}/static/js/AdminLTE-2.4.2/bower_components/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="${ctx}/static/js/AdminLTE-2.4.2/dist/css/AdminLTE.css">
<link rel="stylesheet" href="${ctx}/static/js/AdminLTE-2.4.2/dist/css/skins/_all-skins.min.css">
<link rel="stylesheet" href="${ctx}/static/js/AdminLTE-2.4.2/plugins/iCheck/square/blue.css">
<script src="${ctx}/static/js/jQuery/jquery-2.2.3.min.js"></script>
<script src="${ctx}/static/js/AdminLTE-2.4.2/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="${ctx}/static/js/layer/layer.js"></script>
<script type="text/javascript">
/*
 * 登陆函数
 */
function login(){
	var form_data = $("#form").serialize();
	$.post('${ctx}/user/login',form_data,function(res){
		if(res==='1'){
			window.location='${ctx}/cos/page';
		}else{
			alert('用户名或密码错误');
		}
	});
}

</script>
</head>
<!-- 3c8dbc -->
<body class="hold-transition login-page">
	<div class="login-box">
		<div class="login-logo">
			<a href="#"><b>OLDNOOP</b>管理系统</a>
		</div>
		<!-- /.login-logo -->
		<div class="login-box-body">
			<p class="login-box-msg">登陆</p>

			<form id="form" action="#" method="post">
				<div class="form-group has-feedback">
					<input type="text" name="username" class="form-control" placeholder="用户名">
					<span class="glyphicon glyphicon-user form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<input type="password" name="password" class="form-control" placeholder="密码">
					<span class="glyphicon glyphicon-lock form-control-feedback"></span>
				</div>
				<div class="row">
					<div class="col-xs-8">
						<div class="checkbox icheck">
							<label> <input type="checkbox"> 记住密码
							</label>
						</div>
					</div>
					<!-- /.col -->
					<div class="col-xs-4">
						<button type="button" class="btn btn-primary  btn-flat" onclick="login();">登陆</button>
					</div>
					<!-- /.col -->
				</div>
			</form>

		</div>
		<!-- /.login-box-body -->
	</div>
	<!-- /.login-box -->
	<script src="${ctx}/static/js/AdminLTE-2.4.2/plugins/iCheck/icheck.min.js"></script>
	<script>
	  $(function () {
	    $('input').iCheck({
	      checkboxClass: 'icheckbox_square-blue',
	      radioClass: 'iradio_square-blue',
	      increaseArea: '20%' // optional
	    });
	  });
	</script>
	
</body>
</html>
