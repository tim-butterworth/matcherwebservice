(function($, _){
    var displayAdmins = function(){
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
            url: '../admin'
        });
    };
    var logincall = function(){
        console.log("attempting to login");
    };
    $(document).ready(function(){
        displayAdmins();
    });
})($, _);
