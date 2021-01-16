<html>
    <head>
        <title>Login</title>
        
        <script type="text/javascript" src="js/jquery-2.1.4.js"></script>
    </head>
    <body>
        <form>
            User: <input name="username" type="text"><br>
            Password: <input name="password" type="password"><br>
        </form>
        <button id="submit">Login</button>
        
        <script type="text/javascript">
        
        $(function() {
        	$('#submit').click(function() {
        		var data = {
        			username: $('[name=username]').val(),
        			password: $('[name=password]').val()
        		};
        		$.ajax({
        			type: 'post',
        			url: '/login',
        			contentType: 'application/json',
        			data: JSON.stringify(data),
        			dataType: 'json',
        			succes: function(data) {
        				console.log(data);
        			}
        		})
        	});
        })
        </script>
    </body>
</html>