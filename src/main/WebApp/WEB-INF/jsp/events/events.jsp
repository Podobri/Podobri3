<!DOCTYPE html>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:url var="imgURL" value="/resources/images/" />
<html>
<jsp:include page="../layout/head.jsp" />
<body>
	<div class="container">
		<jsp:include page="../layout/menu.jsp" />
		<div id="content">
			<div id="actualEvents" class="col-md-10">
				<div class="row">
					<div id="actualEventsTitle">
						<h3>
							<strong>Actual Events</strong>
						</h3>
					</div>
				</div>

				<!-- FILTER -->
				<div class="row">
					<div id="eventsFilter">
						<div id="accordion" class="panel-group">
							<div class="panel panel-default">
								<a data-toggle="collapse" data-parent="#accordion" href="#eventsFilterPanel">
									<button type="button" class="btn btn-primary btn-lg btn-block">
										<div class="col-md-2">
											<span id="filterChevron" class="glyphicon glyphicon-chevron-down"></span> ФИЛТЪР
										</div>
									</button>
								</a>
								<div id="eventsFilterPanel" class="panel-collapse collapse">
									<!-- collapse -->
									<div class="panel-body">
										<form:errors path="eventsFilter.*" />
										<form:form id="eventsFilterForm" modelAttribute="eventsFilter" method="post"
											action="${pageContext.request.contextPath}/events/search">
											<fieldset>
												<div class="row">
													<div class="form-group col-md-4">
														<form:label path="categoryId" cssClass="control-label">Category</form:label>
														<form:select id="opportunityCategoryId" path="categoryId" cssClass="form-control">
															<form:option id="opportunityCategoryNone" value="-1">---Категория---</form:option>
															<form:options items="${categoriesList}" itemValue="id" itemLabel="category" />
														</form:select>
													</div>

													<div class="form-group col-md-4">
														<form:label path="opportunityId" cssClass="control-label">Opportunity</form:label>
														<form:select id="opportunityId" path="opportunityId" cssClass="form-control">
															<form:option id="opportunityNone" value="-1">---изберете категория---</form:option>
															<%-- 															<form:options items="${opportunitiesList}" itemValue="id" itemLabel="opportunity" /> --%>
														</form:select>
													</div>

													<div class="form-group col-md-4">
														<form:label path="activities" cssClass="control-label">Сфера</form:label>
														<form:select id="activities" path="activities" multiple="true" cssClass="form-control">
															<form:options items="${activitiesList}" itemValue="id" itemLabel="type" />
														</form:select>
													</div>
												</div>

												<div class="row">
													<div class="form-group col-md-4">
														<form:label path="countryId" cssClass="control-label">Държава</form:label>
														<form:select id="countryId" path="countryId" cssClass="form-control">
															<form:option id="countryNone" value="-1">---Държава---</form:option>
															<form:options items="${countriesList}" itemValue="id" itemLabel="country" />
														</form:select>
													</div>

													<div class="form-group col-md-4">
														<form:label path="cityId" cssClass="control-label">Град</form:label>
														<form:select id="cityId" path="cityId" cssClass="form-control">
															<form:option id="cityNone" value="-1">---изберете държава---</form:option>
															<%-- 															<form:options items="${citiesList}" itemValue="id" itemLabel="city" /> --%>
														</form:select>
													</div>

													<div class="form-group col-md-4">
														<form:label path="costTypeId" cssClass="control-label">Финансиране</form:label>
														<form:select id="eventCostTypesId" path="costTypeId" cssClass="form-control">
															<form:options items="${costTypesList}" itemValue="id" itemLabel="cost" />
														</form:select>
													</div>
												</div>

												<div class="row">
													<div class="control-group col-md-4">
														<div class="controls">
															<form:label path="startDate" cssClass="control-label">Започва на</form:label>
															<form:input id="startDate" path="startDate" type="date"
																cssClass="form-control date datepicker margin-top-none" placeholder="23/9/2013" />
														</div>
													</div>
													<div class="form-group col-md-4">
														<form:label path="deadline" cssClass="control-label">Краен срок за кандидатстване</form:label>
														<form:input id="deadline" path="deadline" type="date"
															cssClass="form-control date datepicker margin-top-none" placeholder="23/9/2013" />
													</div>

													<div class="form-group col-md-4">
														<button id="filterEvents" class="btn btn-success btn-lg" type="submit">
															<span class="glyphicon glyphicon-search"></span>SEARCH
														</button>
													</div>
												</div>
											</fieldset>
										</form:form>
									</div>
								</div>
								<!-- END OF eventsFilterPanel -->
							</div>
						</div>
					</div>
					<!-- END OF FILTER -->
				</div>

				<c:if test="${not empty filteredEventsMessage }">
					<div class="row">
						<div id="filteredEventsMessage">${filteredEventsMessage}</div>
					</div>
				</c:if>

				<div class="row">
					<div class="btn-group col-md-2 col-md-offset-10">
						<a href="#" id="list" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-th-list"> </span>List</a> <a
							href="#" id="grid" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-th"></span>Grid</a>
					</div>
				</div>

				<div id="events" class="row list-group">
					<c:forEach var="event" items="${actualEvents}">
						<div id="eventItem${event.event.id}" class="item col-md-4">
							<div class="thumbnail">
								<a data-toggle="modal" data-target="#eventItemModal${event.event.id}" href=""><img
									class="group list-group-image" src="http://placehold.it/400x250/000/fff" alt="" /></a>
								<div class="caption">
									<a data-toggle="modal" data-target="#eventItemModal${event.event.id}" href="">
										<h4 class="group inner list-group-item-heading">${event.event.title}</h4>
									</a>
									<p class="group inner list-group-item-text">${event.event.description}</p>
									<br />
									<p>
										<span class="eventItemInfo">${event.category}</span> | <span class="eventItemInfo">${event.opportunity}</span>
										<br />
										<c:forEach varStatus="status" var="eventActivity" items="${event.event.activities}">
											<span class="eventItemInfo"> ${eventActivity.type } <c:if test="${!status.last}">,</c:if>
											</span>
										</c:forEach>
									</p>
									<p>
										<span class="eventItemInfo"> <img alt="" src="${imgURL}thumblines/location_24x24.png" /> ${event.city }
										</span>
									</p>
									<p>
										<span class="eventItemInfo"> <img alt="" src="${imgURL}thumblines/calendar_24x24.png" />
											${event.dateFromDayOfMonth } ${event.dateFromMonth }
										</span>
									</p>
									<p>
										<span class="eventItemInfo"> <img alt="" src="${imgURL}thumblines/time_24x24.png" />
											${event.dateFromTime }
										</span>
									</p>
									<p>
										<span class="eventItemInfo"> <img alt="" src="${imgURL}thumblines/views_24x24.png" />
											${event.eventParticipantsSize}
										</span>
									</p>
									<p>
										<span class="eventItemInfo"> Organizator: ${event.organizator} </span>
									</p>
								</div>
							</div>
						</div>

						<div class="modal fade col-md-12 eventItem" id="eventItemModal${event.event.id}" tabindex="-1"
							aria-labelledby="myModalLabel" aria-hidden="true">
							<div class="modal-dialog modal-wide70">
								<div class="modal-content col-md-10">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-hidden="true">X</button>
										<h3>${event.event.title}</h3>
									</div>
									<div class="modal-body">
										<div class="row">
											<div id="EventViewPictureSection">
												<a href="#x" class="thumbnail"> <img alt="" src="${imgURL}team1.jpg" />
												</a>
											</div>
										</div>
										<div class="row">
											<div id="EventViewMainInfo">
												<img alt="" src="${imgURL}thumblines/location_24x24.png" /> ${event.country }, ${event.city },
												${event.event.address } <br /> <img alt="" src="${imgURL}thumblines/calendar_24x24.png" />
												${event.dateFromDayOfMonth } ${event.dateFromMonth } <img alt="" src="${imgURL}thumblines/time_24x24.png" />
												${event.dateFromTime } <img alt="" src="${imgURL}thumblines/views_24x24.png" />
												${event.eventParticipantsSize}
											</div>
										</div>
										<div class="row">
											<div id="EventViewShortDescription" class="col-md-7">${event.event.description}Thisissomesampletext.Thisis
												some sample text.This is some sample text. This is some sample text. This is some sample text. This is some
												sample text.This is some sample text. This is some sample text. This is some sample text. This is some
												sample text.This is some sample text. This is some sample text. This is some sample text. This is some
												sample text. This is some sample text. This is some sample text.This is some sample text. This is some
												sample text. This is some sample text. This is some sample text.This is some sample text. This is some
												sample text. This is some sample text. This is some sample text.This is some sample text. This is some
												sample text. This is some sample text. This is some sample text. This is some sample text.</div>
											<div id="EventViewMoreInfo" class="col-md-5">
												<div id="EventViewOrganizator" class="col-md-12">
													<h4>ОРГАНИЗАТОР:</h4>
													<span class="eventItemInfo">${event.organizator}</span>
												</div>
												<div id="EventViewIntendedFor" class="col-md-12">
													<p>
													<h4>ПРЕДНАЗНАЧЕНО ЗА:</h4>
													<span class="eventItemInfoLabel label label-info">sportisti</span> <span
														class="eventItemInfoLabel label label-info">uchenici</span> <span
														class="eventItemInfoLabel label label-info">atlelti</span> <span
														class="eventItemInfoLabel label label-info">deputati</span> <span
														class="eventItemInfoLabel label label-info">text</span> <span class="eventItemInfoLabel label label-info">text</span>
													<span class="eventItemInfoLabel label label-info">text</span>
												</div>
												<div id="EventViewSocial" class="col-md-12 social-links">
													<p>
														<img alt="" src="${imgURL}thumblines/fb_32.png" /> <img alt="" src="${imgURL}thumblines/twitter_32.png" />
														<img alt="" src="${imgURL}thumblines/linkedin_32.png" /> <img alt=""
															src="${imgURL}thumblines/google_32.png" /> <img alt="" src="${imgURL}thumblines/skype_32.png" />
													</p>
												</div>
												<div id="EventViewFullDescription" class="col-md-12">
													<p>
														<a href="${pageContext.request.contextPath}/events/viewEvent/${event.event.id}">
															<button type="button" class="btn btn-primary btn-lg btn-block">ПЪЛНО ОПИСАНИЕ</button>
														</a> <a href="${pageContext.request.contextPath}/events/apply/${event.event.id}">
															<button type="button" class="btn btn-warning btn-lg btn-block">КАНДИДАТСТВАЙ</button>
														</a>
													</p>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
			<!-- END OF actualEvents -->
			<jsp:include page="../reklams.jsp" />
		</div>
		<!-- End main content -->
		<jsp:include page="../layout/footer.jsp" />
	</div>
	<!-- End container -->
	<script>
		function toggleChevron(e) {
			$('#activities').next('div').css('width', "85%");
			$('#filterChevron').toggleClass(
					'glyphicon-chevron-down glyphicon-chevron-up');
		}
		$('#accordion').on('hidden.bs.collapse', toggleChevron);
		$('#accordion').on('shown.bs.collapse', toggleChevron);
		$('#activities').chosen();

		$(document).ready(function() {
			$('#list').click(function(event) {
				$('#events .item').addClass('list-group-item');
			});
			$('#grid').click(function(event) {
				$('#events .item').removeClass('list-group-item');
				$('#events .item').addClass('grid-group-item');
			});
		});
	</script>
	<script>
		makeAjaxCall('countryId', 'getCitiesByCountry', 'change', "cityId",
				"city");
		makeAjaxCall('opportunityCategoryId', 'getOpportunitiesByCategory',
				'change', "opportunityId", "opportunity");
	</script>
</body>
</html>