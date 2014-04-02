(function($, _){
    var displayAdmins = function(){
        var toHtml = function(admin){
            var result = "<span class='admin'>";
            result = result + "</br>" + "<a href='../admin/" + admin.admin_hash + "'>" + admin.admin_username + "<a/>";
            result = result + "</br>" + admin.admin_id + "</br>";
            result = result + "<button name='"+admin.admin_id+"'>delete</button>"
            result = result + "</span>";
            return result;
        };
        $.ajax({
            contentType: 'application/json',
            data: {},
            dataType: 'json',
            success: function(response){
                console.log(response);
                var data = $(".data")[0];
                var inner = _.reduce(response, function(accume, v){
                    accume = accume + toHtml(v);
                    return accume;
                }, "");
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
    $(document).ready(function(){
        displayAdmins();
    });
})($, _);
