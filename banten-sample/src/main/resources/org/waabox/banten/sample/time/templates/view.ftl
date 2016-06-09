<html>

  <head>
    <title>Time with chuck!</title>
  </head>

  <body role="document">

    <div class="container theme-showcase" role="main">
      <div class="jumbotron">
        <h1>Timezones</h1>

        <#list times as time>
          <div>
            ${time.getTime()} ~ <b> ${time.gmt} </b> 
          </div>
        </#list>

      </div>

      <form role="form" action="save" method="post" name="time-form">
        <div class="form-group">
          <label for="gmt">Add new Time in GMT</label>
          <input type="text" class="form-control" name="gmt" id="gmt">
        </div>
        <button type="submit" class="btn btn-default" name="submit-btn">Submit</button>
      </form>

    </div>

    <@weblet name = "samplepicture" module = "User-Module" quote = "Chuck Norris CAN get bacon from a cow"/>

  </body>

</html>
