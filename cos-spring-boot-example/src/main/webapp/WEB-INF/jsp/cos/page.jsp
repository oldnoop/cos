<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>cos 上传下载</title>
<%@include file="../include.jsp" %>
<script type="text/javascript" src="${ctx}/static/js/cos/cos-js-sdk-v5.js"></script>
</head>
<body>
<div class="container text-center"> 
	<div>用户上传文件目录:${sessionScope.userId}</div>
	<div class="row" style="margin-top:10px">
         <label class="col-sm-2 control-label" for="file">选择文件:</label>
         <div class="col-sm-10">
         	<div class="input-group">
	         	<input type="file" id="uploadFile" class="form-control" style="width:300px">
	         	<input type="button" class="btn btn-info btn-flat" value="上传" onclick="upload();">
        	</div>
         </div>
    </div>
    <div>用户上传文件浏览</div>
    <div class="row" style="margin-top:10px">
    <input type="button" class="btn btn-info btn-flat" value="根目录" onclick="list_files();">
    <input type="button" class="btn btn-info btn-flat" value="返回上级目录" onclick="list_parent();">
    </div>
    <div class="row" style="margin-top:10px">
    	当前路径:<span id="currentdir" style="color:red">/</span>
    </div>
    <div class="row" style="margin-top:10px">
    	<table id="dataTable" class="table table-striped table-bordered"
			cellspacing="0" width="100%">
			<thead>
				<tr>
					<th class="text-left">文件</th>
					<th class="text-left">大小</th>
					<th class="text-left">修改时间</th>
					<th class="text-left">操作</th>
				</tr>
			</thead>
			<tbody id="tbody">
				<!-- 
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td>
						<input type="button" class="btn" value="下载" onclick="download();" >
					</td>
				</tr> 
				-->
			</tbody>
		</table>
    </div>
</div>	
<script type="text/javascript">
var Bucket = '${serverProperties.bucket}';
var Region = '${serverProperties.region}';

function list_parent(){
	var prefix = $('#currentdir').text();
	prefix = prefix.substring(0,prefix.lastIndexOf('/'));
	prefix = prefix.substring(0,prefix.lastIndexOf('/'));
	list_files(prefix);
}

function list_files(prefix){
	var params = {
		    Bucket: Bucket, /* 必须 */
		    Region: Region,    /* 必须 */
		    Delimiter: '/'
		};
	if(prefix){
		params = $.extend(params,{
			Prefix: prefix
		});
	}
	cos.getBucket(params, function(err, data) {
	    console.log(err || data.CommonPrefixes);
	    console.log(err || data.Contents);
	    if(!data ||!data.Contents){
	    	return;
	    }
	    $('#currentdir').html('/' + (prefix ||''));
	    $('#tbody').empty();
	    for(var i in data.CommonPrefixes){
	    	var file = data.CommonPrefixes[i];
	    	var html='';
		    html +='<tr>                                                                          ';
			html +='	<td class="text-left">' + file.Prefix + '</td>                                                                 ';
			html +='	<td class="text-left"></td>                                                                 ';
			html +='	<td class="text-left"></td>                                                                 ';
			html +='	<td class="text-left">                                                                      ';
			html +='		<input type="button" class="btn" value="浏览" onclick="list_files(\'' + file.Prefix + '\');" > ';
			html +='	</td>                                                                     ';
			html +='</tr>                                                                         ';
	    	$('#tbody').append(html)
    	}
	    for(var i in data.Contents){
	    	var file = data.Contents[i];
	    	var html='';
	    	var time = file.LastModified;
		    html +='<tr>                                                                          ';
			html +='	<td class="text-left">' + file.Key + '</td>                                                                 ';
			html +='	<td class="text-left">' + file.Size + '</td>                                                                 ';
			html +='	<td class="text-left">' + handle_time(file.LastModified) + '</td>                                                                 ';
			html +='	<td class="text-left">                                                                      ';
			html +='		<input type="button" class="btn" value="下载" onclick="download(\'' + file.Key + '\');" > ';
			html +='	</td>                                                                     ';
			html +='</tr>                                                                         ';
	    	$('#tbody').append(html)
    	}
	});
}

function handle_time(time){
	//2019-01-20T04:44:23.000Z
	time=time.replace('T',' ');
	time=time.replace('\.000Z','');
	time = $.parseDate(time,'yyyy-MM-dd HH:mm:ss');
	var timezone = 8; //目标时区时间，东八区 
	time = new Date(time.getTime() + timezone * 60 * 60 * 1000);
	return $.formatDate(time);
}

// 初始化实例
var cos = new COS({
    getAuthorization: function (options, callback) {
        // 异步获取临时密钥
        $.get('${ctx}${keyProperties.keyApplyPath}', {
            bucket: options.Bucket,
            region: options.Region,
        }, function (data) {
        	console.log(data);
            callback({
                 TmpSecretId: data.credentials.tmpSecretId, 
                 TmpSecretKey: data.credentials.tmpSecretKey, 
                 XCosSecurityToken: data.credentials.sessionToken, 
                 ExpiredTime: data.expiredTime
            });
        });
    }
});

//上传
function upload() {
    var file = document.getElementById('uploadFile').files[0];
    if (!file){
    	return;
    }
    // 分片上传文件
    cos.sliceUploadFile({
        Bucket: Bucket,
        Region: Region,
        Key: '${sessionScope.userId}/'+ file.name,
        Body: file,
    }, function (err, data) {
        console.log(err, data);
    });
};

//在本窗口下载
function download(file){
// 	console.log(file)
	cos.getObjectUrl({
		Bucket:Bucket,
		Region:Region,
	    Key: file,
	    Sign: true
	}, function (err, data) {
// 		console.log(err);
// 		console.log(data);
	    if (err) {
	       return;
	    }
	    var iframe_id = 'cos_download_iframe';
		var iframe = document.getElementById(iframe_id);
		if(!iframe){
			iframe = document.createElement('iframe');
			iframe.id=iframe_id;
			iframe.style.width = '0';
			iframe.style.height = '0';
			iframe.style.display = 'none';
			iframe.frameBorder='0';
			document.body.appendChild(iframe);
		}
		var downloadUrl = data.Url + (data.Url.indexOf('?') > -1 ? '&' : '?') 
	        	+ 'response-content-disposition=attachment'; // 补充强制下载的参数
     	console.log(downloadUrl);
		iframe.src = downloadUrl; 
	});
}

//在新窗口下载
function downloadOpenWindow(file){
	cos.getObjectUrl({
		Bucket:Bucket,
		Region:Region,
	    Key: file,
	    Sign: true
	}, function (err, data) {
	    if (!err) {
	        var downloadUrl = data.Url + (data.Url.indexOf('?') > -1 ? '&' : '?') 
	        	+ 'response-content-disposition=attachment'; // 补充强制下载的参数
	        window.open(downloadUrl); // 这里是新窗口打开 url
	    }
	});
}

</script>

</body>
</html>