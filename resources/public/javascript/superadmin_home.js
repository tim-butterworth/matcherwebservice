(function($, _){
    var displayAdmins = function(){
        var render = function(obj, anchor){
            console.debug(obj.html());
            anchor.append(obj.html());
            anchor.append("hi");
            $("."+obj.id()).click(obj.deleteFn(obj.id()));
        };
        var toHtml = function(admin){
            var result = {
                "html" : function(){
                    var html = "<span class='"+admin.admin_id+"'>";
                    html = html + "</br>" + "<a href='../admin/" + admin.admin_hash + "'>" + admin.admin_username + "<a/>";
                    html = html + "</br>" + admin.admin_id + "</br>";
                    html = html + "<button name='"+admin.admin_id+"'>delete</button>"
                    html = html + "</span>";
                    return html;
                },
                "id" : function(){
                    return admin.admin_id;
                },
                "deleteFn" : function(id){
                    var obj = $("."+id);
                    return function(){ obj.hide();};
                },
            };
            return result;
        };
        $.ajax({
            contentType: 'application/json',
            data: {},
            dataType: 'json',
            success: function(response){
                console.log(response);
                var data = $(".data");
                var inner = _.reduce(response, function(accume, v){
                    accume = accume + toHtml(v);
                    return accume;
                }, "");
//                var obj = response[0];
                render(toHtml(response[0]), data);
//                $(data).html(inner);
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
