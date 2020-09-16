// websocket

let webSocket;

if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
    showModalResponse("WebSocket aktualnie otwarty.");
} else {
    // heroku remote
    // webSocket = new WebSocket("wss://pki-project-patryk-janowski.herokuapp.com/app");

    // heroku local web
    webSocket = new WebSocket("ws://localhost:5000/app");
}

webSocket.onmessage = function (event) {
    var message = JSON.parse(event.data);

    if (message["type"] === "open") {
        prepareSelectList(message, "table-list")
    } else if (message["type"] === "error") {
        showModalResponse(message["data"]);
    } else {
        showModalResponse("Wystąpił błąd.");
    }
};


// Functions

// https://getbootstrap.com/docs/4.0/components/modal/
function showModalResponse(text) {
    document.getElementById("insert-modal").innerHTML = text;

    $('#alert-modal').modal({
        show: true
    })
}

// https://mdbootstrap.com/docs/jquery/forms/select/
function prepareSelectList(json, id) {
    var obj, content = "", size = json["data"].length, i;

    for (i = 0; i < size - 1; i++) {
        obj = json["data"][i];
        content += "<option value=\"" + obj + "\">" + obj + "</option>";
    }

    obj = json["data"][size - 1]
    content += "<option value=\"" + obj + "\" selected>" + obj + "</option>";

    document.getElementById(id).innerHTML = content;
}

function viewTable() {
    var e = document.getElementById("table-list");
    var value = e.options[e.selectedIndex].value;
    window.location.href = "/view?table_name=" + value;
}

function backToHomePage() {
    window.location.href = "/"
}

// jquery
$(document).on("click", ".editbtn", function() {
    var currentTD = $(this).parents('tr').find('td');

    if ($(this).html() === 'Edit') {
        currentTD = $(this).parents('tr').find($("td").not(":nth-child(1), :nth-last-child(1)"));

        $.each(currentTD, function () {
            $(this).prop('contenteditable', true).css({
                'background':'#B4E7E3',
                'color':'#000'
            })
        });

    } else {
        $.each(currentTD, function () {
            $(this).prop('contenteditable', false).removeAttr("style");
        });
    }

    $(this).html($(this).html() === 'Edit' ? 'Save' : 'Edit');

    if ($(this).html() === 'Save') {
        $(this).prop('contenteditable', false)
    }

});

$(document).on("click", ".dltbtn", function() {
    $(this).parents("tr").remove();
    $(".add-new").removeAttr("disabled");
});


$(document).on("click", ".add-new", function() {
    var actions = $("td:last-child").html();
    $(this).attr("disabled", "disabled");

    var index = $("table tbody tr:last-child").index();
    var size = document.getElementById("thead_tr").children.length;

    var row = '<tr><td></td>';
    for (var i = 0; i < size - 2; i++) {
        row += '<td><input type="text" class="form-control"></td>'
    }
    row += '<td>' + actions + '</td></tr>';

    $("table").append(row);
    $("table tbody tr").eq(index + 1).find(".addbtn, .editbtn").toggle();
});


$(document).on("click", ".addbtn", function(){
    var empty = false;
    var input = $(this).parents("tr").find('input[type="text"]');

    input.each(function(){
        if (!$(this).val()) {
            empty = true;
        }
    });

    if (!empty) {
        input.each(function(){
            $(this).parent("td").html($(this).val());
        });

        $(this).parents("tr").find(".addbtn, .editbtn").toggle();
        $(".add-new").removeAttr("disabled");
    }
});