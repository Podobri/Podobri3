// 		$('.addElement').on(
// 				'click',
// 				function() {
// 					$divNewElement = $(this).parent('div').parent('div').find(
// 							'.elementWrapper');
// 					$new = $divNewElement.toHtmlString();
// 					$(this).parent('div').parent('div').before($new);
// 				});

// 		jQuery.fn.toHtmlString = function() {
// 			return $('<div></div>').html($(this).clone()).html();
// 		};

function addElement(programsSize, lectorsSize, sponsorsSize) {
	var programIndex = programsSize;
	var lectorIndex = lectorsSize;
	var sponsorIndex = sponsorsSize;

	$("#addProgram")
			.click(
					function() {
						var inputSection = "<div class='schedule elementWrapper'>"
								+ "<div class='form-group col-md-6'>"
								+ "<label for='eventsPrograms["
								+ programIndex
								+ "].subject' class='control-label'>Тема</label>"
								+ "<input name='eventsPrograms["
								+ programIndex
								+ "].subject' type='text' class='form-control' />"
								+ "</div>"
								+ "<div class='form-group col-md-3'>"
								+ "<label for='eventsPrograms["
								+ programIndex
								+ "].dateFrom' class='control-label'>Начало</label> "
								+ "<input name='eventsPrograms["
								+ programIndex
								+ "].dateFrom' type='datetime-local' class='form-control date datepicker margin-top-none' placeholder='23/9/2013' />"
								+ "</div>"
								+ "<div class='form-group col-md-3'>"
								+ "<label for='eventsPrograms["
								+ programIndex
								+ "].dateTo' class='control-label'>Край</label> "
								+ "<input name='eventsPrograms["
								+ programIndex
								+ "].dateTo' type='datetime-local' class='form-control date datepicker margin-top-none' placeholder='23/9/2013' />"
								+ "</div>"
								+ "<div class='form-group col-md-12'>"
								+ "<label for='eventsPrograms["
								+ programIndex
								+ "].information' class='control-label'>Описание</label>"
								+ "<textarea name='eventsPrograms["
								+ programIndex
								+ "].information' class='col-md-12 form-control' rows='5'></textarea>"
								+ "</div> </div>";
						$("#addProgramRow").before(inputSection);
						programIndex++;
					});
	

	$("#addLector")
			.click(
					function() {
						var inputSection = "<div class='lector elementWrapper'>"
								+ "<div class='form-group col-md-6'>"
								+ "<label for='lectors["
								+ lectorIndex
								+ "].firstName' class='control-label'>Име</label>"
								+ "<input name='lectors["
								+ lectorIndex
								+ "].firstName' type='text' class='form-control' />"
								+ "</div>"
								+ "<div class='form-group col-md-6'>"
								+ "<label for='lectors["
								+ lectorIndex
								+ "].lastName' class='control-label'>Фамилия</label>"
								+ "<input name='lectors["
								+ lectorIndex
								+ "].lastName' type='text' class='form-control' />"
								+ "</div>"
								+ "<div class='form-group col-md-6'>"
								+ "<label for='lectors["
								+ lectorIndex
								+ "].job' class='control-label'>Професия</label>"
								+ "<input name='lectors["
								+ lectorIndex
								+ "].job' type='text' class='form-control' />"
								+ "</div>"
								+ "<div class='form-group col-md-6'>"
								+ "<label for='lectors["
								+ lectorIndex
								+ "].picture' class='control-label'>Снимка</label>"
								+ "<input name='lectors["
								+ lectorIndex
								+ "].pictureFile' type='file' class='form-control' />"
								+ "</div>"
								+ "<div class='form-group col-md-12'>"
								+ "<label for='lectors["
								+ lectorIndex
								+ "].information' class='control-label'>Информация</label>"
								+ "<textarea id='lectors["
								+ lectorIndex
								+ "].information' class='col-md-12 form-control' rows='5'></textarea>"
								+ "</div> </div>";

						$("#addLectorRow").before(inputSection);
						lectorIndex++;
					});

	$("#addSponsor").click(
			function() {
				var inputSection = "<div class='sponsor elementWrapper'>"
						+ "<div class='form-group col-md-8'>"
						+ "<label for='sponsors[" + sponsorIndex
						+ "].name' class='control-label'>Пълно име</label>"
						+ "<input name='sponsors[" + sponsorIndex
						+ "].name' class='form-control' /> </div>"
						+ "<div class='form-group col-md-4'>"
						+ "<label for='sponsors[" + sponsorIndex
						+ "].picture' class='control-label'>Снимка</label>"
						+ "<input name='sponsors[" + sponsorIndex
						+ "].pictureFile' type='file' class='form-control' />"
						+ "</div> </div>";
				$("#addSponsorRow").before(inputSection);
				sponsorIndex++;
			});
};
