<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en" ng-app="calendarDemoApp" id="top">
<head>
    <title>AngularUI Calendar for AngularJS</title>
    <link rel="icon" href="favicon.ico" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" />
    <link rel="stylesheet" href="./../static/fullcalendar.css">
    <link rel="stylesheet" href="./../static/calendarDemo.css" />

    <script src="./../static/jquery.min.js"></script>

    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.4/angular.js"></script>
    <script src="http://angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.13.0.js"></script>
    <script src="./../static/moment.min.js"></script>
    <script src="./../static/fullcalendar.js"></script>
    <script src="./../static/gcal.js"></script>
    <script src="./../static/calendar.js"></script>
    <script src="./../static/calendarDemo.js"></script>
</head>
<body data-spy="scroll">
<header class="navbar navbar-fixed-top">
    
</header>

<div role="main">
    <div class="container">
        <section id="directives-calendar" ng-controller="CalendarCtrl">
            <div class="page-header">
                <h1>UI-Calendar</h1>
            </div>
            <div class="well">
                <div class="row-fluid">
                    <div class="span4">
                        
                        <div class="btn-group calTools">
                          <button class="btn" ng-click="changeLang()">
                            {{changeTo}}
                          </button>              
                          <button class="btn" ng-click="addRemoveEventSource(eventSources,eventSource)">
                            Toggle Source
                          </button>
                          <button type="button" class="btn btn-primary" ng-click="addEvent()">
                            Add Event
                          </button>
                        </div>

                        <ul class="unstyled">
                            <li ng-repeat="e in events">
                                <div class="alert alert-info">
                                    <a class="close" ng-click="remove($index)"><i class="icon-remove"></i></a>
                                    <b> <input ng-model="e.title"></b> 
                                    {{e.start | date:"MMM dd"}} - {{e.end | date:"MMM dd"}}
                                </div>
                            </li>
                        </ul>

                    </div>

                    <div class="span8">
                        <tabset>
                            <tab select="renderCalendar('myCalendar1');">
                              <tab-heading>
                               <i class="glyphicon glyphicon-bell"></i> Calendar One
                              </tab-heading>
                              <div class="alert-success calAlert" ng-show="alertMessage != undefined && alertMessage != ''">
                                <h4>{{alertMessage}}</h4>
                              </div>
                              <div class="btn-toolbar">
                                <p class="pull-right lead">Calendar One View Options</p>
                                <div class="btn-group">
                                    <button class="btn btn-success" ng-click="changeView('agendaDay', 'myCalendar1')">AgendaDay</button>
                                    <button class="btn btn-success" ng-click="changeView('agendaWeek', 'myCalendar1')">AgendaWeek</button>
                                    <button class="btn btn-success" ng-click="changeView('month', 'myCalendar1')">Month</button>
                                </div>
                              </div>
                            <div class="calendar" ng-model="eventSources" calendar="myCalendar1" ui-calendar="uiConfig.calendar"></div>
                           </tab>
                           <tab select="renderCalendar('myCalendar2');">
                            <tab-heading>
                              <i class="glyphicon glyphicon-bell"></i> Calendar Two
                            </tab-heading>
                              <div class="alert-success calAlert" ng-show="alertMessage != undefined && alertMessage != ''">
                                <h4>{{alertMessage}}</h4>
                              </div>
                              <div class="btn-toolbar">
                                <p class="pull-right lead">Calendar Two View Options</p>
                                <div class="btn-group">
                                    <button class="btn btn-success" ng-click="changeView('agendaDay', 'myCalendar2')">AgendaDay</button>
                                    <button class="btn btn-success" ng-click="changeView('agendaWeek', 'myCalendar2')">AgendaWeek</button>
                                    <button class="btn btn-success" ng-click="changeView('month', 'myCalendar2')">Month</button>
                                </div>
                              </div>
                            <div class="calendar" ng-model="eventSources" calendar="myCalendar2" ui-calendar="uiConfig.calendar"></div>
                           </tab>
                           <tab select="renderCalendar('myCalendar3');">
                            <tab-heading>
                              <i class="glyphicon glyphicon-bell"></i> Calendar Three
                            </tab-heading>
                              <div class="alert-success calAlert">
                                <h4>This calendar uses the extended form</h4>
                              </div>
                              <div class="btn-toolbar">
                                <p class="pull-right lead">Calendar Three View Options</p>
                                <div class="btn-group">
                                    <button class="btn btn-success" ng-click="changeView('agendaDay', 'myCalendar3')">AgendaDay</button>
                                    <button class="btn btn-success" ng-click="changeView('agendaWeek', 'myCalendar3')">AgendaWeek</button>
                                    <button class="btn btn-success" ng-click="changeView('month', 'myCalendar3')">Month</button>
                                </div>
                              </div>
                            <div class="calendar" ng-model="eventSources2" calendar="myCalendar3" ui-calendar="uiConfig.calendar"></div>
                           </tab>
                        </tabset>
                    </div>
                </div>
            </div>
            <h3>How?</h3>
        
        </section>
    </div>
</div>
        <script type="text/javascript">
            var _gaq = _gaq || [];
            _gaq.push(['_setAccount', 'UA-17352735-3']);
            _gaq.push(['_trackPageview']);
            (function () {
                var ga = document.createElement('script');
                ga.type = 'text/javascript';
                ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(ga, s);
            })();
        </script>
    </body>
</html>