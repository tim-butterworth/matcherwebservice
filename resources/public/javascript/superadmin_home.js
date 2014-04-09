(function($, _){
    var getAdminList = function(renderer,adder){
        $.ajax({
            contentType: 'application/json',
            data: {},
            dataType: 'json',
            success: function(response){
                console.log(response);
                var data = $(".data");
                data.html("");
                _.forEach(response, function(r){renderer(r, data);});
                adder.render(data);
            },
            error: function(){
                console.log("Device control failed");
            },
            processData: false,
            type: 'GET',
            url: '../admin'
        });
    };
    var addAdmin = {
        "render" : function(anchor){
            anchor.append(this.toHtml());
        },
        "toHtml" : function(){
            var str = "<span></br>";
            str = str + "<input type='text'>admin name</input>";
            str = str + "</br>";
            str = str + "<input type='text'>admin something</input>";
            str = str + "</br><button>add</button></span>";
            return str;
        }
    };
    var deleteCall = function(id, span_id){
        return function(){
           $.ajax({
               success: function(response){
                   getAdminList(render,addAdmin);
                   $("."+span_id).css("background-color", "red");
               },
               error: function(){
                   console.log("got an error deleting admin: "+id);
                   console.log("changing color of: "+span_id)
                   $("."+span_id).css("background-color", "red");
               },
               data: {},
               dataType: 'json',
               contentType: 'application/json',
               type: 'DELETE',
               url: '../admin/'+id
           });
        };
    };
    var toObj = function(admin){
        var result = {
            "html" : function(){
                var html = "<span class='"+admin.admin_id+"'>";
                html = html + "</br>" + "<a href='../admin/" + admin.admin_hash + "'>" + admin.admin_username + "<a/>";
                html = html + "</br>" + admin.admin_id + "</br>";
                html = html + "<button name='"+admin.admin_id+"' class='delete_"+admin.admin_id+"'>delete</button>"
                html = html + "</span>";
                return html;
            },
            "id" : function(){
                return admin.admin_id;
            },
            "hash" : function(){
                return admin.admin_hash;
            },
            "deleteFn" : function(id){
                var obj = $("."+id);
                return function(){
                    deleteCall(admin.admin_hash, admin.admin_id)();
                };
            },
        };
        return result;
    };
    var render = function(raw_obj, anchor){
        var obj = toObj(raw_obj);
        anchor.append(obj.html());
        $(".delete_"+obj.id()).click(obj.deleteFn(obj.hash()));
    };
    var displayAdmins = function(){
        getAdminList(render,addAdmin);
    };
    $(document).ready(function(){
        displayAdmins();
    });
})($, _);
