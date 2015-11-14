<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Repup Review Database</title>
<link href="css/bootstrap.min.css" rel="stylesheet" />
<link href="css/style.css" rel="stylesheet" />
<meta name="viewport" content="width=device-width,intitial-scale=1.0" />
<script type="text/javascript" src="js/jquery-1.11.2.min.js">
	
</script>
<script type="text/javascript" src="js/bootstrap.min.js">
	
</script>
<script src="js/bootbox.min.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document)
			.ready(
					function(e) {
						
						
						function getDomain(url) {
							var prefix = /^https?:\/\//i;
							var domain = /^[^\/]+/;
							// remove any prefix
							url = url.replace(prefix, "");
							// assume any URL that starts with a / is on the current page's domain
							if (url.charAt(0) === "/") {
								url = window.location.hostname + url;
							}
							// now extract just the domain
							var match = url.match(domain);
							if (match) {
								return (match[0]);
							}
							return (null);
						}
					
						$('#submit')
								.click(
										function(e) {

											$('#myModal').modal({
												backdrop : 'static',
												keyboard : false
											// to prevent closing with Esc button (if you want this too)
											});
											$('#myModal').modal('show');
											
											hotelid = $('#hotelid').val();
											
											hotelid = encodeURIComponent(hotelid);
											var finalUrl = "http://"
													+ window.location.host
													+ "/repup_price_crawler/prices/details?hotelid="
													+ hotelid;
											console.log(finalUrl);
												$.ajax({
																	url : finalUrl,
																	contentType : "application/json",
																	accepts : "application/json"
													}).done(
																function(data) {
																	//alert(data);
																	console.log(data);
																	var message = "<table class='table'><tr><th class='info'>Price</th><th class='info'>View Deal</th><th class='info'>Medium</th></tr>";
																	for(var i in data)
																	{
																		
																	     var price = data[i].currentPrice;
																	    
																	     var dealUrl = data[i].priceUrlViewer;
																		 var medium = data[i].priceDetailType;
																		if(price=="na")
																		{
																			 
																		}else
																		{
																			message += "<tr><td class='info'>"+price+"</td><td class='info'><a target='_blank' href='"+dealUrl+"' >View Deal</a></td>";
																			message += "<td class='info'>"+medium+"</td></tr>";
																		}
																		
																		console.log(message)
																		 
																	}
																	
																	message +="</table>";
																	/* var obj = data;
																	var hotelId = obj.hotelId;
																	var hotelName = obj.hotelName;
																	var reviewUrl = obj.hotelReviewUrl;
																	var processStatus = obj.processCompleted;
																	var sentimentsUrl = obj.hotelSentimentsUrl;
																	var messageText = "Recieved HotelId: "
																			+ hotelId
																			+ " <br />Recieved HotelName:"
																			+ hotelName
																			+ "<br /> Review URL:"
																			+ reviewUrl
																			+ "<br /> Sentiments Url:"
																			+ sentimentsUrl
																			+ "<br /> Review Process Status:"
																			+ processStatus;
																	// alert(messageText);
																	 */
																	 console.log("Html Message :"+ message);
																	 $("#responseMessage").html(message);
																	
																	$(
																			'#myModal')
																			.modal(
																					"hide");
																});
										
										});

					});
</script>
</head>
<body>
	<div id="myModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">

					<h4 class="modal-title">Status</h4>
				</div>
				<div class="modal-body" align="center">
					<p class="h2">Processing</p>
				</div>
				<div class="modal-footer"></div>
			</div>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="col-md-6 col-md-offset-3">
				<div class="panel panel-login">
					<div class="panel-heading">
						<div class="row">
							<div class="col-lg-12" align="center">
								<h1>Repup Price Grabber</h1>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-lg-12">
								<form id="login-form" action="javascript:void(0);" role="form"
									style="display: block">
									<div class="form-group">
										<input type="text" name="hotelid" id="hotelid" tabindex="1"
											class="form-control" placeholder="Enter Hotel-Id " value="" />
									</div>

									<div class="form-group">
										<div class="row">
											<div class="col-sm-6 col-sm-offset-3">
												<input type="submit" id="submit" name="login-submit"
													tabindex="3" class="form-control btn btn-primary"
													value="Grab Prices" />
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
						<div class="row">
							<div class="col-lg-12">
								<div id="responseMessage" align="center" class="info"></div>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
</body>
</html>
