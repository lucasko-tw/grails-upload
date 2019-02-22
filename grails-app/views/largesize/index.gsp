<html>
<head>

</head>

<body>
Helloworld

<div>${msg}</div>

    <g:uploadForm enctype="multipart/form-data" action="upload" method="post">
            <div align="left" style="float:left">
            <input id='fileid' name='fileid'  type='file'  onchange="document.getElementById('submit').click();" multiple />
            <input type='submit' id='submit' value='submit' hidden>  </input>
             <input id='prefix' name='prefix'  type='text'  value="" >
   </div>
            </g:uploadForm>

</body>
</html>



