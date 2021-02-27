<!DOCTYPE html>

<head xmlns="http://www.w3.org/1999/html">
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />
    <#if currentUser??>
      <h3>Players Online</h3>
      <#if player_list?size == 0>
        There are no other players available to play at this time.
      <#else>
        <form id="selectplayer">
          <#list player_list as player>
            <#if player??>
              <input type="radio" id="${player}" name="selectedPlayer" value="${player}">
              <label for="${player}">${player}</label><br>
            <#else>
            </#if>
          </#list>
          <br>
          <button id="play_button" type="submit" formaction="/game" formmethod="post" form="selectplayer">Start a game</button><br>
        </form>
      </#if>

      <h3>Current Games</h3>
      <#if games?size == 0>
        There are no games being played right now.
      <#else>
        <form id="selectGame">
          <#list games as game>
            <#if game??>
              <label for="${game.toString()}">${game.toString()}</label><br>
            <#else>
            </#if>
          </#list>
          <br>
        </form>
      </#if>

    <#else>
      <h3>Players Online: ${player_count - 2}</h3>
    </#if>

  </div>

</div>
</body>

</html>
