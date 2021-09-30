console.log("This is Script file")

/*sidebar hide and show */
const toggleSidebar = () => {
	if ($('.sidebar').is(":visible")) {
		$('.sidebar').css("display", "none");
		$('.content').css("margin-left", "02%");
	}
	else {
		$('.sidebar').css("display", "block");
		$('.content').css("margin-left", "20%");
	}
};



/*Get the user of department in Add request Page*/
var req2 = new XMLHttpRequest();
const search = () => {
	var deptcode = document.getElementById("reqdeptcode").value;
	console.log(deptcode);
	req2.open('GET', 'http://localhost:9091/getdata/getuserbydeptid?deptcode=' + deptcode);
	req2.send();
	req2.onreadystatechange = gotDeptUser;
}
const gotDeptUser = () => {
	if (req2.readyState == 4 && req2.status == 200) {
		var select = document.getElementById("reqassignto");
		var objResp = JSON.parse(req2.responseText);
		select.innerHTML = "<option>SELECT USER</option>";
		for (var i = 0; i < objResp.length; i++) {
			var optn = objResp[i].decreatedby;
			console.log(objResp[i]);

			var el = document.createElement("option");
			el.textContent = objResp[i].uFName;
			el.value = objResp[i].uId;
			select.append(el)
		}
	}
}

















/*Check department is exist or not*/
var req2 = new XMLHttpRequest();
const checkdepartment = () => {
	var deptcode = document.getElementById("decode").value;
	console.log(deptcode);
	document.getElementById("decodeef1").innerHTML = "";
		
	req2.open('GET', 'http://localhost:9091/getdata/getalldept?deptcode=' + deptcode);
	req2.send();
	req2.onreadystatechange = gotDeptUser1;
	}
const gotDeptUser1 = () => {
	if (req2.readyState == 4 && req2.status == 200) {
		array = req2.responseText;
		var objResp = JSON.parse(array);
		var deptcode = document.getElementById("decode").value;
		console.log(deptcode.trim().length);

		if (objResp.length != 0) {
			document.getElementById("decodeef1").innerHTML = "<b>Department is already exist !!!</b>";
			document.getElementById("decodeef1").style.color = 'red';
			document.getElementById("savedept").disabled = true;
		}
		else {
			document.getElementById("decodeef1").innerHTML = "";
		}
	}
}











































/*Generate Request number and Get the user of department in Add request Page*/
/*var req1 = new XMLHttpRequest();
 req2 = new XMLHttpRequest();
const search = () => {
	var deptcode = document.getElementById("reqdeptcode").value;
console.log(deptcode);
	req1.open('GET', 'http://localhost:9091/getdata/getlastrequestnumber?deptcode=' + deptcode);
	req1.send();
	req2.open('GET', 'http://localhost:9091/getdata/getdeptuser?deptcode=' + deptcode);
	req2.send();
	req1.onreadystatechange = gotRequestNewNum;
	req2.onreadystatechange = gotDeptUser;
}
const gotRequestNewNum = () => {
	if (req1.readyState == 4 && req1.status == 200) {
		var deptcode = document.getElementById("reqdeptcode").value;
		console.log("=================");
		console.log(req1.responseText);
		console.log("=================");


		console.log(deptcode + req1.responseText);
		document.getElementById("reqcode").value = deptcode + req1.responseText;
	}
}

const gotDeptUser = () => {
	if (req2.readyState == 4 && req2.status == 200) {
		var array = req2.responseText;
		console.log(array);
		var select = document.getElementById("reqassignto");
		var objResp = JSON.parse(req2.responseText);
		for (var i = 0; i < objResp.length; i++) {
			var optn = objResp[i].decreatedby;
			var el = document.createElement("option");
			el.textContent = optn;
			el.value = optn;
			select.appendChild(el);
		}
	}
}

*/




