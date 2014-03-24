(function($, _){
    var logincall = function(){
        $.ajax({
            contentType: 'application/json',
            data: {},//JSON.stringify(getData()),
            dataType: 'json',
            success: function(response){
                console.log(response);
                var data = $(".data")[0];
                var inner = _.reduce(response, function(accume, v){
                    accume = accume + "</br>" + "<a href='admin/" + v.admin_hash + "'>" + v.admin_username + "<a/>" ;
                    return accume;
                }, "");
                console.debug(inner);
                $(data).html(inner);
            },
            error: function(){
                console.log("Device control failed");
            },
            processData: false,
            type: 'GET',
            url: 'admin'
        });
    };
    $(document).ready(function(){
        $(".login").click(function(){
            console.log("clicked login");
            var pw = $(".password")[0].value;
            var un = $(".username")[0].value;
            console.log("password: "+pw);
            console.log("username: "+un);
            logincall();
        });
    });
})($, _);
