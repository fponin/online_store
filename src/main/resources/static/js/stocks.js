/*Вывод акций*/
$(document).ready(function () {
    create();
});

function create() {
    $("#stocksDiv").empty();
    moment.locale('ru');
    $.ajax("/rest/allStocks", {
        dataType: "json",
        success: function (data) {
            const stocks = JSON.parse(JSON.stringify(data));
            for (let i = 0; i < stocks.length; i++) {
                let out = $("<li>").attr("id", stocks[i].id).attr("data-filter", stocks[i].stockType);
                let endDate = stocks[i].endDate
                if (endDate === null) {
                    endDate = "бессрочно"
                } else {
                    endDate = moment(endDate).format("DD MMM YYYY")
                }
                out.append(
                    `<div class=\"card mb-3\">
                        <div class=\"row no-gutters\">
                            <div class=\"col-md-4\">
                                 <img class=\"card-img\" src=\"../static/img/stocks/1.jpg\" width=\"250\">
                            </div>
                            <div class=\"col-md-6\">
                                <div class=\"card-body\">
                                    <h3 class='card-title'>${stocks[i].stockTitle}</h3>
                                    <p class=\"card-text\">${stocks[i].stockText}</p>
                                    <p>Срок проведения акции: </p>
                                    <div class=\"card-date\">
                                         с ${moment(stocks[i].startDate).format("DD MMM")}
                                         по ${endDate}
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2 flex-row align-items-center">
                                <div class="nav flex-column nav-pills mt-2 container-fluid" role="tablist" aria-orientation="vertical">
                                    <button onclick='getStockForEdit(${stocks[i].id})' class="btn btn-info" data-toggle='modal'
                                            data-target='#editStockModal'>Edit</button>
                                </div>
                                <div class="nav flex-column nav-pills mt-2 container-fluid" role="tablist" aria-orientation="vertical">
                                    <button onclick='deleteStock(${stocks[i].id})' class="btn btn-danger">Delete</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </li> `);
                $("#stocksDiv").append(out)
            }
        }
    })
}

/*Фильтр акций*/
$('.filters button').on('click', function () {
    $('.filters button').removeClass('active');
    $(this).parent('button').addClass('active'); // выделяем выбранную категорию
    const cat = $(this).attr('data-filter'); // определяем категорию
    if (cat === 'ALL') {
        $('.allStocks li').show();
    } else {
        $('.allStocks li').hide();
        $('.allStocks li[data-filter="' + cat + '"]').show();
    }
});

/*Добавить акцию*/
function addStock() {
    let startDate = $('#addStartDate').val()
    startDate = moment(startDate).format("YYYY-MM-DD")
    let endDate = ""
    if ($('#addEndDate').val() !== null || $('#addEndDate').val() !== "") {
        endDate = $('#addEndDate').val()
        endDate = moment(endDate).format("YYYY-MM-DD")
    }
    if (endDate === "Invalid date") {
        endDate = ""
    }
     const stockAdd = {
        id: $('#id').val(),
        stockImg: $('#addStockImg').val(),
        stockTitle: $('#addStockTitle').val(),
        stockText: $('#addStockText').val(),
        startDate: startDate,
        endDate: endDate,
        stock: $('#stockTimeZone').val(),
    };
    $.ajax({
        url: "/rest/addStock",
        data: JSON.stringify(stockAdd),
        dataType: 'json',
        type: 'POST',
        contentType: 'application/JSON; charset=utf-8',
        success: function () {
            create();
            $('.modal-body').find('input,textarea').val('');
            toastr.success('Акция успешно добавлена!', {timeOut: 5000})
        },
        error: function () {
            alert("Заполните все поля!")
        }
    })
}

/*Редактировать акцию*/
function getStockForEdit(id) {
    $.ajax({
        type: "GET",
        url: "/rest/" + id,
        dataType: 'json',
        success: function (stock) { //заполнение полей данными
            $(".modal-body #Eid").val(stock.id)
            $(".modal-body #editStockTitle").val(stock.stockTitle)
            $(".modal-body #editStockText").val(stock.stockText)
            $(".modal-body #editStartDate").val(stock.startDate)
            $(".modal-body #editEndDate").val(stock.endDate)
        }
    });
}

/*Сохранение заполненных полей*/
function updateStock() {
    startDate = $('#editStartDate').val()
    startDate = moment(startDate).format("YYYY-MM-DD")
    let endDate = ""
    if ($('#editEndDate').val() !== null || $('#editEndDate').val() !== "") {
        endDate = $('#editEndDate').val()
        endDate = moment(endDate).format("YYYY-MM-DD")
    }
    if (endDate === "Invalid date") {
        endDate = ""
    }
    const stock = {
        id: $('#Eid').val(),
        stockImg: $('#editStockImg').val(),
        stockTitle: $('#editStockTitle').val(),
        stockText: $('#editStockText').val(),
        startDate: startDate,
        endDate: endDate,
    };
    $.ajax({
        url: "/rest/editStock",
        data: JSON.stringify(stock),
        dataType: 'json',
        type: 'PUT',
        contentType: 'application/JSON; charset=utf-8',
        success: function () {
            create();
            toastr.info('Акция успешно отредактирована!', {timeOut: 5000})
        },
        error: function () {
            alert("Заполните все поля!")
        }
    })
}

/*Удалить пользователя из таблицы*/
function deleteStock(id) {
    $.ajax({
            url: "/rest/" + id,
            type: "DELETE",
            contentType: "application/json",
            success: function (data) {
                $("#stocksDiv #" + id).remove();
                console.log(data)
                toastr.error('Акция успешно удалена!', {timeOut: 5000})
            },
            error: function (er) {
                console.log(er)
            }
        }
    );
}

$("#stockAddModal").ready(function () {
    var name=document.getElementById("addStartDate");
    var click=document.getElementById("save");
    click.onclick=function()
    {
        if(!name.value){
            toastr.warning('Заполните дату начала акции!', {timeOut: 5000})
        } else {
            $("#newStockModal").modal('hide');
            addStock()
        }
    }
})

$("#stockEditModal").ready(function () {
    var name=document.getElementById("editStartDate");
    var click=document.getElementById("editSave");
    click.onclick=function()
    {
        if(!name.value){
            toastr.warning('Заполните дату начала акции!', {timeOut: 5000})
        } else {
            $("#editStockModal").modal('hide');
            updateStock()
        }
    }
})

$("#stockAddModal").ready(function () {
    var name=document.getElementById("addStartDate");
    var click=document.getElementById("save");
    click.onclick=function()
    {
        if(!name.value){
            toastr.warning('Заполните дату начала акции!', {timeOut: 5000})
        } else {
            $("#newStockModal").modal('hide');
            addStock()
        }
    }
})

$("#stockEditModal").ready(function () {
    var name=document.getElementById("editStartDate");
    var click=document.getElementById("editSave");
    click.onclick=function()
    {
        if(!name.value){
            toastr.warning('Заполните дату начала акции!', {timeOut: 5000})
        } else {
            $("#editStockModal").modal('hide');
            updateStock()
        }
    }
})
