<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="10">
    <title>Sign In | ${title}</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<div class="page">

    <h1>Sign In | ${title}</h1>

    <!-- Provide a navigation bar -->
    <#include "nav-bar.ftl" />

    <div class="body">

        <!-- Provide a message to the user, if supplied. -->
        <#include "message.ftl" />

        <form id="nameform">
            <label for="namefield">Enter your player name:</label><br>
            <input type="text" id="namefield" name="namefield" maxlength="13" autofocus><br>
            <button type="submit" form="nameform" formaction="/signin" formmethod="POST">Sign In</button>
        </form>

    </div>

</div>

</body>
</html>