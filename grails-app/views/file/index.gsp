<html>
<head>

</head>

<body>
Helloworld

<script>
	function setS3BucketObjectKey() {
		var filepath=document.getElementById('file').files[0].name;
		var isValide=false

		if(filepath==''){
			isValide=false
			alert("Please select a file to upload")
		}else{
			isValide=true
			document.getElementById('key').value ='${aws.key}'+filepath;
			document.getElementById('waitingDiv').style.visibility ="visible";
			document.getElementById('sumitButton').disabled ="disabled";
		}

		return isValide;
	}
</script>


<div>${msg}</div>
<form action="${aws.submitUrl}" method="post" enctype="multipart/form-data" onsubmit="return setS3BucketObjectKey();">
	<input type="hidden" name="Content-Type" value="text/plain" />
	<input type="hidden" name="key" id="key" />
	<input type="hidden" name="AWSAccessKeyId" value="${aws.awsAccessKeyId}" />
	<input type="hidden" name="success_action_redirect" value="${aws.success_action_redirect}" />
	<input type="hidden" name="acl" value="${aws.acl}" />
	<input type="hidden" name="policy" value="${aws.policy}" />
	<input type="hidden" name="signature" value="${aws.signature}" />

	Select file to upload: <input type="file" name="file" id="file" /><br />

	<input type="submit" id="sumitButton" value="Upload" />
	<div id="waitingDiv" style="visibility: hidden;">
		Please wait for upload to finish, this page will refresh automatically.
	</div>
</form>


</body>
</html>