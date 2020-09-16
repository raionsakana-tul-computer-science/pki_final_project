// jquery

$(document).on("click", ".editbtn", function() {
    var currentTD = $(this).parents('tr').find('td');

    if ($(this).html() === 'Edit') {
        currentTD = $(this).parents('tr').find($("td").not(":nth-child(1), :nth-last-child(1)"));

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