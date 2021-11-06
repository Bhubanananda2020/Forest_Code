console.log("This is Script file")



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



const search = () =>
{
let query=$("#search-input").val();
if(query =='')
{
	$(".search-result").hide();
}
else{
	console.log(query);
let url=`http://localhost:8282/search/${query}`;

fetch(url).then((Response) => {
	return Response.json();
}).then((data) =>{


	let text = `<div class='list-group'>`;

	data.forEach((contact) => {

		text+=`<a href='' class>`


	});
		
	text+=`</div>`;

});



$(".search-result").show();



}



};


