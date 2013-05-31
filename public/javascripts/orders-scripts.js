/**
 * Created with IntelliJ IDEA.
 * User: Artyom
 * Date: 01.06.13
 * Time: 1:24
 * To change this template use File | Settings | File Templates.
 */

$(document).ready(function () {
    $("body").on('click', '#btn', function () {
        jsRoutes.controllers.AdministrationController.getNewOrders().ajax({
            type: "GET",
            data: null,
            dataType: 'text',   //тип ответа
            success: function (data) {
                alert(data);
                //data = $.parseJSON(data);
                //alert(data);
                /*$.each(data, function () {
                    alert(this.id + " " + this.name + " " + this.picturePath);
                    /*$.each(this, function(key, value) {
                     alert(this);
                     }); */
                //});
            },
            error: function () {
                alert("Error!");
            }
        });
    });
});