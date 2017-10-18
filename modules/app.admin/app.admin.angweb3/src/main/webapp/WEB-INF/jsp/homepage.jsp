<!DOCTYPE html>
<!--[if lt IE 7]>      <html lang="en" ng-app="app" class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html lang="en" ng-app="app" class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html lang="en" ng-app="app" class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en" ng-app="app" class="no-js"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Spring and Angularjs Tutorial</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <link rel="stylesheet" href="/app.admin.angweb3/resources/static/css/app.css">
</head>
<body>
<h2>Spring and Angularjs Tutorial</h2>
<div class="home-section">
    <ul class="menu-list">
        <li><a href="#/gallery">Photo Gallery</a></li>
        <li><a href="#/contactus">Contact</a></li>
        <li><a href="#/tabDef">TabDef</a></li>
    </ul>
</div>
<div ng-view></div>
<script src="./webjars/angularjs/1.4.8/angular.js"></script>
<script src="./webjars/angularjs/1.4.8/angular-resource.js"></script>
<script src="./webjars/angularjs/1.4.8/angular-route.js"></script>
<script src="./resources/static/js/app.js"></script>
<script src="./resources/static/js/controller.js"></script>
<link rel="stylesheet" href="./webjars/bootstrap/3.3.6/css/bootstrap.css">
</body>
</html>
