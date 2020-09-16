// jquery

$(document).on("click", ".editbtn", function() {
    var tableName = document.getElementById('table-name').placeholder;
    var currentTD = $(this).parents('tr').find('td');
    var names = document.getElementById("thead_tr");

    var old, new_, out;

    if ($(this).html() === 'Edit') {
        currentTD = $(this).parents('tr').find($("td").not(":nth-last-child(1)"));
        for (var i = 0; i < currentTD.length; i++) {
            currentTD[i].innerHTML = currentTD[i].textContent + '<input style="display: none;" placeholder="' + currentTD[i].textContent + '"></input>';
        }

        $.each(currentTD, function () {
            $(this).prop('contenteditable', true).css({
                'background':'#b4e7c2',
                'color':'#000'
            })
        });

    } else {
        $.each(currentTD, function () {
            $(this).prop('contenteditable', false).removeAttr("style");
        });
    }

    if ($(this).html() === 'Save') {
        $(this).prop('contenteditable', false)

        var input = $(this).parents('tr').find('input');
        old = prepareJsonFromPlaceholder(names, input);

        input = $(this).parents("tr").find('td');
        new_ = prepareJson(names, input)

        if (old !== new_) {
            out = prepareList(old, new_)
            out = prepareRequest(out, "update", tableName)
            webSocket.send(out);
        }
    }

    $(this).html($(this).html() === 'Edit' ? 'Save' : 'Edit');
});

$(document).on("click", ".dltbtn", function() {
    var tableName = document.getElementById('table-name').placeholder;

    var input = $(this).parents("tr").find('td');
    var names = document.getElementById("thead_tr");

    var out = prepareJson(names, input)
    out = prepareRequest(out, "delete", tableName)

    $(this).parents("tr").remove();
    $(".add-new").removeAttr("disabled");

    webSocket.send(out);
});


$(document).on("click", ".add-new", function() {
    var actions = $("td:last-child").html();
    $(this).attr("disabled", "disabled");

    var index = $("table tbody tr:last-child").index();
    var size = document.getElementById("thead_tr").children.length;

    var row = '<tr>';
    for (var i = 0; i < size - 1; i++) {
        row += '<td><input type="text" class="form-control"></td>'
    }
    row += '<td>' + actions + '</td></tr>';

    $("table").append(row);
    $("table tbody tr").eq(index + 1).find(".addbtn, .editbtn").toggle();
});


$(document).on("click", ".addbtn", function() {
    var tableName = document.getElementById('table-name').placeholder;

    var empty = false;
    var input = $(this).parents("tr").find('input[type="text"]');

    input.each(function() {
        if (!$(this).val()) {
            empty = true;
        }
    });

    if (!empty) {
        input.each(function() {
            $(this).parent("td").html($(this).val());
        });

        var input = $(this).parents("tr").find('td');
        var names = document.getElementById("thead_tr");
        var out = prepareJson(names, input)
        out = prepareRequest(out, "add", tableName)

        $(this).parents("tr").find(".addbtn, .editbtn").toggle();
        $(".add-new").removeAttr("disabled");

        webSocket.send(out);
    }
});